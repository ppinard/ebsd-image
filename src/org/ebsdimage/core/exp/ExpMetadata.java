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
package org.ebsdimage.core.exp;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;

import ptpshared.core.math.Quaternion;

/**
 * Metadata held in the <code>ExpMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMetadata extends EbsdMetadata {

    /**
     * Creates a new <code>ExpMetadata</code> with all the default values.
     */
    public ExpMetadata() {
    }



    /**
     * Creates a new <code>ExpMetadata</code> to store all the required
     * parameters of a <code>ExpMMap</code>. All values are validated.
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
    public ExpMetadata(double beamEnergy, double magnification,
            double tiltAngle, double workingDistance, double pixelWidth,
            double pixelHeight, Quaternion sampleRotation, Camera calibration) {
        super(beamEnergy, magnification, tiltAngle, workingDistance,
                pixelWidth, pixelHeight, sampleRotation, calibration);
    }



    /**
     * Creates a <code>ExpMetadata</code> from an <code>EbsdMetadata</code>. All
     * values are validated.
     * 
     * @param metadata
     *            a valid <code>EbsdMetadata</code>
     */
    public ExpMetadata(EbsdMetadata metadata) {
        this(metadata.beamEnergy, metadata.magnification, metadata.tiltAngle,
                metadata.workingDistance, metadata.pixelWidth,
                metadata.pixelHeight, metadata.sampleRotation,
                metadata.calibration);
    }

}
