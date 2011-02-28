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
package ptpshared.math.old;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.math.old.Vector;
import ptpshared.math.old.Vector3D;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.sqrt;

import static junittools.test.Assert.assertEquals;

public class Vector3DTest extends TestCase {

    private Vector3D u, v, w;



    @Before
    public void setUp() throws Exception {
        u = new Vector3D(1, 2, 3);
        v = new Vector3D(4, 5, 6);
        w = new Vector3D(1, 0, 0);
    }



    @Test
    public void testClear() {
        u.clear();
        assertEquals(u, new Vector3D(0, 0, 0), 1e-7);
    }



    @Test
    public void testClone() {
        Vector3D other = u.clone();
        assertEquals(u, other, 1e-7);
    }



    @Test
    public void testCross() {
        assertEquals(u.cross(v), new Vector3D(-3, 6, -3));
        assertEquals(u.cross(u), new Vector3D(0, 0, 0));
    }



    @Test
    public void testDivDouble() {
        assertEquals(u.div(2.0), u.multiply(0.5));
        assertEquals(v.div(3.0), v.multiply(1.0 / 3));
    }



    @Test(expected = ArithmeticException.class)
    public void testDivDoubleException() {
        u.div(0.0);
    }



    @Test
    public void testDot() {
        assertEquals(32, u.dot(v), 1e-7);
    }



    @Test(expected = ArithmeticException.class)
    public void testDotException() {
        u.dot(new Vector(1, 2, 3, 4));
    }



    @Test(expected = NullPointerException.class)
    public void testDotNullException() {
        u.dot(null);
    }



    @Test
    public void testDuplicate() {
        Vector3D dup = u.clone();
        assertEquals(u, dup);
    }



    @Test
    public void testEquals() {
        assertTrue(u.equals(u));

        assertFalse(u.equals(null));

        assertFalse(u.equals(new Object()));

        assertTrue(u.equals(new Vector3D(1, 2, 3)));
        assertFalse(u.equals(new Vector3D(99, 2, 3)));
    }



    @Test
    public void testEqualsBaseVectorDouble() {
        assertTrue(u.equals(u, 1e-7));

        assertFalse(u.equals(null, 1e-7));

        assertTrue(u.equals(new Vector3D(1.001, 2, 3), 1e-2));
        assertFalse(u.equals(new Vector3D(1.1, 2, 3), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(new Vector(1, 2, 3).hashCode(), u.hashCode());
    }



    @Test
    public void testMinus() {
        assertEquals(new Vector3D(3, 3, 3), v.minus(u));
        assertEquals(v.minus(u), v.plus(u.negate()));
        assertFalse(v.minus(u).equals(u.minus(v)));
    }



    @Test(expected = ArithmeticException.class)
    public void testMinusException() {
        u.minus(new Vector(1, 2, 3, 4));
    }



    @Test(expected = NullPointerException.class)
    public void testMinusNullException() {
        u.minus(null);
    }



    @Test
    public void testMultiply() {
        assertTrue(u.multiply(2.2).equals(new Vector3D(2.2, 4.4, 6.6), 1e-7));
        assertTrue(w.multiply(2.2).equals(new Vector3D(2.2, 0, 0), 1e-7));
    }



    @Test
    public void testNegate() {
        assertEquals(u.negate(), new Vector3D(-1, -2, -3));
    }



    @Test
    public void testNorm() {
        assertEquals(sqrt(14), u.norm(), 1e-7);
    }



    @Test
    public void testNormalize() {
        Vector3D dup = u.normalize();
        assertEquals(dup, u.div(u.norm()));
    }



    @Test(expected = ArithmeticException.class)
    public void testNormalizeException() {
        new Vector3D(0, 0, 0).normalize();
    }



    @Test
    public void testPlus() {
        assertEquals(u.plus(w), new Vector3D(2, 2, 3));
        assertEquals(u.plus(v), v.plus(u));
    }



    @Test(expected = ArithmeticException.class)
    public void testPlusException() {
        u.plus(new Vector(1, 2, 3, 4));
    }



    @Test(expected = NullPointerException.class)
    public void testPlusNullException() {
        u.plus(null);
    }



    @Test
    public void testPositive() {
        Vector3D v;
        v = new Vector3D(0, -1, 1).positive();
        assertTrue(v.equals(new Vector3D(0, 1, -1), 1e-7));

        v = new Vector3D(0, 1, -1).positive();
        assertTrue(v.equals(new Vector3D(0, 1, -1), 1e-7));

        v = new Vector3D(0, 0, -1).positive();
        assertTrue(v.equals(new Vector3D(0, 0, 1), 1e-7));
    }



    @Test
    public void testSum() {
        assertEquals(6, u.sum(), 1e-7);
        assertEquals(15, v.sum(), 1e-7);
    }



    @Test
    public void testToArray() {
        double[] array = u.toArray();

        assertEquals(3, array.length);
        assertEquals(1, array[0], 1e-7);
        assertEquals(2, array[1], 1e-7);
        assertEquals(3, array[2], 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals("(1.0;2.0;3.0)", u.toString());
    }



    @Test
    public void testVector3D() {
        Vector3D v = new Vector3D();
        assertEquals(new Vector3D(0, 0, 0), v);
    }



    @Test
    public void testVector3DDoubleDoubleDouble() {
        assertEquals(3, u.size());
        assertEquals(1, u.v[0], 1e-7);
        assertEquals(2, u.v[1], 1e-7);
        assertEquals(3, u.v[2], 1e-7);
    }



    @Test
    public void testVector3DVector() {
        Vector3D vector = new Vector3D(new Vector(1, 2, 3, 4));

        assertEquals(3, vector.size());
        assertEquals(1, vector.v[0], 1e-7);
        assertEquals(2, vector.v[1], 1e-7);
        assertEquals(3, vector.v[2], 1e-7);
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        new XmlSaver().save(u, tmpFile);

        // Read
        Vector3D other = new XmlLoader().load(Vector3D.class, tmpFile);
        assertEquals(u, other, 1e-6);
    }
}
