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
package crystallography.io;

import static crystallography.io.PlaneXmlTags.ATTR_H;
import static crystallography.io.PlaneXmlTags.ATTR_K;
import static crystallography.io.PlaneXmlTags.ATTR_L;
import static crystallography.io.PlaneXmlTags.TAG_NAME;

import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import crystallography.core.Plane;

/**
 * XML saver for <code>Plane</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PlaneXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(Plane)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Plane) obj);
    }



    /**
     * Saves a <code>Plane</code> to an XML <code>Element</code>.
     * 
     * @param plane
     *            a <code>Plane</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Plane plane) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_H, Integer.toString(plane.get(0)));
        element.setAttribute(ATTR_K, Integer.toString(plane.get(1)));
        element.setAttribute(ATTR_L, Integer.toString(plane.get(2)));

        return element;
    }

}
