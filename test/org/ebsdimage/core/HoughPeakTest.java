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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HoughPeakTest {

    private HoughPeak peak1;

    private HoughPeak peak2;



    @Before
    public void setUp() throws Exception {
        Magnitude theta = new Magnitude(0.5, "rad");
        Magnitude rho = new Magnitude(3.0, "px");
        peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(0.1, "rad");
        rho = new Magnitude(5.0, "px");
        peak2 = new HoughPeak(theta, rho);
    }



    @Test
    public void testEqualsObject() {
        Magnitude theta = new Magnitude(0.5, "rad");
        Magnitude rho = new Magnitude(3.0, "px");
        HoughPeak peak = new HoughPeak(theta, rho);
        assertEquals(peak, peak1);

        assertFalse(peak1.equals(peak2));
    }



    @Test
    public void testEqualsPeakDouble() {
        Magnitude theta = new Magnitude(0.5, "rad");
        Magnitude rho = new Magnitude(3.001, "px");
        HoughPeak peak = new HoughPeak(theta, rho);
        assertTrue(peak1.equals(peak, 1e-3));
    }



    @Test
    public void testHoughPeakDoubleDouble() {
        assertEquals(0.5, peak1.theta.getValue("rad"), 1e-7);
        assertEquals(3.0, peak1.rho.getValue("px"), 1e-7);

        assertEquals(0.1, peak2.theta.getValue("rad"), 1e-7);
        assertEquals(5.0, peak2.rho.getValue("px"), 1e-7);
    }



    @Test
    public void testThetaOutsidePI() {
        Magnitude theta = new Magnitude(Math.PI + 1.25, "rad");
        Magnitude rho = new Magnitude(4, "px");
        HoughPeak other = new HoughPeak(theta, rho);
        assertEquals(1.25, other.theta.getValue("rad"), 1e-6);
        assertEquals(-4, other.rho.getValue("px"), 1e-6);

        theta = new Magnitude(Math.PI * 2 + 1.25, "rad");
        rho = new Magnitude(4, "px");
        other = new HoughPeak(theta, rho);
        assertEquals(1.25, other.theta.getValue("rad"), 1e-6);
        assertEquals(4, other.rho.getValue("px"), 1e-6);

        theta = new Magnitude(Math.PI * 3 + 1.25, "rad");
        rho = new Magnitude(4, "px");
        other = new HoughPeak(theta, rho);
        assertEquals(1.25, other.theta.getValue("rad"), 1e-6);
        assertEquals(-4, other.rho.getValue("px"), 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals("(28.65 deg, 3.0 px)", peak1.toString());
        assertEquals("(5.73 deg, 5.0 px)", peak2.toString());
    }

}
