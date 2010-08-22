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
package org.ebsdimage.io;

import static org.ebsdimage.io.EbsdMetadataXmlTags.*;

import org.ebsdimage.core.Camera;
import org.jdom.Element;

import ptpshared.core.math.Quaternion;
import ptpshared.io.math.QuaternionXmlLoader;
import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for <code>EbsdMetadata</code>.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMetadataXmlLoader implements ObjectXmlLoader {

    /**
     * Parses and returns the beam energy from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return beam energy in eV
     * @throws BadUnitException
     *             if the beam energy unit is incorrect
     */
    protected double parseBeamEnergy(Element element) {
        Element child = element.getChild(TAG_BEAM_ENERGY);
        if (!JDomUtil.getStringFromAttributeDefault(child, UnitsXmlTags.ATTR,
                UnitsXmlTags.EV).equals(UnitsXmlTags.EV))
            throw new BadUnitException("Units for beam energy should be "
                    + UnitsXmlTags.EV + ".");
        return JDomUtil.getDoubleFromText(child);
    }



    /**
     * Parses and returns the magnification from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return magnification
     */
    protected double parseMagnification(Element element) {
        Element child = element.getChild(TAG_MAGNIFICATION);
        return JDomUtil.getDoubleFromText(child);
    }



    /**
     * Parses and returns the tilt angle from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return tilt angle in radians
     * @throws BadUnitException
     *             if the tilt angle unit is incorrect
     */
    protected double parseTiltAngle(Element element) {
        Element child = element.getChild(TAG_TILT_ANGLE);
        if (!JDomUtil.getStringFromAttributeDefault(child, UnitsXmlTags.ATTR,
                UnitsXmlTags.RAD).equals(UnitsXmlTags.RAD))
            throw new BadUnitException("Units for tilt angle should be "
                    + UnitsXmlTags.RAD + ".");
        return JDomUtil.getDoubleFromText(child);
    }



    /**
     * Parses and returns the working distance from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return working distance in meters
     * @throws BadUnitException
     *             if the working distance unit is incorrect
     */
    protected double parseWorkingDistance(Element element) {
        Element child = element.getChild(TAG_WORKING_DISTANCE);
        if (!JDomUtil.getStringFromAttributeDefault(child, UnitsXmlTags.ATTR,
                UnitsXmlTags.M).equals(UnitsXmlTags.M))
            throw new BadUnitException(
                    "Units for the working distance should be "
                            + UnitsXmlTags.M + ".");
        return JDomUtil.getDoubleFromText(child);
    }



    /**
     * Parses and returns the pixel width from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return pixel width in meters
     * @throws BadUnitException
     *             if the pixel width unit is incorrect
     */
    protected double parsePixelWidth(Element element) {
        Element child = element.getChild(TAG_PIXEL_DIMENSION);
        if (!JDomUtil.getStringFromAttributeDefault(child, UnitsXmlTags.ATTR,
                UnitsXmlTags.M).equals(UnitsXmlTags.M))
            throw new BadUnitException("Units for pixel dimensions should be "
                    + UnitsXmlTags.M + ".");
        return JDomUtil.getDoubleFromAttribute(child, ATTR_PIXEL_WIDTH);
    }



    /**
     * Parses and returns the pixel height from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return pixel height in meters
     * @throws BadUnitException
     *             if the pixel height unit is incorrect
     */
    protected double parsePixelHeight(Element element) {
        Element child = element.getChild(TAG_PIXEL_DIMENSION);
        if (!JDomUtil.getStringFromAttributeDefault(child, UnitsXmlTags.ATTR,
                UnitsXmlTags.M).equals(UnitsXmlTags.M))
            throw new BadUnitException("Units for pixel dimensions should be "
                    + UnitsXmlTags.M + ".");
        return JDomUtil.getDoubleFromAttribute(child, ATTR_PIXEL_HEIGHT);
    }



    /**
     * Parses and returns the sample's rotation from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return sample's rotation
     */
    protected Quaternion parseSampleRotation(Element element) {
        Element child = element.getChild(TAG_SAMPLE_ROTATION);
        return new QuaternionXmlLoader().load(child, TAG_SAMPLE_ROTATION);
    }



    /**
     * Parses and returns the calibration from the XML metadata.
     * 
     * @param element
     *            XML <code>Element</code>
     * @return calibration
     */
    protected Camera parseCalibration(Element element) {
        Element child = element.getChild(TAG_CALIBRATION);
        return new CameraXmlLoader().load(child, TAG_CALIBRATION);
    }

}
