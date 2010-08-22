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

import org.junit.Test;

public class QuaternionMathTest {

    @Test
    public void testMisorientation() {
        // From A.D. Rollett, Misorientations and Grain Boundaries, Lectures
        // Slides, 2007
        Matrix3D gA =
                new Matrix3D(-0.577, 0.707, 0.408, -0.577, -0.707, 0.408,
                        0.577, 0, 0.817);
        Matrix3D gB =
                new Matrix3D(0.577, -0.707, 0.408, 0.577, 0.707, 0.408, -0.577,
                        0, 0.817);

        Quaternion qA = new Quaternion(gA, 1e-3);
        Quaternion qB = new Quaternion(gB, 1e-3);

        Quaternion qOut = QuaternionMath.misorientation(qA, qB);

        Matrix3D mOut = qOut.toSO3matrix();
        Matrix3D expected =
                new Matrix3D(-0.667, 0.333, 0.667, 0.333, -0.667, 0.667, 0.667,
                        0.667, 0.333);
        assertTrue(mOut.equals(expected, 1e-2));
    }



    @Test
    public void testRotate() {
        // Test of successive rotations
        Vector3D v = new Vector3D(1, 0, 0); // Vector (1,0,0)
        // 90deg rotation along z-axis
        Quaternion q1 = new Quaternion(new AxisAngle(PI / 2, 0, 0, 1));
        // 90deg rotation along x-axis
        Quaternion q2 = new Quaternion(new AxisAngle(PI / 2, 1, 0, 0));

        Vector3D vq1 = QuaternionMath.rotate(v, q1);
        Vector3D expected_vq1 = new Vector3D(0, 1, 0);
        assertTrue(vq1.equals(expected_vq1, 1e-7)); // Vector (0,1,0)

        Vector3D vq1q2 = QuaternionMath.rotate(v, q2.multiply(q1));
        Vector3D expected_vq1q2 = new Vector3D(0, 0, 1);
        assertTrue(vq1q2.equals(expected_vq1q2, 1e-7)); // Vector (0,0,1)

        expected_vq1q2 = QuaternionMath.rotate(v, q1, q2);
        // Order of rotation q1 then q2
        assertTrue(vq1q2.equals(expected_vq1q2, 1e-7));

        Vector3D notexpected_vq1q2 = QuaternionMath.rotate(v, q2, q1);
        assertFalse(vq1q2.equals(notexpected_vq1q2, 1e-7));

        // Test of successive rotations
        v = new Vector3D(1, 0, 0);
        q1 = new Quaternion(new Eulers(0.13, 0, 0.93));
        q2 = new Quaternion(new Eulers(0.60, 0.80, 0.152));
        Quaternion q3 = new Quaternion(new Eulers(0.150, 0, 0.12));

        Vector3D vq1q2q3 =
                QuaternionMath.rotate(v, q3.multiply(q2).multiply(q1));

        // Order of rotation q1, q2 then q3
        Vector3D expected_vq1q2q3 = QuaternionMath.rotate(v, q1, q2, q3);
        assertTrue(vq1q2q3.equals(expected_vq1q2q3, 1e-7));

        Vector3D notexpected_vq1q2q3 = QuaternionMath.rotate(v, q3, q2, q1);
        assertFalse(vq1q2q3.equals(notexpected_vq1q2q3, 1e-7));
    }



    @Test
    public void testRotation() {
        Vector3D v1;
        Vector3D v2;
        Quaternion rotation;
        AxisAngle result;

        // 90 deg rotation around z
        v1 = new Vector3D(1, 0, 0);
        v2 = new Vector3D(0, 1, 0);
        rotation = QuaternionMath.rotation(v1, v2);
        result = rotation.toAxisAngle();
        assertEquals(PI / 2, result.angle, 1e-6);
        assertTrue(result.axis.equals(new Vector3D(0, 0, 1), 1e-6));
        assertTrue(QuaternionMath.rotate(v1, rotation).equals(v2.normalize(),
                1e-6));

        // 180 deg rotation
        v1 = new Vector3D(1, 0, 0);
        v2 = new Vector3D(-1, 0, 0);
        rotation = QuaternionMath.rotation(v1, v2);
        result = rotation.toAxisAngle();
        assertEquals(PI, result.angle, 1e-6);
        assertTrue(result.axis.equals(new Vector3D(0, 0, 1), 1e-6));
        assertTrue(QuaternionMath.rotate(v1, rotation).equals(v2.normalize(),
                1e-6));

        // -90 deg rotation around z
        v1 = new Vector3D(1, 0, 0);
        v2 = new Vector3D(0, -1, 0);
        rotation = QuaternionMath.rotation(v1, v2);
        result = rotation.toAxisAngle();
        assertEquals(PI / 2, result.angle, 1e-6);
        assertTrue(result.axis.equals(new Vector3D(0, 0, -1), 1e-6));
        assertTrue(QuaternionMath.rotate(v1, rotation).equals(v2.normalize(),
                1e-6));

        // 90 deg rotation around diagonal
        v1 = new Vector3D(1, 0, 0);
        v2 = new Vector3D(0, 1, 1);
        rotation = QuaternionMath.rotation(v1, v2);
        result = rotation.toAxisAngle();
        assertTrue(result.axis.equals(new Vector3D(0, -1, 1).normalize(), 1e-6));
        assertTrue(QuaternionMath.rotate(v1, rotation).equals(v2.normalize(),
                1e-6));

    }

}
