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
package org.ebsdimage.core.exp.ops.hough.pre;

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

public class HoughPreOpsMockTest extends TestCase {

    private HoughPreOpsMock op;

    private ByteMap srcMap;



    @Before
    public void setUp() throws Exception {
        op = new HoughPreOpsMock();
        srcMap = new ByteMap(2, 2, new byte[] { 2, 4, 6, 8 });
    }



    @Test
    public void testProcess() {
        ByteMap result = op.process(null, srcMap);

        assertEquals(2, result.width);
        assertEquals(2, result.height);
        assertEquals(3, result.pixArray[0]);
        assertEquals(5, result.pixArray[1]);
        assertEquals(7, result.pixArray[2]);
        assertEquals(9, result.pixArray[3]);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new HoughPreOpsMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new HoughPreOpsMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1040987719, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughPreOpsMock other =
                new XmlLoader().load(HoughPreOpsMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
