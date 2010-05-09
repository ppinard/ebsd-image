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

import crystallography.core.AtomSite;

/**
 * Tags for <code>AtomSite</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class AtomSiteXmlTags {

    /** XML tag name for <code>AtomSite</code>. */
    public static final String TAG_NAME = AtomSite.class.getSimpleName();

    /** XML position node name. */
    public static final String CHILD_POSITION = "Position";

    /** XML element node name. */
    public static final String CHILD_ELEMENT = "Element";

    /** XML attribute for the atomic number. */
    public static final String ATTR_ATOMICNUMBER = "atomicNumber";
}
