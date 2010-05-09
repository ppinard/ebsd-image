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
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakPair;
import org.ebsdimage.core.HoughPeakPairs;
import org.junit.Before;
import org.junit.Test;

public class HoughPeakPairsTest {

    private Camera camera;
    private HoughPeak[] peaks;
    private HoughPeakPairs pairs;



    @Before
    public void setUp() throws Exception {
        camera = new Camera(0.0, 0.0, 0.5);

        peaks = new HoughPeak[] { new HoughPeak(1.0, PI / 2, 10e3),
                new HoughPeak(1.0, PI / 4, 9e3), new HoughPeak(1.0, PI / 3, 1) };

        pairs = new HoughPeakPairs(peaks, camera);
    }



    @Test
    public void testHoughPeakPairs1() {
        // Two vertical lines with a detector distance equal to close to
        // infinity

        Camera camera = new Camera(0.0, 0.0, 10e3);

        HoughPeak[] peaks = new HoughPeak[] { new HoughPeak(1.0, 0),
                new HoughPeak(-1.0, 0) };

        HoughPeakPairs pairs = new HoughPeakPairs(peaks, camera);

        assertEquals(1, pairs.size());
        assertEquals(1, pairs.get(0).directionCosine, 1e-6);
    }



    @Test
    public void testHoughPeakPairs2() {
        // Two horizontal lines with a detector distance equal to close to
        // infinity

        Camera camera = new Camera(0.0, 0.0, 10e3);

        HoughPeak[] peaks = new HoughPeak[] { new HoughPeak(1.0, PI / 2),
                new HoughPeak(-1.0, PI / 2) };

        HoughPeakPairs pairs = new HoughPeakPairs(peaks, camera);

        assertEquals(1, pairs.size());
        assertEquals(1, pairs.get(0).directionCosine, 1e-6);
    }



    @Test
    public void testHoughPeakPairs3() {
        // One horizontal line and a line at 45 deg with a detector distance
        // equal to close to infinity

        Camera camera = new Camera(0.0, 0.0, 10e3);

        HoughPeak[] peaks = new HoughPeak[] { new HoughPeak(1.0, PI / 2),
                new HoughPeak(1.0, PI / 4) };

        HoughPeakPairs pairs = new HoughPeakPairs(peaks, camera);

        assertEquals(1, pairs.size());
        assertEquals(sqrt(2) / 2, pairs.get(0).directionCosine, 1e-6);
    }



    @Test
    public void testHoughPeakPairs4() {
        // One horizontal line and a line at 45 deg with a detector distance
        // equal to 0.5

        assertEquals(3, pairs.size());
        assertEquals(0.94142136, pairs.get(0).directionCosine, 1e-6);
        // Less than 45 deg
    }



    @Test
    public void testSortByDirectionCosineBoolean1() {
        pairs.sortByDirectionCosine(false);

        assertEquals(0.94142136, pairs.get(0).directionCosine, 1e-6);
        assertEquals(0.99318516, pairs.get(pairs.size() - 1).directionCosine,
                1e-6);
    }



    @Test
    public void testSortByDirectionCosineBoolean2() {
        pairs.sortByDirectionCosine(true);

        assertEquals(0.94142136, pairs.get(pairs.size() - 1).directionCosine,
                1e-6);
        assertEquals(0.99318516, pairs.get(0).directionCosine, 1e-6);
    }



    @Test
    public void testFindClosestMatches() {
        // Near 0
        HoughPeakPair[] matches = pairs.findClosestMatches(1, 1e-3);
        assertEquals(1, matches.length); // Match at 6.69 deg

        // Near 13 deg +- 0.001
        matches = pairs.findClosestMatches(0.9732, 1e-3);
        assertEquals(1, matches.length);

        // Near 6 deg +- 6.6 deg
        matches = pairs.findClosestMatches(0.993185, 2e-2);
        assertEquals(2, matches.length);
    }

}
