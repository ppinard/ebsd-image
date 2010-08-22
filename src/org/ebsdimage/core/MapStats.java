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
package org.ebsdimage.core;

import rmlimage.core.ByteMap;
import rmlimage.core.Histogram;
import rmlshared.math.Stats;
import rmlshared.util.Range;

/**
 * Statistical operations on maps. Includes special modifications to
 * {@link rmlimage.core.MapStats} operations specific to EBSD.
 * 
 * @author Philippe T. Pinard
 */
public class MapStats {

    /**
     * Calculates the average pixel value of the <code>ByteMap</code>. <b>Pixels
     * with value = 0 will not be included in the calculation</b>
     * 
     * @param map
     *            <code>ByteMap</code> to get the average pixel value of
     * @return the average pixel value
     * @throws NullPointerException
     *             if the map is null
     */
    public static double average(ByteMap map) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");

        int size = map.size;
        byte[] pixArray = map.pixArray;

        double sum = 0.0;
        int count = 0;
        int pixValue;
        for (int n = 0; n < size; n++) {
            pixValue = pixArray[n] & 0xff;
            if (pixValue != 0) {
                sum += pixValue;
                count++;
            }
        }

        return sum / count;
    }



    /**
     * Calculate the entropy of a <code>ByteMap</code>. The entropy is
     * calculated using the <b>normalized</b> color histogram of the
     * <code>ByteMap</code>. <b>Pixels with value = 0 will not be included in
     * the calculation</b>
     * 
     * <pre>
     *               ---
     *               \
     *  entropy =  -  >  p * log p
     *               /    i       i
     *               ---
     *                i
     * </pre>
     * 
     * @param map
     *            <code>ByteMap</code> to get the entropy of
     * @return the entropy
     * @throws NullPointerException
     *             if the map is null
     */
    public static double entropy(ByteMap map) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");

        Histogram histo = new Histogram(map);
        histo.values[0] = 0; // Do not include the pixel width value=0
        histo.normalize();
        return Stats.entropy(histo.values);
    }



    /**
     * Calculate the population standard deviation of the pixel values of a
     * <code>ByteMap</code>. The standard deviation is the square root of the
     * variance.
     * 
     * @param map
     *            <code>ByteMap</code> to get the standard deviation of
     * @return the population standard deviation
     * @throws NullPointerException
     *             if the map is null
     * @see #variance(ByteMap)
     */
    public static double standardDeviation(ByteMap map) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");

        return Math.sqrt(variance(map));
    }



    /**
     * Calculate the population variance of the pixel values of a
     * <code>ByteMap</code>. The population variance is defined as:
     * 
     * <pre>
     *               N
     *       2      ---              2
     *    __,    1  \     /      _ \
     *   (_)   = -   >   |  x  - x  |
     *           N  /     \  i     /
     *              ---
     *             i = 0
     * </pre>
     * 
     * @param map
     *            <code>ByteMap</code> to get the variance of
     * @return the population variance
     * @throws NullPointerException
     *             if the map is null
     */
    public static double variance(ByteMap map) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");

        int size = map.size;
        byte[] pixArray = map.pixArray;

        double sumSquared = 0.0;
        double sum = 0.0;
        int count = 0;
        int pixValue;
        for (int n = 0; n < size; n++) {
            pixValue = pixArray[n] & 0xff;
            if (pixValue != 0) {
                sumSquared += pixValue * pixValue;
                sum += pixValue;
                count++;
            }
        }

        double average = sum / count;

        return (sumSquared / count) - (average * average);
    }



    /**
     * Returns the highest and lowest pixel values found in the whole
     * <dfn>ByteMap</dfn>. <b>Pixels with value = 0 will not be included in the
     * calculation</b>
     * 
     * @param map
     *            <dfn>ByteMap</dfn> to find the pixel range of
     * @return the highest and lowest pixel values
     * @throws NullPointerException
     *             if the map is null
     */
    public static Range<Integer> range(ByteMap map) {
        if (map == null)
            throw new NullPointerException("Map is null.");

        byte[] pixArray = map.pixArray;
        int size = pixArray.length;

        int min = 255;
        int max = 0;
        int pixValue;
        for (int n = 0; n < size; n++) {
            pixValue = pixArray[n] & 0xff;

            if (pixValue != 0) {
                if (min > pixValue)
                    min = pixValue;
                if (max < pixValue)
                    max = pixValue;
            }
        }

        if (min > max)
            return new Range<Integer>(0, 0);
        else
            return new Range<Integer>(min, max);
    }
}
