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

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class HoughOpMockTest extends TestCase {

    private HoughOpMock op;

    private ByteMap srcMap;



    @Before
    public void setUp() throws Exception {
        op = new HoughOpMock();
        srcMap = new ByteMap(2, 2, new byte[] { 3, 5, 7, 9 });
    }



    @Test
    public void testTransform() {
        HoughMap result = op.transform(null, srcMap);

        assertEquals(4, result.width);
        assertEquals(3, result.height);
        assertEquals(1.0, result.getDeltaRho().getValue("px"), 1e-6);
        assertEquals(1.0, result.getDeltaTheta().getValue("rad"), 1e-6);

        byte[] srcPixArray = new byte[] { 3, 5, 7, 9 };
        for (int i = 0; i < result.pixArray.length; i++)
            assertEquals(srcPixArray[i % 4], result.pixArray[i]);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new HoughOpMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new HoughOpMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1706032537, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughOpMock other = new XmlLoader().load(HoughOpMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
