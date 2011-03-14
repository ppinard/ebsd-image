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
        peak1 = new HoughPeak(0.5, 3.0, 1.5);
        peak2 = new HoughPeak(0.1, 5.0, 55);
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(peak1.equals(peak1, 1e-3));
        assertFalse(peak1.equals(null, 1e-3));
        assertFalse(peak1.equals(new Object(), 1e-3));

        HoughPeak other = new HoughPeak(0.51, 3.0001, 1.5001);
        assertFalse(other.equals(peak1, 1e-3));

        other = new HoughPeak(0.5001, 3.1, 1.5001);
        assertFalse(other.equals(peak1, 1e-3));

        other = new HoughPeak(0.5001, 3.0001, 1.51);
        assertFalse(other.equals(peak1, 1e-3));

        other = new HoughPeak(0.5001, 3.0001, 1.5001);
        assertTrue(other.equals(peak1, 1e-3));
    }



    @Test
    public void testEquivalent() {
        assertTrue(peak1.equivalent(peak1, 1e-3, 1e-2));
        assertFalse(peak1.equivalent(null, 1e-3, 1e-2));

        HoughPeak other = new HoughPeak(0.5001, 3.1, 99);
        assertFalse(other.equivalent(peak1, 1e-3, 1e-2));

        other = new HoughPeak(0.51, 3.001, 99);
        assertFalse(other.equivalent(peak1, 1e-3, 1e-2));

        other = new HoughPeak(0.5001, 3.001, 99);
        assertTrue(other.equivalent(peak1, 1e-3, 1e-2));
    }



    @Test
    public void testHoughPeak() {
        assertEquals(0.5, peak1.theta, 1e-7);
        assertEquals(3.0, peak1.rho, 1e-7);

        assertEquals(0.1, peak2.theta, 1e-7);
        assertEquals(5.0, peak2.rho, 1e-7);
    }



    @Test
    public void testHoughPeakThetaOutsidePI() {
        HoughPeak other = new HoughPeak(Math.PI + 1.25, 4, 1);
        assertEquals(1.25, other.theta, 1e-6);
        assertEquals(-4, other.rho, 1e-6);

        other = new HoughPeak(Math.PI * 2 + 1.25, 4, 1);
        assertEquals(1.25, other.theta, 1e-6);
        assertEquals(4, other.rho, 1e-6);

        other = new HoughPeak(Math.PI * 3 + 1.25, 4, 1);
        assertEquals(1.25, other.theta, 1e-6);
        assertEquals(-4, other.rho, 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals("(28.65 deg, 3.00 px): 1.5", peak1.toString());
        assertEquals("(5.73 deg, 5.00 px): 55.0", peak2.toString());
    }

}
