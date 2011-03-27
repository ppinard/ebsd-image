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
package org.ebsdimage.core.exp.ops.pattern.post;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;

public class PatternPostOps2Mock extends PatternPostOps {

    @Attribute(name = "var")
    public final int var;



    public PatternPostOps2Mock(@Attribute(name = "var") int var) {
        this.var = var;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PatternPostOps2Mock other = (PatternPostOps2Mock) obj;
        if (var != other.var)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + var;
        return result;
    }



    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        ByteMap destMap = srcMap.duplicate();

        // Multiply by var all the values in the pixArray
        for (int i = 0; i < srcMap.size; i++)
            destMap.pixArray[i] *= var;

        return destMap;
    }

}
