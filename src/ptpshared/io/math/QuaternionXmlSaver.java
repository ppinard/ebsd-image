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

import static ptpshared.io.math.QuaternionXmlTags.*;

import org.jdom.Element;

import ptpshared.core.math.Quaternion;
import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * Saves <code>Quaternion</code> object.
 * 
 * @author Philippe T. Pinard
 */
public class QuaternionXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(Quaternion)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Quaternion) obj);
    }



    /**
     * Saves the <code>Quaternion</code> to an XML <code>Element</code>.
     * 
     * @param q
     *            a <code>Quaternion</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Quaternion q) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_Q0, Double.toString(q.getQ0()));
        element.setAttribute(ATTR_Q1, Double.toString(q.getQ1()));
        element.setAttribute(ATTR_Q2, Double.toString(q.getQ2()));
        element.setAttribute(ATTR_Q3, Double.toString(q.getQ3()));

        return element;
    }
}
