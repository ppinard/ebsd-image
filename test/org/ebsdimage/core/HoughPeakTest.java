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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HoughPeakTest {

    private HoughPeak peak1;

    private HoughPeak peak2;



    @Before
    public void setUp() throws Exception {

        peak1 = new HoughPeak(3, 0.5);
        peak2 = new HoughPeak(5, 0.1);
    }



    @Test
    public void testEqualsObject() {
        HoughPeak peak = new HoughPeak(3.0, 0.5);
        assertEquals(peak, peak1);

        assertFalse(peak1.equals(peak2));
    }



    @Test
    public void testEqualsPeakDouble() {
        HoughPeak peak = new HoughPeak(3.001, 0.5);
        assertTrue(peak1.equals(peak, 1e-3));
    }



    @Test
    public void testHoughPeakDoubleDouble() {
        assertEquals(0.5, peak1.theta, 1e-7);
        assertEquals(3.0, peak1.rho, 1e-7);

        assertEquals(0.1, peak2.theta, 1e-7);
        assertEquals(5.0, peak2.rho, 1e-7);
    }



    @Test
    public void testThetaOutsidePI() {
        HoughPeak other = new HoughPeak(4, Math.PI + 1.25);
        assertEquals(1.25, other.theta, 1e-6);
        assertEquals(-4, other.rho, 1e-6);

        other = new HoughPeak(4, Math.PI * 2 + 1.25);
        assertEquals(1.25, other.theta, 1e-6);
        assertEquals(4, other.rho, 1e-6);

        other = new HoughPeak(4, Math.PI * 3 + 1.25);
        assertEquals(1.25, other.theta, 1e-6);
        assertEquals(-4, other.rho, 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals("(3.0, 0.5)", peak1.toString());
        assertEquals("(5.0, 0.1)", peak2.toString());
    }

}
