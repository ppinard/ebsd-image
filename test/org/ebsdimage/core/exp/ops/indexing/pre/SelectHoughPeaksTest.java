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
package org.ebsdimage.core.exp.ops.indexing.pre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import rmlshared.util.Arrays;

public class SelectHoughPeaksTest {

    private HoughPeaksSelector op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new HoughPeaksSelector(4, 6);
        srcPeaks =
                new HoughPeak[] { new HoughPeak(7, 0.1, 3),
                        new HoughPeak(6, 0.2, 6), new HoughPeak(5, 0.3, 4),
                        new HoughPeak(4, 0.4, 7), new HoughPeak(3, 0.5, 2),
                        new HoughPeak(2, 0.6, 5), new HoughPeak(1, 0.7, 1) };
    }



    @Test
    public void testProcess1() {
        HoughPeak[] destPeaks = op.process(null, srcPeaks);

        assertEquals(6, destPeaks.length);
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(4, 0.4, 7)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(6, 0.2, 6)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(2, 0.6, 5)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(5, 0.3, 4)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(7, 0.1, 3)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(3, 0.5, 2)));
    }



    @Test
    public void testProcess2() {
        HoughPeak[] otherPeaks =
                new HoughPeak[] { new HoughPeak(7, 0.1, 3),
                        new HoughPeak(6, 0.2, 6), new HoughPeak(5, 0.3, 4),
                        new HoughPeak(4, 0.4, 1), new HoughPeak(3, 0.5, 2) };
        HoughPeak[] destPeaks = op.process(null, otherPeaks);

        assertEquals(5, destPeaks.length);
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(6, 0.2, 6)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(5, 0.3, 4)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(7, 0.1, 3)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(3, 0.5, 2)));
        assertTrue(Arrays.contains(destPeaks, new HoughPeak(4, 0.4, 1)));
    }



    @Test
    public void testProcess3() {
        HoughPeak[] otherPeaks =
                new HoughPeak[] { new HoughPeak(7, 0.1, 3),
                        new HoughPeak(6, 0.2, 6), new HoughPeak(5, 0.3, 4) };
        HoughPeak[] destPeaks = op.process(null, otherPeaks);

        assertEquals(0, destPeaks.length);
    }



    @Test
    public void testSelectHoughPeaks() {
        HoughPeaksSelector other = new HoughPeaksSelector();

        assertEquals(HoughPeaksSelector.DEFAULT_MINIMUM, other.minimum);
        assertEquals(HoughPeaksSelector.DEFAULT_MAXIMUM, other.maximum);
    }



    @Test
    public void testSelectHoughPeaksIntInt() {
        assertEquals(4, op.minimum);
        assertEquals(6, op.maximum);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSelectHoughPeaksIntIntException1() {
        new HoughPeaksSelector(-1, 6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSelectHoughPeaksIntIntException2() {
        new HoughPeaksSelector(4, -1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSelectHoughPeaksIntIntException3() {
        new HoughPeaksSelector(6, 4);
    }



    @Test
    public void testToString() {
        String expected = "Hough Peaks Selector [minimum=4, maximum=6]";
        assertEquals(expected, op.toString());
    }

}
