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
package org.ebsdimage.io.exp.ops.pattern.results;

import static org.ebsdimage.io.exp.ops.pattern.results.SumRegionXmlTags.*;

import org.ebsdimage.core.exp.ops.pattern.results.SumRegion;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;

/**
 * XML loader for an <code>SumRegion</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class SumRegionXmlLoader implements ObjectXmlLoader {

    /**
     * Loads an <code>SumRegion</code> operation from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return an <code>SumRegion</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public SumRegion load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        double xmin =
                JDomUtil.getDoubleFromAttribute(element, ATTR_XMIN,
                        SumRegion.DEFAULT_XMIN);
        double xmax =
                JDomUtil.getDoubleFromAttribute(element, ATTR_XMAX,
                        SumRegion.DEFAULT_XMAX);
        double ymin =
                JDomUtil.getDoubleFromAttribute(element, ATTR_YMIN,
                        SumRegion.DEFAULT_YMIN);
        double ymax =
                JDomUtil.getDoubleFromAttribute(element, ATTR_YMAX,
                        SumRegion.DEFAULT_XMAX);

        return new SumRegion(xmin, ymin, xmax, ymax);
    }

}
