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
package org.ebsdimage.core.exp.ops.identification.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.BinMap;

public class IdentificationOpMockTest extends TestCase {

    private IdentificationOpMock op;

    private BinMap peaksMap;

    private HoughMap houghMap;



    @Before
    public void setUp() throws Exception {
        op = new IdentificationOpMock();

        peaksMap = new BinMap(4, 3);
        for (int i = 0; i < peaksMap.size; i++)
            peaksMap.pixArray[i] = (byte) (i % 2);

        houghMap = new HoughMap(4, 3, 1, 1);
        byte[] srcPixArray = new byte[] { 7, 11, 15, 19 };
        for (int i = 0; i < houghMap.size; i++)
            houghMap.pixArray[i] = srcPixArray[i % 4];
    }



    @Test
    public void testIdentify() {
        HoughPeak[] destPeaks = op.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);
        assertTrue(destPeaks[0].equals(new HoughPeak(0.07, 0.0), 1e-6));
        assertTrue(destPeaks[1].equals(new HoughPeak(0.11, 1.0), 1e-6));
        assertTrue(destPeaks[2].equals(new HoughPeak(0.15, 0.0), 1e-6));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new IdentificationOpMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new IdentificationOpMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-1244382376, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IdentificationOpMock other =
                new XmlLoader().load(IdentificationOpMock.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
