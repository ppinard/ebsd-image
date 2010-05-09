/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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

import static java.lang.Math.PI;
import ptpshared.core.math.Quaternion;
import ptpshared.utility.xml.ObjectXml;

/**
 * Representation of the metadata held in the <code>EbsdMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMetadata implements ObjectXml {

    /** Energy of the electron beam in eV. */
    public final double beamEnergy;

    /** Default or not defined value of the beam energy. */
    public static final double DEFAULT_BEAM_ENERGY = Double.NaN;

    /** Magnification of the EBSD acquisition. */
    public final double magnification;

    /** Default or not defined value of the magnification. */
    public static final double DEFAULT_MAGNIFICATION = Double.NaN;

    /** Angle of sample's tilt in radians. */
    public final double tiltAngle;

    /** Default or not defined value of the tilt angle. */
    public static final double DEFAULT_TILT_ANGLE = Double.NaN;

    /** Working distance of the EBSD acquisition in meters. */
    public final double workingDistance;

    /** Default or not defined value of the working distance. */
    public static final double DEFAULT_WORKING_DISTANCE = Double.NaN;

    /** Calibration for the width of the pixel in meters. */
    public final double pixelWidth;

    /** Default or not defined value of the pixel width. */
    public static final double DEFAULT_PIXEL_WIDTH = Double.NaN;

    /** Calibration for the height of the pixel in meters. */
    public final double pixelHeight;

    /** Default or not defined value of the pixel height. */
    public static final double DEFAULT_PIXEL_HEIGHT = Double.NaN;

    /** Rotation from the pattern frame (camera) into the sample frame. */
    public final Quaternion sampleRotation;

    /** Default or not defined value of the sample's rotation. */
    public static final Quaternion DEFAULT_SAMPLE_ROTATION =
            Quaternion.IDENTITY;

    /** Calibration of the camera. */
    public final Camera calibration;

    /** Default or not defined value of the camera's calibration. */
    public static final Camera DEFAULT_CALIBRATION = new Camera(0.0, 0.0, 0.0);



    /**
     * Creates a new <code>EbsdMetadata</code> with all the default values.
     */
    public EbsdMetadata() {
        this(DEFAULT_BEAM_ENERGY, DEFAULT_MAGNIFICATION, DEFAULT_TILT_ANGLE,
                DEFAULT_WORKING_DISTANCE, DEFAULT_PIXEL_WIDTH,
                DEFAULT_PIXEL_HEIGHT, DEFAULT_SAMPLE_ROTATION,
                DEFAULT_CALIBRATION);
    }



    /**
     * Creates a new <code>EbsdMetadata</code> to store all the required
     * parameters of a <code>EbsdMMap</code>. All values are validated.
     * 
     * @param beamEnergy
     *            energy of the electron beam in eV
     * @param magnification
     *            magnification of the EBSD acquisition
     * @param tiltAngle
     *            angle of sample's tilt in radians
     * @param workingDistance
     *            working distance of the EBSD acquisition in meters
     * @param pixelWidth
     *            calibration for the width of the pixel in meters
     * @param pixelHeight
     *            calibration for the height of the pixel in meters
     * @param sampleRotation
     *            rotation from the pattern frame (camera) into the sample frame
     * @param calibration
     *            calibration of the camera
     */
    public EbsdMetadata(double beamEnergy, double magnification,
            double tiltAngle, double workingDistance, double pixelWidth,
            double pixelHeight, Quaternion sampleRotation, Camera calibration) {
        // Beam energy
        if (Double.isInfinite(beamEnergy))
            throw new IllegalArgumentException("Beam energy is infinite");
        if (beamEnergy <= 0)
            throw new IllegalArgumentException("Invalid beamEnergy ("
                    + beamEnergy + ')');
        this.beamEnergy = beamEnergy;

        // Magnification
        if (Double.isInfinite(magnification))
            throw new IllegalArgumentException("magnification is infinite");
        if (magnification <= 0)
            throw new IllegalArgumentException("Invalid magnification ("
                    + magnification + ')');
        this.magnification = magnification;

        // Tilt angle
        if (Double.isInfinite(tiltAngle))
            throw new IllegalArgumentException("Tilt angle is infinite");
        if (tiltAngle < 0 || tiltAngle > PI / 2)
            throw new IllegalArgumentException("Invalid tilt angle ("
                    + tiltAngle + "). It must be between [0, PI/2].");
        this.tiltAngle = tiltAngle;

        // Working distance
        if (Double.isInfinite(workingDistance))
            throw new IllegalArgumentException("Working distance is infinite");
        if (workingDistance < 0)
            throw new IllegalArgumentException("Working distance ("
                    + workingDistance + ") must be greater than 0.");
        this.workingDistance = workingDistance;

        // Pixel width
        if (Double.isInfinite(pixelWidth))
            throw new IllegalArgumentException("Pixel width is infinite");
        if (pixelWidth <= 0)
            throw new IllegalArgumentException("Invalid pixel width ("
                    + pixelWidth + ')');
        this.pixelWidth = pixelWidth;

        // Pixel height
        if (Double.isInfinite(pixelHeight))
            throw new IllegalArgumentException("Pixel height is infinite");
        if (pixelHeight <= 0)
            throw new IllegalArgumentException("Invalid pixel height ("
                    + pixelHeight + ')');
        this.pixelHeight = pixelHeight;

        // Sample rotation
        if (sampleRotation == null)
            throw new NullPointerException(
                    "The sample rotation cannot be null.");
        this.sampleRotation = sampleRotation.normalize();

        // Calibration
        if (calibration == null)
            throw new NullPointerException("Calibration cannot be null.");
        this.calibration = calibration;
    }
}
