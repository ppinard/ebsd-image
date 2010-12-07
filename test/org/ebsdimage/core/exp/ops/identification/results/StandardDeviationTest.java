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
package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;

import magnitude.core.Magnitude;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class StandardDeviationTest extends TestCase {

    private StandardDeviation op;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        op = new StandardDeviation(2);

        Magnitude theta = new Magnitude(0.5, "rad");
        Magnitude rho = new Magnitude(3.0, "px");
        HoughPeak peak1 = new HoughPeak(theta, rho, 1);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5.0, "px");
        HoughPeak peak2 = new HoughPeak(theta, rho, 2);

        theta = new Magnitude(2.5, "rad");
        rho = new Magnitude(7.0, "px");
        HoughPeak peak3 = new HoughPeak(theta, rho, 4);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testToString() {
        String expected = "Standard Deviation [max=2]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = op.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(1.41421356, results[0].value.doubleValue(), 1e-6);

        results = new StandardDeviation(-1).calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(1.52752523, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testStandardDeviationInt() {
        assertEquals(2, op.max);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new StandardDeviation(2)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new StandardDeviation(5), 2));
        assertTrue(op.equals(new StandardDeviation(1), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(811617823, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        StandardDeviation other =
                new XmlLoader().load(StandardDeviation.class, file);
        assertEquals(op, other, 1e-6);
    }

}
