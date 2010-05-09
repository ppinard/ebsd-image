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
package org.ebsdimage.io;

import static org.ebsdimage.io.PhasesMapXmlTags.ATTR_ID;
import static org.ebsdimage.io.PhasesMapXmlTags.TAG_PHASES;

import org.jdom.Element;

import crystallography.core.Crystal;
import crystallography.io.CrystalXmlSaver;

/**
 * XML saver for <code>PhasesMap</code>'s defined phases.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PhasesMapXmlSaver {

    /**
     * Saves the defined phases of a <code>PhasesMap</code> to an XML
     * <code>Element</code>.
     * 
     * @param phases
     *            a array of <code>Crystal</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Crystal[] phases) {
        Element element = new Element(TAG_PHASES);

        for (int i = 0; i < phases.length; i++) {
            Element child = new CrystalXmlSaver().save(phases[i]);
            child.setAttribute(ATTR_ID, Integer.toString(i + 1));
            element.addContent(child);
        }

        return element;
    }

}
