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
package org.ebsdimage.core;

import rmlimage.core.Result;

/**
 * Results consisting of maximum values.
 * 
 * @author Philippe T. Pinard
 */
public class Maximum extends Result {

    /** Maximum values. */
    public final float[] val;



    /**
     * Creates a new <code>Maximum</code> containing a number of maximums.
     * 
     * @param nbValues
     *            number of maximums
     */
    public Maximum(int nbValues) {
        super(1, nbValues);
        name[0] = "Maximum";

        units[0] = "";

        val = values[0];
    }



    @Override
    public Maximum duplicate() {
        Maximum dup = new Maximum(val.length);
        System.arraycopy(val, 0, dup.val, 0, val.length);
        return dup;
    }

}
