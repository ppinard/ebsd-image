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

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.MapStats;
import rmlshared.io.FileUtil;
import rmlshared.util.Range;

public class QualityIndexTest extends TestCase {

    private ByteMap pattern;
    private HoughMap houghMap;

    private HoughPeak[] peaks;
    private HoughPeak peak1;
    private HoughPeak peak2;
    private HoughPeak peak3;



    @Before
    public void setUp() throws Exception {
        // Pattern
        pattern = (ByteMap) load("org/ebsdimage/testdata/pattern_masked.bmp");

        // Hough
        houghMap =
                new HoughMapLoader().load(FileUtil
                        .getFile("org/ebsdimage/testdata/houghmap.bmp"));

        // Peak
        peak1 = new HoughPeak(3.0, 0.5, 1);
        peak2 = new HoughPeak(5.0, 1.5, 3);
        peak3 = new HoughPeak(4.0, 1.0, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testAverage() {
        assertEquals(119.1859932, QualityIndex.average(pattern), 1e-7);
    }



    @Test
    public void testEntropy() {
        assertEquals(4.50234954, QualityIndex.entropy(pattern), 1e-7);
    }



    @Test
    public void testFourier() {
        assertEquals(0.60047537134, QualityIndex.fourier(pattern), 1e-7);
    }



    @Test
    public void testHoughPeakRangePeaks() {
        double expected = peak2.intensity - peak1.intensity;
        assertEquals(expected, QualityIndex.houghPeakRange(peaks), 1e-6);
    }



    @Test
    public void testHoughRange() {
        Range<Integer> range = MapStats.range(houghMap);
        int expected = range.max - range.min;
        assertEquals(expected, QualityIndex.houghRange(houghMap));
    }



    @Test
    public void testImageQualityIncaPeaks() {
        double expected = 256 * (peak2.intensity - peak1.intensity) / 20000.0;
        assertEquals(expected, QualityIndex.imageQualityInca(peaks), 1e-7);
    }



    @Test
    public void testImageQualityPeaks() {
        double expected =
                (peak1.intensity + peak2.intensity + peak3.intensity) / 3.0;
        assertEquals(expected, QualityIndex.imageQuality(peaks), 1e-7);
    }



    @Test
    public void testNumberPeaksPeaks() {
        assertEquals(peaks.length, QualityIndex.peaksCount(peaks));
    }



    @Test
    public void testStandardDeviation() {
        assertEquals(22.343286285, QualityIndex.standardDeviation(pattern),
                1e-7);
    }

}
