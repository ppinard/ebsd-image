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
package org.ebsdimage.io.sim;

import static org.ebsdimage.io.sim.EnergyXmlTags.ATTR_VALUE;
import static org.ebsdimage.io.sim.EnergyXmlTags.TAG_NAME;

import org.ebsdimage.core.sim.Energy;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML saver for an <code>Energy</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class EnergyXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(Energy)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Energy) obj);
    }



    /**
     * Saves an <code>Energy</code> to an XML <code>Element</code>.
     * 
     * @param energy
     *            an <code>Energy</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Energy energy) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_VALUE, Double.toString(energy.value));
        element.setAttribute(UnitsXmlTags.ATTR, UnitsXmlTags.EV);

        return element;
    }

}
