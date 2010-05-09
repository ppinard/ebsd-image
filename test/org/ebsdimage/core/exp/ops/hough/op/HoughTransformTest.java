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
package org.ebsdimage.core.exp.ops.hough.op;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

public class HoughTransformTest extends TestCase {

    private HoughTransform hough;



    @Before
    public void setUp() throws Exception {
        hough = new HoughTransform(toRadians(1.0));
    }



    @Test
    public void testEquals() {
        assertTrue(hough.equals(hough));

        assertFalse(hough.equals(null));

        assertFalse(hough.equals(new Object()));

        assertFalse(hough.equals(new HoughTransform()));

        HoughTransform other = new HoughTransform(toRadians(1.0));
        assertFalse(hough == other);
        assertTrue(hough.equals(other));
    }



    @Test
    public void testHoughTransformOp() {
        HoughTransform tmpHough = new HoughTransform();
        assertEquals(HoughTransform.DEFAULT_RESOLUTION, tmpHough.resolution,
                1e-7);
    }



    @Test
    public void testHoughTransformOpDouble() {
        assertEquals(toRadians(1.0), hough.resolution, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testHoughTransformOpDoubleException() {
        new HoughTransform(-1.0);
    }



    @Test
    public void testProcess() throws IOException {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");
        HoughMap expectedMap =
                new HoughMapLoader().load(FileUtil
                        .getFile("org/ebsdimage/testdata/houghmap.bmp"));

        HoughMap destMap = hough.transform(null, srcMap);

        destMap.assertEquals(expectedMap);
    }



    @Test
    public void testToString() {
        assertEquals(hough.toString(),
                "Hough Transform [resolution=0.017453292519943295 rad]");
    }

}
