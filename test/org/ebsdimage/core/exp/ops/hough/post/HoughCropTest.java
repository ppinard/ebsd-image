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
package org.ebsdimage.core.exp.ops.hough.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class HoughCropTest {

    private HoughCrop houghCrop;



    @Before
    public void setUp() throws Exception {
        houghCrop = new HoughCrop(8);
    }



    @Test
    public void testEquals() {
        HoughCrop other = new HoughCrop(8);
        assertFalse(houghCrop == other);
        assertEquals(houghCrop, other);
    }



    @Test
    public void testHoughCrop() {
        HoughCrop tmpHoughCrop = new HoughCrop();
        assertEquals(HoughCrop.DEFAULT_RADIUS, tmpHoughCrop.radius);
    }



    @Test
    public void testHoughCropInt() {
        assertEquals(8, houghCrop.radius);
    }



    @Test
    public void testProcess() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        HoughMap expectedMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/hough_crop_op.bmp"));

        HoughMap destMap = houghCrop.process(null, srcMap);

        destMap.assertEquals(expectedMap);
    }



    @Test
    public void testToString() {
        assertEquals(houghCrop.toString(), "Hough Crop [radius=8 px]");
    }

}
