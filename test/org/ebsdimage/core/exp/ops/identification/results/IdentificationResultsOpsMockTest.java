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

public class IdentificationResultsOpsMockTest extends TestCase {

    private IdentificationResultsOpsMock op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IdentificationResultsOpsMock();

        Magnitude theta = new Magnitude(0.0, "rad");
        Magnitude rho = new Magnitude(14.0, "px");
        HoughPeak peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(1.0, "rad");
        rho = new Magnitude(22.0, "px");
        HoughPeak peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(0.0, "rad");
        rho = new Magnitude(30.0, "px");
        HoughPeak peak3 = new HoughPeak(theta, rho);

        srcPeaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testCalculate() {
        double expected = 66;
        OpResult result = op.calculate(null, srcPeaks)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new IdentificationResultsOpsMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new IdentificationResultsOpsMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(35723475, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IdentificationResultsOpsMock other =
                new XmlLoader().load(IdentificationResultsOpsMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
