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

import crystallography.core.Crystal;

/**
 * Tags for <code>Crystal</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CrystalXmlTags {

    /** XML tag name for <code>Crystal</code>. */
    public static final String TAG_NAME = Crystal.class.getSimpleName();

    /** XML attribute for the crystal's name. */
    public static final String ATTR_NAME = "name";
}
