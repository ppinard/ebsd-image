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
package org.ebsdimage.core.exp.ops.pattern.post;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class RadialNoiseTest extends TestCase {

    private RadialNoise op;



    @Before
    public void setUp() throws Exception {
        op = new RadialNoise(2, 3, 4, 5, 6, 7);
    }



    @Test
    public void testProcess() {
        // Impossible to test because of the random seed for the gaussian noise
        assertTrue(true);
    }



    @Test
    public void testRadialNoiseIntIntDoubleDoubleDoubleDouble() {
        assertEquals(2, op.x);
        assertEquals(3, op.y);
        assertEquals(4, op.stdDevX, 1e-7);
        assertEquals(5, op.stdDevY, 1e-7);
        assertEquals(6, op.initialNoiseStdDev, 1e-7);
        assertEquals(7, op.finalNoiseStdDev, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRadialNoiseIntIntDoubleDoubleDoubleDoubleException1() {
        new RadialNoise(2, 3, 4, 5, 0, 7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRadialNoiseIntIntDoubleDoubleDoubleDoubleException2() {
        new RadialNoise(2, 3, 4, 5, 6, 0);
    }



    @Test
    public void testToString() {
        String expected =
                "RadialNoise [x0=2 px, y0=3 px, std. dev. x=4.0, std. dev. y=5.0, initial noise std. dev.=6.0, final noise std. dev.=7.0]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new RadialNoise(3, 3, 4, 5, 6, 7)));
        assertFalse(op.equals(new RadialNoise(2, 4, 4, 5, 6, 7)));
        assertFalse(op.equals(new RadialNoise(2, 3, 5, 5, 6, 7)));
        assertFalse(op.equals(new RadialNoise(2, 3, 4, 6, 6, 7)));
        assertFalse(op.equals(new RadialNoise(2, 3, 4, 5, 7, 7)));
        assertFalse(op.equals(new RadialNoise(2, 3, 4, 5, 6, 8)));
        assertTrue(op.equals(new RadialNoise(2, 3, 4, 5, 6, 7)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new RadialNoise(3, 3, 4, 5, 6, 7), 1e-2));
        assertFalse(op.equals(new RadialNoise(2, 4, 4, 5, 6, 7), 1e-2));
        assertFalse(op.equals(new RadialNoise(2, 3, 4.1, 5, 6, 7), 1e-2));
        assertFalse(op.equals(new RadialNoise(2, 3, 4, 5.1, 6, 7), 1e-2));
        assertFalse(op.equals(new RadialNoise(2, 3, 4, 5, 6.1, 7), 1e-2));
        assertFalse(op.equals(new RadialNoise(2, 3, 4, 5, 6, 7.1), 1e-2));
        assertTrue(op.equals(new RadialNoise(2, 3, 4.001, 5.001, 6.001, 7.001),
                1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(736648443, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        RadialNoise other = new XmlLoader().load(RadialNoise.class, file);
        assertEquals(op, other, 1e-6);
    }



    @Test
    public void testRadialNoise() throws IOException {
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/noise_radialnoise.bmp");

        ByteMap map = new ByteMap(100, 100);
        map.clear(128);

        op.radialNoise(map, 0, -30, 50, 100, 1.0, 15.0, 1);

        map.assertEquals(expected);
    }

}
