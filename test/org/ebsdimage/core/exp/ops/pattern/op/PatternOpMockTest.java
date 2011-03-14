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
package org.ebsdimage.core.exp.ops.pattern.op;

import java.io.File;
import java.io.IOException;

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

public class PatternOpMockTest extends TestCase {

    private PatternOpMock op;



    @Before
    public void setUp() throws Exception {
        op = new PatternOpMock();
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap map = op.load(null, 1);

        assertEquals(2, map.width);
        assertEquals(2, map.height);
        assertEquals(1, map.pixArray[0]);
        assertEquals(2, map.pixArray[1]);
        assertEquals(3, map.pixArray[2]);
        assertEquals(4, map.pixArray[3]);

        map = op.load(null, 2);

        assertEquals(2, map.width);
        assertEquals(2, map.height);
        assertEquals(2, map.pixArray[0]);
        assertEquals(2, map.pixArray[1]);
        assertEquals(3, map.pixArray[2]);
        assertEquals(4, map.pixArray[3]);
    }



    @Test
    public void testPatternOpMock() throws IOException {
        assertEquals(0, op.startIndex);
        assertEquals(1, op.size);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new PatternOpMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new PatternOpMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(850802777, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PatternOpMock other = new XmlLoader().load(PatternOpMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
