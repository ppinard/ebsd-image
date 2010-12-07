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
package org.ebsdimage.core.exp.ops.detection.post;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.BinMap;
import rmlimage.core.Identification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class OpeningTest extends TestCase {

    private Opening op;



    @Before
    public void setUp() throws Exception {
        op = new Opening(2, 8);
    }



    @Test
    public void testToString() {
        String expected = "Opening [min=2, max=8]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testProcess() {
        BinMap srcMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_stddev.bmp"));
        assertEquals(22, Identification.identify(srcMap).getObjectCount());

        BinMap destMap = op.process(null, srcMap);

        assertEquals(10, Identification.identify(destMap).getObjectCount());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Opening other = new XmlLoader().load(Opening.class, file);
        assertEquals(op, other, 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new Opening(1, 7)));
        assertTrue(op.equals(new Opening(2, 8)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new Opening(5, 6), 2));
        assertTrue(op.equals(new Opening(2, 8), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-735578991, op.hashCode());
    }

}
