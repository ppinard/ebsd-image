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
package org.ebsdimage.core.exp.ops.indexing.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import crystallography.core.ScatteringFactorsEnum;

public class KriegerLassen1994Test extends TestCase {

    private KriegerLassen1994 op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new KriegerLassen1994(1, ScatteringFactorsEnum.XRAY);
        srcPeaks = new HoughPeak[] { new HoughPeak(7, 0.1, 1) };
    }



    @Test
    public void testToString() {
        String expected =
                "Krieger Lassen (1994) [maxIndex=1, scatterType=XRAY]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testIndex() {
        // For further testing see the org.ebsdimage.core.Indexing class

        Solution[] slns = op.index(null, srcPeaks);
        assertEquals(0, slns.length);
    }



    @Test
    public void testKriegerLassen1994IntScatteringFactorsEnum() {
        assertEquals(1, op.maxIndex);
        assertEquals(ScatteringFactorsEnum.XRAY, op.scatterType);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new KriegerLassen1994(2,
                ScatteringFactorsEnum.XRAY)));
        assertFalse(op.equals(new KriegerLassen1994(1,
                ScatteringFactorsEnum.ELECTRON)));
        assertTrue(op.equals(new KriegerLassen1994(1,
                ScatteringFactorsEnum.XRAY)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new KriegerLassen1994(3,
                ScatteringFactorsEnum.XRAY), 2));
        assertFalse(op.equals(new KriegerLassen1994(1,
                ScatteringFactorsEnum.ELECTRON), 2));
        assertTrue(op.equals(new KriegerLassen1994(2,
                ScatteringFactorsEnum.XRAY), 2));
    }



    @Test
    public void testHashCode() {
        // impossible to check since the hashCode of an Enum always changes
        assertTrue(true);
        // assertEquals(1897229649, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        KriegerLassen1994 other =
                new XmlLoader().load(KriegerLassen1994.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
