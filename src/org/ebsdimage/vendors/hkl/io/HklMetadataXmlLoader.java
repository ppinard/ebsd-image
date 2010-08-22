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
package org.ebsdimage.vendors.hkl.io;

import static org.ebsdimage.vendors.hkl.io.HklMetadataXmlTags.PROJECT_NAME_TAG;
import static org.ebsdimage.vendors.hkl.io.HklMetadataXmlTags.PROJECT_PATH_TAG;
import static org.ebsdimage.vendors.hkl.io.HklMetadataXmlTags.TAG_NAME;

import java.io.File;

import org.ebsdimage.core.Camera;
import org.ebsdimage.io.EbsdMetadataXmlLoader;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.core.math.Quaternion;
import ptpshared.utility.BadUnitException;

/**
 * XML loader for <code>HklMetadata</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HklMetadataXmlLoader extends EbsdMetadataXmlLoader {

    /**
     * Parses and returns the project name from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return project name
     */
    protected String parseProjectName(Element element) {
        Element child = element.getChild(PROJECT_NAME_TAG);
        return child.getText();
    }



    /**
     * Parses and returns the project path from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return project path
     */
    protected File parseProjectPath(Element element) {
        Element child = element.getChild(PROJECT_PATH_TAG);
        return new File(child.getText());
    }



    /**
     * Loads a <code>HklMetadata</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>HklMetadata</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect
     * @throws BadUnitException
     *             if a unit from one of the metadata is incorrect
     */
    @Override
    public HklMetadata load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        double beamEnergy = parseBeamEnergy(element);
        double magnification = parseMagnification(element);
        double tiltAngle = parseTiltAngle(element);
        double workingDistance = parseWorkingDistance(element);
        double pixelWidth = parsePixelWidth(element);
        double pixelHeight = parsePixelHeight(element);
        Quaternion sampleRotation = parseSampleRotation(element);
        Camera calibration = parseCalibration(element);

        String projectName = parseProjectName(element);
        File projectPath = parseProjectPath(element);

        return new HklMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration, projectName, projectPath);
    }
}
