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

import ptpshared.core.math.Quaternion;

/**
 * Tags for <code>Quaternion</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class QuaternionXmlTags {

    /** XML tag name for <code>Quaternion</code>. */
    public static final String TAG_NAME = Quaternion.class.getSimpleName();

    /** XML attribute for the scalar value. */
    public static final String ATTR_Q0 = "q0";

    /** XML attribute for the coefficient of <em>i</em>. */
    public static final String ATTR_Q1 = "q1";

    /** XML attribute for the coefficient of <em>j</em>. */
    public static final String ATTR_Q2 = "q2";

    /** XML attribute for the coefficient of <em>k</em>. */
    public static final String ATTR_Q3 = "q3";
}
