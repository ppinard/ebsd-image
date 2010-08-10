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

import crystallography.core.LaueGroup;

/**
 * Tags for <code>LaueGroup</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class LaueGroupXmlTags {

    /** XML tag name for <code>LaueGroup</code>. */
    public static final String TAG_NAME = LaueGroup.class.getSimpleName();

    /** XML attribute for the symbol. */
    public static final String ATTR_SYMBOL = "symbol";

    /** XML attribute for the index. */
    public static final String ATTR_INDEX = "index";

    /** XML attribute for the crystal system. */
    public static final String ATTR_CRYSTAL_SYSTEM = "crystalSystem";
}
