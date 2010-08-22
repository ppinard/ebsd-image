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
package org.ebsdimage.io.exp.ops.pattern.post;

import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.ATTR_CENTROIDX;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.ATTR_CENTROIDY;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.ATTR_RADIUS;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.pattern.post.MaskDisc;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for a <code>MaskDisc</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class MaskDiscXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>MaskDisc</code> operation from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>MaskDisc</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     * @throws BadUnitException
     *             if the dimensions unit is incorrect
     */
    @Override
    public MaskDisc load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, UnitsXmlTags.ATTR,
                UnitsXmlTags.PX).equals(UnitsXmlTags.PX))
            throw new BadUnitException("Unit for the dimensions should be "
                    + UnitsXmlTags.PX);

        int centroidX =
                JDomUtil.getIntegerFromAttribute(element, ATTR_CENTROIDX,
                        MaskDisc.DEFAULT_CENTROID_X);
        int centroidY =
                JDomUtil.getIntegerFromAttribute(element, ATTR_CENTROIDY,
                        MaskDisc.DEFAULT_CENTROID_Y);
        int radius =
                JDomUtil.getIntegerFromAttribute(element, ATTR_RADIUS,
                        MaskDisc.DEFAULT_RADIUS);

        return new MaskDisc(centroidX, centroidY, radius);
    }

}
