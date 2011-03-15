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

import java.util.Arrays;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import static ptpshared.util.Arrays.reverse;
import static java.util.Arrays.sort;

/**
 * Operation to select the number of Hough Peaks for the indexing operation.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeaksSelector extends IndexingPreOps {

    /** Default operation. */
    public static final HoughPeaksSelector DEFAULT = new HoughPeaksSelector(3,
            5);

    /** Minimum number of Hough peaks. */
    @Attribute(name = "min")
    public final int min;

    /** Maximum number of Hough Peaks. */
    @Attribute(name = "max")
    public final int max;



    /**
     * Creates a new <code>SelectHoughPeaks</code> operation with the specified
     * minimum and maximum number of Hough peaks.
     * 
     * @param min
     *            minimum number of Hough peaks to consider during the indexing
     * @param max
     *            maximum number of Hough peaks to consider during the indexing
     * @throws IllegalArgumentException
     *             if the minimum number of Hough peaks is less than zero
     * @throws IllegalArgumentException
     *             if the maximum number of Hough peaks is less than zero
     * @throws IllegalArgumentException
     *             if the maximum number of Hough peaks is greater than the
     *             minimum number
     */
    public HoughPeaksSelector(@Attribute(name = "min") int min,
            @Attribute(name = "max") int max) {
        if (min < 3)
            throw new IllegalArgumentException(
                    "The minimum number of Hough peaks cannot be less than 3.");
        if (max < 3)
            throw new IllegalArgumentException(
                    "The maximum number of Hough peaks cannot be less than 3.");
        if (min > max)
            throw new IllegalArgumentException(
                    "The maximum number of Hough peaks cannot be greater than the minimum number.");

        this.min = min;
        this.max = max;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        HoughPeaksSelector other = (HoughPeaksSelector) obj;
        if (max != other.max)
            return false;
        if (min != other.min)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + max;
        result = prime * result + min;
        return result;
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
        if (srcPeaks.length < min)
            return new HoughPeak[0];

        if (srcPeaks.length <= max)
            return srcPeaks;
        else {
            sort(srcPeaks, new HoughPeakIntensityComparator());
            reverse(srcPeaks);

            return Arrays.copyOf(srcPeaks, max);
        }
    }



    @Override
    public String toString() {
        return "Hough Peaks Selector [minimum=" + min + ", maximum=" + max
                + "]";
    }

}
