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
package org.ebsdimage.core.exp.ops.hough.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.OpResult;
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

public class StandardDeviationTest extends TestCase {

    private StandardDeviation op;



    @Before
    public void setUp() throws Exception {
        op = new StandardDeviation();
    }



    @Test
    public void testCalculate() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        OpResult result = op.calculate(null, srcMap)[0];

        assertEquals(5.596094704, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Standard Deviation");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new StandardDeviation()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new StandardDeviation(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-1082197437, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        StandardDeviation other =
                new XmlLoader().load(StandardDeviation.class, file);
        assertEquals(op, other, 1e-6);
    }

}
