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

import org.ebsdimage.core.exp.ops.pattern.op.PatternFileLoader;

/**
 * Tags for <code>PatternFileLoader</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternFileLoaderXmlTags {

    /** XML tag name for <code>PatternFileLoader</code>. */
    public static final String TAG_NAME =
            PatternFileLoader.class.getSimpleName();

    /** XML attribute for the directory to the pattern file. */
    public static final String ATTR_FILEDIR = "filedir";

    /** XML attribute for the name to the pattern file. */
    public static final String ATTR_FILENAME = "filename";
}
