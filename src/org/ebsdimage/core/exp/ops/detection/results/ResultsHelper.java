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
package org.ebsdimage.core.exp.ops.detection.results;

import rmlshared.math.Stats;

/**
 * Helper methods for detection results operations.
 * 
 * @author Philippe T. Pinard
 */
public class ResultsHelper {

    /**
     * Calculates the average of the array. If the array doesn't contain at
     * least 2 values, <code>Float.NaN</code> is returned.
     * 
     * @param data
     *            set of data
     * @return average
     */
    public static double average(double[] data) {
        if (data.length > 1)
            return Stats.average(data);
        else
            return Float.NaN;
    }



    /**
     * Calculates the maximum value of the array. If the array doesn't contain
     * at least 2 values, <code>Float.NaN</code> is returned.
     * 
     * @param data
     *            set of data
     * @return standard deviation
     */
    public static double max(double[] data) {
        if (data.length > 1)
            return Stats.max(data);
        else
            return Float.NaN;
    }



    /**
     * Calculates the minimum value of the array. If the array doesn't contain
     * at least 2 values, <code>Float.NaN</code> is returned.
     * 
     * @param data
     *            set of data
     * @return standard deviation
     */
    public static double min(double[] data) {
        if (data.length > 1)
            return Stats.min(data);
        else
            return Float.NaN;
    }



    /**
     * Calculates the standard deviation of the array. If the array doesn't
     * contain at least 2 values, <code>Float.NaN</code> is returned.
     * 
     * @param data
     *            set of data
     * @return standard deviation
     */
    public static double standardDeviation(double[] data) {
        if (data.length > 1)
            return Stats.standardDeviation(data);
        else
            return Float.NaN;
    }
}
