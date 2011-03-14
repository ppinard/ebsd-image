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

import java.io.File;

import magnitude.core.Magnitude;

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

public class IdentificationPostOpsMockTest extends TestCase {

    private IdentificationPostOpsMock op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IdentificationPostOpsMock();

        Magnitude theta = new Magnitude(0.0, "rad");
        Magnitude rho = new Magnitude(7.0, "px");
        HoughPeak peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(1.0, "rad");
        rho = new Magnitude(11.0, "px");
        HoughPeak peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(0.0, "rad");
        rho = new Magnitude(15.0, "px");
        HoughPeak peak3 = new HoughPeak(theta, rho);

        srcPeaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = op.process(null, srcPeaks);

        assertEquals(3, destPeaks.length);

        Magnitude theta = new Magnitude(0.0, "rad");
        Magnitude rho = new Magnitude(14.0, "px");
        HoughPeak other = new HoughPeak(theta, rho);
        assertEquals(other, destPeaks[0], 1e-6);

        theta = new Magnitude(1.0, "rad");
        rho = new Magnitude(22.0, "px");
        other = new HoughPeak(theta, rho);
        assertEquals(other, destPeaks[1], 1e-6);

        theta = new Magnitude(0.0, "rad");
        rho = new Magnitude(30.0, "px");
        other = new HoughPeak(theta, rho);
        assertEquals(other, destPeaks[2], 1e-6);
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
        assertEquals(op, other, 1e-6);
    }

}
