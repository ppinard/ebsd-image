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
package org.ebsdimage.core.exp.ops.pattern.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class SmoothingTest extends TestCase {

    private Smoothing smoothing;



    @Before
    public void setUp() throws Exception {
        smoothing = new Smoothing(3);
    }



    @Test
    public void testEquals() {
        Smoothing other = new Smoothing(3);
        assertFalse(smoothing == other);
        assertEquals(smoothing, other);
    }



    @Test
    public void testProcess() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/smoothing.bmp");

        ByteMap destMap = smoothing.process(null, srcMap);

        destMap.assertEquals(expectedMap);
    }



    @Test
    public void testSmoothing() {
        Smoothing tmpSmoothing = new Smoothing();
        assertEquals(Smoothing.DEFAULT_KERNEL_SIZE, tmpSmoothing.kernelSize);
    }



    @Test
    public void testSmoothingInt() {
        assertEquals(3, smoothing.kernelSize);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSmoothingIntException() {
        new Smoothing(0);
    }



    @Test
    public void testToString() {
        assertEquals(smoothing.toString(), "Smoothing [kernel size=3]");
    }

}
