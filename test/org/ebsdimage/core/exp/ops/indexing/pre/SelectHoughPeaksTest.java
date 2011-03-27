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
package org.ebsdimage.core.exp.ops.indexing.pre;

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

import static junittools.test.Assert.assertContains;
import static junittools.test.Assert.assertEquals;

public class SelectHoughPeaksTest extends TestCase {

    private HoughPeaksSelector op;

    private HoughPeak[] srcPeaks;

    private HoughPeak peak1, peak2, peak3, peak4, peak5, peak6, peak7;



    @Before
    public void setUp() throws Exception {
        op = new HoughPeaksSelector(4, 6);

        peak1 = new HoughPeak(0.1, 7.0, 3);
        peak2 = new HoughPeak(0.2, 6.0, 6);
        peak3 = new HoughPeak(0.3, 5.0, 4);
        peak4 = new HoughPeak(0.4, 4.0, 7);
        peak5 = new HoughPeak(0.5, 3.0, 2);
        peak6 = new HoughPeak(0.6, 2.0, 5);
        peak7 = new HoughPeak(0.7, 1.0, 1);

        srcPeaks =
                new HoughPeak[] { peak1, peak2, peak3, peak4, peak5, peak6,
                        peak7 };
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new HoughPeaksSelector(3, 6)));
        assertFalse(op.equals(new HoughPeaksSelector(4, 5)));
        assertTrue(op.equals(new HoughPeaksSelector(4, 6)));
    }



    @Test
    public void testHashCode() {
        assertEquals(-7739839, op.hashCode());
    }



    @Test
    public void testProcess1() {
        HoughPeak[] destPeaks = op.process(null, srcPeaks);

        assertEquals(6, destPeaks.length);
        assertContains(peak1, destPeaks);
        assertContains(peak2, destPeaks);
        assertContains(peak3, destPeaks);
        assertContains(peak4, destPeaks);
        assertContains(peak5, destPeaks);
        assertContains(peak6, destPeaks);
    }



    @Test
    public void testProcess2() {
        HoughPeak[] otherPeaks =
                new HoughPeak[] { peak1, peak2, peak3, peak4, peak5 };
        HoughPeak[] destPeaks = op.process(null, otherPeaks);

        assertEquals(5, destPeaks.length);
        assertContains(peak1, destPeaks);
        assertContains(peak2, destPeaks);
        assertContains(peak3, destPeaks);
        assertContains(peak4, destPeaks);
        assertContains(peak5, destPeaks);
    }



    @Test
    public void testProcess3() {
        HoughPeak[] otherPeaks = new HoughPeak[] { peak1, peak2, peak3 };
        HoughPeak[] destPeaks = op.process(null, otherPeaks);

        assertEquals(0, destPeaks.length);
    }



    @Test
    public void testSelectHoughPeaksIntInt() {
        assertEquals(4, op.min);
        assertEquals(6, op.max);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSelectHoughPeaksIntIntException1() {
        new HoughPeaksSelector(-1, 6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSelectHoughPeaksIntIntException2() {
        new HoughPeaksSelector(4, -1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSelectHoughPeaksIntIntException3() {
        new HoughPeaksSelector(6, 4);
    }



    @Test
    public void testToString() {
        String expected = "Hough Peaks Selector [minimum=4, maximum=6]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughPeaksSelector other =
                new XmlLoader().load(HoughPeaksSelector.class, file);
        assertEquals(op, other, 1e-6);
    }

}
