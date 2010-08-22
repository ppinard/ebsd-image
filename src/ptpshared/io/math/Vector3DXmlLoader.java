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
package ptpshared.io.math;

import static ptpshared.io.math.Vector3DXmlTags.ATTR_X;
import static ptpshared.io.math.Vector3DXmlTags.ATTR_Y;
import static ptpshared.io.math.Vector3DXmlTags.ATTR_Z;
import static ptpshared.io.math.Vector3DXmlTags.TAG_NAME;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.core.math.Vector3D;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;

/**
 * XML loader for <code>Vector3D</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3DXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>Vector3D</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>Vector3D</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public Vector3D load(Element element) {
        return load(element, TAG_NAME);
    }



    /**
     * Loads a <code>Vector3D</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param tagName
     *            name of the XML <code>Element</code>'s tag
     * @return a <code>Vector3D</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    public Vector3D load(Element element, String tagName) {
        if (!element.getName().equals(tagName))
            throw new IllegalNameException("Name of the element should be "
                    + tagName + " not " + element.getName() + ".");

        return new Vector3D(JDomUtil.getDoubleFromAttribute(element, ATTR_X),
                JDomUtil.getDoubleFromAttribute(element, ATTR_Y),
                JDomUtil.getDoubleFromAttribute(element, ATTR_Z));
    }

}
