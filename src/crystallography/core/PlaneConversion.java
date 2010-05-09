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

import ptpshared.core.math.Vector;

/**
 * Conversion between Miller and Bravais indices. Miller indices consists of the
 * 3 integer indices whereas the Bravais indices consists of 4 integer indices.
 * The Bravais definition is typically used for the hexagonal crystal system.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PlaneConversion {
    /**
     * Convert Bravais indices to Miller indices.
     * 
     * @param h
     *            indice
     * @param k
     *            indice
     * @param i
     *            indice
     * @param l
     *            indice
     * @return Miller indices
     * @throws InvalidPlaneException
     *             if the miller plane is a null plane
     */
    public static Plane bravaisToMiller(int h, int k, int i, int l)
            throws InvalidPlaneException {
        return new Plane(h, k, l);
    }



    /**
     * Convert Bravais indices to Miller indices.
     * 
     * @param plane
     *            a 4 indices plane
     * @return Miller indices
     * @throws InvalidPlaneException
     *             if the miller plane is a null plane
     */
    public static Plane bravaisToMiller(Vector plane)
            throws InvalidPlaneException {
        if (plane.size() != 4)
            throw new IllegalArgumentException(
                    "A Bravais plane has 4 indices (Given plane has "
                            + plane.size() + "indices.");

        int h = plane.get(0).intValue();
        int k = plane.get(1).intValue();
        int i = plane.get(2).intValue();
        int l = plane.get(3).intValue();

        return bravaisToMiller(h, k, i, l);
    }



    /**
     * Converts Miller indices to Bravais indices.
     * 
     * @param h
     *            indice
     * @param k
     *            indice
     * @param l
     *            indice
     * @return Bravais indices
     */
    public static Vector millerToBravais(int h, int k, int l) {
        int i = -(h + k);
        return new Vector(h, k, i, l);
    }



    /**
     * Convert Miller indices to Bravais indices.
     * 
     * @param plane
     *            a 3 indices plane
     * @return Bravais indices
     */
    public static Vector millerToBravais(Plane plane) {
        int h = plane.get(0);
        int k = plane.get(1);
        int l = plane.get(2);

        return millerToBravais(h, k, l);
    }
}
