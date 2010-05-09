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
import org.ebsdimage.core.MaskDisc;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class MaskTest extends TestCase {

    private Mask mask;



    @Before
    public void setUp() throws Exception {
        mask = new Mask(10, 11, 8);
    }



    @Test
    public void testCrop() {
        Mask other = new Mask();
        assertEquals(Mask.DEFAULT_CENTROID_X, other.centroidX);
        assertEquals(Mask.DEFAULT_CENTROID_Y, other.centroidY);
        assertEquals(Mask.DEFAULT_RADIUS, other.radius);
    }



    @Test
    public void testCropIntIntInt() {
        assertEquals(10, mask.centroidX);
        assertEquals(11, mask.centroidY);
        assertEquals(8, mask.radius);
    }



    @Test
    public void testEquals() {
        Mask other = new Mask(10, 11, 8);
        assertFalse(mask == other);
        assertEquals(mask, other);
    }



    @Test
    public void testProcess() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        ByteMap expectedMap = (ByteMap) load("org/ebsdimage/testdata/mask.bmp");

        ByteMap destMap = mask.process(null, srcMap);

        destMap.assertEquals(expectedMap);

        assertEquals(10, destMap.getProperty(MaskDisc.KEY_CENTROID_X, -1));
        assertEquals(11, destMap.getProperty(MaskDisc.KEY_CENTROID_Y, -1));
        assertEquals(8, destMap.getProperty(MaskDisc.KEY_RADIUS, -1));
    }



    @Test
    public void testProcess2() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        Mask other = new Mask(-1, -1, -3);

        ByteMap destMap = other.process(null, srcMap);

        assertEquals(srcMap.width / 2, destMap.getProperty(
                MaskDisc.KEY_CENTROID_X, -1));
        assertEquals(srcMap.height / 2, destMap.getProperty(
                MaskDisc.KEY_CENTROID_Y, -1));
        assertEquals(srcMap.width / 2 - 2, destMap.getProperty(
                MaskDisc.KEY_RADIUS, -1));
    }



    @Test
    public void testToString() {
        assertEquals(mask.toString(),
                "Mask [centroid X=10 px, centroid Y=11 px, radius=8 px]");
    }

}
