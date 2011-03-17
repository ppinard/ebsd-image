package org.ebsdimage.core;

import junittools.core.AlmostEquable;
import magnitude.core.Magnitude;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ptpshared.geom.*;

/**
 * Microscope and acquisition parameters.
 * 
 * @author ppinard
 */
@Root
public class Microscope implements AlmostEquable {

    /** Name of the microscope setup. */
    @Attribute(name = "name")
    private String name = "Unnamed";

    /** Definition of the EBSD camera. */
    @Element(name = "camera")
    private Camera camera;

    /** Axis along which the sample is tilted. */
    @Element(name = "tiltAxis")
    private Vector3D tiltAxis;

    /** Angle of the tilt (from the horizontal) in radians. */
    @Element(name = "tiltAngle")
    private double tiltAngle = 0.0;

    /** Microscope's working distance (in meters). */
    @Element(name = "workingDistance")
    private double workingDistance = 0.0;

    /** Beam energy (in eV). */
    @Element(name = "beamEnergy")
    private double beamEnergy = 0.0;

    /** Magnification. */
    @Element(name = "magnification")
    private double magnification = 1.0;

    /** Rotation of the sample. */
    @Element(name = "sampleRotation")
    private Rotation sampleRotation = Rotation.IDENTITY;

    /**
     * Position of the pattern center along the x-axis of the diffraction
     * pattern. The position is given as a fraction of the camera's width.
     */
    @Element(name = "patternCenterX")
    private double patternCenterX = 0;

    /**
     * Position of the pattern center along the y-axis of the diffraction
     * pattern. The position is given as a fraction of the camera's height.
     */
    @Element(name = "patternCenterY")
    private double patternCenterY = 0;

    /** Distance between the sample and the camera (in meters). */
    @Element(name = "cameraDistance")
    private double cameraDistance = 0.0;



    /**
     * Creates a new <code>Microscope</code> with no camera and an arbitrary
     * tilt axis.
     */
    public Microscope() {
        this(Camera.NO_CAMERA, new Vector3D(0, 1, 0));
    }



    /**
     * Creates a new <code>Microscope</code> from the camera and tilt axis
     * information.
     * 
     * @param camera
     *            camera definition
     * @param tiltAxis
     *            axis along which the sample is tilted
     */
    public Microscope(@Element(name = "camera") Camera camera,
            @Element(name = "tiltAxis") Vector3D tiltAxis) {
        setCamera(camera);
        setTiltAxis(tiltAxis);
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        double delta = ((Number) precision).doubleValue();
        if (delta < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(delta))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Microscope other = (Microscope) obj;
        if (!camera.equals(other.camera, precision))
            return false;
        if (!Vector3DUtils.equals(tiltAxis, other.tiltAxis, delta))
            return false;
        if (Math.abs(tiltAngle - other.tiltAngle) > delta)
            return false;
        if (Math.abs(workingDistance - other.workingDistance) > delta)
            return false;
        if (Math.abs(beamEnergy - other.beamEnergy) > delta)
            return false;
        if (!RotationUtils.equals(sampleRotation, other.sampleRotation, delta))
            return false;
        if (Math.abs(patternCenterX - other.patternCenterX) > delta)
            return false;
        if (Math.abs(patternCenterY - other.patternCenterY) > delta)
            return false;
        if (Math.abs(cameraDistance - other.cameraDistance) > delta)
            return false;

        return true;
    }



    /**
     * Returns the acquisition position's coordinate system.
     * 
     * @return acquisition position's coordinate system
     */
    public EuclideanSpace getAcquisitionPositionCS() {
        Vector3D translation = new Vector3D(0, 0, -workingDistance);

        // rotation
        Rotation tilt = new Rotation(tiltAxis, tiltAngle);
        // Rotation rotation = sampleRotation.applyTo(tilt);
        Rotation rotation = tilt.applyTo(sampleRotation);
        // TODO: Verify rotation order
        // tilt.applyTo(sampleRotation) seems to be the correct order

        double[][] m = rotation.getMatrix();
        Vector3D i = new Vector3D(m[0][0], m[1][0], m[2][0]);
        Vector3D j = new Vector3D(m[0][1], m[1][1], m[2][1]);
        Vector3D k = new Vector3D(m[0][2], m[1][2], m[2][2]);

        return new EuclideanSpace(i, j, k, translation);
    }



    /**
     * Returns the beam energy of the microscope during the acquisition.
     * 
     * @return beam energy in eV
     */
    public double getBeamEnergy() {
        return beamEnergy;
    }



    /**
     * Returns the EBSD camera of this microscope.
     * 
     * @return camera
     */
    public Camera getCamera() {
        return camera;
    }



    /**
     * Returns the camera coordinate system for the specified camera, pattern
     * center and detector distance.
     * 
     * @return camera coordinate system
     */
    public EuclideanSpace getCameraCS() {
        Vector3D translation = getCameraTranslation();

        return new EuclideanSpace(camera.x, Vector3D.crossProduct(camera.n,
                camera.x), camera.n, translation);
    }



    /**
     * Returns the camera distance: distance between the sample and the camera.
     * 
     * @return camera distance in meters
     */
    public double getCameraDistance() {
        return cameraDistance;
    }



    /**
     * Returns the plane of the camera's screen.
     * 
     * @return plane of the camera
     */
    public Plane getCameraPlane() {
        return new Plane(getCameraTranslation(), camera.n);
    }



    /**
     * Calculates the translation vector of the camera based on the pattern
     * center, detector distance and working distance.
     * 
     * @return translation vector of the camera coordinate system
     */
    private Vector3D getCameraTranslation() {
        // C: origin of camera CS
        // P: pattern center
        // O: origin of microscope
        // S: sample

        Vector3D os = new Vector3D(0, 0, -workingDistance);
        Vector3D sp = camera.n.scalarMultiply(cameraDistance);
        Vector3D pc =
                new Vector3D(-patternCenterX * camera.width, -patternCenterY
                        * camera.height, 0.0);

        // rotate vector from camera CS to microscope CS
        EuclideanSpace tmpCameraCS =
                new EuclideanSpace(camera.x, Vector3D.crossProduct(camera.n,
                        camera.x), camera.n);
        AffineTransform3D at =
                tmpCameraCS.getTransformationTo(getMicroscopeCS());
        pc = at.transformVector(pc);

        return os.add(sp.add(pc));
    }



    /**
     * Returns the magnification.
     * 
     * @return magnification
     */
    public double getMagnification() {
        return magnification;
    }



    /**
     * Returns the microscope's coordinate system.
     * 
     * @return microscope's coordinate system
     */
    public EuclideanSpace getMicroscopeCS() {
        return EuclideanSpace.ORIGIN;
    }



    /**
     * Returns the name of this microscope setup.
     * 
     * @return name of this microscope setup
     */
    public String getName() {
        return name;
    }



    /**
     * Returns the position of the pattern center in the x direction of the
     * diffraction pattern. The position is given as a fraction of the camera's
     * width.
     * 
     * @return pattern center in the x direction
     */
    public double getPatternCenterX() {
        return patternCenterX;
    }



    /**
     * Returns the position of the pattern center in the y direction of the
     * diffraction pattern. The position is given as a fraction of the camera's
     * height.
     * 
     * @return pattern center in the y direction
     */
    public double getPatternCenterY() {
        return patternCenterY;
    }



    /**
     * Returns the sample rotation.
     * 
     * @return sample rotation
     */
    public Rotation getSampleRotation() {
        return sampleRotation;
    }



    /**
     * Returns the tilt angle.
     * 
     * @return tilt angle in radians
     */
    public double getTiltAngle() {
        return tiltAngle;
    }



    /**
     * Returns the tilt axis of the sample.
     * 
     * @return the tilt axis of the sample
     */
    public Vector3D getTiltAxis() {
        return tiltAxis;
    }



    /**
     * Returns the microscope's working distance.
     * 
     * @return working distance in meters
     */
    public double getWorkingDistance() {
        return workingDistance;
    }



    /**
     * Sets the microscope's beam energy.
     * 
     * @param energy
     *            beam energy (in units of eV)
     */
    public void setBeamEnergy(double energy) {
        if (energy < 0.0)
            throw new IllegalArgumentException("Beam energy (" + energy
                    + ") cannot be less than zero.");

        beamEnergy = energy;
    }



    /**
     * Sets the microscope's beam energy.
     * 
     * @param energy
     *            beam energy (in units of eV)
     */
    public void setBeamEnergy(Magnitude energy) {
        setBeamEnergy(energy.getValue("eV"));
    }



    /**
     * Sets the EBSD camera of this microscope.
     * 
     * @param camera
     *            camera
     */
    public void setCamera(Camera camera) {
        if (camera == null)
            throw new NullPointerException("Camera canont be null.");
        this.camera = camera;
    }



    /**
     * Sets the camera distance, distance between the sample and the camera.
     * 
     * @param distance
     *            camera distance (in units of meters)
     */
    public void setCameraDistance(double distance) {
        if (distance < 0.0)
            throw new IllegalArgumentException("Detector distance (" + distance
                    + ") cannot be less than zero.");
        this.cameraDistance = distance;
    }



    /**
     * Sets the detector distance, distance between the sample and the camera.
     * 
     * @param detectorDistance
     *            detector distance (in units of meters)
     */
    public void setCameraDistance(Magnitude detectorDistance) {
        setCameraDistance(detectorDistance.getValue("m"));
    }



    /**
     * Sets the magnification.
     * 
     * @param magnification
     *            magnification
     */
    public void setMagnification(double magnification) {
        if (magnification < 1)
            throw new IllegalArgumentException(
                    "Magnitication cannot be less than 1.");
        this.magnification = magnification;
    }



    /**
     * Sets the name of this microscope setup.
     * 
     * @param name
     *            name of this microscope setup
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name cannot be null.");
        if (name.isEmpty())
            throw new IllegalArgumentException(
                    "Name cannot be an empty string.");
        this.name = name;
    }



    /**
     * Sets the pattern center in the x direction. The pattern center should be
     * given as a fraction of the camera's width.
     * 
     * @param patternCenterX
     *            pattern center in the x direction as a fraction of the
     *            camera's width
     * @throws IllegalArgumentException
     *             if the position is outside [0.0, 1.0]
     */
    public void setPatternCenterX(double patternCenterX) {
        if (patternCenterX < 0.0 || patternCenterX > 1.0)
            throw new IllegalArgumentException(
                    "Pattern center must be between 0.0 and 1.0.");
        this.patternCenterX = patternCenterX;
    }



    /**
     * Sets the pattern center in the y direction. The pattern center should be
     * given as a fraction of the camera's height.
     * 
     * @param patternCenterY
     *            pattern center in the y direction as a fraction of the
     *            camera's height
     * @throws IllegalArgumentException
     *             if the position is outside [0.0, 1.0]
     */
    public void setPatternCenterY(double patternCenterY) {
        if (patternCenterY < 0.0 || patternCenterY > 1.0)
            throw new IllegalArgumentException(
                    "Pattern center must be between 0.0 and 1.0.");
        this.patternCenterY = patternCenterY;
    }



    /**
     * Sets the sample rotation. The sample rotation corresponds to the rotation
     * of the sample on the holder. It is required to align the sample's
     * direction according to how the sample is/was mounted on the holder.
     * 
     * @param rotation
     *            rotation of the sample
     */
    public void setSampleRotation(Rotation rotation) {
        if (rotation == null)
            throw new NullPointerException("Sample rotation cannot be null.");
        sampleRotation = rotation;
    }



    /**
     * Sets the tilt angle of the sample.
     * 
     * @param angle
     *            tilt angle (in units of radians)
     */
    public void setTiltAngle(double angle) {
        tiltAngle = angle;
    }



    /**
     * Sets the tilt angle of the sample.
     * 
     * @param angle
     *            tilt angle (in units of radians)
     */
    public void setTiltAngle(Magnitude angle) {
        setTiltAngle(angle.getValue("rad"));
    }



    /**
     * Sets the tilt axis of the sample.
     * 
     * @param tiltAxis
     *            tilt axis
     */
    public void setTiltAxis(Vector3D tiltAxis) {
        if (tiltAxis == null)
            throw new NullPointerException("Tilt axis cannot be null.");
        if (tiltAxis.getNorm() == 0.0)
            throw new IllegalArgumentException(
                    "The tilt axis cannot be a null vector.");
        this.tiltAxis = tiltAxis;
    }



    /**
     * Sets the microscope's working distance.
     * 
     * @param wd
     *            working distance (in units of meters)
     */
    public void setWorkingDistance(double wd) {
        if (wd < 0.0)
            throw new IllegalArgumentException("Working distance (" + wd
                    + ") cannot be less than zero.");
        workingDistance = wd;
    }



    /**
     * Sets the microscope's working distance.
     * 
     * @param wd
     *            working distance (in units of meters)
     */
    public void setWorkingDistance(Magnitude wd) {
        setWorkingDistance(wd.getValue("m"));
    }



    @Override
    public String toString() {
        return name;
    }
}
