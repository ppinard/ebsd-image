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

import static org.ebsdimage.core.exp.CurrentMapsFileSaver.*;
import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.*;

import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;

/**
 * XML loader for <code>CurrentMapsFileSaver</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CurrentMapsFileSaverXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>CurrentMapsFileSaver</code> from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>CurrentMapsFileSaver</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public CurrentMapsFileSaver load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        boolean saveAllMaps =
                JDomUtil.getBooleanFromAttribute(element, ATTR_SAVEALLMAPS,
                        DEFAULT_SAVEMAPS_ALL);
        boolean savePatternMap =
                JDomUtil.getBooleanFromAttribute(element, ATTR_SAVEPATTERNMAP,
                        DEFAULT_SAVE_PATTERNMAP);
        boolean saveHoughMap =
                JDomUtil.getBooleanFromAttribute(element, ATTR_SAVEHOUGHMAP,
                        DEFAULT_SAVE_HOUGHMAP);
        boolean savePeaksMap =
                JDomUtil.getBooleanFromAttribute(element, ATTR_SAVEPEAKSMAP,
                        DEFAULT_SAVE_PEAKSMAP);
        boolean saveHoughPeaksOverlay =
                JDomUtil.getBooleanFromAttribute(element,
                        ATTR_SAVEHOUGHPEAKSOVERLAY,
                        DEFAULT_SAVE_HOUGHPEAKSOVERLAY);
        boolean saveSolutionOverlay =
                JDomUtil.getBooleanFromAttribute(element,
                        ATTR_SAVESOLUTIONOVERLAY, DEFAULT_SAVE_SOLUTIONOVERLAY);

        return new CurrentMapsFileSaver(saveAllMaps, savePatternMap,
                saveHoughMap, savePeaksMap, saveHoughPeaksOverlay,
                saveSolutionOverlay);
    }

}
