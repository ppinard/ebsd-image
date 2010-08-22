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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class Matrix3DTest {

    private Matrix3D m1;

    private Matrix3D m2;

    private Matrix3D m3;

    private Matrix3D mso1;

    private Matrix3D mso2;



    @Before
    public void setUp() throws Exception {
        m1 =
                new Matrix3D(new double[][] { { 1, 2, 3 }, { 4, 5, 6 },
                        { 7, 8, 9 } });
        m2 = new Matrix3D(2, 2, 2, 3, 3, 3, 4, 4, 4);
        m3 = new Matrix3D(1, 2, 3, 0, 1, 4, 5, 6, 0);

        // Special orthogonal
        mso1 =
                new Matrix3D(1, 0, 0, 0, sqrt(3) / 2, 1.0 / 2, 0, -1.0 / 2,
                        sqrt(3) / 2);
        mso2 = new Matrix3D(0.36, 0.48, -0.8, -0.8, 0.6, 0, 0.48, 0.64, 0.6);
    }



    @Test
    public void testClear() {
        m1.clear();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                assertEquals(0, m1.get(i, j), 1e-3);
    }



    @Test
    public void testDet() {
        assertEquals(m1.det(), 0, 1e-7);
        assertEquals(m2.det(), 0, 1e-7);
        assertEquals(m3.det(), 1, 1e-7);
        assertEquals(mso1.det(), 1, 1e-7);
        assertEquals(mso2.det(), 1, 1e-7);
    }



    @Test
    public void testDuplicate() {
        Matrix3D m = m1.duplicate();
        assertEquals(m, m1);
    }



    @Test
    public void testEqualsMatrix3DDouble() {
        assertTrue(m1.equals(m1, 1e-6));

        assertFalse(m1.equals(null, 1e-6));

        Matrix3D m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9.00000011);
        assertFalse(m1.equals(m, 1e-7));

        m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9.00000001);
        assertTrue(m1.equals(m, 1e-7));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(m1.equals(m1));

        assertFalse(m1.equals(null));

        assertFalse(m1.equals(new Object()));

        Matrix3D m = new Matrix3D(11, 21, 31, 41, 51, 61, 71, 81, 91);
        assertFalse(m1.equals(m));

        m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertTrue(m1.equals(m));
    }



    @Test
    public void testGet() {
        assertEquals(1, m1.get(0, 0), 1e-6);
        assertEquals(2, m1.get(0, 1), 1e-6);
        assertEquals(3, m1.get(0, 2), 1e-6);
        assertEquals(4, m1.get(1, 0), 1e-6);
        assertEquals(5, m1.get(1, 1), 1e-6);
        assertEquals(6, m1.get(1, 2), 1e-6);
        assertEquals(7, m1.get(2, 0), 1e-6);
        assertEquals(8, m1.get(2, 1), 1e-6);
        assertEquals(9, m1.get(2, 2), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetExceptionCol1() {
        m1.get(0, -1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetExceptionCol2() {
        m1.get(0, 3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetExceptionRow1() {
        m1.get(-1, 0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetExceptionRow2() {
        m1.get(3, 0);
    }



    @Test
    public void testHashCode() {
        assertEquals(new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9).hashCode(),
                m1.hashCode());
    }



    @Test
    public void testInverse() {
        Matrix3D expectedInverse =
                new Matrix3D(-24, 18, 5, 20, -15, -4, -5, 4, 1);
        assertTrue(m3.inverse().equals(expectedInverse, 1e-7));
    }



    @Test(expected = ArithmeticException.class)
    public void testInverseException() {
        assertNotNull(new Matrix3D(0, 0, 0, 0, 0, 0, 0, 0, 0).inverse());
    }



    @Test
    public void testIsSpecialOrthogonal() {
        assertFalse(m1.isSpecialOrthogonal());
        assertFalse(m2.isSpecialOrthogonal());
        assertFalse(m3.isSpecialOrthogonal());

        assertTrue(mso1.isSpecialOrthogonal());
        assertTrue(mso2.isSpecialOrthogonal());
    }



    @Test
    public void testZERO() {
        Matrix3D m = Matrix3D.ZERO;
        for (int i = 0; i > 3; i++)
            for (int j = 0; j < 3; j++)
                assertEquals(0.0, m.get(i, j), 1e-3);
    }



    @Test
    public void testMatrix3DDouble9() {
        Matrix3D m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9);

        double value = 1.0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                assertEquals(value, m.get(i, j), 1e-3);
                value++;
            }
    }



    @Test
    public void testMatrix3DDoubleArray() {
        double[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Matrix3D m = new Matrix3D(data);

        double value = 1.0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                assertEquals(value, m.get(i, j), 1e-3);
                value++;
            }
    }



    @Test
    public void testMatrix3DDoubleArrayArray() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix3D m = new Matrix3D(data);

        double value = 1.0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                assertEquals(value, data[i][j], 1e-3);
                assertEquals(value, m.get(i, j), 1e-3);
                value++;
            }
    }



    // 1D constructor without the appropriate number of items
    @Test(expected = IllegalArgumentException.class)
    public void testMatrix3DDoubleArrayMissingItem() {
        double[] data = { 1, 2, 3, 4, 5, 6, 7, 8 };
        Matrix3D m = new Matrix3D(data);
        assertEquals(m, null);
    }



    // 1D constructor without the appropriate number of items
    @Test(expected = IllegalArgumentException.class)
    public void testMatrix3DDoubleArrayMissingRow() {
        double[] data = { 1, 2, 3, 4, 5, 6 };
        Matrix3D m = new Matrix3D(data);
        assertEquals(m, null);
    }



    // 2D constructor without the appropriate number of columns
    @Test(expected = IllegalArgumentException.class)
    public void testMatrix3DMissingColumnItem() {
        double[][] data = { { 1, 2 }, { 4, 5 }, { 7, 8 } };
        Matrix3D m = new Matrix3D(data);
        assertEquals(m, null);
    }



    // 2D constructor with a missing item
    @Test(expected = IllegalArgumentException.class)
    public void testMatrix3DMissingItem() {
        double[][] data = { { 1, 2 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix3D m = new Matrix3D(data);
        assertEquals(m, null);
    }



    // 2D constructor without the appropriate number of rows
    @Test(expected = IllegalArgumentException.class)
    public void testMatrix3DMissingRow() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix3D m = new Matrix3D(data);
        assertEquals(m, null);
    }



    @Test
    public void testMultiplyDouble() {
        Matrix3D mult = m1.multiply(3);

        Matrix3D expected = new Matrix3D(3, 6, 9, 12, 15, 18, 21, 24, 27);
        assertTrue(mult.equals(expected, 1e-7));
    }



    @Test
    public void testMultiplyMatrix3D() {
        Matrix3D m1m2 = m1.multiply(m2);
        Matrix3D expectedm1m2 =
                new Matrix3D(20, 20, 20, 47, 47, 47, 74, 74, 74);
        assertTrue(m1m2.equals(expectedm1m2, 1e-7));
    }



    @Test
    public void testMultiplyVector3D() {
        Vector3D vector = m1.multiply(new Vector3D(1, 1, 1));
        Vector3D expectedVector = new Vector3D(6, 15, 24);
        assertTrue(vector.equals(expectedVector, 1e-7));
    }



    @Test
    public void testToString() {
        assertEquals("1.0, 2.0, 3.0\n4.0, 5.0, 6.0\n7.0, 8.0, 9.0",
                m1.toString());
    }



    @Test
    public void testTrace() {
        assertEquals(15, m1.trace(), 1e-7);
        assertEquals(9, m2.trace(), 1e-7);
        assertEquals(2, m3.trace(), 1e-7);

        assertEquals(2.7320508, mso1.trace(), 1e-7);
        assertEquals(1.56, mso2.trace(), 1e-7);
    }



    @Test
    public void testTranspose() {
        assertEquals(new Matrix3D(1, 4, 7, 2, 5, 8, 3, 6, 9), m1.transpose());
    }

}
