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
package crystallography.core;

import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public class ReflectorTest {

    private Reflector refl;



    @Before
    public void setUp() throws Exception {
        refl = new Reflector(1, -1, 1, 3.0, 0.03512, 0.5);
    }



    @Test
    public void testEquals() {
        assertTrue(refl.equals(refl));

        assertFalse(refl.equals(null));

        assertFalse(refl.equals(new Object()));

        Reflector refl2 = new Reflector(1, -1, 1, 4.0, 0.5, 0.5);
        assertEquals(refl, refl2);
    }



    @Test
    public void testGetBravaisIndices() {
        // Reflector 1
        Reflector refl = new Reflector(1, -2, 0, 1.0, 1.0);

        int[] expected = new int[] { 1, -2, 1, 0 };
        int[] actual = refl.getBravaisIndices();
        assertArrayEquals(expected, actual);

        // Reflector 2
        refl = new Reflector(1, 1, 0, 1.0, 1.0);

        expected = new int[] { 1, 1, -2, 0 };
        actual = refl.getBravaisIndices();
        assertArrayEquals(expected, actual);
    }



    @Test
    public void testGetMillerIndices() {
        int[] expected = new int[] { 1, -1, 1 };
        int[] actual = refl.getMillerIndices();
        assertArrayEquals(expected, actual);
    }



    @Test
    public void testGetPlane() {
        Vector3D expected = new Vector3D(1, -1, 1);
        Vector3D actual = refl.getNormal();
        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testHasCode() {
        assertEquals(new Reflector(1, -1, 1, 3.0, 0.03512).hashCode(),
                refl.hashCode());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testReflectorException() {
        new Reflector(0, 0, 0, 3.0, 0.03512, 0.5);
    }



    @Test
    public void testReflectorPlaneDoubleDouble() {
        Reflector other = new Reflector(1, -1, 1, 3.0, 0.03512);
        assertEquals(1, refl.h);
        assertEquals(-1, refl.k);
        assertEquals(1, refl.l);
        assertEquals(3.0, other.planeSpacing, 1e-7);
        assertEquals(0.03512, other.intensity, 1e-7);
        assertEquals(1.0, other.normalizedIntensity, 1e-7);
    }



    @Test
    public void testReflectorPlaneDoubleDoubleDouble() {
        assertEquals(1, refl.h);
        assertEquals(-1, refl.k);
        assertEquals(1, refl.l);
        assertEquals(3.0, refl.planeSpacing, 1e-7);
        assertEquals(0.03512, refl.intensity, 1e-7);
        assertEquals(0.5, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testReflectorReflectorDouble() {
        Reflector other = new Reflector(refl, 1.0);
        assertEquals(1, refl.h);
        assertEquals(-1, refl.k);
        assertEquals(1, refl.l);
        assertEquals(refl.planeSpacing, other.planeSpacing, 1e-7);
        assertEquals(refl.intensity, other.intensity, 1e-7);
        assertEquals(1.0, other.normalizedIntensity, 1e-7);
    }

}
