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
package org.ebsdimage.core.exp.ops.pattern.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class SumTest extends TestCase {

    private Sum sum;

    private ByteMap patternMap;



    @Before
    public void setUp() throws Exception {
        sum = new Sum(0.2, 0.3, 0.5, 0.6);
        patternMap =
                (ByteMap) load(getFile("org/ebsdimage/testdata/pattern.bmp"));
    }



    @Test
    public void testCalculateByteMap() {
        double value = sum.calculateSum(patternMap);
        assertEquals(801235, value, 1e-6);
    }



    @Test
    public void testAverageRegionDoubleDoubleDoubleDouble() {
        assertEquals(0.2, sum.xmin, 1e-6);
        assertEquals(0.3, sum.ymin, 1e-6);
        assertEquals(0.5, sum.xmax, 1e-6);
        assertEquals(0.6, sum.ymax, 1e-6);
    }



    @Test
    public void testAverageRegion() {
        Sum other = new Sum();
        assertEquals(Sum.DEFAULT_XMIN, other.xmin, 1e-6);
        assertEquals(Sum.DEFAULT_YMIN, other.ymin, 1e-6);
        assertEquals(Sum.DEFAULT_XMAX, other.xmax, 1e-6);
        assertEquals(Sum.DEFAULT_YMAX, other.ymax, 1e-6);
    }



    @Test
    public void testEqualsObject() {
        Sum other = new Sum(0.2, 0.3, 0.5, 0.6);

        assertFalse(other == sum);
        assertTrue(other.equals(sum));

        assertFalse(new Sum(0.2, 0.3, 0.5, 0.9).equals(sum));
    }



    @Test
    public void testToString() {
        String expected = "Sum [xmin=0.2, ymin=0.3, xmax=0.5, ymax=0.6]";
        assertEquals(expected, sum.toString());
    }

}
