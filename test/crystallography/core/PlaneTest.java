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

import ptpshared.core.math.Vector;
import ptpshared.core.math.Vector3D;

public class PlaneTest {

    private Plane plane1;

    private Plane plane2;



    @Before
    public void setUp() throws Exception {
        plane1 = new Plane(3, 3, 3);
        plane2 = new Plane(new Vector3D(-3, 3.0, 3.0));
    }



    @Test
    public void testEquals() {
        assertTrue(plane1.equals(plane1));

        assertFalse(plane1.equals(null));

        assertFalse(plane1.equals(new Object()));

        assertFalse(plane1.equals(plane2));

        Plane plane = new Plane(3, 3, 3);
        assertTrue(plane1.equals(plane));
        assertEquals(plane, plane1);
    }



    @Test
    public void testHashCode() {
        assertEquals(new Plane(3, 3, 3).hashCode(), plane1.hashCode());
    }



    @Test
    public void testPlaneIntIntInt() {
        assertEquals((int) plane1.get(0), 3);
        assertEquals((int) plane1.get(1), 3);
        assertEquals((int) plane1.get(2), 3);
    }



    @Test(expected = InvalidPlaneException.class)
    public void testPlaneIntIntIntException() {
        new Plane(0, 0, 0);
    }



    @Test
    public void testPlaneVector3D() {
        assertEquals((int) plane2.get(0), -3);
        assertEquals((int) plane2.get(1), 3);
        assertEquals((int) plane2.get(2), 3);
    }



    @Test
    public void testSimplify() {
        Plane other = plane1.simplify();
        assertEquals((int) other.get(0), 1);
        assertEquals((int) other.get(1), 1);
        assertEquals((int) other.get(2), 1);

        other = plane2.simplify();
        assertEquals((int) other.get(0), 1);
        assertEquals((int) other.get(1), -1);
        assertEquals((int) other.get(2), -1);
    }



    @Test
    public void testToBravais() {
        Vector bravais1 = plane1.toBravais();
        assertEquals(bravais1.get(0).intValue(), 3);
        assertEquals(bravais1.get(1).intValue(), 3);
        assertEquals(bravais1.get(2).intValue(), -6);
        assertEquals(bravais1.get(3).intValue(), 3);

        Vector bravais2 = plane2.toBravais();
        assertEquals(bravais2.get(0).intValue(), -3);
        assertEquals(bravais2.get(1).intValue(), 3);
        assertEquals(bravais2.get(2).intValue(), 0);
        assertEquals(bravais2.get(3).intValue(), 3);
    }



    @Test
    public void testToString() {
        assertEquals("(3;3;3)", plane1.toString());
    }

}
