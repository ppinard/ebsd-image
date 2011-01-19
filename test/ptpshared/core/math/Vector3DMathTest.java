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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Vector3DMathTest {

    private Vector3D u, v, w;



    @Before
    public void setUp() {
        u = new Vector3D(1, 2, 3);
        v = new Vector3D(4, 5, 6);
        w = new Vector3D(1, 0, 0);
    }



    @Test
    public void testAngle() {
        assertEquals(Vector3DMath.angle(u, u), 0.0, 0.001);
        assertEquals(Vector3DMath.angle(u, u.multiply(2)), 0.0, 0.001);
        assertEquals(Vector3DMath.angle(u, v), 0.225726128553, 0.0000001);
    }



    @Test
    public void testDirectionCosine() {
        assertEquals(Vector3DMath.directionCosine(u, u), 1.0, 0.001);
        assertEquals(Vector3DMath.directionCosine(u, u.multiply(2)), 1.0, 0.001);
        assertEquals(Vector3DMath.directionCosine(u, v), 0.97463184619707621,
                0.0001);
    }



    @Test
    public void testTripleProduct() {
        double tp = Vector3DMath.tripleProduct(u, v, w);
        assertEquals(tp, -3.0, 0.001);

        tp = Vector3DMath.tripleProduct(v, w, u);
        assertEquals(tp, -3.0, 0.001);

        tp = Vector3DMath.tripleProduct(w, u, v);
        assertEquals(tp, -3.0, 0.001);

        tp = Vector3DMath.tripleProduct(u, w, v);
        assertEquals(tp, 3.0, 0.001);
    }



    @Test
    public void testAreCoplanar() {
        assertFalse(Vector3DMath.areCoplanar(u, v, w, 1e-6));

        Vector3D a = new Vector3D(1, 2, 0);
        Vector3D b = new Vector3D(4, 4, 0);
        Vector3D c = new Vector3D(0, 1, 0);
        assertTrue(Vector3DMath.areCoplanar(a, b, c, 1e-6));
    }



    @Test
    public void testAreParallel() {
        assertFalse(Vector3DMath.areParallel(u, w, 1e-6));
        assertFalse(Vector3DMath.areParallel(v, w, 1e-6));
        assertFalse(Vector3DMath.areParallel(u, v, 1e-6));

        Vector3D a = new Vector3D(1, 2, 3);
        Vector3D b = new Vector3D(2, 4, 6);
        assertTrue(Vector3DMath.areParallel(a, b, 1e-6));
    }



    @Test
    public void testLinearDecomposition() {
        Vector3D a = new Vector3D(1, 0, 0);
        Vector3D b = new Vector3D(0, 4, 6);

        double[] expected = new double[] { 1.0, 0.5 };
        double[] actual = Vector3DMath.linearDecomposition(u, a, b);
        assertArrayEquals(expected, actual, 1e-6);

    }



    @Test(expected = ArithmeticException.class)
    public void testLinearDecompositionException() {
        Vector3D a = new Vector3D(1, 0, 0);
        Vector3D b = new Vector3D(0, 4, 6);

        Vector3DMath.linearDecomposition(v, a, b);
    }
}
