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
package org.ebsdimage.core.exp.ops.pattern.op;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;

public class PatternOpMock extends PatternOp {

    public PatternOpMock(@Attribute(name = "size") int size) {
        super(0, size);
    }



    public PatternOpMock(@Attribute(name = "startIndex") int startIndex,
            @Attribute(name = "size") int size) {
        this(size);
    }



    @Override
    public PatternOp extract(int startIndex, int endIndex) {
        return new PatternOpMock(endIndex - startIndex + 1);
    }



    @Override
    public ByteMap load(Exp exp, int index) {
        return new ByteMap(2, 2, new byte[] { (byte) index, 2, 3, 4 });
    }



    @Override
    public String toString() {
        return "PatternOpMock";
    }

}
