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
package org.ebsdimage.core.exp.ops.pattern.op;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpListener;
import org.ebsdimage.core.exp.ExpOperation;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;

/**
 * Superclass of operation to load one or many patterns. Entry point of the
 * experiment engine.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PatternOp extends ExpOperation {

    /** First index of the patterns. */
    @Attribute(name = "startIndex")
    public final int startIndex;



    @Override
    public final Object execute(Exp exp, Object... args) {
        return load(exp, (Integer) args[0]);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object result) {
        listener.patternOpPerformed(exp, this, (ByteMap) result);
    }

    /** Number of patterns to load. */
    @Attribute(name = "size")
    public final int size;



    /**
     * Creates a pattern operation with the specified index.
     * 
     * @param startIndex
     *            first index of the patterns
     * @param size
     *            number of patterns
     * @throws IllegalArgumentException
     *             if the start index is less than 0
     * @throws IllegalArgumentException
     *             if the size is less than 1
     */
    public PatternOp(int startIndex, int size) {
        if (startIndex < 0)
            throw new IllegalArgumentException(
                    "Index cannot be less than zero.");
        if (size < 1)
            throw new IllegalArgumentException(
                    "The size must be equal or greater than 1.");

        this.startIndex = startIndex;
        this.size = size;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PatternOp other = (PatternOp) obj;
        if (size != other.size)
            return false;
        if (startIndex != other.startIndex)
            return false;

        return true;
    }



    /**
     * Extracts the current <code>PattenrOp</code> in a smaller
     * <code>PattenrOp</code> containing less patterns.
     * 
     * @param startIndex
     *            index of the first pattern in the split
     * @param endIndex
     *            index of the last pattern in the split
     * @return reduced <code>PattenrOp</code>
     */
    public abstract PatternOp extract(int startIndex, int endIndex);



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + size;
        result = prime * result + startIndex;
        return result;
    }



    /**
     * Loads a pattern.
     * 
     * @param exp
     *            experiment executing this method
     * @param index
     *            index of the pattern to load
     * @return the pattern map
     */
    public abstract ByteMap load(Exp exp, int index);

}
