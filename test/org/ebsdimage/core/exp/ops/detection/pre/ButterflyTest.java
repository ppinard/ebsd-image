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
package org.ebsdimage.core.exp.ops.detection.pre;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class ButterflyTest extends TestCase {

    private Butterfly op;



    @Before
    public void setUp() throws Exception {
        op = new Butterfly(9, -500, 500);
    }



    @Test
    public void testButterflyIntFloatFloat() {
        assertEquals(9, op.kernelSize);
        assertEquals(-500, op.flattenLowerLimit, 1e-6);
        assertEquals(500, op.flattenUpperLimit, 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testButterflyIntFloatFloatException1() {
        new Butterfly(4, -500, 500);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testButterflyIntFloatFloatException2() {
        new Butterfly(9, 500, -500);
    }



    @Test
    public void testEquals() {
        Butterfly other = new Butterfly(9, -500, 500);
        assertFalse(op == other);
        assertEquals(op, other);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new Butterfly(3, -500, 500), 1e-2));
        assertFalse(op.equals(new Butterfly(9, -500.01f, 500), 1e-2));
        assertFalse(op.equals(new Butterfly(9, -500, 500.01f), 1e-2));
        assertTrue(op.equals(new Butterfly(9, -500.001f, 500.001f), 1e-2));
    }



    @Test
    public void testProcess() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        HoughMap destMap = op.process(null, srcMap);

        HoughMap expected =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/butterfly_op.bmp"));
        destMap.assertEquals(expected);
    }



    @Test
    public void testToString() {
        assertEquals(
                op.toString(),
                "Butterfly [flatten lower limit=-500.0, flatten upper limit=500.0, kernel size=9]");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Butterfly other = new XmlLoader().load(Butterfly.class, file);
        assertEquals(op, other, 1e-6);
    }

}
