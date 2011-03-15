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

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class EntropyTest extends TestCase {

    private Entropy op;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        op = new Entropy(2);

        HoughPeak peak1 = new HoughPeak(0.5, 3.0, 3);
        HoughPeak peak2 = new HoughPeak(1.5, 5.0, 2);
        HoughPeak peak3 = new HoughPeak(2.5, 7.0, 4);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testCalculate() {
        OpResult[] results = op.calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(-8.84101431, results[0].value.doubleValue(), 1e-6);

        results = new Entropy(-1).calculate(null, peaks);
        assertEquals(1, results.length);
        assertEquals(-10.2273086, results[0].value.doubleValue(), 1e-6);
    }



    @Test
    public void testEntropyInt() {
        assertEquals(2, op.max);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new Entropy(2)));
    }



    @Test
    public void testHashCode() {
        assertEquals(-2029299310, op.hashCode());
    }



    @Test
    public void testToString() {
        String expected = "Entropy [max=2]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Entropy other = new XmlLoader().load(Entropy.class, file);
        assertEquals(op, other, 1e-6);
    }

}
