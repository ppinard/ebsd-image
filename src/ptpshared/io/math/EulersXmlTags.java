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

import ptpshared.core.math.Eulers;

/**
 * Tags for <code>Eulers</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class EulersXmlTags {

    /** XML attribute for theta1. */
    public static final String ATTR_THETA1 = "theta1";

    /** XML attribute for theta2. */
    public static final String ATTR_THETA2 = "theta2";

    /** XML attribute for theta3. */
    public static final String ATTR_THETA3 = "theta3";

    /** XML tag name for <code>Eulers</code>. */
    public static final String TAG_NAME = Eulers.class.getSimpleName();
}
