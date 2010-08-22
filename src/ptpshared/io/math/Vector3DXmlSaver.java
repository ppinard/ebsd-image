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

import ptpshared.core.math.Vector3D;
import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * Saves <code>Vector3D</code> object.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3DXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(Vector3D)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Vector3D) obj);
    }



    /**
     * Saves the <code>Vector3D</code> to an XML <code>Element</code>.
     * 
     * @param vector
     *            a <code>Vector3D</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Vector3D vector) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_X, Double.toString(vector.get(0)));
        element.setAttribute(ATTR_Y, Double.toString(vector.get(1)));
        element.setAttribute(ATTR_Z, Double.toString(vector.get(2)));

        return element;
    }

}
