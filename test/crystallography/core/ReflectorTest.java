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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import crystallography.core.crystals.Silicon;

public class ReflectorTest {

    private Reflector refl;
    private Plane plane;



    @Before
    public void setUp() throws Exception {
        plane = new Plane(1, -1, 1);
        refl = new Reflector(plane, 3.0, 0.03512, 0.5);
    }



    @Test
    public void testEquals() {
        assertTrue(refl.equals(refl));

        assertFalse(refl.equals(null));

        assertFalse(refl.equals(new Object()));

        Reflector refl2 = new Reflector(plane, 4.0, 0.5, 0.5);
        assertEquals(refl, refl2);
    }



    @Test
    public void testHasCode() {
        assertEquals(new Reflector(plane, 3.0, 0.03512).hashCode(), refl
                .hashCode());
    }



    @Test
    public void testReflectorPlaneDoubleDoubleDouble() {
        assertEquals(plane, refl.plane);
        assertEquals(3.0, refl.planeSpacing, 1e-7);
        assertEquals(0.03512, refl.intensity, 1e-7);
        assertEquals(0.5, refl.normalizedIntensity, 1e-7);
    }



    @Test(expected = NullPointerException.class)
    public void testReflectorException() {
        new Reflector(null, 3.0, 0.03512, 0.5);
    }



    @Test
    public void testReflectorPlaneDoubleDouble() {
        Reflector other = new Reflector(plane, 3.0, 0.03512);
        assertEquals(plane, other.plane);
        assertEquals(3.0, other.planeSpacing, 1e-7);
        assertEquals(0.03512, other.intensity, 1e-7);
        assertEquals(1.0, other.normalizedIntensity, 1e-7);
    }



    @Test
    public void testReflectorPlaneUnitCellAtomSitesScatteringFactors() {
        Crystal crystal = new Silicon();
        Plane plane = new Plane(1, 1, 1);

        Reflector other = new Reflector(plane, crystal,
                new XrayScatteringFactors());

        assertEquals(plane, other.plane);
        assertEquals(3.13501196, other.planeSpacing, 1e-7);
        assertEquals(1775.88446522, other.intensity, 1e-7);
        assertEquals(1.0, other.normalizedIntensity, 1e-7);
    }



    @Test
    public void testReflectorReflectorDouble() {
        Reflector other = new Reflector(refl, 1.0);
        assertEquals(refl.plane, other.plane);
        assertEquals(refl.planeSpacing, other.planeSpacing, 1e-7);
        assertEquals(refl.intensity, other.intensity, 1e-7);
        assertEquals(1.0, other.normalizedIntensity, 1e-7);
    }

}
