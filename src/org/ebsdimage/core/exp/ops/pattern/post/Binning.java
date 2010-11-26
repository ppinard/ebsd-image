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
package org.ebsdimage.core.exp.ops.pattern.post;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.Transform;
import rmlshared.math.IntUtil;

/**
 * Operation to reduce the size of the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class Binning extends PatternPostOps {

    /** Default operation. */
    public static final Binning DEFAULT = new Binning(1);

    /** Binning size, i.e. reduction factor. */
    @Attribute(name = "factor")
    public final int factor;



    /**
     * Creates a new binning operation from the specified binning factor.
     * 
     * @param factor
     *            binning factor (must be a power of two)
     */
    public Binning(@Attribute(name = "factor") int factor) {
        if (factor <= 0)
            throw new IllegalArgumentException("Binning size (" + factor
                    + ") must be > " + 0 + '.');
        if (!IntUtil.isPowerOfTwo(factor))
            throw new IllegalArgumentException(
                    "The factor must be a power of two.");

        this.factor = factor;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        Binning other = (Binning) obj;
        if (factor != other.factor)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (!super.equals(obj, precision))
            return false;

        Binning other = (Binning) obj;
        if (Math.abs(factor - other.factor) >= precision)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + factor;
        return result;
    }



    /**
     * Performs a binning of the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input pattern map
     * @return output pattern map
     * @see Transform#binning(ByteMap, int, int)
     */
    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        ByteMap destMap = Transform.binning(srcMap, factor, factor);

        // Apply properties of srcMap
        destMap.setProperties(srcMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Binning [factor=" + factor + "]";
    }

}
