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
package org.ebsdimage.vendors.hkl.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.vendors.hkl.core.HklMetadata;

import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;

/**
 * Loads a HKL CPR file and save some of its parameters in a
 * <code>HklMetadata</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CprLoader implements Monitorable {

    /**
     * Checks if the specified file can be loaded as a CPR file.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is a CPR file, <code>false</code>
     *         otherwise
     */
    public boolean canLoad(File file) {
        return FileUtil.getExtension(file).equalsIgnoreCase("CPR");
    }



    @Override
    public double getTaskProgress() {
        return 0;
    }



    @Override
    public String getTaskStatus() {
        return "";
    }



    /**
     * Loads some parameters inside a CPR file and saves them as a
     * <code>HklMetadata</code>.
     * 
     * @param file
     *            a file
     * @param microscope
     *            microscope configuration
     * @return <code>HklMetadata</code>
     * @throws IOException
     *             if an error while parsing the CPR file
     */
    public HklMetadata load(File file, Microscope microscope)
            throws IOException {
        if (!canLoad(file))
            throw new IllegalArgumentException("Cannot load (" + file
                    + ") as a CPR file.");

        HierarchicalINIConfiguration config;
        try {
            config = new HierarchicalINIConfiguration(file);
        } catch (ConfigurationException e) {
            throw new IOException(e);
        }

        // project
        String projectName = FileUtil.getBaseName(file);
        File projectPath = FileUtil.getParentFile(file);

        // Projection parameters
        SubnodeConfiguration section =
                config.getSection("ProjectionParameters");

        double pcX = section.getDouble("PCX");
        microscope.setPatternCenterX(pcX);

        double pcY = section.getDouble("PCY");
        microscope.setPatternCenterY(pcY);

        double cameraDistance = section.getDouble("DD");
        microscope.setCameraDistance(cameraDistance
                * microscope.getCamera().width);

        // Acquisition surface
        section = config.getSection("Acquisition Surface");

        double acqEuler1 = section.getDouble("Euler1");
        double acqEuler2 = section.getDouble("Euler2");
        double acqEuler3 = section.getDouble("Euler3");

        microscope.setSampleRotation(new Rotation(RotationOrder.ZXZ, acqEuler1,
                acqEuler2, acqEuler3));

        // Job
        section = config.getSection("Job");

        double magnification = section.getDouble("Magnification");
        microscope.setMagnification(magnification);

        double tiltAngle = section.getDouble("TiltAngle");
        microscope.setTiltAngle(Math.toRadians(tiltAngle));

        double beamEnergy = section.getDouble("kV");
        microscope.setBeamEnergy(beamEnergy * 1e3);

        return new HklMetadata(microscope, projectName, projectPath);
    }

}
