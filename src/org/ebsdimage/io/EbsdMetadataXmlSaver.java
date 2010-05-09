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

import org.ebsdimage.core.EbsdMetadata;
import org.jdom.Element;

import ptpshared.io.math.QuaternionXmlSaver;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for <code>EbsdMetadata</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class EbsdMetadataXmlSaver implements ObjectXmlSaver {

    /**
     * Creates an XML <code>Element</code> with the metadata defines in an
     * <code>EbsdMetadata</code>.
     * 
     * @param metadata
     *            an <code>EbsdMetadata</code>
     * @param tagName
     *            tag name of the element
     * @return an XML <code>Element</code>
     */
    protected Element createElement(EbsdMetadata metadata, String tagName) {
        Element element = new Element(tagName);

        Element child = new Element(TAG_BEAM_ENERGY);
        child.setText(Double.toString(metadata.beamEnergy));
        child.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.EV);
        element.addContent(child);

        child = new Element(TAG_MAGNIFICATION);
        child.setText(Double.toString(metadata.magnification));
        element.addContent(child);

        child = new Element(TAG_TILT_ANGLE);
        child.setText(Double.toString(metadata.tiltAngle));
        child.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.RAD);
        element.addContent(child);

        child = new Element(TAG_WORKING_DISTANCE);
        child.setText(Double.toString(metadata.workingDistance));
        child.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.M);
        element.addContent(child);

        child = new Element(TAG_PIXEL_DIMENSION);
        child.setAttribute(ATTR_PIXEL_WIDTH, Double
                .toString(metadata.pixelWidth));
        child.setAttribute(ATTR_PIXEL_HEIGHT, Double
                .toString(metadata.pixelHeight));
        child.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.M);
        element.addContent(child);

        child = new QuaternionXmlSaver().save(metadata.sampleRotation);
        child.setName(TAG_SAMPLE_ROTATION);
        element.addContent(child);

        child = new CameraXmlSaver().save(metadata.calibration);
        child.setName(TAG_CALIBRATION);
        element.addContent(child);

        return element;
    }

}
