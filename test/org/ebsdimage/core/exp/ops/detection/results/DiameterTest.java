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
package org.ebsdimage.core.exp.ops.detection.results;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.BinMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class DiameterTest extends TestCase {

    private Diameter op;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        op = new Diameter();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/peaksMap.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Diameter", op.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = op.calculate(null, peaksMap);

        assertEquals(4, results.length);

        // Average
        assertEquals(13.57892894, results[0].value.doubleValue(), 1e-6);

        // Std Dev
        assertEquals(2.361767530, results[1].value.doubleValue(), 1e-6);

        // Min
        assertEquals(9.245048523, results[2].value.doubleValue(), 1e-6);

        // Max
        assertEquals(15.55825805, results[3].value.doubleValue(), 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new Diameter()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new Diameter(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-168560436, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Diameter other = new XmlLoader().load(Diameter.class, file);
        assertEquals(op, other, 1e-6);
    }

}
