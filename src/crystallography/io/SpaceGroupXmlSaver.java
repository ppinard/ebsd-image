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

import static crystallography.io.SpaceGroupXmlTags.ATTR_INDEX;
import static crystallography.io.SpaceGroupXmlTags.TAG_NAME;

import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import crystallography.core.SpaceGroup;

/**
 * XML saver for <code>SpaceGroup</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SpaceGroupXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(SpaceGroup)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((SpaceGroup) obj);
    }



    /**
     * Saves a <code>SpaceGroup</code> to an XML <code>Element</code>.
     * 
     * @param sg
     *            a <code>SpaceGroup</code>
     * @return an XML <code>Element</code>
     */
    public Element save(SpaceGroup sg) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_INDEX, Integer.toString(sg.index));

        return element;
    }
}
