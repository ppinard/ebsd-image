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

import magnitude.core.Magnitude;
import magnitude.geom.Line2D;
import rmlshared.math.Stats;
import static magnitude.core.Math.PI;
import static magnitude.core.Math.atan;
import static magnitude.core.Math.cos;
import static magnitude.core.Math.sin;

/**
 * Miscellaneous calculations in Hough space.
 * 
 * @author Philippe T. Pinard
 */
public class HoughMath {

    /**
     * Returns a value how much the <code>peaks1</code> fits the
     * <code>peaks2</code>. For each peak in <code>peaks1</code>, the closest
     * peak in <code>peak2</code> is found and the squared distance is stored.
     * The fit is the average of the square distance for all the peaks.
     * <p/>
     * Smaller the value of the fit is, better the <code>peaks1</code> matches
     * <code>peaks2</code>.
     * 
     * @param peaks1
     *            list of Hough peaks
     * @param peaks2
     *            list of Hough peaks
     * @return fit measure of how much two series of peaks match
     */
    public static double fit(HoughPeak[] peaks1, HoughPeak[] peaks2) {
        if (peaks1.length == 0)
            return Double.POSITIVE_INFINITY;

        double[] distances = new double[peaks1.length];

        double tmpTheta;
        Magnitude tmpThetaMag;
        double tmpRho;
        Magnitude tmpRhoMag;
        for (int i = 0; i < peaks1.length; i++) {
            HoughPeak peak1 = peaks1[i];
            double distance = Double.POSITIVE_INFINITY;

            for (HoughPeak peak2 : peaks2) {
                // (peak1.theta - peak2.theta)^2
                tmpThetaMag = peak1.theta.minus(peak2.theta).power(2);
                tmpTheta = tmpThetaMag.getPreferredUnitsValue();

                // (peak1.rho - peak2.rho)^2
                tmpRhoMag = peak1.rho.minus(peak2.rho).power(2);
                tmpRho = tmpRhoMag.getPreferredUnitsValue();

                distance = Math.min(distance, tmpTheta + tmpRho);
            }

            distances[i] = distance;
        }

        return Stats.average(distances);
    }



    /**
     * Calculates the slope of the line in the image space represented by the
     * specified Hough peak.
     * <p/>
     * A vertical line has a slope of positive infinity.
     * 
     * @param peak
     *            a Hough peak
     * @return slope of the line in the image space
     */
    public static double getSlope(HoughPeak peak) {
        double m;

        // -cos(theta) / sin(theta)
        // unitless by definition
        m =
                cos(peak.theta).multiply(-1).div(sin(peak.theta)).getBaseUnitsValue();
        if (Double.isInfinite(m))
            m = Double.POSITIVE_INFINITY;

        return m;
    }



    /**
     * Returns the intercept of the line in the image space represented by the
     * specified Hough Peak.
     * <p/>
     * For vertical lines, the intercept along the x axis is returned (i.e.
     * <code>b</code> in <code>x = b</code>).
     * 
     * @param peak
     *            a Hough peak
     * @return slope of the line in the image space
     */
    public static Magnitude getIntercept(HoughPeak peak) {
        Magnitude b;

        b = peak.rho.div(sin(peak.theta));
        if (b.isInfinite() || b.isNaN())
            b = peak.rho;

        return b;
    }



    /**
     * Returns a <code>Line2D</code> representing the Hough peak in the image
     * space.
     * 
     * @param peak
     *            a Hough peak
     * @return a <code>Line2D</code> of the Hough peak
     */
    public static Line2D getLine(HoughPeak peak) {
        return Line2D.fromSlopeIntercept(getSlope(peak), getIntercept(peak));
    }



    /**
     * Converts the slope and intercept of a line to a Hough peak. Equations:
     * <ul>
     * <li><code>theta = -arccot(m)</code></li>
     * <li><code>rho = k * sin(theta)</code></li>
     * </ul>
     * 
     * @param line
     *            a <code>Line</code>
     * @param intensity
     *            intensity of the Hough peak
     * @return a hough peak
     */
    public static HoughPeak getHoughPeak(Line2D line, double intensity) {
        Magnitude rho;
        Magnitude theta;

        Magnitude m = line.getSlope();

        if (Magnitude.isInfinite(m)) { // Vertical line
            theta = new Magnitude(0.0, "rad");
            rho = line.getInterceptX();
        } else { // Any other line
            // theta = -arccot(m) = PI / 2 + atan(m)
            theta = PI.div(2.0).add(atan(m));

            // rho = x*cos(theta) + y*sin(theta)
            // if x == 0: rho = y*sin(theta)
            rho = line.getInterceptY().multiply(sin(theta));
        }

        return new HoughPeak(theta, rho, intensity);
    }

    // TODO: Fix when calibration is fully implemented
    // /**
    // * Converts a list of bands to a list of Hough peaks.
    // *
    // * @param bands
    // * list of bands
    // * @return list of Hough peaks
    // */
    // public static HoughPeak[] bandsToHoughPeaks(Band[] bands) {
    // HoughPeak[] peaks = new HoughPeak[bands.length];
    //
    // for (int i = 0; i < bands.length; i++) {
    // peaks[i] =
    // HoughMath.lineSpaceToHoughSpace(bands[i].line,
    // bands[i].intensity);
    // }
    //
    // return peaks;
    // }
}
