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
package org.ebsdimage.io.exp;

import static org.ebsdimage.io.exp.ExpMetadataXmlTags.TAG_NAME;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.exp.ExpMetadata;
import org.ebsdimage.io.EbsdMetadataXmlLoader;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.core.math.Quaternion;
import ptpshared.utility.BadUnitException;

/**
 * XML loader for <code>ExpMetadata</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMetadataXmlLoader extends EbsdMetadataXmlLoader {

    /**
     * Loads a <code>ExpMetadata</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>ExpMetadata</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect
     * @throws BadUnitException
     *             if a unit from one of the metadata is incorrect
     */
    @Override
    public ExpMetadata load(Element element) {
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

        return new ExpMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration);
    }

}
