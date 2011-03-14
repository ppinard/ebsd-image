package org.ebsdimage.core.sim.ops.patternsim;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.BandException;

import ptpshared.geom.*;
import rmlshared.geom.LineUtil;
import crystallography.core.Calculations;
import crystallography.core.Reflector;
import crystallography.core.Reflectors;

public abstract class PatternLinearBand extends PatternSimOp {

    public PatternLinearBand(int width, int height) {
        super(width, height);
    }



    @Override
    protected void calculateBands(Microscope microscope, Reflectors reflectors,
            Rotation rotation) {
        EuclideanSpace microscopeCS = microscope.getMicroscopeCS();
        EuclideanSpace acqPositionCS = microscope.getAcquisitionPositionCS();
        EuclideanSpace cameraCS = microscope.getCameraCS();
        Plane cameraPlane = microscope.getCameraPlane();

        AffineTransform3D atFromAcqPositionToMicroscope =
                acqPositionCS.getTransformationTo(microscopeCS);
        AffineTransform3D atFromMicroscopeToCamera =
                microscopeCS.getTransformationTo(cameraCS);

        double dx = width / microscope.camera.width;
        double dy = height / microscope.camera.height;

        double wavelength =
                Calculations.electronWavelength(microscope.getBeamEnergy());

        for (Reflector reflector : reflectors.getReflectorsSortedByIntensity(true)) {
            Band band;

            try {
                band =
                        calculateBand(reflector, rotation,
                                atFromAcqPositionToMicroscope,
                                atFromMicroscopeToCamera, cameraPlane, dx, dy,
                                wavelength);
            } catch (BandException ex) {
                continue;
            }

            bands.add(band);
        }
    }



    private Band calculateBand(Reflector reflector, Rotation rotation,
            AffineTransform3D atFromAcqPositionToMicroscope,
            AffineTransform3D atFromMicroscopeToCamera, Plane cameraPlane,
            double dx, double dy, double wavelength) throws BandException {
        // crystal CS to sample CS (rotation due to the orientation)
        Vector3D normal = rotation.applyTo(reflector.getNormal());
        Plane plane = new Plane(new Vector3D(0, 0, 0), normal);

        // sample CS to acquisition position CS
        // transformation = nothing to change

        // intersect plane with camera in microscope CS
        plane = plane.transform(atFromAcqPositionToMicroscope);
        Line3D intersectLine =
                getIntersectionLine(plane, cameraPlane,
                        atFromAcqPositionToMicroscope);

        // line in camera CS
        Line2D middle =
                getLineInCamera(intersectLine, atFromMicroscopeToCamera, dx, dy);

        // edges
        double theta =
                Calculations.diffractionAngle(reflector.planeSpacing,
                        wavelength);
        Rotation opening = new Rotation(intersectLine.v, theta);
        AffineTransform3D atOpening =
                new AffineTransform3D(opening, new Vector3D(0, 0, 0));

        Plane edge1Plane = plane.transform(atOpening);
        Line2D edge1 =
                getLineInCamera(
                        getIntersectionLine(edge1Plane, cameraPlane,
                                atFromAcqPositionToMicroscope),
                        atFromMicroscopeToCamera, dx, dy);

        Plane edge2Plane = plane.transform(atOpening.inverse());
        Line2D edge2 =
                getLineInCamera(
                        getIntersectionLine(edge2Plane, cameraPlane,
                                atFromAcqPositionToMicroscope),
                        atFromMicroscopeToCamera, dx, dy);

        return new Band(reflector, middle, edge1, edge2);
    }



    private Line3D getIntersectionLine(Plane plane, Plane cameraPlane,
            AffineTransform3D atFromAcqPositionToMicroscope)
            throws BandException {
        Line3D intersectLine;
        try {
            intersectLine = PlaneUtil.planesIntersection(plane, cameraPlane);
        } catch (ArithmeticException ex) {
            throw new BandException(ex);
        }

        return intersectLine;
    }



    private Line2D getLineInCamera(Line3D intersectLine,
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
