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
package org.ebsdimage.core.exp.ops.detection.op;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Root;

import rmlimage.core.BinMap;

@Root
public class DetectionOpMock extends DetectionOp {

    @Override
    public BinMap detect(Exp exp, HoughMap srcMap) {
        BinMap destMap = new BinMap(srcMap.width, srcMap.height);

        for (int i = 0; i < destMap.size; i++)
            destMap.pixArray[i] = 0;

        return destMap;
    }

}
