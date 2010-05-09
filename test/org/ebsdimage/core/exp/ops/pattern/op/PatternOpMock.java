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
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;

import rmlimage.core.ByteMap;

public class PatternOpMock extends PatternOp {

    @Override
    public String toString() {
        return "PatternOpMock [index=" + index + "]";
    }



    @Override
    public ByteMap load(Exp exp) throws IOException {
        return new ByteMap(2, 2, new byte[] { 1, 2, 3, 4 });
    }



    public PatternOpMock(int index) {
        super(index);
    }



    public PatternOpMock() {
        super();
    }

}
