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
package org.ebsdimage.core.exp.ops.positioning.results;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.exp.*;

import ptpshared.util.Arrays;

/**
 * Superclass of operation to calculate result(s) from the Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PositioningResultsOps extends ExpOperation {

    /**
     * Calculates the result(s) from the Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcPeaks
     *            Hough peaks
     * @return result(s)
     * @throws ExpError
     *             if an error occurs during the execution
     */
    public abstract OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks)
            throws ExpError;



    @Override
    public final Object execute(Exp exp, Object... args) throws ExpError {
        return calculate(exp, (HoughPeak[]) args);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object results) {
        for (OpResult result : (OpResult[]) results)
            listener.positioningResultsPerformed(exp, this, result);
    }



    /**
     * Returns the last index to consider in the calculations. If
     * <code>max</code> is negative, all peaks are considered. If
     * <code>max</code> is positive, the minimum between the maximum number of
     * allowed peaks and the number of peaks in the array is returned.
     * 
     * @param peaks
     *            array of <code>HoughPeak</code>
     * @param max
     *            maximum number of peaks to considered in the calculations
     * @return last index to consider in the array of <code>HoughPeak</code>
     * @throws IllegalArgumentException
     *             if the array of <code>HoughPeak</code> is empty
     * @throws IllegalArgumentException
     *             if the maximum number of peaks is equal to zero
     */
    protected int getLastIndex(HoughPeak[] peaks, int max) {
        if (peaks.length == 0)
            throw new IllegalArgumentException("No Hough peak in arrays.");
        if (max == 0)
            throw new IllegalArgumentException(
                    "Maximum number of peaks is zero.");

        if (max < 0)
            return peaks.length - 1;
        else
            return Math.min(max, peaks.length) - 1;
    }



    /**
     * Sort peaks from the most intense to the least intense peak.
     * 
     * @param peaks
     *            array of <code>HoughPeak</code>
     */
    protected void sortDescending(HoughPeak[] peaks) {
        Arrays.sort(peaks, new HoughPeakIntensityComparator(), true);
    }
}
