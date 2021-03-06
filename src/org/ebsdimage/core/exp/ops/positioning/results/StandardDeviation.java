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
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;
import org.simpleframework.xml.Attribute;

import rmlimage.module.real.core.RealMap;
import rmlshared.math.Stats;

/**
 * Operation to calculate the standard deviation between the Hough peaks'
 * intensities.
 * 
 * @author Philippe T. Pinard
 */
public class StandardDeviation extends PositioningResultsOps {

    /** Default operation. */
    public static final StandardDeviation DEFAULT = new StandardDeviation(-1);

    /** Maximum number of peaks to consider in the calculations. */
    @Attribute(name = "max")
    public final int max;



    /**
     * Creates a new <code>StandardDeviation</code> result operation.
     * 
     * @param max
     *            maximum number of peaks to consider in the calculations
     * @throws IllegalArgumentException
     *             if the maximum number of peaks is zero
     */
    public StandardDeviation(@Attribute(name = "max") int max) {
        if (max == 0)
            throw new IllegalArgumentException(
                    "The maximum number of peaks cannot be zero.");
        this.max = max;
    }



    /**
     * Calculates the standard deviation between intensities of the Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaks
     *            Hough peaks
     * @return one entry with the result
     */
    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] peaks) {
        double value;

        if (peaks.length == 0)
            value = 0.0;
        else {
            int lastIndex = getLastIndex(peaks, max);
            sortDescending(peaks);

            double[] intensities = new double[lastIndex + 1];
            for (int i = 0; i <= lastIndex; i++)
                intensities[i] = peaks[i].intensity;

            value = Stats.standardDeviation(intensities);
        }

        String name = "Peaks Standard Deviation [max=" + max + "]";
        OpResult result = new OpResult(name, value, RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        StandardDeviation other = (StandardDeviation) obj;
        if (max != other.max)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + max;
        return result;
    }



    @Override
    public String toString() {
        return "Standard Deviation [max=" + max + "]";
    }

}
