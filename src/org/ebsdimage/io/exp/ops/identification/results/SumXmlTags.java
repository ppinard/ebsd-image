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
package org.ebsdimage.io.exp.ops.identification.results;

import org.ebsdimage.core.exp.ops.identification.results.Sum;

/**
 * Tags for <code>Sum</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SumXmlTags {

    /** XML tag name for <code>Sum</code>. */
    public static final String TAG_NAME = Sum.class.getSimpleName();

    /**
     * XML attribute for the maximum number of peaks to considered in the
     * calculations.
     */
    public static final String ATTR_MAX = "max";
}
