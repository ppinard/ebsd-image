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

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

public class VectorTest extends TestCase {

    private Vector u, v, w, x, y, z, a;



    @Before
    public void setUp() throws Exception {
        u = new Vector(1, 2, 3);
        v = new Vector(4, 5, 6);
        w = new Vector(1, 0, 0);
        x = new Vector(1, 0, 0, 0, -5, 6);
        y = new Vector(1, 2, 3, 4);
        z = new Vector(8, 9);
        a = new Vector(-1, -2, 3);
    }



    @Test
    public void testClear() {
        v.clear();
        assertEquals(v, new Vector(0, 0, 0));
    }



    @Test
    public void testClone() {
        Vector other = x.clone();
        assertAlmostEquals(x, other, 1e-6);
    }



    @Test
    public void testDiv() {
        assertEquals(u.div(2.0), u.multiply(0.5));
        assertEquals(v.div(3.0), v.multiply(1.0 / 3));
    }



    @Test(expected = ArithmeticException.class)
    public void testDivException() {
        u.div(0.0);
    }



    @Test
    public void testDot() {
        assertEquals(u.dot(w), 1.0, 0.001);
        assertEquals(u.dot(v), v.dot(u), 0.001);
    }



    @Test(expected = ArithmeticException.class)
    public void testDotException() {
        u.dot(x);
    }



    @Test(expected = NullPointerException.class)
    public void testDotNullException() {
        u.dot(null);
    }



    @Test
    public void testDuplicate() {
        Vector dup = u.clone();
        assertEquals(u, dup);
        assertNotSame(u, dup);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(u.equals(u, 1e-7));

        assertFalse(u.equals(null, 1e-7));

        Vector u = new Vector(1, 2, 5);
        assertFalse(this.u.equals(u, 1e-7));

        u = new Vector(1, 2, 3.00000001);
        assertTrue(this.u.equals(u, 1e-7));
        assertTrue(u.equals(this.u, 1e-7));

        assertFalse(this.u.equals(x, 1e-7));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(u.equals(u));

        assertFalse(u.equals(null));

        assertFalse(u.equals(new Object()));

        assertTrue(x.equals(x));
        assertFalse(u.equals(v));
    }



    @Test
    public void testHashCode() {
        assertEquals(new Vector(1, 2, 3).hashCode(), u.hashCode());
    }



    @Test
    public void testMinus() {
        assertEquals(new Vector(3, 3, 3), v.minus(u));
        assertEquals(v.minus(u), v.plus(u.negate()));
        assertFalse(v.minus(u).equals(u.minus(v)));
    }



    @Test(expected = ArithmeticException.class)
    public void testMinusException() {
        x.minus(u);
    }



    @Test(expected = NullPointerException.class)
    public void testMinusNullException() {
        x.minus(null);
    }



    @Test
    public void testMultiplyDouble() {
        assertTrue(u.multiply(2.2).equals(new Vector(2.2, 4.4, 6.6), 1e-7));
    }



    @Test
    public void testNegate() {
        assertTrue(u.negate().equals(new Vector(-1, -2, -3), 1e-7));
        assertTrue(x.negate().equals(new Vector(-1, 0, 0, 0, 5, -6), 1e-7));
    }



    @Test
    public void testNorm() {
        assertEquals(u.norm(), sqrt(1 * 1 + 2 * 2 + 3 * 3), 0.001);
        assertEquals(x.norm(), sqrt(1 * 1 + 5 * 5 + 6 * 6), 0.001);
    }



    @Test
    public void testNormalize() {
        Vector dup = w.normalize();
        assertEquals(dup, w);

        dup = u.normalize();
        assertEquals(dup, u.div(u.norm()));
    }



    @Test(expected = ArithmeticException.class)
    public void testNormalizeException() {
        new Vector(0, 0, 0, 0).normalize();
    }



    @Test
    public void testPlus() {
        assertEquals(u.plus(w), new Vector(2, 2, 3));
        assertEquals(u.plus(v), v.plus(u));
    }



    @Test(expected = ArithmeticException.class)
    public void testPlusException() {
        x.plus(u);
    }



    @Test(expected = NullPointerException.class)
    public void testPlusNullException() {
        x.plus(null);
    }



    @Test
    public void testPositive() {
        Vector dup = w.positive();
        assertEquals(dup, w);

        assertEquals(a, new Vector(-1, -2, 3));
        assertEquals(a.positive(), new Vector(1, 2, -3));

        // Other tests
        Vector v;
        v = new Vector(0, -1, 1).positive();
        assertTrue(v.equals(new Vector(0, 1, -1), 1e-7));

        v = new Vector(0, 1, -1).positive();
        v.positive();
        assertTrue(v.equals(new Vector(0, 1, -1), 1e-7));

        v = new Vector(0, 0, -1).positive();
        v.positive();
        assertTrue(v.equals(new Vector(0, 0, 1), 1e-7));
    }



    @Test
    public void testSum() {
        assertEquals(6, u.sum(), 1e-7);
        assertEquals(2, x.sum(), 1e-7);
    }



    @Test
    public void testToArray() {
        double[] array = y.toArray();

        assertEquals(4, array.length);
        assertEquals(1, array[0], 1e-7);
        assertEquals(2, array[1], 1e-7);
        assertEquals(3, array[2], 1e-7);
        assertEquals(4, array[3], 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals("(4.0;5.0;6.0)", v.toString());
        assertEquals("()", new Vector(0).toString());
    }



    @Test
    public void testVectorDoubleArray() {
        assertEquals(1, u.v[0], 1e-7);
        assertEquals(2, u.v[1], 1e-7);
        assertEquals(3, u.v[2], 1e-7);

        assertEquals(3, u.size());
        assertEquals(3, v.size());
        assertEquals(3, w.size());
        assertEquals(6, x.size());
        assertEquals(4, y.size());
        assertEquals(2, z.size());
        assertEquals(3, a.size());
    }



    @Test
    public void testVectorInt() {
        Vector v = new Vector(3);
        assertEquals(3, v.size());
    }
}
