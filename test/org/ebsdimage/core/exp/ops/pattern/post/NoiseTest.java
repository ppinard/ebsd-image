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

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class NoiseTest extends TestCase {

    private Noise op;



    @Before
    public void setUp() {
        op = new Noise(25.0);
    }



    @Test
    public void testEquals() {
        Noise other = new Noise(25.0);
        assertFalse(op == other);
        assertEquals(op, other);
    }



    @Test
    public void testNoiseInt() {
        assertEquals(25.0, op.stdDev, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testNoiseIntException() {
        new Noise(-1.0);
    }



    @Test
    public void testProcess() {
        // Impossible to test because of the random seed for the gaussian noise
        assertTrue(true);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Noise [std. dev.=25.0]");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new Noise(26.0)));
        assertTrue(op.equals(new Noise(25.0)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new Noise(25.1), 1e-2));
        assertTrue(op.equals(new Noise(25.001), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-878662649, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Noise other = new XmlLoader().load(Noise.class, file);
        assertEquals(op, other, 1e-6);
    }

}
