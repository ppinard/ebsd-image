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

import org.ebsdimage.core.exp.CurrentMapsFileSaver;

/**
 * Tags for <code>CurrentMapsFileSaver</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class CurrentMapsFileSaverXmlTags {
    /** XML tag name for Results. */
    public static final String TAG_NAME =
            CurrentMapsFileSaver.class.getSimpleName();

    /** XML attribute for saving all maps. */
    public static final String ATTR_SAVEALLMAPS = "saveAllMaps";

    /** XML attribute for saving the pattern map. */
    public static final String ATTR_SAVEPATTERNMAP = "savePatternMap";

    /** XML attribute for saving the Hough map. */
    public static final String ATTR_SAVEHOUGHMAP = "saveHoughMap";

    /** XML attribute for saving the peaks map. */
    public static final String ATTR_SAVEPEAKSMAP = "savePeaksMap";

    /** XML attribute for saving the solution overlay. */
    public static final String ATTR_SAVESOLUTIONOVERLAY = "saveSolutionOverlay";
}
