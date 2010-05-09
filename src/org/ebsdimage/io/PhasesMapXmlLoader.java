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

import java.util.HashMap;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import crystallography.core.Crystal;
import crystallography.io.CrystalXmlLoader;

/**
 * XML loader for the phases of a <code>PhasesMap</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PhasesMapXmlLoader {

    /**
     * Loads the phases for a <code>PhasesMap</code> from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return phases
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect
     */
    public Crystal[] load(Element element) {
        if (!element.getName().equals(TAG_PHASES))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_PHASES + " not " + element.getName() + ".");

        // Load phases
        HashMap<Integer, Crystal> tmpPhases = new HashMap<Integer, Crystal>();

        for (Object obj : element.getChildren()) {
            Element child = (Element) obj;

            int id = JDomUtil.getIntegerFromAttribute(child, ATTR_ID);
            Crystal phase = new CrystalXmlLoader().load(child);

            tmpPhases.put(id, phase);
        }

        // Order phases by id
        Crystal[] phases = new Crystal[tmpPhases.size()];
        Integer[] orderedIds =
                tmpPhases.keySet().toArray(new Integer[tmpPhases.size()]);
        for (int i = 0; i < orderedIds.length; i++)
            phases[i] = tmpPhases.get(orderedIds[i]);

        return phases;
    }

}
