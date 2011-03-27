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
package org.ebsdimage.core;

import java.util.Arrays;

import rmlimage.core.Calibration;
import rmlimage.core.IdentMap;

/**
 * Analysis of maps and results.
 * 
 * @author Philippe T. Pinard
 */
public class Analysis {

    /**
     * Returns a list of centre of mass of each object in the
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
    public static Centroid getCenterOfMass(IdentMap peaksMap, HoughMap houghMap) {
        // Validate maps
        validate(peaksMap, houghMap);

        int nbObjects = peaksMap.getObjectCount();

        Calibration cal = peaksMap.getCalibration();
        Centroid result = new Centroid(nbObjects, cal.unitsY);

        // If there are no objects, return an empty result
        if (nbObjects <= 0)
            return result;

        // +1 for object 0 (background)
        int[] xs = new int[nbObjects + 1];
        int[] ys = new int[nbObjects + 1];
        int[] values = new int[nbObjects + 1];

        // Initialize the array to 0
        Arrays.fill(xs, 0);
        Arrays.fill(ys, 0);
        Arrays.fill(values, 0);

        int width = peaksMap.width;
        int height = peaksMap.height;
        short[] pixArray = peaksMap.pixArray;

        int n = 0;
        int value;
        short objectNumber;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                value = houghMap.pixArray[houghMap.getIndex(x, y)] & 0xff;

                xs[objectNumber] += x * value;
                ys[objectNumber] += y * value;
                values[objectNumber] += value;

                n++;
            }
        }

        // Assemble result
        n = 0;
        double theta;
        double rho;
        byte[] houghPixArray = houghMap.pixArray;
        for (int i = 1; i <= nbObjects; i++) {
            if (values[i] == 0)
                continue;

            theta = (double) xs[i] / (double) values[i];
            rho = (double) ys[i] / (double) values[i];

            result.x[n] = (float) cal.getCalibratedX(theta);
            result.y[n] = (float) cal.getCalibratedY(rho);
            result.intensity[n] =
                    houghPixArray[houghMap.getIndex((int) theta, (int) rho)] & 0xff;

            n++;
        }

        // Resize array
        System.arraycopy(result.x, 0, result.x, 0, n - 1);
        System.arraycopy(result.y, 0, result.y, 0, n - 1);
        System.arraycopy(result.intensity, 0, result.intensity, 0, n - 1);

        return result;
    }



    /**
     * Returns a list of the centroid of all the objects in the
     * <code>BinMap</code>. The <code>BinMap</code> must come from the
     * thresholding of a <code>HoughMap</code>. The centroid coordinates are
     * returned in the (r;theta) coordinate system.
     * 
     * @param peaksMap
     *            <code>IdentMap</code> to get the centroid of
     * @param houghMap
     *            <code>HoughMap</code> to get the intensity of
     * @return the list of centroid coordinates in (r;theta)
     * @throws NullPointerException
     *             if the bin map is null
     */
    public static Centroid getCentroid(IdentMap peaksMap, HoughMap houghMap) {
        // Validate maps
        validate(peaksMap, houghMap);

        int nbObjects = peaksMap.getObjectCount();

        Calibration cal = peaksMap.getCalibration();
        Centroid result = new Centroid(nbObjects, cal.unitsY);

        // If there are no objects, return an empty result
        if (nbObjects <= 0)
            return result;

        // centroidX and centroidY should be made double to avoid overflow
        // +1 for object 0 (background)
        int[] centroidX = new int[nbObjects + 1];
        int[] centroidY = new int[nbObjects + 1];
        int[] area = new int[nbObjects + 1];

        Arrays.fill(centroidX, 0); // Initialize the array to 0
        Arrays.fill(centroidY, 0); // Initialize the array to 0
        Arrays.fill(area, 0); // Initialize the array to 0

        short objectNumber;

        int n = 0;
        int width = peaksMap.width;
        int height = peaksMap.height;
        short[] pixArray = peaksMap.pixArray;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                centroidX[objectNumber] += x;
                centroidY[objectNumber] += y;
                area[objectNumber]++;

                n++;
            }
        }

        // Apply the calibration factor and save the value to the Result object
        double theta;
        double rho;
        byte[] houghPixArray = houghMap.pixArray;
        for (n = 1; n <= nbObjects; n++) {
            theta = (double) centroidX[n] / (double) area[n];
            rho = (double) centroidY[n] / (double) area[n];

            result.x[n - 1] = (float) cal.getCalibratedX(theta);
            result.y[n - 1] = (float) cal.getCalibratedY(rho);
            result.intensity[n - 1] =
                    houghPixArray[houghMap.getIndex((int) theta, (int) rho)] & 0xff;
        }

        return result;
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
    public static Centroid getMaximumLocation(IdentMap peaksMap,
            HoughMap houghMap) {
        // Validate maps
        validate(peaksMap, houghMap);

        int nbObjects = peaksMap.getObjectCount();

        Calibration cal = peaksMap.getCalibration();
        Centroid result = new Centroid(nbObjects, cal.unitsY);

        // If there are no objects, return an empty result
        if (nbObjects <= 0)
            return result;

        // +1 for object 0 (background)
        int[] maximums = new int[nbObjects + 1];
        int[] xs = new int[nbObjects + 1];
        int[] ys = new int[nbObjects + 1];

        // Initialize the array to 0
        Arrays.fill(maximums, Integer.MIN_VALUE);
        Arrays.fill(xs, 0);
        Arrays.fill(ys, 0);

        int width = peaksMap.width;
        int height = peaksMap.height;
        short[] pixArray = peaksMap.pixArray;

        int x;
        int y;
        int n = 0;
        short objectNumber;
        int value;

        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                value = houghMap.pixArray[houghMap.getIndex(x, y)] & 0xff;

                if (value > maximums[objectNumber]) {
                    maximums[objectNumber] = value;
                    xs[objectNumber] = x;
                    ys[objectNumber] = y;
                }

                n++;
            }
        }

        // Assemble results
        for (n = 1; n <= nbObjects; n++) {
            result.x[n - 1] = (float) cal.getCalibratedX(xs[n]);
            result.y[n - 1] = (float) cal.getCalibratedY(ys[n]);
            result.intensity[n - 1] = maximums[n];
        }

        return result;
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
     * Validates that the peaks map and Hough map have the same size and
     * calibration.
     * 
     * @param peaksMap
     *            peaks map
     * @param houghMap
     *            Hough map
     * @throws IllegalArgumentException
     *             if the two maps do not have the same size
     * @throws IllegalArgumentException
     *             if the two maps do not have the same calibration
     * @throws NullPointerException
     *             if a map is null
     */
    private static void validate(IdentMap peaksMap, HoughMap houghMap) {
        if (houghMap == null)
            throw new NullPointerException("Hough map cannot be null.");
        if (peaksMap == null)
            throw new NullPointerException("Peaks map cannot be null.");

        if (!peaksMap.isSameSize(houghMap))
            throw new IllegalArgumentException("The peaks map ("
                    + peaksMap.getDimensionLabel()
                    + ") must have the same size as the Hough map ("
                    + houghMap.getDimensionLabel() + ")");
        if (!peaksMap.getCalibration().equals(houghMap.getCalibration(), 1e-6))
            throw new IllegalArgumentException(
                    "The calibration of the peaks map ("
                            + peaksMap.getCalibration()
                            + ") must be the same as the Hough map ("
                            + houghMap.getCalibration() + ").");
    }

}
