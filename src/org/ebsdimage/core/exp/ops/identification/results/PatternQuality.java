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

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.MapStats;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;
import static ptpshared.util.Arrays.reverse;
import static java.util.Arrays.sort;

/**
 * Result operation to calculate Oxford's INCA pattern quality index.
 * 
 * @author Philippe T. Pinard
 */
public class PatternQuality extends IdentificationResultsOps {

    /** Default operation. */
    public static final PatternQuality DEFAULT = new PatternQuality();



    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks) {
        OpResult result =
                new OpResult(getName(), calculate(srcPeaks,
                        exp.getSourceHoughMap()), RealMap.class);

        return new OpResult[] { result };
    }



    /**
     * Calculate Oxford's INCA pattern quality index. It consists of the sum of
     * the three most intense peaks divided by 3 times the standard deviation of
     * the Hough map.
     * 
     * @param peaks
     *            list of peaks
     * @param houghMap
     *            hough map
     * @return pattern quality index (PQI)
     */
    public double calculate(HoughPeak[] peaks, HoughMap houghMap) {
        if (peaks.length < 3)
            return 0.0;

        double houghStdDev = MapStats.standardDeviation(houghMap);

        sort(peaks, new HoughPeakIntensityComparator());
        reverse(peaks);

        double quality = 0.0;
        for (int i = 0; i < 3; i++)
            quality += peaks[i].intensity;

        return quality / (3.0 * houghStdDev);
    }



    @Override
    public String toString() {
        return "Pattern Quality";
    }
}
