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

import static org.ebsdimage.core.HoughMap.DELTA_R;
import static org.ebsdimage.core.HoughMap.DELTA_THETA;

import java.util.Arrays;

import rmlimage.core.*;
import rmlshared.math.IntUtil;
import rmlshared.math.Stats;

/**
 * Analysis of maps and results.
 * 
 * @author Philippe T. Pinard
 */
public class Analysis {

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
     * Returns a list of the centroid of all the objects in the
     * <code>BinMap</code>. The <code>BinMap</code> must come from the
     * thresholding of a <code>HoughMap</code>. The centroid coordinates are
     * returned in the (r;theta) coordinate system.
     * 
     * @param binMap
     *            <code>BinMap</code> to get the centroid of
     * @return the list of centroid coordinates in (r;theta)
     * @throws NullPointerException
     *             if the bin map is null
     */
    public static Centroid getCentroid(BinMap binMap) {
        if (binMap == null)
            throw new NullPointerException("Bin map cannot be null.");

        IdentMap identMap = Identification.identify(binMap);
        rmlimage.core.Centroid centroids =
                rmlimage.core.Analysis.getCentroid(identMap);

        int nbPeaks = centroids.getValueCount();
        Centroid houghCentroids = new Centroid(nbPeaks);
        for (int n = 0; n < nbPeaks; n++) {
            int index =
                    binMap.getPixArrayIndex((int) (centroids.x[n] + 0.5f),
                            (int) (centroids.y[n] + 0.5f));
            houghCentroids.rho[n] = (float) getR(binMap, index);
            houghCentroids.theta[n] = (float) getTheta(binMap, index);
        }

        return houghCentroids;
    }



    /**
     * Return the maximum pixel color index in each object in the
     * <code>IdentMap</code>. The pixel color index is determined from the
     * specified <code>ByteMap</code>.
     * 
     * @param identMap
     *            <code>IdentMap</code> to get the objects maximum of
     * @param byteMap
     *            corresponding <code>ByteMap</code> to get the maximum color
     *            index from
     * @return the list of the objects maximum pixel color index
     * @throws NullPointerException
     *             if the ident map or the byte map is null
     * @throws IllegalArgumentException
     *             if the ident map and byte map don't have the same size
     */
    public static Maximum getMaximum(IdentMap identMap, ByteMap byteMap) {
        if (identMap == null)
            throw new NullPointerException("Ident map cannot be null.");
        if (byteMap == null)
            throw new NullPointerException("Byte map cannot be null.");

        if (!identMap.isSameSize(byteMap))
            throw new IllegalArgumentException("identMap ("
                    + identMap.getName() + ")(" + identMap.getDimensionLabel()
                    + ") must be the same size as byteMap ("
                    + byteMap.getName() + ")(" + byteMap.getDimensionLabel()
                    + ')');

        int nbObjects = identMap.getObjectCount();

        // If no objects, return empty results
        if (nbObjects <= 0)
            return new Maximum(0);

        int[] maxima = new int[nbObjects + 1]; // +1 for object 0 (background)
        Arrays.fill(maxima, 0); // Initialize the array to 0

        // Calculate the maxima
        int objectNumber;
        short[] identPixArray = identMap.pixArray;
        byte[] bytePixArray = byteMap.pixArray;
        int size = identMap.size;
        for (int n = 0; n < size; n++) {
            objectNumber = identPixArray[n] & 0xff;
            if (objectNumber == 0)
                continue;
            if ((bytePixArray[n] & 0xff) > maxima[objectNumber])
                maxima[objectNumber] = bytePixArray[n] & 0xff;
        }

        // Create a proper Result object
        // and apply the value to the Result object
        Maximum result = new Maximum(nbObjects);
        for (int n = 1; n <= nbObjects; n++)
            result.val[n - 1] = maxima[n];

        return result;
    }



    /**
     * Returns the <code>r</code> coordinate of the specified index of the
     * specified <code>ByteMap</code>. The <code>ByteMap</code> must be holding
     * the {@link HoughMap#DELTA_R} property.
     * 
     * @param map
     *            <code>ByteMap</code> to use
     * @param index
     *            index to the the <code>r</code> coordinate of
     * @return the <code>r</code> coordinate
     * @throws IllegalArgumentException
     *             if <code>map</code> does not hold the
     *             <code>HoughMap.DELTA_R</code> property
     * @throws IllegalArgumentException
     *             if its height is not odd
     * @throws NullPointerException
     *             if the map is null
     */
    public static double getR(Map map, int index) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");

        if (!IntUtil.isOdd(map.height))
            throw new IllegalArgumentException(map.getName() + "'s height ("
                    + map.height + ") must be odd.");

        if (!map.containsProperty(DELTA_R))
            throw new IllegalArgumentException(map.getName()
                    + " does not hold the " + DELTA_R + " property.");

        double deltaR = Double.longBitsToDouble(map.getProperty(DELTA_R, -1L));
        if (deltaR < 0)
            throw new IllegalArgumentException("Incorrect value for property "
                    + DELTA_R + " (" + deltaR + ").");

        return getR(index, map.width, map.height, deltaR);
    }



    /**
     * Returns the <code>r</code> coordinate of the specified index. The
     * dimensions of the HoughMap are specified as arguments.
     * 
     * @param index
     *            index to the the <code>r</code> coordinate of
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param deltaR
     *            rho resolution (y direction)
     * @return the <code>r</code> coordinate
     */
    private static double getR(int index, int width, int height, double deltaR) {
        if (width <= 0)
            throw new IllegalArgumentException("width (" + width
                    + ") must be > " + 0 + '.');
        if (height <= 0)
            throw new IllegalArgumentException("height (" + height
                    + ") must be > " + 0 + '.');
        if (!IntUtil.isOdd(height))
            throw new IllegalArgumentException("height (" + height
                    + ") must be odd.");

        int size = width * height;
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (size - 1));

        if (deltaR <= 0)
            throw new IllegalArgumentException("deltaR (" + deltaR
                    + ") must be > 0");

        // return (height-1 - y) * deltaR + rMin; (equivalent equation)
        return (height - 1 - index / width) * deltaR - deltaR
                * ((height - 1) / 2);
    }



    /**
     * Returns the <code>theta</code> coordinate of the specified index of the
     * specified <code>ByteMap</code>. The <code>ByteMap</code> must be holding
     * the {@link HoughMap#DELTA_THETA} property.
     * 
     * @param map
     *            <code>ByteMap</code> to use
     * @param index
     *            index to the the <code>r</code> coordinate of
     * @return the <code>theta</code> coordinate
     * @throws IllegalArgumentException
     *             if <code>map</code> does not hold the
     *             <code>HoughMap.DELTA_THETA</code> property
     * @throws IllegalArgumentException
     *             if its height is not odd
     * @throws NullPointerException
     *             if the map is null
     */
    public static double getTheta(Map map, int index) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");

        if (!IntUtil.isOdd(map.height))
            throw new IllegalArgumentException(map.getName() + "'s height ("
                    + map.height + ") must be odd.");

        if (!map.containsProperty(HoughMap.DELTA_THETA))
            throw new IllegalArgumentException(map.getName()
                    + " does not hold the " + HoughMap.DELTA_THETA
                    + " property.");

        double deltaTheta =
                Double.longBitsToDouble(map.getProperty(DELTA_THETA, -1L));
        if (deltaTheta < 0)
            throw new IllegalArgumentException("Incorrect value for property "
                    + DELTA_THETA + " (" + deltaTheta + ").");

        return getTheta(index, map.width, deltaTheta);
    }



    /**
     * Returns the <code>theta</code> coordinate of the specified index. The
     * dimensions of the HoughMap are specified as arguments.
     * 
     * @param index
     *            index to the the <code>r</code> coordinate of
     * @param width
     *            width of the map
     * @param deltaTheta
     *            theta resolution (x direction)
     * @return the <code>theta</code> coordinate
     */
    private static double getTheta(int index, int width, double deltaTheta) {
        return (index % width) * deltaTheta;
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

}
