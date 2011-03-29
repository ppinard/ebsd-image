/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ebsdimage.core;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ptpshared.geom.*;

/**
 * Acquisition configuration parameters.
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class AcquisitionConfig implements AlmostEquable {

    /** Default acquisition configuration. */
    public static final AcquisitionConfig DEFAULT = new AcquisitionConfig(
            Microscope.DEFAULT, Math.toRadians(70), 0.0, 0.0, 1,
            Rotation.IDENTITY, 0.0, 0.0, 0.0);

    /** Definition of the microscope configuration. */
    @Element(name = "microscope")
    public final Microscope microscope;

    /** Definition of the camera. */
    public final Camera camera;

    /** Axis along which the sample is tilted. */
    public final Vector3D tiltAxis;

    /** Angle of the tilt (from the horizontal) in radians. */
    @Element(name = "tiltAngle")
    public final double tiltAngle;

    /** Microscope's working distance (in meters). */
    @Element(name = "workingDistance")
    public final double workingDistance;

    /** Beam energy (in eV). */
    @Element(name = "beamEnergy")
    public final double beamEnergy;

    /** Magnification. */
    @Element(name = "magnification")
    public final double magnification;

    /** Rotation of the sample. */
    @Element(name = "sampleRotation")
    public final Rotation sampleRotation;

    /**
     * Position of the pattern center along the x-axis of the diffraction
     * pattern. The position is given as a fraction of the camera's width.
     */
    @Element(name = "patternCenterX")
    public final double patternCenterX;

    /**
     * Position of the pattern center along the y-axis of the diffraction
     * pattern. The position is given as a fraction of the camera's height.
     */
    @Element(name = "patternCenterY")
    public final double patternCenterY;

    /** Distance between the sample and the camera (in meters). */
    @Element(name = "cameraDistance")
    public final double cameraDistance;



    /**
     * Creates a new <code>AcquisitionConfig</code>.
     * 
     * @param microscope
     *            microscope configuration
     * @param tiltAngle
     *            tilt angle (in radians)
     * @param workingDistance
     *            working distance of the microscope (in meters)
     * @param beamEnergy
     *            beam energy (in eV)
     * @param magnification
     *            magnification
     * @param sampleRotation
     *            rotation of the sample with respect to the sample frame
     * @param patternCenterX
     *            position of the pattern center in X (fraction of the
     *            diffraction pattern's width)
     * @param patternCenterY
     *            position of the pattern center in y (fraction of the
     *            diffraction pattern's height)
     * @param cameraDistance
     *            distance between the EBSD camera and the sample
     */
    public AcquisitionConfig(
            @Element(name = "microscope") Microscope microscope,
            @Element(name = "tiltAngle") double tiltAngle,
            @Element(name = "workingDistance") double workingDistance,
            @Element(name = "beamEnergy") double beamEnergy,
            @Element(name = "magnification") double magnification,
            @Element(name = "sampleRotation") Rotation sampleRotation,
            @Element(name = "patternCenterX") double patternCenterX,
            @Element(name = "patternCenterY") double patternCenterY,
            @Element(name = "cameraDistance") double cameraDistance) {
        // Microscope
        if (microscope == null)
            throw new NullPointerException(
                    "Microscope configuration cannot be null.");
        this.microscope = microscope;
        this.camera = microscope.camera;
        this.tiltAxis = microscope.tiltAxis;

        // Tilt angle
        if (tiltAngle < -Math.PI || tiltAngle > Math.PI)
            throw new IllegalArgumentException("Tilt angle (" + tiltAngle
                    + ") must be between [-PI, PI].");
        this.tiltAngle = tiltAngle;

        // Working distance
        if (workingDistance < 0.0)
            throw new IllegalArgumentException("Working distance ("
                    + workingDistance + ") cannot be less than zero.");
        this.workingDistance = workingDistance;

        // Beam energy
        if (beamEnergy < 0.0)
            throw new IllegalArgumentException("Beam energy (" + beamEnergy
                    + ") cannot be less than zero.");
        this.beamEnergy = beamEnergy;

        // Magnification
        if (magnification < 1)
            throw new IllegalArgumentException(
                    "Magnitication cannot be less than 1.");
        this.magnification = magnification;

        // Sample rotation
        if (sampleRotation == null)
            throw new NullPointerException("Sample rotation cannot be null.");
        this.sampleRotation = sampleRotation;

        // Pattern center X
        if (patternCenterX < 0.0 || patternCenterX > 1.0)
            throw new IllegalArgumentException(
                    "Pattern center must be between 0.0 and 1.0.");
        this.patternCenterX = patternCenterX;

        // Pattern center Y
        if (patternCenterY < 0.0 || patternCenterY > 1.0)
            throw new IllegalArgumentException(
                    "Pattern center must be between 0.0 and 1.0.");
        this.patternCenterY = patternCenterY;

        // Camera distance
        if (cameraDistance < 0.0)
            throw new IllegalArgumentException("Detector distance ("
                    + cameraDistance + ") cannot be less than zero.");
        this.cameraDistance = cameraDistance;
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

        AcquisitionConfig other = (AcquisitionConfig) obj;
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
     * Returns the microscope's coordinate system.
     * 
     * @return microscope's coordinate system
     */
    public EuclideanSpace getMicroscopeCS() {
        return EuclideanSpace.ORIGIN;
    }

}
