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

import magnitude.core.Magnitude;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;

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
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));

        // Peak
        Magnitude theta = new Magnitude(3.0, "rad");
        Magnitude rho = new Magnitude(0.5, "px");
        peak1 = new HoughPeak(theta, rho, 1);

        theta = new Magnitude(5.0, "rad");
        rho = new Magnitude(1.5, "px");
        peak2 = new HoughPeak(theta, rho, 3);

        theta = new Magnitude(4.0, "rad");
        rho = new Magnitude(1.0, "px");
        peak3 = new HoughPeak(theta, rho, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testPatternQuality() {
        assertEquals(0.3573920932,
                QualityIndex.patternQuality(peaks, houghMap), 1e-6);
    }



    @Test
    public void testFourier() {
        assertEquals(0.60047537134, QualityIndex.fourier(pattern), 1e-7);
    }



    @Test
    public void testImageQualityIncaPeaks() {
        double expected = peak2.intensity - peak1.intensity;
        assertEquals(expected, QualityIndex.imageQualityInca(peaks), 1e-7);
    }



    @Test
    public void testImageQualityPeaks() {
        double expected =
                (peak1.intensity + peak2.intensity + peak3.intensity) / 3.0;
        assertEquals(expected, QualityIndex.imageQuality(peaks), 1e-7);
    }

}
