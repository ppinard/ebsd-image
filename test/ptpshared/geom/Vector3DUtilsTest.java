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
package ptpshared.geom;

import org.apache.commons.math.geometry.Vector3D;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public class Vector3DUtilsTest {

    private Vector3D u, v, w;



    @Before
    public void setUp() {
        u = new Vector3D(1, 2, 3);
        v = new Vector3D(4, 5, 6);
        w = new Vector3D(1, 0, 0);
    }



    @Test
    public void testAreCoplanar() {
        assertFalse(Vector3DUtils.areCoplanar(u, v, w, 1e-6));

        Vector3D a = new Vector3D(1, 2, 0);
        Vector3D b = new Vector3D(4, 4, 0);
        Vector3D c = new Vector3D(0, 1, 0);
        assertTrue(Vector3DUtils.areCoplanar(a, b, c, 1e-6));
    }



    @Test
    public void testAreParallel() {
        assertFalse(Vector3DUtils.areParallel(u, w, 1e-6));
        assertFalse(Vector3DUtils.areParallel(v, w, 1e-6));
        assertFalse(Vector3DUtils.areParallel(u, v, 1e-6));

        Vector3D a = new Vector3D(1, 2, 3);
        Vector3D b = new Vector3D(2, 4, 6);
        assertTrue(Vector3DUtils.areParallel(a, b, 1e-6));
    }



    @Test
    public void testDirectionCosine() {
        assertEquals(Vector3DUtils.directionCosine(u, u), 1.0, 0.001);
        assertEquals(Vector3DUtils.directionCosine(u, u.scalarMultiply(2)),
                1.0, 0.001);
        assertEquals(Vector3DUtils.directionCosine(u, v), 0.97463184619707621,
                0.0001);
    }



    @Test
    public void testEquals() {
        assertFalse(Vector3DUtils.equals(u, null, 1e-3));
        assertFalse(Vector3DUtils.equals(null, v, 1e-3));

        Vector3D other = new Vector3D(1.01, 2.01, 3.01);
        assertTrue(Vector3DUtils.equals(u, other, 1e-1));

        other = new Vector3D(1.1, 2.01, 3.01);
        assertFalse(Vector3DUtils.equals(u, other, 1e-1));

        other = new Vector3D(1.01, 2.1, 3.01);
        assertFalse(Vector3DUtils.equals(u, other, 1e-1));

        other = new Vector3D(1.01, 2.01, 3.1);
        assertFalse(Vector3DUtils.equals(u, other, 1e-1));
    }



    @Test
    public void testPositive() {
        Vector3D v = Vector3DUtils.positive(new Vector3D(0, -1, 1));
        assertEquals(new Vector3D(0, 1, -1), v, 1e-6);

        v = Vector3DUtils.positive(new Vector3D(0, 1, -1));
        assertEquals(new Vector3D(0, 1, -1), v, 1e-6);

        v = Vector3DUtils.positive(new Vector3D(0, 0, -1));
        assertEquals(new Vector3D(0, 0, 1), v, 1e-6);
    }



    @Test
    public void testToArray() {
        double[] expected = new double[] { 1, 2, 3 };
        double[] actual = Vector3DUtils.toArray(u);

        assertArrayEquals(expected, actual, 1e-6);
    }



    @Test
    public void testToRealVector() {
        RealVector expected = new ArrayRealVector(new double[] { 1, 2, 3 });
        RealVector actual = Vector3DUtils.toRealVector(u);

        assertArrayEquals(expected.toArray(), actual.toArray(), 1e-6);
    }



    @Test
    public void testToVector3D() {
        RealVector a = new ArrayRealVector(new double[] { 1, 2, 3 });
        Vector3D actual = Vector3DUtils.toVector3D(a);

        assertEquals(u.getX(), actual.getX(), 1e-6);
        assertEquals(u.getY(), actual.getY(), 1e-6);
        assertEquals(u.getZ(), actual.getZ(), 1e-6);
    }



    @Test
    public void testTripleProduct() {
        double tp = Vector3DUtils.tripleProduct(u, v, w);
        assertEquals(tp, -3.0, 0.001);

        tp = Vector3DUtils.tripleProduct(v, w, u);
        assertEquals(tp, -3.0, 0.001);

        tp = Vector3DUtils.tripleProduct(w, u, v);
        assertEquals(tp, -3.0, 0.001);

        tp = Vector3DUtils.tripleProduct(u, w, v);
        assertEquals(tp, 3.0, 0.001);
    }
}
