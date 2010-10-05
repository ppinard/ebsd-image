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

public class AnalysisTest extends TestCase {

    @Test
    public void testGetR() {
        ByteMap map = (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");
        assertEquals(114.7818, Analysis.getR(map, 9405), 0.001);
        assertEquals(-31.9884, Analysis.getR(map, 23437), 0.001);
        assertEquals(107.2551, Analysis.getR(map, 10211), 0.001);
        assertEquals(-101.6101, Analysis.getR(map, 30143), 0.001);
    }



    @Test
    public void testGetTheta() {
        ByteMap map = (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");
        assertEquals(toRadians(45), Analysis.getTheta(map, 9405), 0.001);
        assertEquals(toRadians(37), Analysis.getTheta(map, 23437), 0.001);
        assertEquals(toRadians(131), Analysis.getTheta(map, 10211), 0.001);
        assertEquals(toRadians(135), Analysis.getTheta(map, 21195), 0.001);
    }

}
