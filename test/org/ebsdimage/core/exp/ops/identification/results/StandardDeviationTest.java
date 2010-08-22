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
package org.ebsdimage.core.exp.ops.identification.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

public class StandardDeviationTest {

    private StandardDeviation stdDev;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        stdDev = new StandardDeviation(2);
        peaks =
                new HoughPeak[] { new HoughPeak(3.0, 0.5, 1),
                        new HoughPeak(5.0, 1.5, 2), new HoughPeak(7.0, 2.5, 4) };
    }



    @Test
    public void testEqualsObject() {
        StandardDeviation other = new StandardDeviation(2);

        assertFalse(other == stdDev);
        assertTrue(other.equals(stdDev));

        assertFalse(new StandardDeviation(3).equals(stdDev));
    }



    @Test
    public void testToString() {
        String expected = "Standard Deviation [max=2]";
        assertEquals(expected, stdDev.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = stdDev.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(1.41421356, results[0].value.doubleValue(), 1e-6);

        results = new StandardDeviation().calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(1.52752523, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testStandardDeviation() {
        StandardDeviation other = new StandardDeviation();
        assertEquals(StandardDeviation.DEFAULT_MAX, other.max);
    }



    @Test
    public void testStandardDeviationInt() {
        assertEquals(2, stdDev.max);
    }

}
