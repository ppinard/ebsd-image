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

import static crystallography.io.PointGroupXmlTags.*;

import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import crystallography.core.PointGroup;

/**
 * XML saver for <code>PointGroup</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PointGroupXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(PointGroup)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((PointGroup) obj);
    }



    /**
     * Saves a <code>PointGroup</code> to an XML <code>Element</code>.
     * 
     * @param pg
     *            a <code>PointGroup</code>
     * @return an XML <code>Element</code>
     */
    public Element save(PointGroup pg) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_LAUE_GROUP, Integer.toString(pg.laueGroup));
        element.setAttribute(ATTR_SCHOENFLIES, pg.schoenfliesSymbol);
        element.setAttribute(ATTR_HM, pg.hmSymbol);
        element.setAttribute(ATTR_CRYSTAL_SYSTEM, pg.crystalSystem.toString());

        return element;
    }
}
