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
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.ebsdimage.core.sim.Band;

import ptpshared.core.geom.Line;
import ptpshared.core.geom.Line3D;
import ptpshared.core.geom.LinePlane;
import ptpshared.core.math.Vector3D;
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

        for (int i = 0; i < peaks1.length; i++) {
            HoughPeak peak1 = peaks1[i];
            double distance = Double.POSITIVE_INFINITY;

            for (HoughPeak peak2 : peaks2)
                distance =
                        Math.min(
                                distance,
                                Math.pow(peak1.theta - peak2.theta, 2)
                                        + Math.pow(peak1.rho - peak2.rho, 2));

            distances[i] = distance;
        }

        return Stats.average(distances);
    }



    /**
     * Converts a Hough peak into the slope and intercept of a line. Equations:
     * <ul>
     * <li><code>m = -cos(theta) / sin(theta)</code></li>
     * <li><code>k = rho / sin(theta)</code></li>
     * </ul>
     * 
     * @param peak
     *            a <code>HoughPeak</code>
     * @return a line
     */
    public static Line houghSpaceToLine(HoughPeak peak) {
        double m;
        double k;

        m = -cos(peak.theta) / sin(peak.theta);
        if (Double.isInfinite(m))
            m = Double.POSITIVE_INFINITY;

        k = peak.rho / sin(peak.theta);
        if (Double.isInfinite(k) || Double.isNaN(k))
            k = peak.rho;

        return new Line(m, k);
    }



    /**
     * Converts a Hough peak to a normal vector representing the Hough peak in
     * 3D space. The normal is calculated from the position of the camera.
     * 
     * @param peak
     *            a Hough peak
     * @param cameraPosition
     *            position of the camera in space
     * @return normal vector representing the Hough peak
     */
    public static Vector3D houghSpaceToVectorNormal(HoughPeak peak,
            Vector3D cameraPosition) {
        Line3D line0 = HoughMath.houghSpaceToLine(peak).toLine3D(LinePlane.XZ);

        return line0.vector.cross(line0.point.minus(cameraPosition)).normalize();

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
    public static HoughPeak lineSpaceToHoughSpace(Line line, double intensity) {
        double rho;
        double theta;

        if (Double.isInfinite(line.m)) {
            theta = 0.0;
            rho = line.k;
        } else {
            theta = PI / 2 + atan(line.m);
            rho = line.k * sin(theta);
        }

        return new HoughPeak(rho, theta, intensity);
    }



    /**
     * Converts a list of bands to a list of Hough peaks.
     * 
     * @param bands
     *            list of bands
     * @return list of Hough peaks
     */
    public static HoughPeak[] bandsToHoughPeaks(Band[] bands) {
        HoughPeak[] peaks = new HoughPeak[bands.length];

        for (int i = 0; i < bands.length; i++) {
            peaks[i] =
                    HoughMath.lineSpaceToHoughSpace(bands[i].line,
                            bands[i].intensity);
        }

        return peaks;
    }
}
