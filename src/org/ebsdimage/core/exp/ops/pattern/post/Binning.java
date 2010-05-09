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

import rmlimage.core.ByteMap;
import rmlimage.core.Transform;

/**
 * Operation to reduce the size of the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class Binning extends PatternPostOps {

    /** Binning size, i.e. reduction factor. */
    public final int size;

    /** Default binning size. */
    public static final int DEFAULT_BINNING_SIZE = 1;



    /**
     * Creates a new binning operation with the default binning size.
     */
    public Binning() {
        size = DEFAULT_BINNING_SIZE;
    }



    /**
     * Creates a new binning operation from the specified binning size.
     * 
     * @param size
     *            binning size
     */
    public Binning(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Binning size (" + size
                    + ") must be > " + 0 + '.');

        this.size = size;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Binning other = (Binning) obj;
        if (size != other.size)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
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
        ByteMap destMap = Transform.binning(srcMap, size, size);
        return destMap;
    }



    @Override
    public String toString() {
        return "Binning [binning size=" + size + "]";
    }

}
