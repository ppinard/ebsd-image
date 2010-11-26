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

public class IdentificationPostOpsMockTest extends TestCase {

    private IdentificationPostOpsMock op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IdentificationPostOpsMock();

        srcPeaks =
                new HoughPeak[] { new HoughPeak(7.0, 0.0),
                        new HoughPeak(11.0, 1.0), new HoughPeak(15.0, 0.0) };
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = op.process(null, srcPeaks);

        assertEquals(3, destPeaks.length);
        assertTrue(destPeaks[0].equals(new HoughPeak(14.0, 0.0), 1e-6));
        assertTrue(destPeaks[1].equals(new HoughPeak(22.0, 1.0), 1e-6));
        assertTrue(destPeaks[2].equals(new HoughPeak(30.0, 0.0), 1e-6));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new IdentificationPostOpsMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new IdentificationPostOpsMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1963676493, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IdentificationPostOpsMock other =
                new XmlLoader().load(IdentificationPostOpsMock.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
