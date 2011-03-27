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
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class ThetaExpandTest extends TestCase {

    private ThetaExpand op;

    private HoughMap houghMap;



    @Before
    public void setUp() throws Exception {
        op = new ThetaExpand(1);
        houghMap = new HoughMap(3, 3, 1, 1);
        houghMap.pixArray[0] = 1;
        houghMap.pixArray[1] = 2;
        houghMap.pixArray[2] = 3;

        houghMap.pixArray[3] = 4;
        houghMap.pixArray[4] = 5;
        houghMap.pixArray[5] = 6;

        houghMap.pixArray[6] = 7;
        houghMap.pixArray[7] = 8;
        houghMap.pixArray[8] = 9;
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new ThetaExpand(1.1), 1e-2));
        assertTrue(op.equals(new ThetaExpand(1.001), 1e-2));
    }



    @Test
    public void testProcess() throws IOException {
        HoughMap destMap = op.process(null, houghMap);

        assertEquals(4, destMap.width);
        assertEquals(3, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(2, destMap.pixArray[1]);
        assertEquals(3, destMap.pixArray[2]);
        assertEquals(7, destMap.pixArray[3]);

        assertEquals(4, destMap.pixArray[4]);
        assertEquals(5, destMap.pixArray[5]);
        assertEquals(6, destMap.pixArray[6]);
        assertEquals(4, destMap.pixArray[7]);

        assertEquals(7, destMap.pixArray[8]);
        assertEquals(8, destMap.pixArray[9]);
        assertEquals(9, destMap.pixArray[10]);
        assertEquals(1, destMap.pixArray[11]);

        destMap.getCalibration().assertEquals(houghMap.getCalibration(), 1e-6);
    }



    @Test
    public void testThetaExpandInt() {
        assertEquals(1, op.increment, 1e-7);
    }



    @Test
    public void testToString() {
        String expected = "ThetaExpand [increment=57.29577951308232 deg]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        ThetaExpand other = new XmlLoader().load(ThetaExpand.class, file);
        assertEquals(op, other, 1e-6);
    }

}
