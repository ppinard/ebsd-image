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
import static java.lang.Math.sin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughMath;
import org.ebsdimage.core.HoughPeak;
import org.junit.Test;

import ptpshared.core.geom.Line;

public class HoughMathTest {

    @Test
    public void testFit1() {
        // Peaks1
        HoughPeak[] peaks1 = new HoughPeak[] { new HoughPeak(1, 2),
                new HoughPeak(5, 1.5), new HoughPeak(8, 1) };

        // Peak2
        HoughPeak[] peaks2 = new HoughPeak[] { new HoughPeak(5, 1.5),
                new HoughPeak(1, 2), new HoughPeak(8, 1) };

        assertEquals(0.0, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testFit2() {
        // Peaks1
        HoughPeak[] peaks1 = new HoughPeak[] { new HoughPeak(1, 2),
                new HoughPeak(5, 1.5), new HoughPeak(8, 1) };

        // Peak2
        HoughPeak[] peaks2 = new HoughPeak[] { new HoughPeak(5, 1.5),
                new HoughPeak(1, 3), new HoughPeak(8, 2) };

        assertEquals(2.0 / 3.0, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testFit3() {
        // Peaks1
        HoughPeak[] peaks1 = new HoughPeak[] { new HoughPeak(1, 2),
                new HoughPeak(5, 1.5), new HoughPeak(8, 1) };

        // Peak2
        HoughPeak[] peaks2 = new HoughPeak[] { new HoughPeak(50, 3),
                new HoughPeak(10, 0), new HoughPeak(80, 1) };

        assertEquals(39.0833333, HoughMath.fit(peaks1, peaks2), 1e-6);
    }



    @Test
    public void testHoughSpaceToLine1() {
        HoughPeak peak = new HoughPeak(1, PI / 4);

        Line line = HoughMath.houghSpaceToLine(peak);
        assertEquals(-1.0, line.m, 1e-6);
        assertEquals(1.0 / sin(PI / 4), line.k, 1e-6);

        HoughPeak other = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testHoughSpaceToLine2() {
        HoughPeak peak = new HoughPeak(1, 0);

        Line line = HoughMath.houghSpaceToLine(peak);
        assertTrue(Double.isInfinite(line.m));
        assertEquals(1.0, line.k, 1e-6);

        HoughPeak other = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testHoughSpaceToLine3() {
        HoughPeak peak = new HoughPeak(-1, 3 * PI / 4);

        Line line = HoughMath.houghSpaceToLine(peak);
        assertEquals(1.0, line.m, 1e-6);
        assertEquals(-1.0 / sin(PI / 4), line.k, 1e-6);

        HoughPeak other = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testHoughSpaceToLine4() {
        HoughPeak peak = new HoughPeak(1, PI / 2);

        Line line = HoughMath.houghSpaceToLine(peak);
        assertEquals(0.0, line.m, 1e-6);
        assertEquals(1.0, line.k, 1e-6);

        HoughPeak other = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertTrue(peak.equals(other, 1e-6));
    }



    @Test
    public void testLineSpaceToHoughSpace1() {
        Line line = new Line(-1.0, 1.0 / sin(PI / 4));

        HoughPeak peak = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(PI / 4, peak.theta, 1e-6);

        Line other = HoughMath.houghSpaceToLine(peak);
        assertTrue(line.equals(other, 1e-6));
    }



    @Test
    public void testLineSpaceToHoughSpace2() {
        Line line = new Line(Double.POSITIVE_INFINITY, 1.0);

        HoughPeak peak = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(0.0, peak.theta, 1e-6);

        Line other = HoughMath.houghSpaceToLine(peak);
        assertTrue(line.equals(other, 1e-6));
    }



    @Test
    public void testLineSpaceToHoughSpace3() {
        Line line = new Line(1.0, -1.0 / sin(PI / 4));

        HoughPeak peak = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertEquals(-1.0, peak.rho, 1e-6);
        assertEquals(3 * PI / 4, peak.theta, 1e-6);

        Line other = HoughMath.houghSpaceToLine(peak);
        assertTrue(line.equals(other, 1e-6));
    }



    @Test
    public void testLineSpaceToHoughSpace4() {
        Line line = new Line(0.0, 1.0);

        HoughPeak peak = HoughMath.lineSpaceToHoughSpace(line, -1);
        assertEquals(1.0, peak.rho, 1e-6);
        assertEquals(PI / 2, peak.theta, 1e-6);

        Line other = HoughMath.houghSpaceToLine(peak);
        assertTrue(line.equals(other, 1e-6));
    }

}
