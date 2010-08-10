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

import static crystallography.io.LaueGroupXmlTags.ATTR_CRYSTAL_SYSTEM;
import static crystallography.io.LaueGroupXmlTags.ATTR_INDEX;
import static crystallography.io.LaueGroupXmlTags.ATTR_SYMBOL;
import static crystallography.io.LaueGroupXmlTags.TAG_NAME;

import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import crystallography.core.LaueGroup;

/**
 * XML saver for <code>LaueGroup</code>.
 * 
 * @author Philippe T. Pinard
 */
public class LaueGroupXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(LaueGroup)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((LaueGroup) obj);
    }



    /**
     * Saves a <code>LaueGroup</code> to an XML <code>Element</code>.
     * 
     * @param pg
     *            a <code>LaueGroup</code>
     * @return an XML <code>Element</code>
     */
    public Element save(LaueGroup pg) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_INDEX, Integer.toString(pg.index));
        element.setAttribute(ATTR_SYMBOL, pg.symbol);
        element.setAttribute(ATTR_CRYSTAL_SYSTEM, pg.crystalSystem.toString());

        return element;
    }
}
