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

import rmlimage.module.real.core.RealMap;

/**
 * Operation to calculate the Hough peak range quality index.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeakRange extends IdentificationResultsOps {

    @Override
    public String toString() {
        return "Hough Peak Range";
    }



    /**
     * Calculates the Hough peak range quality index of the Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaks
     *            Hough peaks
     * @return one entry with the Hough peak range quality index
     * 
     * @see QualityIndex#houghPeakRange(HoughPeak[])
     */
    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] peaks) {
        OpResult result =
                new OpResult(getName(), QualityIndex.houghPeakRange(peaks),
                        RealMap.class);

        return new OpResult[] { result };
    }

}
