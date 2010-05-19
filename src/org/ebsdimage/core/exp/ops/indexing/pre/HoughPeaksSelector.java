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

import static java.util.Arrays.sort;
import static ptpshared.utility.Arrays.reverse;

import java.util.Arrays;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.exp.Exp;

/**
 * Operation to select the number of Hough Peaks for the indexing operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughPeaksSelector extends IndexingPreOps {

    @Override
    public String toString() {
        return "Hough Peaks Selector [minimum=" + minimum + ", maximum="
                + maximum + "]";
    }



    /** Minimum number of Hough peaks. */
    public final int minimum;

    /** Default minimum number of Hough peaks. */
    public static final int DEFAULT_MINIMUM = 3;

    /** Maximum number of Hough Peaks. */
    public final int maximum;

    /** Default maximum number of Hough peaks. */
    public static final int DEFAULT_MAXIMUM = 5;



    /**
     * Creates a new <code>SelectHoughPeaks</code> operation with the default
     * minimum and maximum number of Hough peaks.
     */
    public HoughPeaksSelector() {
        this.maximum = DEFAULT_MAXIMUM;
        this.minimum = DEFAULT_MINIMUM;
    }



    /**
     * Creates a new <code>SelectHoughPeaks</code> operation with the specified
     * minimum and maximum number of Hough peaks.
     * 
     * @param minimum
     *            minimum number of Hough peaks to consider during the indexing
     * @param maximum
     *            maximum number of Hough peaks to consider during the indexing
     * 
     * @throws IllegalArgumentException
     *             if the minimum number of Hough peaks is less than zero
     * @throws IllegalArgumentException
     *             if the maximum number of Hough peaks is less than zero
     * @throws IllegalArgumentException
     *             if the maximum number of Hough peaks is greater than the
     *             minimum number
     */
    public HoughPeaksSelector(int minimum, int maximum) {
        if (minimum < 3)
            throw new IllegalArgumentException(
                    "The minimum number of Hough peaks cannot be less than zero.");
        if (maximum < 3)
            throw new IllegalArgumentException(
                    "The maximum number of Hough peaks cannot be less than zero.");
        if (minimum > maximum)
            throw new IllegalArgumentException(
                    "The maximum number of Hough peaks cannot be greater than the minimum number.");

        this.minimum = minimum;
        this.maximum = maximum;
    }



    /**
     * Performs the Hough peaks selection based on the minimum and maximum
     * number of Hough peaks.
     * <p/>
     * Three cases are possible:
     * <ul>
     * <li>If the number of Hough peaks is less than the minimum value, an empty
     * array is returned.</li>
     * <li>If the number of Hough peaks is less than the maximum value, all the
     * Hough peaks are returned.</li>
     * <li>If the number of Hough peaks is greater than the maximum value, the
     * most intense Hough peaks up to the maximum value are returned.</li>
     * </ul>
     * 
     * @param exp
     *            experiment executing this method
     * @param srcPeaks
     *            input Hough peaks
     * @return output Hough peaks
     */
    @Override
    public HoughPeak[] process(Exp exp, HoughPeak[] srcPeaks) {
        if (srcPeaks.length < minimum)
            return new HoughPeak[0];

        if (srcPeaks.length <= maximum)
            return srcPeaks;
        else {
            sort(srcPeaks, new HoughPeakIntensityComparator());
            reverse(srcPeaks);

            return Arrays.copyOf(srcPeaks, maximum);
        }
    }

}
