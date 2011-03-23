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

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.util.Range;

import static org.junit.Assert.assertEquals;

public class MapStatsTest extends TestCase {

    @Test
    public void testAverage() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(127.0, MapStats.average(map), 0.01);
    }



    @Test
    public void testEntropy() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, MapStats.entropy(map), 0.01);
    }



    @Test
    public void testRange() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        Range<Integer> range = MapStats.range(map);

        assertEquals(127, range.min.intValue());
        assertEquals(127, range.max.intValue());

        map = (ByteMap) load("org/ebsdimage/testdata/pattern_masked.bmp");
        range = MapStats.range(map);

        assertEquals(8, range.min.intValue());
        assertEquals(232, range.max.intValue());
    }



    @Test
    public void testStandardDeviation() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, MapStats.standardDeviation(map), 0.01);
    }



    @Test
    public void testVariance() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, MapStats.variance(map), 0.01);
    }

}
