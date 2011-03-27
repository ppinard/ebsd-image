package org.ebsdimage.core.sim;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.core.Microscope;

import ptpshared.geom.*;
import rmlshared.geom.LineUtil;
import rmlshared.util.ArrayList;
import crystallography.core.Calculations;
import crystallography.core.Reflector;
import crystallography.core.Reflectors;

/**
 * A <code>BandsCalculator</code> that approximates the shape of the bands by
 * two straight edges.
 * 
 * @author Philippe T. Pinard
 */
public class LinearBandsCalculator implements BandsCalculator {

    @Override
    public Band[] calculate(int width, int height, Microscope microscope,
            Reflectors reflectors, Rotation rotation) {
        EuclideanSpace microscopeCS = microscope.getMicroscopeCS();
        EuclideanSpace acqPositionCS = microscope.getAcquisitionPositionCS();
        EuclideanSpace cameraCS = microscope.getCameraCS();
        Plane cameraPlane = microscope.getCameraPlane();

        AffineTransform3D atFromAcqPositionToMicroscope =
                acqPositionCS.getTransformationTo(microscopeCS);
        AffineTransform3D atFromMicroscopeToCamera =
                microscopeCS.getTransformationTo(cameraCS);

        double dx = width / microscope.getCamera().width;
        double dy = height / microscope.getCamera().height;

        double wavelength =
                Calculations.electronWavelength(microscope.getBeamEnergy());

        ArrayList<Band> bands = new ArrayList<Band>();

        for (Reflector reflector : reflectors.getReflectorsSortedByIntensity(true)) {
            Band band;

            try {
                band =
                        calculateBand(width, height, reflector, rotation,
                                atFromAcqPositionToMicroscope,
                                atFromMicroscopeToCamera, cameraPlane, dx, dy,
                                wavelength);
            } catch (BandException ex) {
                continue;
            }

            bands.add(band);
        }

        return bands.toArray(new Band[0]);
    }



    /**
     * Calculates a band.
     * 
     * @param width
     *            width of the diffraction pattern image
     * @param height
     *            height of the diffraction pattern image
     * @param reflector
     *            reflector of the band
     * @param rotation
     *            rotation of the crystal
     * @param atFromAcqPositionToMicroscope
     *            affine transformation from the acquisition position coordinate
     *            system to the microscope coordinate system
     * @param atFromMicroscopeToCamera
     *            affine transformation from the microscope coordinate system to
     *            the camera coordinate system
     * @param cameraPlane
     *            plane of the camera
     * @param dx
     *            calibration of the camera/diffraction pattern in x
     * @param dy
     *            calibration of the camera/diffraction pattern in x
     * @param wavelength
     *            wavelength of the electron
     * @return a band
     * @throws BandException
     *             if the band for the specified reflector is outside the camera
     *             or parallel to it
     */
    private Band calculateBand(int width, int height, Reflector reflector,
            Rotation rotation, AffineTransform3D atFromAcqPositionToMicroscope,
            AffineTransform3D atFromMicroscopeToCamera, Plane cameraPlane,
            double dx, double dy, double wavelength) throws BandException {
        // crystal CS to sample CS (rotation due to the orientation)
        Vector3D normal = rotation.applyTo(reflector.getNormal());
        Plane plane = new Plane(new Vector3D(0, 0, 0), normal);

        // sample CS to acquisition position CS
        // transformation = nothing to change

        // intersect plane with camera in microscope CS
        plane = plane.transform(atFromAcqPositionToMicroscope);
        Line3D intersectLine = getIntersectionLine(plane, cameraPlane);

        // line in camera CS
        Line2D middle =
                getLineInCamera(width, height, intersectLine,
                        atFromMicroscopeToCamera, dx, dy);

        // edges
        double theta =
                Calculations.diffractionAngle(reflector.planeSpacing,
                        wavelength);
        Rotation opening = new Rotation(intersectLine.v, theta);
        AffineTransform3D atOpening =
                new AffineTransform3D(opening, new Vector3D(0, 0, 0));

        Plane edge1Plane = plane.transform(atOpening);
        Line2D edge1 =
                getLineInCamera(width, height,
                        getIntersectionLine(edge1Plane, cameraPlane),
                        atFromMicroscopeToCamera, dx, dy);

        Plane edge2Plane = plane.transform(atOpening.inverse());
        Line2D edge2 =
                getLineInCamera(width, height,
                        getIntersectionLine(edge2Plane, cameraPlane),
                        atFromMicroscopeToCamera, dx, dy);

        return new Band(reflector, middle, edge1, edge2);
    }



    /**
     * Returns the line in 3D space resulting from the intersection of the
     * reflector's plane with the camera's plane.
     * 
     * @param plane
     *            reflector's plane
     * @param cameraPlane
     *            camera's plane
     * @return intersection line
     * @throws BandException
     *             if the reflector's plane does not intersect the camera's
     *             plane
     */
    private Line3D getIntersectionLine(Plane plane, Plane cameraPlane)
            throws BandException {
        Line3D intersectLine;

        try {
            intersectLine = PlaneUtil.planesIntersection(plane, cameraPlane);
        } catch (ArithmeticException ex) {
            throw new BandException(ex);
        }

        return intersectLine;
    }



    /**
     * Returns the representation of the intersection line in 3D space as drawn
     * in the camera's 2D space (i.e. diffraction pattern).
     * 
     * @param width
     *            width of the diffraction pattern image
     * @param height
     *            height of the diffraction pattern image
     * @param intersectLine
     *            intersection line in 3D space of the reflector's plane and
     *            camera's plane
     * @param atFromMicroscopeToCamera
     *            affine transformation from the microscope coordinate system to
     *            the camera coordinate system
     * @param dx
     *            calibration of the camera/diffraction pattern in x
     * @param dy
     *            calibration of the camera/diffraction pattern in y
     * @return 2D representation of the 3D intersection line
     * @throws BandException
     *             if the 2D line is outside the camera/diffraction pattern
     */
    private Line2D getLineInCamera(int width, int height, Line3D intersectLine,
            AffineTransform3D atFromMicroscopeToCamera, double dx, double dy)
            throws BandException {
        Line3D cameraLine = intersectLine.transform(atFromMicroscopeToCamera);

        // middle line
        double x1 = cameraLine.p.getX() * dx;
        double y1 = cameraLine.p.getY() * dy;
        double x2 = cameraLine.getPointFromS(1).getX() * dx;
        double y2 = cameraLine.getPointFromS(1).getY() * dy;
        Line2D line = new Line2D.Double(x1, y1, x2, y2);

        try {
            LineUtil.extendTo(line, new Rectangle2D.Double(0, 0, width, height));
        } catch (IllegalArgumentException ex) {
            throw new BandException(ex);
        }

        return line;
    }

}
