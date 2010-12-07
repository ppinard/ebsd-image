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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.toRadians;

import static junittools.test.Assert.assertEquals;

public class AutoHoughTransformTest extends TestCase {

    private AutoHoughTransform op;



    @Before
    public void setUp() throws Exception {
        op = new AutoHoughTransform(toRadians(1.0));
    }



    // @Test
    // public void testAutoHoughTransformOp() {
    // AutoHoughTransform tmpHough = new AutoHoughTransform();
    // assertEquals(AutoHoughTransform.DEFAULT_DELTA_THETA,
    // tmpHough.deltaTheta, 1e-7);
    // }

    @Test
    public void testAutoHoughTransformOpDouble() {
        assertEquals(toRadians(1.0), op.deltaTheta, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAutoHoughTransformOpDoubleException() {
        new AutoHoughTransform(-1.0);
    }



    @Test
    public void testProcess() throws IOException {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");
        HoughMap expectedMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/autohoughtransformop.bmp"));

        HoughMap destMap = op.transform(null, srcMap);

        destMap.assertEquals(expectedMap);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(),
                "Auto Hough Transform [resolution=1.0 deg/px]");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new AutoHoughTransform(toRadians(2.0))));
        assertTrue(op.equals(new AutoHoughTransform(toRadians(1.0))));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new AutoHoughTransform(toRadians(1.0) - 0.01),
                1e-2));
        assertTrue(op.equals(new AutoHoughTransform(toRadians(1.0)), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-470840940, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        AutoHoughTransform other =
                new XmlLoader().load(AutoHoughTransform.class, file);
        assertEquals(op, other, 1e-6);
    }
}
