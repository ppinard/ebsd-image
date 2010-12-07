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
package org.ebsdimage.core.exp.ops.indexing.pre;

import java.io.File;

import magnitude.core.Magnitude;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

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

        Magnitude theta = new Magnitude(0.1, "rad");
        Magnitude rho = new Magnitude(7.0, "px");
        peak1 = new HoughPeak(theta, rho, 3);

        theta = new Magnitude(0.2, "rad");
        rho = new Magnitude(6.0, "px");
        peak2 = new HoughPeak(theta, rho, 6);

        theta = new Magnitude(0.3, "rad");
        rho = new Magnitude(5.0, "px");
        peak3 = new HoughPeak(theta, rho, 4);

        theta = new Magnitude(0.4, "rad");
        rho = new Magnitude(4.0, "px");
        peak4 = new HoughPeak(theta, rho, 7);

        theta = new Magnitude(0.5, "rad");
        rho = new Magnitude(3.0, "px");
        peak5 = new HoughPeak(theta, rho, 2);

        theta = new Magnitude(0.6, "rad");
        rho = new Magnitude(2.0, "px");
        peak6 = new HoughPeak(theta, rho, 5);

        theta = new Magnitude(0.7, "rad");
        rho = new Magnitude(1.0, "px");
        peak7 = new HoughPeak(theta, rho, 1);

        srcPeaks =
                new HoughPeak[] { peak1, peak2, peak3, peak4, peak5, peak6,
                        peak7 };
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
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new HoughPeaksSelector(3, 6)));
        assertFalse(op.equals(new HoughPeaksSelector(4, 5)));
        assertTrue(op.equals(new HoughPeaksSelector(4, 6)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new HoughPeaksSelector(7, 7), 2));
        assertFalse(op.equals(new HoughPeaksSelector(4, 9), 2));
        assertTrue(op.equals(new HoughPeaksSelector(3, 5), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-7739839, op.hashCode());
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
