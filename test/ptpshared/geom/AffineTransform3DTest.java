/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package ptpshared.geom;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class AffineTransform3DTest extends TestCase {

    private AffineTransform3D transform;

    private double[][] m;

    private double[] t;



    @Before
    public void setUp() throws Exception {
        m = new double[][] { { 0, 0, 1 }, { 0, 1, 0 }, { -1, 0, 0 } };
        t = new double[] { 1, 2, 3 };
        transform = new AffineTransform3D(m, t);
    }



    @Test
    public void testAffineTransform3DDoubleArrayArray() {
        AffineTransform3D transform =
                new AffineTransform3D(new double[][] { { 0, 0, 1, 1 },
                        { 0, 1, 0, 2 }, { -1, 0, 0, 3 }, { 0, 0, 0, 1 } });

        assertEquals(m, transform.getRotationMatrix(), 1e-6);
        assertArrayEquals(t, Vector3DUtils.toArray(transform.getTranslation()),
                1e-6);
    }



    @Test
    public void testAffineTransform3DMatrix3DVector3D() {
        assertEquals(m, transform.getRotationMatrix(), 1e-6);
        assertArrayEquals(t, Vector3DUtils.toArray(transform.getTranslation()),
                1e-6);
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(transform.equals(transform, 1e-6));
        assertFalse(transform.equals(null, 1e-6));
        assertFalse(transform.equals(new Object(), 1e-6));

        double[] t1 = new double[] { 99, 99, 99 };
        AffineTransform3D diff = new AffineTransform3D(m, t1);
        assertFalse(transform.equals(diff, 1e-6));

        t1 = new double[] { 1.01, 2.01, 3.01 };
        AffineTransform3D same = new AffineTransform3D(m, t1);
        assertTrue(transform.equals(same, 1e-1));
    }



    @Test
    public void testGetRotation() {
        Rotation rotation = transform.getRotation();
        assertEquals(m, rotation.getMatrix(), 1e-6);
    }



    @Test
    public void testGetRotationMatrix() {
        assertEquals(m, transform.getRotationMatrix(), 1e-6);
    }



    @Test
    public void testGetTranslation() {
        assertArrayEquals(t, Vector3DUtils.toArray(transform.getTranslation()),
                1e-6);
    }



    @Test
    public void testInverse() {
        double[][] mExpected =
                new double[][] { { 0, 0, -1 }, { 0, 1, 0 }, { 1, 0, 0 } };
        Vector3D tExpected = new Vector3D(3, -2, -1);

        AffineTransform3D actual = transform.inverse();

        assertEquals(mExpected, actual.getRotationMatrix(), 1e-6);
        assertEquals(tExpected, actual.getTranslation(), 1e-6);
    }



    @Test
    public void testInverse2() {
        double[][] m =
                new double[][] { { 0, 1, 0 }, { -1, 0, 0 }, { 0, 0, 1 } };
        double[] t = new double[] { 2, 5, -3 };

        AffineTransform3D actual = new AffineTransform3D(m, t).inverse();

        double[][] mExpected =
                new double[][] { { 0, -1, 0 }, { 1, 0, 0 }, { 0, 0, 1 } };
        Vector3D tExpected = new Vector3D(5, -2, 3);

        assertEquals(mExpected, actual.getRotationMatrix(), 1e-6);
        assertEquals(tExpected, actual.getTranslation(), 1e-6);
    }



    @Test
    public void testInverse3() {
        AffineTransform3D actual = transform.inverse().inverse();
        assertEquals(transform.getRotationMatrix(), actual.getRotationMatrix(),
                1e-6);
        assertEquals(transform.getTranslation(), actual.getTranslation(), 1e-6);
    }



    @Test
    public void testMultiply() {
        AffineTransform3D a =
                new AffineTransform3D(new double[][] { { 0, 0, -1, 1 },
                        { -1, 0, 0, 10 }, { 0, 1, 0, 0 }, { 0, 0, 0, 1 } });
        AffineTransform3D b =
                new AffineTransform3D(new double[][] { { 0, 1, 0, 0 },
                        { -1, 0, 0, 5 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });

        AffineTransform3D expected =
                new AffineTransform3D(new double[][] { { 0, 0, -1, 1 },
                        { 0, -1, 0, 10 }, { -1, 0, 0, 5 }, { 0, 0, 0, 1 } });
        AffineTransform3D actual = a.multiply(b);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testToString() {
        String expected =
                "0.0, 0.0, 1.0, 1.0\n0.0, 1.0, 0.0, 2.0\n-1.0, 0.0, 0.0, 3.0\n0.0, 0.0, 0.0, 1.0";
        assertEquals(expected, transform.toString());
    }



    @Test
    public void testTransformPoint() {
        Vector3D point = new Vector3D(1, 0, 0);
        Vector3D expected = new Vector3D(1, 2, 2);
        Vector3D actual = transform.transformPoint(point);
        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testTransformVector() {
        Vector3D vector = new Vector3D(1, 0, 0);
        Vector3D expected = new Vector3D(0, 0, -1);
        Vector3D actual = transform.transformVector(vector);
        assertEquals(expected, actual, 1e-6);
    }

}
