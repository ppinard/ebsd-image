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

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.util.Range;

public class AnalysisTest extends TestCase {

    @Test
    public void testAverage() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(127.0, Analysis.average(map), 0.01);
    }



    @Test
    public void testEntropy() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, Analysis.entropy(map), 0.01);
    }



    @Test
    public void testGetR() {
        ByteMap map = (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");
        assertEquals(139.3777, Analysis.getR(map, 9405), 0.001);
        assertEquals(28.4444, Analysis.getR(map, 23437), 0.001);
        assertEquals(133.6888, Analysis.getR(map, 10211), 0.001);
        assertEquals(-24.1777, Analysis.getR(map, 30143), 0.001);
    }



    @Test
    public void testGetTheta() {
        ByteMap map = (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");
        assertEquals(toRadians(45), Analysis.getTheta(map, 9405), 0.001);
        assertEquals(toRadians(37), Analysis.getTheta(map, 23437), 0.001);
        assertEquals(toRadians(131), Analysis.getTheta(map, 10211), 0.001);
        assertEquals(toRadians(135), Analysis.getTheta(map, 21195), 0.001);
    }



    @Test
    public void testStandardDeviation() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, Analysis.standardDeviation(map), 0.01);
    }



    @Test
    public void testVariance() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, Analysis.variance(map), 0.01);
    }



    @Test
    public void testRange() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        Range<Integer> range = Analysis.range(map);

        assertEquals(127, range.min.intValue());
        assertEquals(127, range.max.intValue());

        map = (ByteMap) load("org/ebsdimage/testdata/pattern_masked.bmp");
        range = Analysis.range(map);

        assertEquals(8, range.min.intValue());
        assertEquals(232, range.max.intValue());
    }

}
