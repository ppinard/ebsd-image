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
package org.ebsdimage.io.exp;

import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.*;

import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * XML saver for <code>CurrentMapsFileSaver</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class CurrentMapsFileSaverXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(CurrentMapsFileSaver)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((CurrentMapsFileSaver) obj);
    }



    /**
     * Saves a <code>CurrentMapsFileSaver</code> to an XML <code>Element</code>.
     * 
     * @param saveMaps
     *            a <code>CurrentMapsFileSaver</code>
     * @return an XML <code>Element</code>
     */
    public Element save(CurrentMapsFileSaver saveMaps) {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_SAVEALLMAPS, Boolean
                .toString(saveMaps.saveAllMaps));
        element.setAttribute(ATTR_SAVEPATTERNMAP, Boolean
                .toString(saveMaps.savePatternMap));
        element.setAttribute(ATTR_SAVEHOUGHMAP, Boolean
                .toString(saveMaps.saveHoughMap));
        element.setAttribute(ATTR_SAVEPEAKSMAP, Boolean
                .toString(saveMaps.savePeaksMap));
        element.setAttribute(ATTR_SAVESOLUTIONOVERLAY, Boolean
                .toString(saveMaps.saveSolutionOverlay));

        return element;
    }

}
