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
package org.ebsdimage.vendors.hkl.core;

import java.io.File;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;

import ptpshared.core.math.Quaternion;

/**
 * Metadata held in a <code>HKLMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HklMetadata extends EbsdMetadata {

    /** Name of the project. */
    public final String projectName;

    /** Directory where the project is located. */
    public final File projectPath;



    /**
     * Creates a <code>HklMetadata</code> with the required parameters. All
     * values are validated.
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
     * @param projectName
     *            name of the project
     * @param projectPath
     *            path of the project
     */
    public HklMetadata(double beamEnergy, double magnification,
            double tiltAngle, double workingDistance, double pixelWidth,
            double pixelHeight, Quaternion sampleRotation, Camera calibration,
            String projectName, File projectPath) {
        super(beamEnergy, magnification, tiltAngle, workingDistance,
                pixelWidth, pixelHeight, sampleRotation, calibration);

        // Project name
        if (projectName == null)
            throw new NullPointerException("Undefined project name");
        this.projectName = projectName;

        // Project path
        if (projectPath == null)
            throw new NullPointerException("Undefined project path");
        this.projectPath = projectPath.getAbsoluteFile();
    }



    /**
     * Creates a <code>HklMetadata</code> from an <code>EbsdMetadata</code> and
     * the extra required parameters. All values are validated.
     * 
     * @param metadata
     *            a valid <code>EbsdMetadata</code>
     * @param projectName
     *            name of the project
     * @param projectPath
     *            path of the project
     */
    public HklMetadata(EbsdMetadata metadata, String projectName,
            File projectPath) {
        this(metadata.beamEnergy, metadata.magnification, metadata.tiltAngle,
                metadata.workingDistance, metadata.pixelWidth,
                metadata.pixelHeight, metadata.sampleRotation,
                metadata.calibration, projectName, projectPath);
    }
}
