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
package org.ebsdimage.io.exp.ops.hough.post;

import static org.ebsdimage.io.exp.ops.hough.post.HoughCropXmlTags.ATTR_RADIUS;
import static org.ebsdimage.io.exp.ops.hough.post.HoughCropXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.hough.post.HoughCrop;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for a <code>HoughCrop</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class HoughCropXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>HoughCrop</code> operation from an XML <code>Element</code>
     * .
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>HoughCrop</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     * @throws BadUnitException
     *             if the radius unit is incorrect
     */
    @Override
    public HoughCrop load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, UnitsXmlTags.ATTR,
                UnitsXmlTags.PX).equals(UnitsXmlTags.PX))
            throw new BadUnitException("Unit for the radius should be "
                    + UnitsXmlTags.PX);

        int radius =
                JDomUtil.getIntegerFromAttribute(element, ATTR_RADIUS,
                        HoughCrop.DEFAULT_RADIUS);

        return new HoughCrop(radius);
    }

}
