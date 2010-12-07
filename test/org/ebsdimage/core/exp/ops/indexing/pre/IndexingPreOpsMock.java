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
package org.ebsdimage.core.exp.ops.indexing.pre;

import magnitude.core.Magnitude;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;

public class IndexingPreOpsMock extends IndexingPreOps {

    @Override
    public HoughPeak[] process(Exp exp, HoughPeak[] srcPeaks) {
        HoughPeak[] destPeaks = new HoughPeak[srcPeaks.length];

        Magnitude term = new Magnitude(1, srcPeaks[0].rho);
        for (int i = 0; i < srcPeaks.length; i++)
            destPeaks[i] =
                    new HoughPeak(srcPeaks[i].theta, srcPeaks[i].rho.add(term));

        return destPeaks;
    }

}
