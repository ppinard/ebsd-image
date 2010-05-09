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

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.core.exp.ops.identification.results.HoughPeakRange;
import org.junit.Before;
import org.junit.Test;


public class HoughPeakRangeTest {

    private HoughPeakRange houghPeakRange;
    private HoughPeak[] peaks;
    private HoughPeak peak1;
    private HoughPeak peak2;



    @Before
    public void setUp() throws Exception {
        peak1 = new HoughPeak(3.0, 0.5, 1);
        peak2 = new HoughPeak(5.0, 1.5, 2);

        peaks = new HoughPeak[] { peak1, peak2 };

        houghPeakRange = new HoughPeakRange();
    }



    @Test
    public void testCalculate() {
        double expected = peak2.intensity - peak1.intensity;
        OpResult result = houghPeakRange.calculate(null, peaks)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(houghPeakRange.toString(), "Hough Peak Range");
    }

}
