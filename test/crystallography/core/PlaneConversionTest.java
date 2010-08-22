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

import org.junit.Test;

import ptpshared.core.math.Vector;

public class PlaneConversionTest {

    @Test
    public void testBravaisToMillerIntIntIntInt() {
        Plane expected = new Plane(1, 1, 0);
        Plane vector = PlaneConversion.bravaisToMiller(1, 1, -2, 0);
        assertEquals(vector, expected);
    }



    @Test
    public void testBravaisToMillerVector() {
        Plane expected = new Plane(1, -2, 0);
        Plane vector = PlaneConversion.bravaisToMiller(new Vector(1, -2, 1, 0));
        assertEquals(vector, expected);
    }



    @Test
    public void testMillerToBravaisIntIntInt() {
        Vector expected = new Vector(1, 1, -2, 0);
        Vector vector = PlaneConversion.millerToBravais(1, 1, 0);
        assertEquals(vector, expected);
    }



    @Test
    public void testMillerToBravaisVector3D() {
        Vector expected = new Vector(1, -2, 1, 0);
        Vector vector = PlaneConversion.millerToBravais(new Plane(1, -2, 0));
        assertEquals(vector, expected);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testBravaisToMillerVectorException() {
        PlaneConversion.bravaisToMiller(new Vector(1, 2, 3, 4, 5));
    }

}
