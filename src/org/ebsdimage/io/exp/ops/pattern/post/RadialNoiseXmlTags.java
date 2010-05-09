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
package org.ebsdimage.io.exp.ops.pattern.post;

import org.ebsdimage.core.exp.ops.pattern.post.RadialNoise;

/**
 * Tags for <code>RadialNoise</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class RadialNoiseXmlTags {
    /** XML tag name for <code>RadialNoise</code>. */
    public static final String TAG_NAME = RadialNoise.class.getSimpleName();

    /** XML attribute for the location of the center in x. */
    public static final String ATTR_X = "x";

    /** XML attribute for the location of the center in y. */
    public static final String ATTR_Y = "y";

    /** XML attribute for the standard deviation in x. */
    public static final String ATTR_STDDEVX = "stdDevX";

    /** XML attribute for the standard deviation in y. */
    public static final String ATTR_STDDEVY = "stdDevY";

    /** XML attribute for the initial noise level. */
    public static final String ATTR_INITIALNOISESTDDEV = "initialNoiseStdDev";

    /** XML attribute for the final noise level. */
    public static final String ATTR_FINALNOISESTDDEV = "finalNoiseStdDev";
}
