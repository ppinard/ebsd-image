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
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps;

import rmlimage.core.ByteMap;

public class PatternPostOps2Mock extends PatternPostOps {

    public final int var;

    public static final int defaultVar = 1;



    public PatternPostOps2Mock() {
        var = defaultVar;
    }



    public PatternPostOps2Mock(int var) {
        this.var = var;
    }



    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        byte[] pixArray = srcMap.pixArray.clone();

        // Multiply by var all the values in the pixArray
        for (int i = 0; i < srcMap.size; i++)
            pixArray[i] *= var;

        return new ByteMap(srcMap.width, srcMap.height, pixArray);
    }

}
