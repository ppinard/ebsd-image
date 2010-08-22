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

public class TransformTest extends TestCase {

    @Test
    public void testHough() {
        // Create a HoughMap
        ByteMap pattern = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");
        pattern.setProperty("test", 1.2345);

        HoughMap houghMap = Transform.hough(pattern, toRadians(1.0));

        // Check the pattern size
        assertEquals(336, pattern.width);
        assertEquals(256, pattern.height);

        // Check the HoughMap size
        assertEquals(180, houghMap.width);
        assertEquals(301, houghMap.height);

        // Check the HoughMap parameters
        assertEquals(213.3333, houghMap.rMax, 0.001);
        assertEquals(-213.3333, houghMap.rMin, 0.001);
        assertEquals(1.4222, houghMap.deltaR, 0.001);
        assertEquals(toRadians(1.0), houghMap.deltaTheta, 0.001);

        ByteMap houghByteMap = Conversion.toByteMap(houghMap);
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");
        houghByteMap.assertEquals(expected);

        // Check that the pattern properties are in the hough properties
        assertEquals(1.2345, houghMap.getProperty("test", Double.NaN), 1e-6);
    }

}
