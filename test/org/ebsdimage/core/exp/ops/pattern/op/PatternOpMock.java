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
import org.simpleframework.xml.Root;

import rmlimage.core.ByteMap;

@Root
public class PatternOpMock extends PatternOp {

    public PatternOpMock() {
        super(0, 1);
    }



    public PatternOpMock(int size) {
        super(0, size);
    }



    @Override
    public String toString() {
        return "PatternOpMock";
    }



    @Override
    public ByteMap load(Exp exp, int index) throws IOException {
        return new ByteMap(2, 2, new byte[] { (byte) index, 2, 3, 4 });
    }



    @Override
    public PatternOp extract(int startIndex, int endIndex) {
        return new PatternOpMock(endIndex - startIndex + 1);
    }

}
