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
package org.ebsdimage.core.exp.ops.positioning.pre;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.ops.positioning.pre.Dilation;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class DilationTest extends TestCase {

    private Dilation op;



    @Before
    public void setUp() throws Exception {
        op = new Dilation(2, 8);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new Dilation(3, 8)));
        assertFalse(op.equals(new Dilation(2, 7)));
        assertTrue(op.equals(new Dilation(2, 8)));
    }



    @Test
    public void testHashCode() {
        assertEquals(1572686023, op.hashCode());
    }



    @Test
    public void testProcess() {
        BinMap srcMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_stddev.bmp"));

        IdentMap identMap = Identification.identify(srcMap);
        identMap.setCalibration(Calibration.NONE);

        Area area = Analysis.getArea(identMap);
        assertEquals(20.0, area.val[0], 1e-6);

        BinMap destMap = op.process(null, srcMap);

        // Test
        identMap = Identification.identify(destMap);
        identMap.setCalibration(Calibration.NONE);
        area = Analysis.getArea(identMap);

        assertEquals(10.0, area.val[0], 1e-6);

        destMap.getCalibration().assertEquals(srcMap.getCalibration(), 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "Dilation";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Dilation other = new XmlLoader().load(Dilation.class, file);
        assertEquals(op, other, 1e-6);
    }

}
