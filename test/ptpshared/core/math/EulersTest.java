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
package ptpshared.core.math;

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;

public class EulersTest extends TestCase {

    private Eulers e1;

    private Eulers e2;

    private Eulers e3;



    @Before
    public void setUp() {
        e1 = new Eulers(0.1, 0.2, 0.3);
        e2 = new Eulers(0.4, 0.5, 0.6);
        e3 = new Eulers(0.7, 0.8, 0.9);
    }



    @Test
    public void testClone() {
        Eulers dup = e1.clone();
        assertEquals(e1, dup);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(e1.equals(e1, 1e-6));

        assertFalse(e1.equals(null, 1e-6));

        assertFalse(e1.equals(new Eulers(1.001, 0.2, 0.3), 1e-3));

        assertFalse(e1.equals(new Eulers(0.1, 1.002, 0.3), 1e-3));

        assertFalse(e1.equals(new Eulers(0.1, 0.2, 1.003), 1e-3));

        assertTrue(e1.equals(new Eulers(0.10001, 0.20001, 0.30001), 1e-3));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(e1.equals(e1));

        assertFalse(e1.equals(null));

        assertFalse(e1.equals(new Object()));

        assertFalse(e1.equals(new Eulers(1.1, 0.2, 0.3)));

        assertFalse(e1.equals(new Eulers(0.1, 1.2, 0.3)));

        assertFalse(e1.equals(new Eulers(0.1, 0.2, 1.3)));

        assertTrue(e1.equals(new Eulers(0.1, 0.2, 0.3)));
    }



    @Test
    public void testEulersDoubleDoubleDouble() {
        assertEquals(e1.theta1, 0.1, 1e-7);
        assertEquals(e1.theta2, 0.2, 1e-7);
        assertEquals(e1.theta3, 0.3, 1e-7);
    }



    @Test
    public void testEulersDoubleDoubleDouble1() {
        Eulers e = new Eulers(-PI, 0.2, 0.3);
        assertEquals(PI, e.theta1, 1e-6);
        assertEquals(0.2, e.theta2, 1e-6);
        assertEquals(0.3, e.theta3, 1e-6);
    }



    @Test
    public void testEulersDoubleDoubleDouble2() {
        Eulers e = new Eulers(0.1, 0.2, PI + 0.1);
        assertEquals(0.1, e.theta1, 1e-6);
        assertEquals(0.2, e.theta2, 1e-6);
        assertEquals(-PI + 0.1, e.theta3, 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testEulersDoubleDoubleDoubleException() {
        new Eulers(0.1, -0.1, 0.3); // Throw exception
    }



    @Test
    public void testHashCode() {
        assertEquals(new Eulers(0.1, 0.2, 0.3).hashCode(), e1.hashCode());
    }



    @Test
    public void testPositive() {
        double[] e1 = new Eulers(0, 0, 0).positive();
        assertEquals(e1[0], 0.0, 0.001);
        assertEquals(e1[1], 0.0, 0.001);
        assertEquals(e1[2], 0.0, 0.001);

        e1 = new Eulers(PI, 0, PI).positive();
        assertEquals(e1[0], PI, 0.001);
        assertEquals(e1[1], 0.0, 0.001);
        assertEquals(e1[2], PI, 0.001);

        e1 = new Eulers(-PI / 2.0, 0, -PI / 3).positive();
        assertEquals(e1[0], 3.0 * PI / 2.0, 0.001);
        assertEquals(e1[1], 0.0, 0.001);
        assertEquals(e1[2], 5.0 * PI / 3.0, 0.001);
    }



    @Test
    public void testToArray() {
        double[] array = e1.toArray();
        assertEquals(0.1, array[0], 1e-7);
        assertEquals(0.2, array[1], 1e-7);
        assertEquals(0.3, array[2], 1e-7);
    }



    @Test
    public void testToString() {
        String expected_str = "(0.1, 0.2, 0.3)";
        assertEquals(e1.toString(), expected_str);

        expected_str = "(0.4, 0.5, 0.6)";
        assertEquals(e2.toString(), expected_str);

        expected_str = "(0.7, 0.8, 0.9)";
        assertEquals(e3.toString(), expected_str);
    }



    @Test
    public void testToStringDegs() {
        String expected =
                "(5.729577951308232, 11.459155902616464, 17.188733853924695)";
        assertEquals(expected, e1.toStringDegs());
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        new XmlSaver().save(e1, tmpFile);

        // Read
        Eulers other = new XmlLoader().load(Eulers.class, tmpFile);
        assertAlmostEquals(e1, other, 1e-6);
    }

}
