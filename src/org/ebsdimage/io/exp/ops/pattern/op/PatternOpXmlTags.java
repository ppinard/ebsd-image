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
package org.ebsdimage.io.exp.ops.pattern.op;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;

/**
 * Tags for <code>PatternOp</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternOpXmlTags {

    /** XML tag name for <code>PatternOp</code>. */
    public static final String TAG_NAME = PatternOp.class.getSimpleName();

    /** XML attribute for the pattern's index. */
    public static final String ATTR_INDEX = "index";

    /** XML attribute for whether the file path is relative. */
    public static final String ATTR_IS_RELATIVE = "isRelative";
}
