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

import java.io.IOException;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.run.Operation;

import rmlimage.core.ByteMap;

/**
 * Superclass of operation to load a pattern. Entry point of the experiment
 * engine.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PatternOp extends Operation {

    /** Index of the pattern. */
    public final int index;

    /** Default value for the index. */
    public static final int DEFAULT_INDEX = 0;



    /**
     * Creates a pattern operation with the default index.
     */
    public PatternOp() {
        index = DEFAULT_INDEX;
    }



    /**
     * Creates a pattern operation with the specified index.
     * 
     * @param index
     *            index of the pattern
     */
    public PatternOp(int index) {
        if (index < 0)
            throw new IllegalArgumentException(
                    "Index cannot be less than zero.");

        this.index = index;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        PatternOp other = (PatternOp) obj;
        if (index != other.index)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + index;
        return result;
    }



    /**
     * Loads a pattern.
     * 
     * @param exp
     *            experiment executing this method
     * @return the pattern map
     * @throws IOException
     *             if an error occurs while loading the pattern
     */
    public abstract ByteMap load(Exp exp) throws IOException;

}
