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
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Arrays;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import ptpshared.core.math.Quad;
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
     * Integrand of the double integral to calculate the delta rho from the
     * delta theta.
     * 
     * @author ppinard
     */
    private static class Function implements UnivariateRealFunction {

        /** Width of the band. */
        public final double b;

        /** Radius of the circular mask. */
        public final double radius;



        /**
         * Creates a new function for the integrand.
         * 
         * @param b
         *            width of the bnad
         * @param radius
         *            radius of the circular mask
         */
        public Function(double b, double radius) {
            this.b = b;
            this.radius = radius;
        }



        @Override
        public double value(double rho) {
            double value =
                    b
                            / (2.0 * Math.atan(b
                                    / (2.0 * Math.sqrt(radius * radius - rho
                                            * rho))));
            return value;
        }
    }

    /**
     * Inside function to perform the double integral.
     * 
     * @author ppinard
     */
    private static class InFunction implements UnivariateRealFunction {

        /** Radius of the circular mask. */
        public final double radius;

        /** Lower limit of the integral of rho variable. */
        public final double rho0;

        /** Upper limit of the integral of rho variable. */
        public final double rho1;



        /**
         * Creates a new function for performing the double integral.
         * 
         * @param rho0
         *            lower limit of the integral of rho variable
         * @param rho1
         *            upper limit of the integral of rho variable
         * @param radius
         *            radius of the circular mask
         */
        public InFunction(double rho0, double rho1, double radius) {
            this.rho0 = rho0;
            this.rho1 = rho1;
            this.radius = radius;
        }



        @Override
        public double value(double b) {
            try {
                double value =
                        new Quad().integrate(new Function(b, radius), rho0,
                                rho1);
                return value;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    /**
     * Calculates the resolution in rho (delta R) to ensure that the aspect
     * ratio of the peaks is close to unity (square peaks). Delta R is
     * calculated based on the theta resolution and the radius of the circular
     * mask (greatest circle inscribed inside the diffraction pattern). The
     * lower and upper limit of the band width are taken to be 1% and 25% of the
     * radius, respectively. The lower and upper limit of the rho position of
     * the peaks are taken to be -90\% and 90\% of the radius, respectively.
     * 
     * @param radius
     *            radius of the circular mask
     * @param deltaTheta
     *            resolution in theta
     * @return resolution in rho
     */
    public static double calculateDeltaR(double radius, double deltaTheta) {
        double b0 = 0.01 * radius;
        double b1 = 0.25 * radius;
        double r0 = -0.9 * radius;
        double r1 = Math.abs(r0);

        double value;
        try {
            value =
                    new Quad().integrate(new InFunction(r0, r1, radius), b0, b1);
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException(e);
        }
        value = value / (b1 - b0) / (r1 - r0) * deltaTheta;
        return value;
    }



    /**
     * Calculates the resolution in rho (delta R) to ensure that the aspect
     * ratio of the peaks is close to unity (square peaks). Delta R is
     * calculated based on the theta resolution, the lower and upper limit of
     * the band width and the lower and upper limit of the rho position of the
     * peaks.
     * 
     * @param radius
     *            radius of the circular mask
     * @param b0
     *            lower limit of the band width
     * @param b1
     *            upper limit of the band width
     * @param rho0
     *            lower limit of the position of the peaks
     * @param rho1
     *            upper limit of the position of the peaks
     * @param deltaTheta
     *            resolution in theta
     * @return resolution in rho
     */
    public static double calculateDeltaR(double radius, double b0, double b1,
            double rho0, double rho1, double deltaTheta) {
        if (radius <= 0)
            throw new IllegalArgumentException("Radius (" + radius
                    + ") must be greater than zero.");
        if (b0 <= 0)
            throw new IllegalArgumentException(
                    "The lower limit of the band width (" + b0
                            + ") must be greater than zero.");
        if (b1 <= 0)
            throw new IllegalArgumentException(
                    "The upper limit of the band width (" + b1
                            + ") must be greater than zero.");
        if (b0 > b1)
            throw new IllegalArgumentException(
                    "The upper limit of the band width (" + b1
                            + ") must be greater or equal to the lower limit ("
                            + b0 + ").");
        if (rho1 > rho0)
            throw new IllegalArgumentException(
                    "The upper limit of the peak position (" + rho1
                            + ") must be greater or equal to the lower limit ("
                            + rho0 + ").");
        if (deltaTheta <= 0)
            throw new IllegalArgumentException("Theta resolution ("
                    + deltaTheta + ") must be > 0");

        double value;

        try {
            value =
                    new Quad().integrate(new InFunction(rho0, rho1, radius),
                            b0, b1);
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException(e);
        }

        value = value / (b1 - b0) / (rho1 - rho0) * deltaTheta;

        return value;
    }



    /**
     * Does a Hough transform using the specified resolution in theta. The
     * resolution in rho is automatically calculated to ensure that the aspect
     * ratio of the peaks is close to unity (square peaks).
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



    /**
     * Does a Hough transform with the specified resolution in rho and theta.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of
     * @param deltaTheta
     *            resolution in theta (radians/px)
     * @param deltaRho
     *            resolution in rho (px/px)
     * @return the Hough transform.
     */
    public static HoughMap hough(ByteMap byteMap, double deltaTheta,
            double deltaRho) {
        return new Transform().doHough(byteMap, deltaTheta, deltaRho);
    }

    /** Flag indicating if the operation should be interrupted. */
    private boolean isInterrupted = false;

    /** Progress value. */
    protected double progress = 0;



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
        // Radius
        // The radius is either the radius read from the property of the
        // pattern map or the minimum between half the pattern map's height
        // or half the pattern map's width
        int radius =
                byteMap.getProperty(MaskDisc.KEY_RADIUS,
                        min(byteMap.width / 2, byteMap.height / 2));

        double deltaRho = calculateDeltaR(radius, deltaTheta);

        return createHoughMap(byteMap, deltaTheta, deltaRho);
    }



    /**
     * Returns a <code>HoughMap</code> with the specified resolution in theta
     * and rho.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of
     * @param deltaTheta
     *            resolution in theta (radians/px)
     * @param deltaRho
     *            resolution in rho (px/px)
     * @return a empty <code>HoughMap</code>
     */
    protected HoughMap createHoughMap(ByteMap byteMap, double deltaTheta,
            double deltaRho) {
        double rMax =
                ceil(sqrt(pow(byteMap.width, 2) + pow(byteMap.height, 2)) / 2);
        return new HoughMap(rMax, deltaRho, deltaTheta);
    }



    /**
     * Does a Hough transform using the specified resolution in theta. The
     * resolution in rho is automatically calculated to ensure that the aspect
     * ratio of the peaks is close to unity (square peaks). This is the same as
     * {@link #hough(ByteMap, double)} but as an instance method. This method is
     * used to get the progress of the operation.
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
     * Does a Hough transform with the specified resolution in rho and theta.
     * This is the same as {@link #hough(ByteMap, double, double)} but as an
     * instance method. This method is used to get the progress of the
     * operation.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of
     * @param deltaTheta
     *            resolution in theta (radians/px)
     * @param deltaRho
     *            resolution in rho (px/px)
     * @return the Hough transform
     * @see #getTaskProgress()
     * @see #getTaskStatus()
     */
    public HoughMap doHough(ByteMap byteMap, double deltaTheta, double deltaRho) {
        return doHough(byteMap, createHoughMap(byteMap, deltaTheta, deltaRho));
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
