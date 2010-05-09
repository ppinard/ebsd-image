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
package ptpshared.io.math;

import ptpshared.core.math.Vector3D;

/**
 * Tags for <code>Vector3D</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Vector3DXmlTags {
    /** XML tag name for <code>Vector3D</code>. */
    public static final String TAG_NAME = Vector3D.class.getSimpleName();

    /** XML attribute for x coordinate of the vector. */
    public static final String ATTR_X = "x";

    /** XML attribute for y coordinate of the vector. */
    public static final String ATTR_Y = "y";

    /** XML attribute for z coordinate of the vector. */
    public static final String ATTR_Z = "z";
}
