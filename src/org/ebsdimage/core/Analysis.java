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

import static java.lang.Math.PI;
import static org.ebsdimage.core.HoughMap.DELTA_R;
import static org.ebsdimage.core.HoughMap.DELTA_THETA;

import java.util.ArrayList;
import java.util.Arrays;

import rmlimage.core.IdentMap;
import rmlimage.core.Map;
import rmlshared.math.IntUtil;

/**
 * Analysis of maps and results.
 * 
 * @author Philippe T. Pinard
 */
public class Analysis {

    /**
     * Returns a list of the centroid of all the objects in the
     * <code>BinMap</code>. The <code>BinMap</code> must come from the
     * thresholding of a <code>HoughMap</code>. The centroid coordinates are
     * returned in the (r;theta) coordinate system.
     * 
     * @param peaksMap
     *            <code>IdentMap</code> to get the centroid of
     * @return the list of centroid coordinates in (r;theta)
     * @throws NullPointerException
     *             if the bin map is null
     */
    public static HoughPoint getCentroid(IdentMap peaksMap) {
        if (peaksMap == null)
            throw new NullPointerException("Bin map cannot be null.");

        rmlimage.core.Centroid centroids =
                rmlimage.core.Analysis.getCentroid(peaksMap);

        int nbPeaks = centroids.getValueCount();
        HoughPoint houghCentroids = new HoughPoint(nbPeaks);

        for (int n = 0; n < nbPeaks; n++) {
            int index =
                    peaksMap.getPixArrayIndex((int) (centroids.x[n] + 0.5f),
                            (int) (centroids.y[n] + 0.5f));

            houghCentroids.rho[n] = (float) getR(peaksMap, index);
            houghCentroids.theta[n] = (float) getTheta(peaksMap, index);
        }

        return houghCentroids;
    }



    /**
     * Returns a list of center of mass of each object in the
     * <code>BinMap</code>.
     * 
     * @param houghMap
     *            hough map where the intensity (mass) will be taken
     * @param peaksMap
     *            map where the Hough peaks are detected
     * @return the list of points in the Hough (rho, theta)
     * @throws NullPointerException
     *             if either the hough map or peaks map is null
     */
    public static HoughPoint getCenterOfMass(HoughMap houghMap,
            IdentMap peaksMap) {
        if (houghMap == null)
            throw new NullPointerException("Hough map cannot be null.");
        if (peaksMap == null)
            throw new NullPointerException("Peaks map cannot be null.");

        // Apply houghMap properties on peaksMap
        peaksMap.setProperties(houghMap);

        int nbObjects = peaksMap.getObjectCount();

        // +1 for object 0 (background)
        double[] massRho = new double[nbObjects + 1];
        double[] massTheta = new double[nbObjects + 1];
        double[] area = new double[nbObjects + 1];

        // Initialize the array to 0
        Arrays.fill(massRho, 0);
        Arrays.fill(massTheta, 0);
        Arrays.fill(area, 0);

        int width = peaksMap.width;
        int height = peaksMap.height;
        short[] pixArray = peaksMap.pixArray;

        short objectNumber;
        int n = 0;
        double rho;
        double theta;
        double value;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                theta = getTheta(peaksMap, n);
                rho = getR(peaksMap, n);

                value = houghMap.pixArray[houghMap.getIndex(rho, theta)] & 0xff;

                massRho[objectNumber] += rho * value;
                massTheta[objectNumber] += theta * value;
                area[objectNumber] += value;

                n++;
            }
        }

        // Calculate rhos and thetas
        ArrayList<Float> rhos = new ArrayList<Float>();
        ArrayList<Float> thetas = new ArrayList<Float>();

        for (n = 0; n < nbObjects; n++) {
            if (area[n + 1] == 0.0)
                continue;

            rhos.add((float) (massRho[n + 1] / area[n + 1]));
            thetas.add((float) (massTheta[n + 1] / area[n + 1]));
        }

        // Create Hough points
        HoughPoint points = new HoughPoint(nbObjects);

        for (n = 0; n < rhos.size(); n++) {
            points.rho[n] = rhos.get(n);
            points.theta[n] = thetas.get(n);
        }

        return points;
    }



    /**
     * Returns the location of the maximum for each object in the
     * <code>BinMap</code>.
     * 
     * @param houghMap
     *            hough map where the intensity (mass) will be taken
     * @param peaksMap
     *            map where the Hough peaks are detected
     * @return the list of points in the Hough (rho, theta)
     * @throws NullPointerException
     *             if either the hough map or peaks map is null
     */
    public static HoughPoint getMaximumLocation(HoughMap houghMap,
            IdentMap peaksMap) {
        if (houghMap == null)
            throw new NullPointerException("Hough map cannot be null.");
        if (peaksMap == null)
            throw new NullPointerException("Peaks map cannot be null.");

        // Apply houghMap properties on peaksMap
        peaksMap.setProperties(houghMap);

        int nbObjects = peaksMap.getObjectCount();

        // +1 for object 0 (background)
        double[] maximums = new double[nbObjects + 1];
        double[] rhos = new double[nbObjects + 1];
        double[] thetas = new double[nbObjects + 1];

        // Initialize the array to 0
        Arrays.fill(maximums, Double.NEGATIVE_INFINITY);
        Arrays.fill(rhos, 0);
        Arrays.fill(thetas, 0);

        int width = peaksMap.width;
        int height = peaksMap.height;
        short[] pixArray = peaksMap.pixArray;

        short objectNumber;
        int n = 0;
        double rho;
        double theta;
        double value;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                rho = getR(peaksMap, n);
                theta = getTheta(peaksMap, n);

                value = houghMap.pixArray[houghMap.getIndex(rho, theta)] & 0xff;

                if (value > maximums[objectNumber]) {
                    maximums[objectNumber] = value;
                    rhos[objectNumber] = rho;
                    thetas[objectNumber] = theta;
                }

                n++;
            }
        }

        // Create Hough points
        HoughPoint points = new HoughPoint(nbObjects);

        for (n = 0; n < nbObjects; n++) {
            points.rho[n] = (float) rhos[n + 1];
            points.theta[n] = (float) thetas[n + 1];
        }

        return points;
    }



    // /**
    // * Return the maximum pixel color index in each object in the
    // * <code>IdentMap</code>. The pixel color index is determined from the
    // * specified <code>ByteMap</code>.
    // *
    // * @param houghMap
    // * corresponding <code>HoughMap</code> to get the maximum color
    // * index from
    // * @param peaksMap
    // * map where the Hough peaks are detected
    // * @return the list of the objects maximum pixel color index
    // * @throws NullPointerException
    // * if the ident map or the byte map is null
    // * @throws IllegalArgumentException
    // * if the ident map and byte map don't have the same size
    // */
    // public static Maximum getMaximumIntensity(HoughMap houghMap,
    // IdentMap peaksMap) {
    // if (houghMap == null)
    // throw new NullPointerException("Ident map cannot be null.");
    // if (peaksMap == null)
    // throw new NullPointerException("Byte map cannot be null.");
    //
    // // Apply houghMap properties on peaksMap
    // peaksMap.setProperties(houghMap);
    //
    // int nbObjects = peaksMap.getObjectCount();
    //
    // // If no objects, return empty results
    // if (nbObjects <= 0)
    // return new Maximum(0);
    //
    // int[] maxima = new int[nbObjects + 1]; // +1 for object 0 (background)
    // Arrays.fill(maxima, 0); // Initialize the array to 0
    //
    // // Calculate the maxima
    // int objectNumber;
    // int value;
    // double rho, theta;
    //
    // for (int n = 0; n < peaksMap.size; n++) {
    // objectNumber = peaksMap.pixArray[n] & 0xff;
    // if (objectNumber == 0)
    // continue;
    //
    // theta = Analysis.getTheta(peaksMap, n);
    // rho = Analysis.getR(peaksMap, n);
    //
    // value = houghMap.pixArray[houghMap.getIndex(rho, theta)] & 0xff;
    //
    // if (value > maxima[objectNumber])
    // maxima[objectNumber] = value;
    // }
    //
    // // Create a proper Result object
    // // and apply the value to the Result object
    // Maximum result = new Maximum(nbObjects);
    // for (int n = 1; n <= nbObjects; n++)
    // result.val[n - 1] = maxima[n];
    //
    // return result;
    // }

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
        if (index < 0 || index >= map.size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (map.size - 1));

        double deltaR = getDeltaR(map);
        double theta = ((index % map.width) * getDeltaTheta(map));

        // return (height-1 - y) * deltaR + rMin; (equivalent equation)
        return ((map.height - 1 - index / map.width) * deltaR - deltaR
                * ((map.height - 1) / 2))
                * Math.pow(-1, (int) (theta / PI));
    }



    /**
     * Returns the delta rho saved in the property of the map.
     * 
     * @param map
     *            a map
     * @return delta rho
     * @throws IllegalArgumentException
     *             if <code>map</code> does not hold the
     *             <code>HoughMap.DELTA_R</code> property
     * @throws IllegalArgumentException
     *             if its height is not odd
     * @throws NullPointerException
     *             if the map is null
     */
    private static double getDeltaR(Map map) {
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

        return deltaR;
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
        if (index < 0 || index >= map.size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (map.size - 1));

        double deltaTheta = getDeltaTheta(map);

        return ((index % map.width) * deltaTheta) % PI;
    }



    /**
     * Returns the delta theta value saved in the property of the map.
     * 
     * @param map
     *            a map
     * @return delta theta
     * @throws IllegalArgumentException
     *             if <code>map</code> does not hold the
     *             <code>HoughMap.DELTA_THETA</code> property
     * @throws IllegalArgumentException
     *             if its height is not odd
     * @throws NullPointerException
     *             if the map is null
     */
    private static double getDeltaTheta(Map map) {
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

        return deltaTheta;
    }

}
