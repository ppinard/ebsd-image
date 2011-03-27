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
package ptpshared.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static java.lang.Math.PI;

public class MathTest {

    @Test
    public void testAcos() {
        assertEquals(Math.acos(4), 0, 1e-7);
        assertEquals(Math.acos(-4), PI, 1e-7);
        assertEquals(Math.acos(0.5), 60 / 180.0 * PI, 1e-7);
        assertEquals(Math.acos(0.45675), java.lang.Math.acos(0.45675), 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testIsPositiveException() {
        Math.sign(Double.NaN);
    }



    @Test
    public void testSign() {
        assertEquals(1, Math.sign(Double.POSITIVE_INFINITY));
        assertEquals(1, Math.sign(1.0));
        assertEquals(0, Math.sign(0.0));
        assertEquals(-1, Math.sign(-1.0));
        assertEquals(-1, Math.sign(Double.NEGATIVE_INFINITY));
    }



    @Test
    public void testSqrt() {
        assertEquals(Math.sqrt(4), 2, 1e-7);
        assertEquals(Math.sqrt(-4), 0.0, 1e-7);
    }

}
