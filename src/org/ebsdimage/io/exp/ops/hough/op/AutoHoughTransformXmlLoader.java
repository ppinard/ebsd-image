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
package org.ebsdimage.io.exp.ops.hough.op;

import static org.ebsdimage.io.exp.ops.hough.op.AutoHoughTransformXmlTags.ATTR_DELTA_THETA;
import static org.ebsdimage.io.exp.ops.hough.op.AutoHoughTransformXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.hough.op.AutoHoughTransform;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for a <code>AutoHoughTransform</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class AutoHoughTransformXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>AutoHoughTransform</code> operation from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>AutoHoughTransform</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     * @throws BadUnitException
     *             if the resolution unit is incorrect
     */
    @Override
    public AutoHoughTransform load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, UnitsXmlTags.ATTR,
                UnitsXmlTags.RAD).equals(UnitsXmlTags.RAD))
            throw new BadUnitException("Unit for the resolution should be "
                    + UnitsXmlTags.RAD);

        double resolution =
                JDomUtil.getDoubleFromAttribute(element, ATTR_DELTA_THETA,
                        AutoHoughTransform.DEFAULT_DELTA_THETA);

        return new AutoHoughTransform(resolution);
    }

}
