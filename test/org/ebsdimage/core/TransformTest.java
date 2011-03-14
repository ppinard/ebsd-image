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

import static org.junit.Assert.assertEquals;

import static java.lang.Math.toRadians;

public class TransformTest extends TestCase {

    @Test
    public void testDoHoughByteMapDouble() {
        // Create a HoughMap
        ByteMap pattern = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");

        HoughMap houghMap = Transform.hough(pattern, toRadians(1.0));

        // Check the pattern size
        assertEquals(336, pattern.width);
        assertEquals(256, pattern.height);

        // Check the HoughMap size
        assertEquals(180, houghMap.width);
        assertEquals(227, houghMap.height);

        // Check the HoughMap parameters
        assertEquals(212.628585835, houghMap.getRhoMax(), 1e-3);
        assertEquals(-212.628585835, houghMap.getRhoMin(), 1e-3);
        assertEquals(1.8816689, houghMap.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(1.0, houghMap.getDeltaTheta().getValue("deg"), 1e-3);

        ByteMap houghByteMap = Conversion.toByteMap(houghMap);
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");
        houghByteMap.assertEquals(expected);
    }



    @Test
    public void testDoHoughByteMapDoubleDouble() {
        // Create a HoughMap
        ByteMap pattern = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");

        HoughMap houghMap = Transform.hough(pattern, toRadians(1.0), 1.0);

        // Check the pattern size
        assertEquals(336, pattern.width);
        assertEquals(256, pattern.height);

        // Check the HoughMap size
        assertEquals(180, houghMap.width);
        assertEquals(425, houghMap.height);

        // Check the HoughMap parameters
        assertEquals(212.0, houghMap.getRhoMax(), 1e-3);
        assertEquals(-212.0, houghMap.getRhoMin(), 1e-3);
        assertEquals(1.0, houghMap.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(1.0, houghMap.getDeltaTheta().getValue("deg"), 1e-3);

        ByteMap houghByteMap = Conversion.toByteMap(houghMap);
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/houghmap2.bmp");
        houghByteMap.assertEquals(expected);
    }

}
