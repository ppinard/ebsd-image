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

public class PatternPostOpsMock extends PatternPostOps {

    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        byte[] pixArray = srcMap.pixArray.clone();

        // Multiply by 2 all the values in the pixArray
        for (int i = 0; i < srcMap.size; i++)
            pixArray[i] *= 2;

        return new ByteMap(srcMap.width, srcMap.height, pixArray);
    }

}
