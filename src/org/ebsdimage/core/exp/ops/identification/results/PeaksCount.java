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
package org.ebsdimage.core.exp.ops.identification.results;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.QualityIndex;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.ByteMap;

/**
 * Operation to calculate the number of peaks quality index.
 * 
 * @author Philippe T. Pinard
 */
public class PeaksCount extends IdentificationResultsOps {

    /**
     * Calculates the number of peaks quality index of the Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaks
     *            Hough peaks
     * @return one entry with the peaks count quality index
     * 
     * @see QualityIndex#peaksCount(HoughPeak[])
     */
    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] peaks) {
        OpResult result =
                new OpResult(getName(), QualityIndex.peaksCount(peaks),
                        ByteMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Peaks Count";
    }

}
