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

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.MathMorph;

/**
 * Threshold of EBSD specific maps (e.g. <code>HoughMap</code>).
 * 
 * @author Philippe T. Pinard
 */
public class Threshold {

    /** Property key for the sigma factor value. */
    public static final String KEY_SIGMAFACTOR =
            "EBSD.threshold.automatic.sigmaFactor";

    /** Property key for the average value. */
    public static final String KEY_AVERAGE = "EBSD.threshold.automatic.average";

    /** Property key for the standard deviation value. */
    public static final String KEY_STDDEV = "EBSD.threshold.automatic.stdDev";

    /** Property key for the threshold value. */
    public static final String KEY_THRESHOLD =
            "EBSD.threshold.automatic.threshold";

    /** Property key for the overflow. */
    public static final String KEY_OVERFLOW =
            "EBSD.threshold.automatic.overflow";

    /** Property key for the Kapur thresholding value. */
    public static final String KEY_KAPUR = "EBSD.threshold.automatic.kapur";

    /** Property key for the minimum error thresholding value. */
    public static final String KEY_MINERROR =
            "EBSD.threshold.automatic.minError";



    /**
     * Performs a thresholding based on the standard deviation of the
     * <code>HoughMap</code>. The thresholding selects all the pixels that are
     * above the specified <code>sigmaFactor</code> of the standard deviation.
     * If no pixel are above this threshold, the value of 255 is used as the
     * threshold and the property <code>KEY_OVERFLOW</code> is set to
     * <code>true</code>.
     * 
     * @param map
     *            a <code>ByteMap</code> representation of a
     *            <code>HoughMap</code>
     * @param sigmaFactor
     *            standard deviation scaling factor
     * @return the thresholded map
     * @throws NullPointerException
     *             if the map is null
     * @throws IllegalArgumentException
     *             if the sigma factor is less than 0
     */
    public static BinMap automaticStdDev(ByteMap map, double sigmaFactor) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");
        if (sigmaFactor < 0)
            throw new IllegalArgumentException("Sigma factor (" + sigmaFactor
                    + ") cannot be less than 0.");

        ByteMap dup = map.duplicate();

        // Calculate the average and standard deviation within the kink
        double average = MapStats.average(dup);
        double stdDev = MapStats.standardDeviation(dup);

        int threshold = (int) (average + sigmaFactor * stdDev + 0.5);

        if (threshold == 0)
            threshold = 1;

        // Check if the threshold overflow
        if (threshold > 255) {
            threshold = 255;
            dup.setProperty(KEY_OVERFLOW, "true");
        } else
            dup.setProperty(KEY_OVERFLOW, "false");

        // Set properties
        dup.setProperty(KEY_SIGMAFACTOR, sigmaFactor);
        dup.setProperty(KEY_AVERAGE, average);
        dup.setProperty(KEY_STDDEV, stdDev);
        dup.setProperty(KEY_THRESHOLD, threshold);

        // Do the thresholding
        BinMap binMap =
                rmlimage.core.Threshold.densitySlice(dup, threshold, 255);

        return binMap;

    }



    /**
     * Performs a thresholding based on the standard deviation of the
     * <code>HoughMap</code>.
     * 
     * @param houghMap
     *            a <code>HoughMap</code> to be threshold
     * @param sigmaFactor
     *            standard deviation scaling factor
     * @return the thresholded map
     * @see Threshold#automaticStdDev(ByteMap, double)
     */
    public static BinMap automaticStdDev(HoughMap houghMap, double sigmaFactor) {
        return automaticStdDev((ByteMap) houghMap, sigmaFactor);
    }



    /**
     * Does a thresholding using a tophat-like filter. Peak found above and
     * below the kink at 90deg are removed.
     * <p/>
     * See {@link #automaticTopHat(HoughMap)} for more info.
     * 
     * @param map
     *            map to do the thresholding on
     * @return the thresholded map
     */
    public static BinMap automaticTopHat(ByteMap map) {
        // Do a "top hat" filter
        ByteMap dup = map.duplicate();
        MathMorph.opening(dup, 5);
        ByteMap topHat = new ByteMap(map.width, map.height);

        // Important to get a hough-related binMap
        topHat.setProperties(map);

        rmlimage.core.MapMath.subtraction(map, dup, 1.0, 0, topHat);

        // Calculate the threshold
        int minError = rmlimage.core.Threshold.minErrorThreshold(topHat);
        int kapur = rmlimage.core.Threshold.kapurThreshold(topHat);
        int threshold = (minError + kapur) / 2;

        if (threshold == 0)
            threshold = 1;

        // Set properties
        topHat.setProperty(KEY_MINERROR, minError);
        topHat.setProperty(KEY_KAPUR, kapur);
        topHat.setProperty(KEY_THRESHOLD, threshold);

        // Do the thresholding
        BinMap binMap =
                rmlimage.core.Threshold.densitySlice(topHat, threshold, 255);

        return binMap;
    }



    /**
     * Does a thresholding using a tophat-like filter. Peak found above and
     * below the kink at 90deg are removed.
     * <p/>
     * The procedure used can be described by the following code::
     * 
     * <pre>
     * // Do the tophat
     * ByteMap dup = houghMap.duplicate();
     * MathMorph.opening(dup, 5);
     * ByteMap topHat = new ByteMap(houghMap.width, houghMap.height);
     * MapMath.subtraction(houghMap, dup, topHat);
     * 
     * // Calculate the threshold
     * int minError = Threshold.minErrorThreshold(topHat);
     * int kapur = Threshold.kapurThreshold(topHat);
     * int threshold = (minError + kapur) / 2;
     * BinMap binMap = rmlimage.core.Threshold.densitySlice(topHat, threshold, 255);
     * MathMorph.opening(binMap, 2, 8);
     * return binMap;
     * </pre>
     * 
     * @param houghMap
     *            map to do the thresholding on
     * @return the thresholded map
     */
    public static BinMap automaticTopHat(HoughMap houghMap) {
        return automaticTopHat((ByteMap) houghMap);
    }



    /**
     * Thresholds a specified item from an <code>IndexedByteMap</code>.
     * 
     * @param map
     *            source <code>PhasesMap</code>
     * @param id
     *            id of the item to threshold in the <code>IndexedByteMap</code>
     * @return the thresholded map
     * @throws IllegalArgumentException
     *             if the id corresponds to an unregistered item dimensions
     */
    public static BinMap item(IndexedByteMap<?> map, int id) {
        if (!map.isRegistered(id))
            throw new IllegalArgumentException("Id (" + id
                    + ") is not a registered id of the source map ("
                    + map.getName() + ").");

        return rmlimage.core.Threshold.densitySlice(map, id, id);
    }



    /**
     * Thresholds a specified item from an <code>IndexedByteMap</code>.
     * 
     * @param src
     *            source <code>IndexedByteMap</code>
     * @param id
     *            id of the item to threshold in the <code>IndexedByteMap</code>
     * @param dest
     *            destination map
     * @throws IllegalArgumentException
     *             if the id corresponds to an unregistered item dimensions
     */
    public static void item(IndexedByteMap<?> src, int id, BinMap dest) {
        if (!src.isRegistered(id))
            throw new IllegalArgumentException("Id (" + id
                    + ") is not a registered id of the source map ("
                    + src.getName() + ").");

        rmlimage.core.Threshold.densitySlice(src, id, id, dest);
    }



    /**
     * Thresholds a specified item from an <code>IndexedByteMap</code>.
     * 
     * @param <Item>
     *            type of item in the <code>IndexedByteMap</code>
     * @param map
     *            source <code>PhasesMap</code>
     * @param item
     *            item to threshold in the <code>IndexedByteMap</code>
     * @return the thresholded map
     */
    public static <Item> BinMap item(IndexedByteMap<Item> map, Item item) {
        return item(map, map.getItemId(item));
    }



    /**
     * Thresholds a specified item from an <code>IndexedByteMap</code>.
     * 
     * @param <Item>
     *            type of item in the <code>IndexedByteMap</code>
     * @param map
     *            source <code>PhasesMap</code>
     * @param item
     *            item to threshold in the <code>IndexedByteMap</code>
     * @param dest
     *            destination map
     */
    public static <Item> void item(IndexedByteMap<Item> map, Item item,
            BinMap dest) {
        item(map, map.getItemId(item), dest);
    }
}
