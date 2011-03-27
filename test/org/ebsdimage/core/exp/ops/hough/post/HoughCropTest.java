/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class HoughCropTest extends TestCase {

    private HoughCrop op;



    @Before
    public void setUp() throws Exception {
        op = new HoughCrop(8);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new HoughCrop(7)));
        assertTrue(op.equals(new HoughCrop(8)));
    }



    @Test
    public void testHashCode() {
        assertEquals(1983706858, op.hashCode());
    }



    @Test
    public void testHoughCropInt() {
        assertEquals(8, op.radius);
    }



    @Test
    public void testProcess() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap.bmp"));
        HoughMap destMap = op.process(null, srcMap);

        HoughMap expectedMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/hough_crop_op.bmp"));
        destMap.assertEquals(expectedMap);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Hough Crop [radius=8 px]");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughCrop other = new XmlLoader().load(HoughCrop.class, file);
        assertEquals(op, other, 1e-6);
    }

}
