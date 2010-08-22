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

import org.ebsdimage.core.PhasesMap;

/**
 * Tags for <code>PhasesMap</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesMapXmlTags {

    /** XML tag name for <code>PhasesMap</code>. */
    public static final String TAG_NAME = PhasesMap.class.getSimpleName();

    /** XML tag name for the defined phases. */
    public static final String TAG_PHASES = "Phases";

    /** XML attribute for the id of a phase. */
    public static final String ATTR_ID = "id";

    /** Version of the <code>PhasesMap</code>. */
    public static final int VERSION = 1;

    /** Header line to identify a <code>PhasesMap</code>. */
    public static final String HEADER = TAG_NAME + " v" + VERSION;
}
