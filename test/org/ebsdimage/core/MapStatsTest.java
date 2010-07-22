package org.ebsdimage.core;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.util.Range;

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
    public void testStandardDeviation() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, MapStats.standardDeviation(map), 0.01);
    }



    @Test
    public void testVariance() {
        ByteMap map = (ByteMap) load("org/ebsdimage/core/disk127.bmp");
        assertEquals(0.0, MapStats.variance(map), 0.01);
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

}
