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
package org.ebsdimage.core.exp.ops.hough.pre;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOps;

import rmlimage.core.ByteMap;

public class HoughPreOpsMock extends HoughPreOps {

    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        byte[] pixArray = srcMap.pixArray.clone();

        // Multiply by 2 all the values in the pixArray
        for (int i = 0; i < srcMap.size; i++)
            pixArray[i] += 1;

        return new ByteMap(2, 2, pixArray);
    }

}
