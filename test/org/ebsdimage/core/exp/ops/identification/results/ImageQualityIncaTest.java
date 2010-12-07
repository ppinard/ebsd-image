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

public class ImageQualityIncaTest extends TestCase {

    private ImageQualityInca op;

    private HoughPeak[] peaks;

    private HoughPeak peak1;

    private HoughPeak peak2;

    private HoughPeak peak3;



    @Before
    public void setUp() throws Exception {

        Magnitude theta = new Magnitude(0.5, "rad");
        Magnitude rho = new Magnitude(3.0, "px");
        peak1 = new HoughPeak(theta, rho, 1);

        theta = new Magnitude(1.5, "rad");
        rho = new Magnitude(5.0, "px");
        peak2 = new HoughPeak(theta, rho, 3);

        theta = new Magnitude(4.0, "rad");
        rho = new Magnitude(1.0, "px");
        peak3 = new HoughPeak(theta, rho, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };

        op = new ImageQualityInca();
    }



    @Test
    public void testCalculate() {
        double expected = peak2.intensity - peak1.intensity;
        OpResult result = op.calculate(null, peaks)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Image Quality (INCA)");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new ImageQualityInca()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new ImageQualityInca(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1810001350, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        ImageQualityInca other =
                new XmlLoader().load(ImageQualityInca.class, file);
        assertEquals(op, other, 1e-6);
    }

}
