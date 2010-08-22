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
package org.ebsdimage.io.sim.ops.patternsim.op;

import org.ebsdimage.core.sim.ops.patternsim.op.PatternSimOp;

/**
 * Tags for <code>PatternSimOp</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PatternSimOpXmlTags {
    /** XML tag name for <code>PatternSimOp</code>. */
    public static final String TAG_NAME = PatternSimOp.class.getSimpleName();

    /** XML attribute for the width. */
    public static final String ATTR_WIDTH = "width";

    /** XML attribute for the height. */
    public static final String ATTR_HEIGHT = "height";

    /** XML attribute for the maximum index. */
    public static final String ATTR_MAXINDEX = "maxIndex";

    /** XML attribute for the type of scattering factors. */
    public static final String ATTR_SCATTER_TYPE = "scatterType";
}
