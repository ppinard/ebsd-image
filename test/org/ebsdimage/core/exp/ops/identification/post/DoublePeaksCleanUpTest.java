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
package org.ebsdimage.core.exp.ops.identification.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

public class DoublePeaksCleanUpTest extends TestCase {

    private DoublePeaksCleanUp op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new DoublePeaksCleanUp(2, 3);

        srcPeaks =
                new HoughPeak[] { new HoughPeak(0.1, 0.2),
                        new HoughPeak(0.3, 0.6), new HoughPeak(0.4, 0.7),
                        new HoughPeak(0.0, 0.7) };
    }



    @Test
    public void testToString() {
        String expected = "DoublePeaksCleanUp [deltaRho=2, deltaTheta=3]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = op.process(srcPeaks, 0.1, 0.1);

        assertEquals(3, destPeaks.length);
        assertEquals(new HoughPeak(0.1, 0.2), destPeaks[0]);
        assertEquals(new HoughPeak(0.4, 0.7), destPeaks[1]);
        assertEquals(new HoughPeak(0.0, 0.7), destPeaks[2]);
    }



    @Test
    public void testDoublePeaksCleanUpIntInt() {
        assertEquals(2, op.deltaRho);
        assertEquals(3, op.deltaTheta);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new DoublePeaksCleanUp(1, 3)));
        assertFalse(op.equals(new DoublePeaksCleanUp(2, 4)));
        assertTrue(op.equals(new DoublePeaksCleanUp(2, 3)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new DoublePeaksCleanUp(4, 3), 2));
        assertFalse(op.equals(new DoublePeaksCleanUp(2, 5), 2));
        assertTrue(op.equals(new DoublePeaksCleanUp(1, 4), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-972408927, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        DoublePeaksCleanUp other =
                new XmlLoader().load(DoublePeaksCleanUp.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
