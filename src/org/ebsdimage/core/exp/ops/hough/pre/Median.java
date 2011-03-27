/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package org.ebsdimage.core.exp.ops.hough.pre;

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.ByteMap;
import rmlimage.core.Filter;

/**
 * Operation to perform a median on the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class Median extends HoughPreOps {

    /** Default operation. */
    public static final Median DEFAULT = new Median();



    /**
     * Performs a median on the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            pattern map
     * @return output map
     */
    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        ByteMap destMap = srcMap.duplicate();

        Filter.median(destMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Median";
    }

}
