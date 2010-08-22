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

import static crystallography.io.UnitCellXmlTags.*;

import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;
import crystallography.core.UnitCell;

/**
 * XML saver for <code>UnitCell</code>.
 * 
 * @author Philippe T. Pinard
 */
public class UnitCellXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(UnitCell)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((UnitCell) obj);
    }



    /**
     * Saves a <code>UnitCell</code> to an XML <code>Element</code>.
     * 
     * @param unitCell
     *            a <code>UnitCell</code>
     * @return an XML <code>Element</code>
     */
    public Element save(UnitCell unitCell) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_A, Double.toString(unitCell.a));
        element.setAttribute(ATTR_B, Double.toString(unitCell.b));
        element.setAttribute(ATTR_C, Double.toString(unitCell.c));
        element.setAttribute(ATTR_DIM_UNITS, UnitsXmlTags.ANGSTROM);
        element.setAttribute(ATTR_ALPHA, Double.toString(unitCell.alpha));
        element.setAttribute(ATTR_BETA, Double.toString(unitCell.beta));
        element.setAttribute(ATTR_GAMMA, Double.toString(unitCell.gamma));
        element.setAttribute(ATTR_ANGLE_UNITS, UnitsXmlTags.RAD);

        return element;
    }

}
