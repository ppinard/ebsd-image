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

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import magnitude.core.Magnitude;

import org.junit.Test;

import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.core.NullMap;
import rmlshared.geom.LineUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

import static junittools.test.Assert.assertEquals;

public class HoughMathTest {

    @Test
    public void testFit1() {
        // Peaks1
        Magnitude theta = new Magnitude(2, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak1 = new HoughPeak(theta, rho, 1.0);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        HoughPeak peak2 = new HoughPeak(theta, rho, 1.0);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(8, "m");
        HoughPeak peak3 = new HoughPeak(theta, rho, 1.0);

        HoughPeak[] peaks1 = new HoughPeak[] { peak1, peak2, peak3 };

        // Peaks2
        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        peak1 = new HoughPeak(theta, rho, 1.0);

        theta = new Magnitude(2, "rad");
        rho = new Magnitude(1, "m");
        peak2 = new HoughPeak(theta, rho, 1.0);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(8, "m");
        peak3 = new HoughPeak(theta, rho, 1.0);

        HoughPeak[] peaks2 = new HoughPeak[] { peak1, peak2, peak3 };

        assertEquals(0.0, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testFit2() {
        // Peaks1
        HoughPeak peak1 = new HoughPeak(2, 1, 1.0);
        HoughPeak peak2 = new HoughPeak(1.5, 5, 1.0);
        HoughPeak peak3 = new HoughPeak(1, 8, 1.0);

        HoughPeak[] peaks1 = new HoughPeak[] { peak1, peak2, peak3 };

        // Peaks2
        peak1 = new HoughPeak(1.5, 5, 1.0);
        peak2 = new HoughPeak(3, 1, 1.0);
        peak3 = new HoughPeak(2, 8, 1.0);

        HoughPeak[] peaks2 = new HoughPeak[] { peak1, peak2, peak3 };

        assertEquals(2.0 / 3.0, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testFit3() {
        // Peaks1
        HoughPeak peak1 = new HoughPeak(2, 1, "m", 1.0);
        HoughPeak peak2 = new HoughPeak(1.5, 5, "m", 1.0);
        HoughPeak peak3 = new HoughPeak(1, 8, "m", 1.0);

        HoughPeak[] peaks1 = new HoughPeak[] { peak1, peak2, peak3 };

        // Peaks2
        peak1 = new HoughPeak(1.0, 50, "m", 1.0);
        peak2 = new HoughPeak(0, 10, "m", 1.0);
        peak3 = new HoughPeak(1, 80, "m", 1.0);

        HoughPeak[] peaks2 = new HoughPeak[] { peak1, peak2, peak3 };

        assertEquals(39.0833333, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testGetHoughPeak1() {
        Map map = new NullMap(101, 101);

        Line2D line = new Line2D.Double();
        double m = 1.0;
        Point2D center = new Point2D.Double(0.0, -1.0 / sin(PI / 4));
        LineUtil.setAnalyticalLine(line, m, center, 100.0);
        LineUtil.extendTo(line, new Rectangle(0, 0, map.width - 1,
                map.height - 1));

        HoughPeak peak = HoughMath.getHoughPeak(line, map);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(PI / 4, peak.theta, 1e-6);

        Line2D other = HoughMath.getLine2D(peak, map);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testGetHoughPeak2() {
        Map map = new NullMap(101, 101);

        Line2D line = new Line2D.Double();
        double m = Double.POSITIVE_INFINITY;
        Point2D center = new Point2D.Double(51.0, 0.0);
        LineUtil.setAnalyticalLine(line, m, center, 100.0);
        LineUtil.extendTo(line, new Rectangle(0, 0, map.width - 1,
                map.height - 1));

        HoughPeak peak = HoughMath.getHoughPeak(line, map);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(0.0, peak.theta, 1e-6);

        Line2D other = HoughMath.getLine2D(peak, map);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testGetHoughPeak3() {
        Map map = new NullMap(101, 101);

        Line2D line = new Line2D.Double();
        double m = -1.0;
        Point2D center = new Point2D.Double(0.0, 101.41421356);
        LineUtil.setAnalyticalLine(line, m, center, 100.0);
        LineUtil.extendTo(line, new Rectangle(0, 0, map.width - 1,
                map.height - 1));

        HoughPeak peak = HoughMath.getHoughPeak(line, map);
        assertEquals(-1.0, peak.rho, 1e-6);
        assertEquals(3 * PI / 4, peak.theta, 1e-6);

        Line2D other = HoughMath.getLine2D(peak, map);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testGetHoughPeak4() {
        Map map = new NullMap(101, 101);

        Line2D line = new Line2D.Double();
        double m = 0.0;
        Point2D center = new Point2D.Double(0.0, 49.0);
        LineUtil.setAnalyticalLine(line, m, center, 100.0);
        LineUtil.extendTo(line, new Rectangle(0, 0, map.width - 1,
                map.height - 1));

        HoughPeak peak = HoughMath.getHoughPeak(line, map);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(PI / 2, peak.theta, 1e-6);

        Line2D other = HoughMath.getLine2D(peak, map);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testGetHoughPeakCalibrated1() {
        Map map = new NullMap(101, 101);
        map.setCalibration(new Calibration(1.0, 1.0, "m"));

        Line2D line = new Line2D.Double();
        double m = 1.0;
        Point2D center = new Point2D.Double(0.0, -1.0 / sin(PI / 4));
        LineUtil.setAnalyticalLine(line, m, center, 100.0);
        LineUtil.extendTo(line, new Rectangle(0, 0, map.width - 1,
                map.height - 1));

        HoughPeak peak = HoughMath.getHoughPeak(line, map);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(PI / 4, peak.theta, 1e-6);
        assertEquals("m", peak.rhoUnits);

        Line2D other = HoughMath.getLine2D(peak, map);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testGetHoughPeakCalibrated2() {
        Map map = new NullMap(101, 101);
        map.setCalibration(new Calibration(2.0, 2.0, "m"));

        Line2D line = new Line2D.Double();
        double m = 1.0;
        Point2D center = new Point2D.Double(0.0, -1.0 / sin(PI / 4));
        LineUtil.setAnalyticalLine(line, m, center, 100.0);
        LineUtil.extendTo(line, new Rectangle(0, 0, map.width - 1,
                map.height - 1));

        HoughPeak peak = HoughMath.getHoughPeak(line, map);
        assertEquals(2.0, peak.rho, 1e-6);
        assertEquals(PI / 4, peak.theta, 1e-6);
        assertEquals("m", peak.rhoUnits);

        Line2D other = HoughMath.getLine2D(peak, map);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testGetLine2D1() {
        Map map = new NullMap(101, 101);
        HoughPeak peak = new HoughPeak(PI / 4, 1.0, 0.0);

        Line2D line = HoughMath.getLine2D(peak, map);
        assertEquals(1.0, LineUtil.getSlope(line), 1e-6);
        assertEquals(-1.0 / sin(PI / 4), LineUtil.getOrdinate(line), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, map);
        assertEquals(peak, other, 1e-6);
    }



    @Test
    public void testGetLine2D2() {
        Map map = new NullMap(101, 101);
        HoughPeak peak = new HoughPeak(0.0, 1.0, 0.0);

        Line2D line = HoughMath.getLine2D(peak, map);
        assertTrue(LineUtil.isVertical(line));
        assertEquals(51.0, line.getX1(), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, map);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testGetLine2D3() {
        Map map = new NullMap(101, 101);
        HoughPeak peak = new HoughPeak(3 * PI / 4, -1, 0.0);

        Line2D line = HoughMath.getLine2D(peak, map);
        assertEquals(-1.0, LineUtil.getSlope(line), 1e-6);
        assertEquals(101.41421356, LineUtil.getOrdinate(line), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, map);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testGetLine2D4() {
        Map map = new NullMap(101, 101);
        HoughPeak peak = new HoughPeak(PI / 2, 1.0, 0.0);

        Line2D line = HoughMath.getLine2D(peak, map);
        assertEquals(0.0, LineUtil.getSlope(line), 1e-6);
        assertEquals(49.0, LineUtil.getOrdinate(line), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, map);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testGetLine2DCalibrated2() {
        Map map = new NullMap(101, 101);
        map.setCalibration(new Calibration(2.0, 2.0, "m"));
        HoughPeak peak = new HoughPeak(PI / 4, 1.0, "m", 0.0);

        Line2D line = HoughMath.getLine2D(peak, map);
        assertEquals(1.0, LineUtil.getSlope(line), 1e-6);
        assertEquals(-1.0 / sin(PI / 4) / 2, LineUtil.getOrdinate(line), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, map);
        assertEquals(peak, other, 1e-6);
    }



    @Test
    public void testtGetLine2DCalibrated1() {
        Map map = new NullMap(101, 101);
        map.setCalibration(new Calibration(1.0, 1.0, "m"));
        HoughPeak peak = new HoughPeak(PI / 4, 1.0, "m", 0.0);

        Line2D line = HoughMath.getLine2D(peak, map);
        assertEquals(1.0, LineUtil.getSlope(line), 1e-6);
        assertEquals(-1.0 / sin(PI / 4), LineUtil.getOrdinate(line), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, map);
        assertEquals(peak, other, 1e-6);
    }

}
