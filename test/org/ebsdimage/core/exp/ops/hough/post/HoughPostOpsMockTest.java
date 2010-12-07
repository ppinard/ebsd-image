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
package org.ebsdimage.core.exp.ops.hough.post;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class HoughPostOpsMockTest extends TestCase {

    private HoughPostOpsMock op;

    private HoughMap srcMap;



    @Before
    public void setUp() throws Exception {
        op = new HoughPostOpsMock();

        srcMap = new HoughMap(4, 3, 1, 1);
        byte[] srcPixArray = new byte[] { 3, 5, 7, 9 };
        for (int i = 0; i < srcMap.size; i++)
            srcMap.pixArray[i] = srcPixArray[i % 4];
    }



    @Test
    public void testProcess() {
        HoughMap result = op.process(null, srcMap);

        assertEquals(4, result.width);
        assertEquals(3, result.height);
        assertEquals(1.0, result.getDeltaRho().getValue("px"), 1e-6);
        assertEquals(1.0, result.getDeltaTheta().getValue("rad"), 1e-6);

        byte[] srcPixArray = new byte[] { 6, 10, 14, 18 };
        for (int i = 0; i < result.size; i++)
            assertEquals(srcPixArray[i % 4], result.pixArray[i]);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new HoughPostOpsMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new HoughPostOpsMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-249310804, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughPostOpsMock other =
                new XmlLoader().load(HoughPostOpsMock.class, file);
        assertEquals(op, other, 1e-6);
    }
}
