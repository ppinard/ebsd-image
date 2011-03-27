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
package org.ebsdimage.core.exp.ops.positioning.results;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.core.exp.ops.positioning.results.Average;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class AverageTest extends TestCase {

    private Average op;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        op = new Average(2);

        HoughPeak peak1 = new HoughPeak(0.5, 3.0, 1);
        HoughPeak peak2 = new HoughPeak(1.5, 5.0, 2);
        HoughPeak peak3 = new HoughPeak(2.5, 7.0, 4);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testAverageInt() {
        assertEquals(2, op.max);
    }



    @Test
    public void testCalculate() {
        OpResult[] results = op.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(3.0, results[0].value.doubleValue(), 1e-6);

        results = new Average(-1).calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(7.0 / 3.0, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new Average(2)));
    }



    @Test
    public void testHashCode() {
        assertEquals(1964592486, op.hashCode());
    }



    @Test
    public void testToString() {
        String expected = "Average [max=2]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Average other = new XmlLoader().load(Average.class, file);
        assertEquals(op, other, 1e-6);
    }

}
