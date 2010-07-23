package org.ebsdimage.core.exp.ops.detection.results;

import rmlimage.module.real.core.Stats;

/**
 * Helper methods for detection results operations.
 * 
 * @author ppinard
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
    public static float average(float[] data) {
        if (data.length > 1)
            return Stats.average(data);
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
    public static float standardDeviation(float[] data) {
        if (data.length > 1)
            return Stats.standardDeviation(data);
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
    public static float min(float[] data) {
        if (data.length > 1)
            return Stats.min(data);
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
    public static float max(float[] data) {
        if (data.length > 1)
            return Stats.max(data);
        else
            return Float.NaN;
    }
}
