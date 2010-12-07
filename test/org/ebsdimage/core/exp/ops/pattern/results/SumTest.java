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
package org.ebsdimage.core.exp.ops.pattern.results;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class SumTest extends TestCase {

    private Sum op;

    private ByteMap patternMap;



    @Before
    public void setUp() throws Exception {
        op = new Sum(0.2, 0.3, 0.5, 0.6);
        patternMap =
                (ByteMap) load(getFile("org/ebsdimage/testdata/pattern.bmp"));
    }



    @Test
    public void testCalculateByteMap() {
        double value = op.calculateSum(patternMap);
        assertEquals(801235, value, 1e-6);
    }



    @Test
    public void testAverageRegionDoubleDoubleDoubleDouble() {
        assertEquals(0.2, op.xmin, 1e-6);
        assertEquals(0.3, op.ymin, 1e-6);
        assertEquals(0.5, op.xmax, 1e-6);
        assertEquals(0.6, op.ymax, 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "Sum [xmin=0.2, ymin=0.3, xmax=0.5, ymax=0.6]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new Sum(0.3, 0.3, 0.5, 0.6)));
        assertFalse(op.equals(new Sum(0.2, 0.4, 0.5, 0.6)));
        assertFalse(op.equals(new Sum(0.2, 0.3, 0.6, 0.6)));
        assertFalse(op.equals(new Sum(0.2, 0.3, 0.5, 0.7)));
        assertTrue(op.equals(new Sum(0.2, 0.3, 0.5, 0.6)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-3));
        assertFalse(op.equals(null, 1e-3));
        assertFalse(op.equals(new Object(), 1e-3));

        assertFalse(op.equals(new Sum(0.21, 0.3, 0.5, 0.6), 1e-3));
        assertFalse(op.equals(new Sum(0.2, 0.31, 0.5, 0.6), 1e-3));
        assertFalse(op.equals(new Sum(0.2, 0.3, 0.51, 0.6), 1e-3));
        assertFalse(op.equals(new Sum(0.2, 0.3, 0.5, 0.61), 1e-3));
        assertTrue(op.equals(new Sum(0.2001, 0.3001, 0.5001, 0.6001), 1e-3));
    }



    @Test
    public void testHashCode() {
        assertEquals(377560205, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Sum other = new XmlLoader().load(Sum.class, file);
        assertEquals(op, other, 1e-6);
    }

}
