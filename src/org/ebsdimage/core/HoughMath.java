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

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlshared.geom.LineUtil;
import rmlshared.math.Stats;

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
        double tmpRho;
        for (int i = 0; i < peaks1.length; i++) {
            HoughPeak peak1 = peaks1[i];
            double distance = Double.POSITIVE_INFINITY;

            for (HoughPeak peak2 : peaks2) {
                // (peak1.theta - peak2.theta)^2
                tmpTheta = Math.pow(peak1.theta - peak2.theta, 2);

                // (peak1.rho - peak2.rho)^2
                tmpRho = Math.pow(peak1.rho - peak2.rho, 2);

                distance = Math.min(distance, tmpTheta + tmpRho);
            }

            distances[i] = distance;
        }

        return Stats.average(distances);
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
     * @param map
     *            map containing the line
     * @return a hough peak
     */
    public static HoughPeak getHoughPeak(Line2D line, Map map) {
        return getHoughPeak(line, map, 0.0);
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
     * @param map
     *            map containing the line
     * @param intensity
     *            of the peak
     * @return a hough peak
     */
    public static HoughPeak getHoughPeak(Line2D line, Map map, double intensity) {
        Line2D.Double adjustedLine =
                new Line2D.Double(line.getP1(), line.getP2());

        // Translate the origin from the upper left corner
        // to the center of the image
        // and invert the y axis to have the positive going up
        adjustedLine.y1 = map.height - 1 - adjustedLine.y1;
        adjustedLine.y2 = map.height - 1 - adjustedLine.y2;
        LineUtil.translate(adjustedLine, -map.width / 2, -map.height / 2);

        // Calculate theta and rho
        double theta;
        double rho;

        if (LineUtil.isVertical(adjustedLine)) {
            theta = 0.0;
            rho = adjustedLine.getX1();
        } else { // Any other line
            // theta = -arccot(m) = PI / 2 + atan(m)
            theta = Math.PI / 2.0 + Math.atan(LineUtil.getSlope(adjustedLine));

            // rho = x*cos(theta) + y*sin(theta)
            // if x == 0: rho = y*sin(theta)
            rho = LineUtil.getY(adjustedLine, 0.0) * Math.sin(theta);
        }

        Calibration cal = map.getCalibration();
        return new HoughPeak(theta, cal.getCalibratedY(rho), cal.unitsY,
                intensity);
    }



    /**
     * Returns a <code>Line2D</code> representing the Hough peak in the image
     * space.
     * 
     * @param peak
     *            a Hough peak
     * @param map
     *            map where the <code>Line2D</code> will be drawn
     * @return a <code>Line2D</code> of the Hough peak
     */
    public static Line2D getLine2D(HoughPeak peak, Map map) {
        Calibration cal = map.getCalibration();

        // Assert calibration Y units and rho units are the same
        if (peak.rhoUnits != cal.unitsY)
            throw new IllegalArgumentException("Map's calibration Y units ("
                    + cal.unitsY + ") and peak's rho units (" + peak.rhoUnits
                    + ") must be equal.");

        // Create the line perpendicular to the specified vector
        double slope = getSlope(peak);
        double x0 = cal.getUncalibratedX(peak.rho * Math.cos(peak.theta));
        double y0 = cal.getUncalibratedY(peak.rho * Math.sin(peak.theta));
        Point2D.Double center = new Point2D.Double(x0, y0);
        Line2D.Double line = new Line2D.Double();
        LineUtil.setAnalyticalLine(line, slope, center, map.width);

        // Translate the origin from the center of the image
        // to the upper left corner
        // and invert the y axis to have the positive going down
        LineUtil.translate(line, map.width / 2, map.height / 2);
        line.y1 = map.height - 1 - line.y1;
        line.y2 = map.height - 1 - line.y2;

        // Extends the line to fit the whole map
        Rectangle frame = new Rectangle(0, 0, map.width - 1, map.height - 1);
        LineUtil.extendTo(line, frame);

        return line;
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
        m = -Math.cos(peak.theta) / Math.sin(peak.theta);
        if (Double.isInfinite(m))
            m = Double.POSITIVE_INFINITY;

        return m;
    }

}
