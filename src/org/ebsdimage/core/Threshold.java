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

import java.io.File;

import rmlimage.core.*;
import rmlshared.io.FileUtil;

/**
 * Threshold of EBSD specific maps (e.g. <code>HoughMap</code>).
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Threshold {

    /**
     * Performs a thresholding based on the standard deviation of the
     * <code>HoughMap</code>.
     * 
     * @param houghMap
     *            a <code>HoughMap</code> to be threshold
     * @return the thresholded map
     * @see Threshold#automaticStdDev(ByteMap)
     */
    public static BinMap automaticStdDev(HoughMap houghMap) {
        return automaticStdDev((ByteMap) houghMap);
    }



    /**
     * Performs a thresholding based on the standard deviation of the
     * <code>HoughMap</code>. The thresholding selects all the pixels that are
     * above a threshold of 2 sigma. If no pixel are above this threshold, a 1
     * sigma threshold is used.
     * 
     * @param houghMap
     *            a <code>ByteMap</code> representation of a
     *            <code>HoughMap</code>
     * @return the thresholded map
     */
    public static BinMap automaticStdDev(ByteMap houghMap) {
        ByteMap dup;
        if (houghMap instanceof HoughMap)
            dup = Conversion.toByteMap((HoughMap) houghMap);
        else
            dup = houghMap.duplicate();

        rmlimage.core.MapMath.not(dup);
        rmlimage.core.MapMath.division(houghMap, dup, 128.0, 0, dup);
        dup.setProperties(houghMap); // Important to get a hough-related binMap

        // Calculate the average and stdDev within the kink
        double average = MapStats.average(dup);
        // System.out.println("average = " + average);
        double stdDev = MapStats.standardDeviation(dup);
        // System.out.println("stdDev = " + stdDev);

        int threshold = (int) (average + 2 * stdDev + 0.5);
        // System.out.println("threshold = " + threshold);
        dup.setProperty("EBSD.threshold.automatic.mode", "2*sigma");

        if (threshold > 255) {
            threshold = (int) (average + stdDev + 0.5);
            dup.setProperty("EBSD.threshold.automatic.mode", "1*sigma");
        }

        if (threshold > 255) {
            threshold = 255;
            dup.setProperty("EBSD.threshold.automatic.mode", "overflow");
        }

        dup.setProperty("EBSD.threshold.automatic.average", average);
        dup.setProperty("EBSD.threshold.automatic.stdDev", stdDev);

        // Do the thresholding
        Filter.median(dup);
        BinMap binMap =
                rmlimage.core.Threshold.densitySlice(dup, threshold, 255);

        MathMorph.opening(binMap, 2, 8);

        return binMap;

    }



    /**
     * Does a thresholding using a tophat-like filter. Peak found above and
     * below the kink at 90deg are removed.
     * <p/>
     * The procedure used can be described by the following code::
     * 
     * <pre>
     * //Do the tophat
     * ByteMap dup = houghMap.duplicate();
     * MathMorph.opening(dup, 5);
     * ByteMap topHat = new ByteMap(houghMap.width, houghMap.height);
     * MapMath.subtraction(houghMap, dup, topHat);
     * 
     * //Calculate the threshold
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
     * 
     * @return the thresholded map
     */
    public static BinMap automaticTopHat(HoughMap houghMap) {
        return automaticTopHat((ByteMap) houghMap);
    }



    /**
     * Does a thresholding using a tophat-like filter. Peak found above and
     * below the kink at 90deg are removed.
     * <p/>
     * See {@link #automaticTopHat(HoughMap)} for more info.
     * 
     * @param houghMap
     *            map to do the thresholding on
     * 
     * @return the thresholded map
     */
    public static BinMap automaticTopHat(ByteMap houghMap) {
        // Do a "top hat" filter
        ByteMap dup = houghMap.duplicate();
        MathMorph.opening(dup, 5);
        ByteMap topHat = new ByteMap(houghMap.width, houghMap.height);
        topHat.setProperties(houghMap); // Important to get a hough-related
        // binMap
        rmlimage.core.MapMath.subtraction(houghMap, dup, 1.0, 0, topHat);

        // Calculate the threshold
        int minError = rmlimage.core.Threshold.minErrorThreshold(topHat);
        int kapur = rmlimage.core.Threshold.kapurThreshold(topHat);
        int threshold = (minError + kapur) / 2;

        dup.setProperty("EBSD.threshold.automatic.minError", minError);
        dup.setProperty("EBSD.threshold.automatic.kapur", kapur);

        // Do the thresholding
        BinMap binMap =
                rmlimage.core.Threshold.densitySlice(topHat, threshold, 255);

        MathMorph.opening(binMap, 2, 8);

        return binMap;
    }



    /**
     * Thresholds a specified phase id from a <code>PhasesMap</code>.
     * 
     * @param src
     *            source <code>PhasesMap</code>
     * @param phaseId
     *            phase id to threshold
     * @param dest
     *            destination map
     * 
     * @throws NullPointerException
     *             if the source or destination map is null
     * @throws IllegalArgumentException
     *             if the phase id is less than 0 or greater than the number of
     *             phases defined in the phases map
     * @throws IllegalArgumentException
     *             if the source and destination map don't have the same
     *             dimensions
     */
    public static void phase(PhasesMap src, int phaseId, BinMap dest) {
        if (src == null)
            throw new NullPointerException("Source phases map cannot be null.");
        if (dest == null)
            throw new NullPointerException("Destination map cannot be null.");

        if (phaseId < 0 || phaseId > src.getPhases().length)
            throw new IllegalArgumentException("Phase id must be between [0, "
                    + src.getPhases().length + "].");

        if (!dest.isSameSize(src))
            throw new IllegalArgumentException("dest (" + dest.getName() + ")("
                    + dest.getDimensionLabel()
                    + ") must be the same size as src (" + src.getName() + ")("
                    + src.getDimensionLabel() + ')');

        byte[] srcPixArray = src.pixArray;
        byte[] destPixArray = dest.pixArray;

        int size = src.size;

        int pixValue;

        for (int n = 0; n < size; n++) {
            pixValue = srcPixArray[n] & 0xff;
            destPixArray[n] = (pixValue == phaseId) ? (byte) 1 : (byte) 0;
        }

        // Copy the properties from the source ByteMap
        dest.clearProperties();
        dest.setProperties(src);

        // Save the min and max threshold in the prop file
        dest.setProperty(Constants.MIN_THRESHOLD, phaseId);
        dest.setProperty(Constants.MAX_THRESHOLD, phaseId);

        dest.setChanged(BinMap.MAP_CHANGED);
    }



    /**
     * Thresholds a specified phase id from a <code>PhasesMap</code>.
     * 
     * @param map
     *            source <code>PhasesMap</code>
     * @param phaseId
     *            phase id to threshold
     * 
     * @return the thresholded map
     * 
     * @throws NullPointerException
     *             if the phases map is null
     * @throws IllegalArgumentException
     *             if the phase id is less than 0 or greater than the number of
     *             phases defined in the phases map
     */
    public static BinMap phase(PhasesMap map, int phaseId) {
        if (map == null)
            throw new NullPointerException("Source phases map cannot be null.");

        if (phaseId < 0 || phaseId > map.getPhases().length)
            throw new IllegalArgumentException("Phase id must be between [0, "
                    + map.getPhases().length + "].");

        BinMap binMap = new BinMap(map.width, map.height);

        String phaseName;
        if (phaseId == 0)
            phaseName = "Non-indexed";
        else
            phaseName = map.getPhases()[phaseId - 1].name;
        File file =
                FileUtil.appendBeforeNumber(map.getFile(), "(Phase_"
                        + phaseName + ')');
        file = FileUtil.setExtension(file, "bmp");
        binMap.setFile(file);

        phase(map, phaseId, binMap);

        return binMap;
    }
}
