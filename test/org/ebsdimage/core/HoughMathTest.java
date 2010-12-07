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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import static junittools.test.Assert.assertEquals;

public class HoughMathTest {

    @Test
    public void testFit1() {
        // Peaks1
        Magnitude theta = new Magnitude(2, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        HoughPeak peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(8, "m");
        HoughPeak peak3 = new HoughPeak(theta, rho);

        HoughPeak[] peaks1 = new HoughPeak[] { peak1, peak2, peak3 };

        // Peaks2
        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(2, "rad");
        rho = new Magnitude(1, "m");
        peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(8, "m");
        peak3 = new HoughPeak(theta, rho);

        HoughPeak[] peaks2 = new HoughPeak[] { peak1, peak2, peak3 };

        assertEquals(0.0, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testFit2() {
        // Peaks1
        Magnitude theta = new Magnitude(2, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        HoughPeak peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(8, "m");
        HoughPeak peak3 = new HoughPeak(theta, rho);

        HoughPeak[] peaks1 = new HoughPeak[] { peak1, peak2, peak3 };

        // Peaks2
        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(3, "rad");
        rho = new Magnitude(1, "m");
        peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(2, "rad");
        rho = new Magnitude(8, "m");
        peak3 = new HoughPeak(theta, rho);

        HoughPeak[] peaks2 = new HoughPeak[] { peak1, peak2, peak3 };

        assertEquals(2.0 / 3.0, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testFit3() {
        // Peaks1
        Magnitude theta = new Magnitude(2, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5, "m");
        HoughPeak peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(8, "m");
        HoughPeak peak3 = new HoughPeak(theta, rho);

        HoughPeak[] peaks1 = new HoughPeak[] { peak1, peak2, peak3 };

        // Peaks2
        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(50, "m");
        peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(0, "rad");
        rho = new Magnitude(10, "m");
        peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(1, "rad");
        rho = new Magnitude(80, "m");
        peak3 = new HoughPeak(theta, rho);

        HoughPeak[] peaks2 = new HoughPeak[] { peak1, peak2, peak3 };

        assertEquals(39.0833333, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testHoughSpaceToLine1() {
        Magnitude theta = new Magnitude(PI / 4, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak = new HoughPeak(theta, rho);

        Line2D line = HoughMath.getLine(peak);
        assertEquals(-1.0, line.getSlope().getBaseUnitsValue(), 1e-6);
        assertEquals(1.0 / sin(PI / 4), line.getInterceptY().getValue("m"),
                1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, -1);
        assertEquals(peak, other, 1e-6);
    }



    @Test
    public void testHoughSpaceToLine2() {
        Magnitude theta = new Magnitude(0, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak = new HoughPeak(theta, rho);

        Line2D line = HoughMath.getLine(peak);
        assertTrue(Magnitude.isInfinite(line.getSlope()));
        assertEquals(1.0, line.getInterceptX().getValue("m"), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testHoughSpaceToLine3() {
        Magnitude theta = new Magnitude(3 * PI / 4, "rad");
        Magnitude rho = new Magnitude(-1, "m");
        HoughPeak peak = new HoughPeak(theta, rho);

        Line2D line = HoughMath.getLine(peak);
        assertEquals(1.0, line.getSlope().getBaseUnitsValue(), 1e-6);
        assertEquals(-1.0 / sin(PI / 4), line.getInterceptY().getValue("m"),
                1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testHoughSpaceToLine4() {
        Magnitude theta = new Magnitude(PI / 2, "rad");
        Magnitude rho = new Magnitude(1, "m");
        HoughPeak peak = new HoughPeak(theta, rho);

        Line2D line = HoughMath.getLine(peak);
        assertEquals(0.0, line.getSlope().getBaseUnitsValue(), 1e-6);
        assertEquals(1.0, line.getInterceptY().getValue("m"), 1e-6);

        HoughPeak other = HoughMath.getHoughPeak(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testLineSpaceToHoughSpace1() {
        double m = -1.0;
        Magnitude b = new Magnitude(1.0 / sin(PI / 4), "cm");
        Line2D line = Line2D.fromSlopeIntercept(m, b);

        HoughPeak peak = HoughMath.getHoughPeak(line, -1);
        assertEquals(1.0, peak.rho.getValue("cm"), 1e-6);
        assertEquals(PI / 4, peak.theta.getValue("rad"), 1e-6);

        Line2D other = HoughMath.getLine(peak);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testLineSpaceToHoughSpace2() {
        double m = Double.POSITIVE_INFINITY;
        Magnitude b = new Magnitude(1.0, "cm");
        Line2D line = Line2D.fromSlopeIntercept(m, b);

        HoughPeak peak = HoughMath.getHoughPeak(line, -1);
        assertEquals(1.0, peak.rho.getValue("cm"), 1e-6);
        assertEquals(0.0, peak.theta.getValue("rad"), 1e-6);

        Line2D other = HoughMath.getLine(peak);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testLineSpaceToHoughSpace3() {
        double m = 1.0;
        Magnitude b = new Magnitude(-1.0 / sin(PI / 4), "cm");
        Line2D line = Line2D.fromSlopeIntercept(m, b);

        HoughPeak peak = HoughMath.getHoughPeak(line, -1);
        assertEquals(-1.0, peak.rho.getValue("cm"), 1e-6);
        assertEquals(3 * PI / 4, peak.theta.getValue("rad"), 1e-6);

        Line2D other = HoughMath.getLine(peak);
        assertEquals(line, other, 1e-6);
    }



    @Test
    public void testLineSpaceToHoughSpace4() {
        double m = 0.0;
        Magnitude b = new Magnitude(1.0, "cm");
        Line2D line = Line2D.fromSlopeIntercept(m, b);

        HoughPeak peak = HoughMath.getHoughPeak(line, -1);
        assertEquals(1.0, peak.rho.getValue("cm"), 1e-6);
        assertEquals(PI / 2, peak.theta.getValue("rad"), 1e-6);

        Line2D other = HoughMath.getLine(peak);
        assertEquals(line, other, 1e-6);
    }

}
