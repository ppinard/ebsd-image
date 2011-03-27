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
package org.ebsdimage.core.exp.ops.identification.op;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Root;

import rmlimage.core.BinMap;

@Root
public class IdentificationOpMock extends IdentificationOp {

    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        HoughPeak[] peaks = new HoughPeak[peaksMap.height];

        for (int i = 0; i < peaksMap.height; i++)
            peaks[i] =
                    new HoughPeak(peaksMap.pixArray[i],
                            houghMap.pixArray[i] / 100.0, i);

        return peaks;
    }

}
