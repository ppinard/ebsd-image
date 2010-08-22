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
package org.ebsdimage.io.run;

import org.jdom.Element;

import ptpshared.utility.xml.JDomUtil;

/**
 * Utilities for <code>Operation</code> XML loaders.
 * 
 * @author Philippe T. Pinard
 */
public class OperationXmlLoader {
    /**
     * Return the order of an <code>Operation</code>. The order is stored in the
     * text value of the XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return order
     */
    public static int getOrder(Element element) {
        return JDomUtil.getIntegerFromText(element, 1);
    }
}
