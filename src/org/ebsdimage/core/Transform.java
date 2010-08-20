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

import static java.lang.Math.ceil;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import java.util.Arrays;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlshared.ui.Monitorable;

/**
 * Transformation of one map into another.
 * 
 * @author Philippe T. Pinard
 */
public class Transform implements Monitorable {

    /**
     * Does a Hough transform with the specified angle increment.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of.
     * @param deltaTheta
     *            angle increment (in radians).
     * @return the Hough transform.
     */
    public static HoughMap hough(ByteMap byteMap, double deltaTheta) {
        return new Transform().doHough(byteMap, deltaTheta);
    }

    /** Progress value. */
    protected double progress = 0;

    /** Flag indicating if the operation should be interrupted. */
    private boolean isInterrupted = false;



    /**
     * Returns a HoughMap such as the region of the biggest inscribed circular
     * mask will give a square region in the HoughMap.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of
     * @param deltaTheta
     *            angle increment in radians (Width of a pixel)
     * @return a empty <code>HoughMap</code>
     */
    protected HoughMap createHoughMap(ByteMap byteMap, double deltaTheta) {

        // Calculate rMax as the half diagonal
        double rMax =
                ceil(sqrt(byteMap.width * byteMap.width + byteMap.height
                        * byteMap.height) / 2);

        // Calculate deltaR such as the region of the biggest inscribed
        // circular mask will give a square HoughMap
        double biggestCircularDiameter = min(byteMap.width, byteMap.height);
        int houghMapWidth = HoughMap.calculateWidth(deltaTheta);
        double deltaR = biggestCircularDiameter / houghMapWidth;

        return new HoughMap(rMax, deltaR, deltaTheta);
    }



    /**
     * Does a Hough transform with the specified angle increment. This is the
     * same as {@link #hough(ByteMap, double)} but as an instance method. This
     * method is used to get the progress of the operation.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of
     * @param deltaTheta
     *            angle increment (in radians)
     * @return the Hough transform
     * @see #getTaskProgress()
     * @see #getTaskStatus()
     */
    public HoughMap doHough(ByteMap byteMap, double deltaTheta) {
        return doHough(byteMap, createHoughMap(byteMap, deltaTheta));
    }



    /**
     * Does a Hough transform on the specified <code>ByteMap</code> and stores
     * it in the specified <code>HoughMap</code>. The resolution and dimensions
     * of the <code>HoughMap</code> is decided prior to calling this method.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of
     * @param houghMap
     *            input Hough map
     * @return the Hough transform
     */
    public HoughMap doHough(ByteMap byteMap, HoughMap houghMap) {
        // Discussed on 2009-12-06 whether the properties of the pattern should
        // follow in the hough map. It was agreed that it will for now unless a
        // counter-argument is found.
        houghMap.setProperties(byteMap);

        // Get the deltaTheta set by the HoughMap.
        double deltaTheta = houghMap.deltaTheta;

        // Precalculate the sin and cos to accelerate the transform calculation
        int thetaCount = houghMap.width; // Number of precalculated theta
        double[] sin = new double[thetaCount];
        double[] cos = new double[thetaCount];
        double angle;
        for (int n = 0; n < thetaCount; n++) {
            angle = n * deltaTheta;
            sin[n] = Math.sin(angle);
            cos[n] = Math.cos(angle);
        }

        // Create a buffer that will hold the sum of values of all the pixels
        // that belong to each line
        int[] sum = new int[houghMap.size];
        Arrays.fill(sum, 0);

        // Create a buffer that will hold the number of original pixels
        // held in each HoughMap pixel for later normalization
        int[] pixelCount = new int[houghMap.size];
        Arrays.fill(pixelCount, 0);

        int x;
        int y;
        double r;
        int thetaIndex;
        byte[] pixArray = byteMap.pixArray;
        int pixValue;
        int houghIndex;
        int width = byteMap.width;
        int height = byteMap.height;
        int size = byteMap.size;
        for (int index = 0; index < size; index++) {
            progress = (double) index / size;

            // Check for interruption every 10 pixels
            if (index % 10 == 0 && isInterrupted()) {
                houghMap.setChanged(Map.MAP_CHANGED);
                return houghMap;
            }

            // Get x;y coordinates of the pixel with the origin at the center
            // of the ByteMap
            // The y axis is inverted to have the positive y going up
            // and negative y going down
            x = index % width - width / 2;
            y = (height - 1 - index / width) - height / 2;

            pixValue = (pixArray[index] & 0xff);
            if (pixValue == 0)
                continue; // Skip if color = 0
            for (thetaIndex = 0; thetaIndex < thetaCount; thetaIndex++) {
                r = x * cos[thetaIndex] + y * sin[thetaIndex];
                // if (r < 0) continue;
                houghIndex = houghMap.getIndex(r, thetaIndex * deltaTheta);
                sum[houghIndex] += pixValue;
                pixelCount[houghIndex]++;
            }
        }

        // Normalize
        // We won't check for interruption during normalization.
        // It is fast enough
        pixArray = houghMap.pixArray;
        size = houghMap.size;
        for (int index = 0; index < size; index++)
            pixArray[index] =
                    (pixelCount[index] == 0) ? 0
                            : (byte) (sum[index] / pixelCount[index]);

        houghMap.setChanged(Map.MAP_CHANGED);

        return houghMap;
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return "";
    }



    /**
     * Interrupts the operation.
     */
    public synchronized void interrupt() {
        isInterrupted = true;
    }



    /**
     * Checks if the operation should be interrupted. This method must be
     * synchronized because interrupt() may be called from any thread.
     * 
     * @return <code>true</code> if the operation is interrupted,
     *         <code>false</code> otherwise
     */
    private synchronized boolean isInterrupted() {
        return isInterrupted;
    }
}
