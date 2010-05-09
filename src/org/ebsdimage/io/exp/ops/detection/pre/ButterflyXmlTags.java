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
package org.ebsdimage.io.exp.ops.detection.pre;

import org.ebsdimage.core.exp.ops.detection.pre.Butterfly;

/**
 * Tags for <code>Butterfly</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class ButterflyXmlTags {

    /** XML tag name for <code>Butterfly</code>. */
    public static final String TAG_NAME = Butterfly.class.getSimpleName();

    /** XML attribute for the butterfly kernel size. */
    public static final String ATTR_KERNELSIZE = "kernelSize";

    /** XML attribute for the lower flattening limit. */
    public static final String ATTR_FLATTEN_LOWERLIMIT = "flattenLowerLimit";

    /** XML attribute for the upper flattening limit. */
    public static final String ATTR_FLATTEN_UPPERLIMIT = "flattenUpperLimit";

}
