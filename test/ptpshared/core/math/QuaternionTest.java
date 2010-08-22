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
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class QuaternionTest {

    private Quaternion q1;

    private Quaternion q2;

    private Quaternion q3;



    @Before
    public void setUp() throws Exception {
        q1 = new Quaternion(1, -2, 3, 4);
        q2 = new Quaternion(0, -1, 1, 1);
        q3 = new Quaternion(sqrt(2) / 2.0, sqrt(2) / 2.0, 0, 0);
    }



    @Test
    public void testConjugate() {
        // Test for validity
        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        Quaternion q = new Quaternion(4, 2, 1, 1);
        Quaternion expected_conjugate = new Quaternion(4, -2, -1, -1);
        assertEquals(q.conjugate(), expected_conjugate);

        q =
                new Quaternion(0.21566554640687682, 0.10783277320343841, 0,
                        0.9704949588309457);
        expected_conjugate =
                new Quaternion(0.21566554640687682, -0.10783277320343841, 0,
                        -0.9704949588309457);
        assertTrue(q.conjugate().equals(expected_conjugate, 1e-7));
    }



    @Test
    public void testDivDouble() {
        Quaternion expected = new Quaternion(0.25, -0.5, 0.75, 1.0);
        Quaternion division = q1.div(4.0);
        assertTrue(division.equals(expected, 1e-7));
    }



    @Test(expected = ArithmeticException.class)
    public void testDivDoubleException() {
        assertNotNull(q1.div(0.0));
    }



    @Test
    public void testDivQuaternion() {
        Quaternion expected =
                new Quaternion(5.196152422706632, 1.1547005383792515,
                        0.57735026918962584, -1.1547005383792519);
        Quaternion division = q1.div(q2);
        assertTrue(division.equals(expected, 1e-7));
    }



    @Test(expected = NullPointerException.class)
    public void testDivQuaternionException() {
        assertNotNull(q1.div(null));
    }



    @Test
    public void testEqualsObject() {
        assertTrue(q1.equals(q1));

        assertFalse(q1.equals(null));

        assertFalse(q1.equals(new Object()));

        assertFalse(q1.equals(new Quaternion(99, -2, 3, 4)));

        assertFalse(q1.equals(new Quaternion(1, 99, 99, 99)));

        assertTrue(q1.equals(new Quaternion(1, -2, 3, 4)));
    }



    @Test
    public void testEqualsQuaternionDouble() {
        assertTrue(q1.equals(q1, 1e-6));

        assertFalse(q1.equals(null, 1e-6));

        assertFalse(q1.equals(new Quaternion(1.0001, -2, 3, 4), 1e-5));
        assertTrue(q1.equals(new Quaternion(1.0001, -2, 3, 4), 1e-4));

        assertFalse(q1.equals(new Quaternion(1, -2, 3, 4.0003), 1e-7));
        assertTrue(q1.equals(new Quaternion(1, -2, 3, 4.0003), 1e-3));
    }



    @Test
    public void testGetQ0() {
        assertEquals(1, q1.getQ0(), 1e-7);
    }



    @Test
    public void testGetQ1() {
        assertEquals(-2, q1.getQ1(), 1e-7);
    }



    @Test
    public void testGetQ2() {
        assertEquals(3, q1.getQ2(), 1e-7);
    }



    @Test
    public void testGetQ3() {
        assertEquals(4, q1.getQ3(), 1e-7);
    }



    @Test
    public void testHashCode() {
        assertEquals(new Quaternion(1, -2, 3, 4).hashCode(), q1.hashCode());
    }



    @Test
    public void testHashCodeDouble() {
        Quaternion other = new Quaternion(1.0001, -2.0001, 3.0001, 4.0001);
        assertEquals(other.hashCode(1e-3), q1.hashCode(1e-3));
    }



    @Test
    public void testInvert() {
        // Test for validity
        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        Quaternion q = new Quaternion(4, 3, -1, 2);
        Quaternion expected =
                new Quaternion(0.7302967433402214, -0.5477225575051661,
                        0.18257418583505536, -0.3651483716701107);
        assertTrue(q.invert().equals(expected, 1e-7));

        assertTrue(q.normalize().invert().equals(expected, 1e-7));
    }



    @Test
    public void testIsNormalized() {
        // Test for validity
        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        Quaternion q = new Quaternion(4, 2, 1, 1);
        assertFalse(q.isNormalized());

        q =
                new Quaternion(0.21566554640687682, 0.10783277320343841, 0,
                        0.9704949588309457);
        assertTrue(q.isNormalized());
    }



    @Test
    public void testMinus() {
        // Test for validity
        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        Quaternion q1 = new Quaternion(5, 2, 1, 2);
        Quaternion q2 = new Quaternion(4, 8, 25, 4);
        assertEquals(q1.minus(q2), new Quaternion(1, -6, -24, -2));
    }



    @Test(expected = NullPointerException.class)
    public void testMinusException() {
        assertNotNull(q1.minus(null));
    }



    @Test
    public void testMultiplyDouble() {
        Quaternion q = new Quaternion(5, 2, 1, 2).multiply(2);
        assertEquals(q.getQ0(), 10, 0.001);
        assertEquals(q.getQ1(), 4, 0.001);
        assertEquals(q.getQ2(), 2, 0.001);
        assertEquals(q.getQ3(), 4, 0.001);
    }



    @Test
    public void testMultiplyQuaternion() {
        // Test for validity
        // http://reference.wolfram.com/mathematica/Quaternions/tutorial/Quaternions.html
        Quaternion q1 = new Quaternion(2, 0, -6, 3);
        Quaternion q2 = new Quaternion(1, 3, -2, 2);
        assertEquals(q1.multiply(q2), new Quaternion(-16, 0, -1, 25));

        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        q1 = new Quaternion(5, 2, 1, 2);
        q2 = new Quaternion(4, 8, 25, 4);
        assertEquals(q1.multiply(q2), new Quaternion(-29, 2, 137, 70));
    }



    @Test(expected = NullPointerException.class)
    public void testMultiplyQuaternionException() {
        assertNotNull(q1.multiply(null));
    }



    @Test
    public void testNorm() {
        double expected = 5.4772255750516612;
        assertEquals(q1.norm(), expected, 0.000001);

        expected = 1.7320508075688772;
        assertEquals(q2.norm(), expected, 0.000001);

        expected = 1.0;
        assertEquals(q3.norm(), expected, 0.000001);
    }



    @Test
    public void testNormalize() {
        // Test for validity
        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        Quaternion q = new Quaternion(4, 2, -1, 1);
        Quaternion expected =
                new Quaternion(0.8528028654224417, 0.42640143271122083,
                        -0.21320071635561041, 0.21320071635561041);
        assertTrue(q.normalize().equals(expected, 1e-7));
    }



    @Test
    public void testPlus() {
        // Test for validity
        // http://reference.wolfram.com/mathematica/Quaternions/tutorial/Quaternions.html
        Quaternion q1 = new Quaternion(1, 2, 3, 4);
        Quaternion q2 = new Quaternion(2, 3, 4, 5);
        assertEquals(q1.plus(q2), new Quaternion(3, 5, 7, 9));
        assertEquals(q2.plus(q1), new Quaternion(3, 5, 7, 9));

        // http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/arithmetic/index.htm
        q1 = new Quaternion(5, 2, 1, 2);
        q2 = new Quaternion(4, 8, 25, 4);
        assertEquals(q1.plus(q2), new Quaternion(9, 10, 26, 6));
        assertEquals(q2.plus(q1), new Quaternion(9, 10, 26, 6));
    }



    @Test(expected = NullPointerException.class)
    public void testPlusException() {
        assertNotNull(q1.plus(null));
    }



    @Test
    public void testZERO() {
        assertEquals(new Quaternion(0, 0, 0, 0), Quaternion.ZERO);
    }



    @Test
    public void testIDENTITY() {
        assertEquals(new Quaternion(1, 0, 0, 0), Quaternion.IDENTITY);
    }



    @Test
    public void testQuaternionAxisAngle() {
        // Verified with Martin Baker (2008) Quaternion to AxisAngle,
        // http://www.euclideansplace.com

        // Test input data
        Quaternion q = new Quaternion(new AxisAngle(0, 1, 0, 0));
        Quaternion expected = new Quaternion(1, 0, 0, 0);
        assertTrue(q.equals(expected, 1e-7));

        // Test calculation
        q = new Quaternion(new AxisAngle(PI / 2, 1, 0, 0));
        expected = new Quaternion(sqrt(2) / 2.0, sqrt(2) / 2.0, 0, 0);
        assertTrue(q.equals(expected, 1e-7));

        // Test back-conversion
        q = new Quaternion(new AxisAngle(PI / 3, 1, 1, 1));
        AxisAngle axisAngle = q.toAxisAngle();
        assertEquals(axisAngle.angle, PI / 3, 1e-6);
        assertEquals(axisAngle.axis.v[0], 1.0 / sqrt(3), 1e-6);
        assertEquals(axisAngle.axis.v[1], 1.0 / sqrt(3), 1e-6);
        assertEquals(axisAngle.axis.v[2], 1.0 / sqrt(3), 1e-6);
    }



    @Test
    public void testQuaternionDouble() {
        assertEquals(new Quaternion(2, 0, 0, 0), new Quaternion(2));
    }



    @Test
    public void testQuaternionDoubleDoubleDoubleDouble() {
        assertEquals(new Quaternion(1, -2, 3, 4), q1);
    }



    @Test
    public void testQuaternionDoubleVector3D() {
        assertEquals(q1, new Quaternion(1, new Vector3D(-2, 3, 4)));
    }



    @Test(expected = NullPointerException.class)
    public void testQuaternionDoubleVector3DException() {
        new Quaternion(1, null);
    }



    @Test
    public void testQuaternionEulers() {
        // Verified with Rollett, Tony (2008)
        // Advanced Characterization and Microstructural Analysis

        // Test calculation
        Eulers e = new Eulers(0, 0, 0);
        Quaternion q = new Quaternion(e);
        Quaternion expected = new Quaternion(1, 0, 0, 0);
        assertTrue(q.equals(expected, 1e-7));

        e = new Eulers(0, PI / 4, 0);
        q = new Quaternion(e);
        expected = new Quaternion(cos(0.5 * PI / 4), sin(0.5 * PI / 4), 0, 0);
        assertTrue(q.equals(expected, 1e-7));

        e = new Eulers(35 / 180.0 * PI, 27 / 180.0 * PI, 102 / 180.0 * PI);
        q = new Quaternion(e);

        Quaternion q1 = new Quaternion(new AxisAngle(35 / 180.0 * PI, 0, 0, 1));
        Quaternion q2 = new Quaternion(new AxisAngle(27 / 180.0 * PI, 1, 0, 0));
        Quaternion q3 =
                new Quaternion(new AxisAngle(102 / 180.0 * PI, 0, 0, 1));
        expected = q1.multiply(q2).multiply(q3);
        assertTrue(q.equals(expected, 1e-7));
    }



    @Test
    public void testQuaternionMatrix3D() {
        // Verified with Martin Baker (2008) Quaternion to AxisAngle
        // http://www.euclideansplace.com

        // Test calculation
        Matrix3D m = new Matrix3D(1, 0, 0, 0, 0, -1, 0, 1, 0);
        Quaternion q = new Quaternion(m);
        Quaternion expected =
                new Quaternion(sqrt(2) / 2.0, -sqrt(2) / 2.0, 0, 0);
        assertTrue(q.equals(expected, 1e-7));

        // Test back-conversion
        Matrix3D expected2 = q.toSO3matrix();
        assertTrue(m.equals(expected2, 1e-7));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testQuaternionMatrix3DException() {
        Matrix3D m = new Matrix3D(1, 10, 0, 0, 0, -1, 0, 1, 0);
        new Quaternion(m);
    }



    @Test
    public void testQuaternionVector3D() {
        assertEquals(q2, new Quaternion(new Vector3D(-1, 1, 1)));
    }



    @Test
    public void testToArray() {
        double[] array = q1.toArray();
        assertEquals(1, array[0], 1e-6);
        assertEquals(-2, array[1], 1e-6);
        assertEquals(3, array[2], 1e-6);
        assertEquals(4, array[3], 1e-6);
    }



    @Test
    public void testToAxisAngle() {
        // http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/index.htm
        AxisAngle axisAngle = q3.toAxisAngle();
        assertTrue(axisAngle.equals(new AxisAngle(PI / 2, 1.0, 0.0, 0.0), 1e-7));
    }



    @Test
    public void testToEuler() {
        // Random eulers
        Quaternion q = new Quaternion(0.394248, 0.068243, 0.892958, 0.206245);
        Eulers expected = new Eulers(1.976504, 2.219387, -1.0125373);
        assertTrue(q.toEuler().equals(expected, 1e-5));

        q = new Quaternion(0.018757, 0.952608, -0.108378, -0.283621);
        expected = new Eulers(-1.6180403, 2.565164, -1.3914753);
        assertTrue(q.toEuler().equals(expected, 1e-5));

        // Theta2 == 0
        q = new Quaternion(0.581489, 0.000000, 0.000000, 0.813554);
        expected = new Eulers(1.9004768, 0.0, 0.0);
        assertTrue(q.toEuler().equals(expected, 1e-5));

        q = new Quaternion(0.413062, 0.000000, 0.000000, -0.910703);
        expected = new Eulers(-2.2899652, 0.0, 0.0);
        assertTrue(q.toEuler().equals(expected, 1e-5));

        // Theta2 == PI
        q = new Quaternion(-0.000000, 0.480646, 0.876915, 0.000000);
        expected = new Eulers(2.138810, PI, 0.000000);
        assertTrue(q.toEuler().equals(expected, 1e-5));

        q = new Quaternion(-0.000000, 0.329727, 0.944076, 0.000000);
        expected = new Eulers(2.469564, PI, 0.000000);
        assertTrue(q.toEuler().equals(expected, 1e-5));
    }



    @Test
    public void testToSo3matrix() {
        // http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/index.htm
        Matrix3D matrix = q3.toSO3matrix();
        Matrix3D expected = new Matrix3D(1, 0, 0, 0, 0, 1, 0, -1, 0);
        assertTrue(matrix.equals(expected, 1e-7));
    }



    @Test
    public void testToString() {
        assertEquals("[[1.0; -2.0; 3.0; 4.0]]", q1.toString());
        assertEquals("[[-0.0; 1.0; -1.0; -1.0]]", q2.toString());
        assertEquals("[[0.7071067811865476; 0.7071067811865476; 0.0; 0.0]]",
                q3.toString());
    }
}
