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
package org.ebsdimage.core.exp.ops.identification.pre;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.BinMap;
import rmlimage.core.MathMorph;

/**
 * Operation to perform a dilation on the peaks map to increase the size of the
 * Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public class Dilation extends IdentificationPreOps {

    /** Default operation. */
    public static final Dilation DEFAULT = new Dilation(2, 8);

    /**
     * Minimum number of <code>ON</code> neighbor to an <code>OFF</code> pixel
     * for it to be turned to <code>ON</code>.
     */
    @Attribute(name = "min")
    public final int min;

    /**
     * Maximum number of <code>ON</code> neighbor to an <code>OFF</code> pixel
     * for it to be turned to <code>ON</code>.
     */
    @Attribute(name = "max")
    public final int max;



    /**
     * Creates a new <code>Opening</code> operation.
     * 
     * @param min
     *            minimum number of <code>ON</code> neighbor to an
     *            <code>OFF</code> pixel for it to be turned to <code>ON</code>
     * @param max
     *            maximum number of <code>OFF</code> neighbor to an
     *            <code>OFF</code> pixel for it to be turned to <code>ON</code>
     */
    public Dilation(@Attribute(name = "min") int min,
            @Attribute(name = "max") int max) {
        if (min < 0 || min > 8)
            throw new IllegalArgumentException("min (" + min
                    + ") must be between 0 and 8.");

        if (max < 0 || max > 8)
            throw new IllegalArgumentException("max (" + max
                    + ") must be between 0 and 8.");

        this.min = min;
        this.max = max;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        Dilation other = (Dilation) obj;
        if (max != other.max)
            return false;
        if (min != other.min)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + max;
        result = prime * result + min;
        return result;
    }



    /**
     * Applies a dilation on the peaks map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input peaks map
     * @return output peaks map
     * @see MathMorph#dilation
     */
    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        BinMap destMap = srcMap.duplicate();

        MathMorph.dilation(destMap, min, max);

        return destMap;
    }



    @Override
    public String toString() {
        return "Dilation";
    }

}
