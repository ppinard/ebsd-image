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
package org.ebsdimage.core.exp.ops.identification.post;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class DoublePeaksCleanUpTest extends TestCase {

    private DoublePeaksCleanUp op;

    private HoughPeak[] srcPeaks;

    private HoughPeak peak1;

    private HoughPeak peak2;

    private HoughPeak peak3;

    private HoughPeak peak4;



    @Before
    public void setUp() throws Exception {
        op = new DoublePeaksCleanUp(2, 3);

        peak1 = new HoughPeak(0.2, 0.1, 1);
        peak2 = new HoughPeak(0.6, 0.3, 2);
        peak3 = new HoughPeak(0.7, 0.4, 3);
        peak4 = new HoughPeak(0.7, 0.0, 4);

        srcPeaks = new HoughPeak[] { peak1, peak2, peak3, peak4 };
    }



    @Test
    public void testDoublePeaksCleanUpIntInt() {
        assertEquals(2, op.spacingRho);
        assertEquals(3, op.spacingTheta);
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
    public void testHashCode() {
        assertEquals(-972408927, op.hashCode());
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = op.process(srcPeaks, 0.1, 0.1);

        // peak2 is equivalent to peak3
        // peak3 is kept because it has a higher intensity

        assertEquals(3, destPeaks.length);
        assertEquals(peak1, destPeaks[0], 1e-6);
        assertEquals(peak3, destPeaks[1], 1e-6);
        assertEquals(peak4, destPeaks[2], 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "DoublePeaksCleanUp [spacingRho=2, spacingTheta=3]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        DoublePeaksCleanUp other =
                new XmlLoader().load(DoublePeaksCleanUp.class, file);
        assertEquals(op, other, 1e-6);
    }

}
