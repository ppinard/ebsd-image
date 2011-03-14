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

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;
import static org.ebsdimage.core.MaskDisc.KEY_CENTROID_X;
import static org.ebsdimage.core.MaskDisc.KEY_CENTROID_Y;
import static org.ebsdimage.core.MaskDisc.KEY_RADIUS;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class MaskDiscTest extends TestCase {

    private MaskDisc op;



    @Before
    public void setUp() throws Exception {
        op = new MaskDisc(10, 11, 8);
    }



    @Test
    public void testCropIntIntInt() {
        assertEquals(10, op.centroidX);
        assertEquals(11, op.centroidY);
        assertEquals(8, op.radius);
    }



    @Test
    public void testEquals() {
        MaskDisc other = new MaskDisc(10, 11, 8);
        assertFalse(op == other);
        assertEquals(op, other);
    }



    @Test
    public void testProcess() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        ByteMap expectedMap = (ByteMap) load("org/ebsdimage/testdata/mask.bmp");

        ByteMap destMap = op.process(null, srcMap);

        destMap.assertEquals(expectedMap);

        assertEquals(10, destMap.getProperty(KEY_CENTROID_X, -1));
        assertEquals(11, destMap.getProperty(KEY_CENTROID_Y, -1));
        assertEquals(8, destMap.getProperty(KEY_RADIUS, -1));
    }



    @Test
    public void testProcess2() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        MaskDisc other = new MaskDisc(-1, -1, -3);

        ByteMap destMap = other.process(null, srcMap);

        assertEquals(srcMap.width / 2, destMap.getProperty(KEY_CENTROID_X, -1));
        assertEquals(srcMap.height / 2, destMap.getProperty(KEY_CENTROID_Y, -1));
        assertEquals(srcMap.width / 2 - 2, destMap.getProperty(KEY_RADIUS, -1));
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(),
                "Mask Disc [centroid X=10 px, centroid Y=11 px, radius=8 px]");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new MaskDisc(99, 11, 8)));
        assertFalse(op.equals(new MaskDisc(10, 99, 8)));
        assertFalse(op.equals(new MaskDisc(10, 11, 99)));
        assertTrue(op.equals(new MaskDisc(10, 11, 8)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new MaskDisc(8, 11, 8), 2));
        assertFalse(op.equals(new MaskDisc(8, 9, 8), 2));
        assertFalse(op.equals(new MaskDisc(8, 11, 6), 2));
        assertTrue(op.equals(new MaskDisc(9, 10, 7), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(626893831, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        MaskDisc other = new XmlLoader().load(MaskDisc.class, file);
        assertEquals(op, other, 1e-6);
    }

}
