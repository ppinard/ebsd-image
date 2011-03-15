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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class SmoothingTest extends TestCase {

    private Smoothing op;



    @Before
    public void setUp() throws Exception {
        op = new Smoothing(3);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new Smoothing(5)));
        assertTrue(op.equals(new Smoothing(3)));
    }



    @Test
    public void testHashCode() {
        assertEquals(457585392, op.hashCode());
    }



    @Test
    public void testProcess() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/smoothing.bmp");

        ByteMap destMap = op.process(null, srcMap);

        destMap.assertEquals(expectedMap);
    }



    @Test
    public void testSmoothingInt() {
        assertEquals(3, op.kernelSize);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSmoothingIntException() {
        new Smoothing(0);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Smoothing [kernel size=3]");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Smoothing other = new XmlLoader().load(Smoothing.class, file);
        assertEquals(op, other, 1e-6);
    }

}
