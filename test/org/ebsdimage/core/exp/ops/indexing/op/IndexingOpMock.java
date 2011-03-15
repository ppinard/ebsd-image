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
package org.ebsdimage.core.exp.ops.indexing.op;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Root;

import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

@Root
public class IndexingOpMock extends IndexingOp {

    @Override
    public Solution[] index(Exp exp, HoughPeak[] srcPeaks) {
        Solution[] slns = new Solution[srcPeaks.length];

        Crystal phase = CrystalFactory.silicon();
        Rotation rotation = Rotation.IDENTITY;

        for (int i = 0; i < slns.length; i++)
            slns[i] =
                    new Solution(phase, rotation, (double) i / srcPeaks.length);

        return slns;
    }

}
