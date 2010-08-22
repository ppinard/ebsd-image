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
package org.ebsdimage.core.exp.ops.detection.pre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class ButterflyTest {

    private Butterfly butterfly;



    @Before
    public void setUp() throws Exception {
        butterfly = new Butterfly(9, -500, 500);
    }



    @Test
    public void testButterfly() {
        Butterfly tmpButterfly = new Butterfly();

        assertEquals(Butterfly.DEFAULT_KERNEL_SIZE, tmpButterfly.kernelSize);
        assertEquals(Butterfly.DEFAULT_FLATTEN_LOWER_LIMIT,
                tmpButterfly.flattenLowerLimit, 1e-6);
        assertEquals(Butterfly.DEFAULT_FLATTEN_UPPER_LIMIT,
                tmpButterfly.flattenUpperLimit, 1e-6);
    }



    @Test
    public void testButterflyIntFloatFloat() {
        assertEquals(9, butterfly.kernelSize);
        assertEquals(-500, butterfly.flattenLowerLimit, 1e-6);
        assertEquals(500, butterfly.flattenUpperLimit, 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testButterflyIntFloatFloatException1() {
        new Butterfly(3, -500, 500);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testButterflyIntFloatFloatException2() {
        new Butterfly(9, 500, -500);
    }



    @Test
    public void testEquals() {
        Butterfly other = new Butterfly(9, -500, 500);
        assertFalse(butterfly == other);
        assertEquals(butterfly, other);
    }



    @Test
    public void testProcess() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        HoughMap expected =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/butterfly_op.bmp"));

        HoughMap destMap = butterfly.process(null, srcMap);

        destMap.assertEquals(expected);
    }



    @Test
    public void testToString() {
        assertEquals(
                butterfly.toString(),
                "Butterfly [flatten lower limit=-500.0, flatten upper limit=500.0, kernel size=9]");
    }

}
