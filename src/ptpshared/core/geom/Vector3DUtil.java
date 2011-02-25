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
package ptpshared.core.geom;

import org.apache.commons.math.geometry.Vector3D;

/**
 * Operations on vectors.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3DUtil {

    /**
     * Returns the direction cosine between two vectors. The direction cosine is
     * calculated from the dot product of two unit vectors.
     * 
     * @param v1
     *            the first vector
     * @param v2
     *            the second vector
     * @return direction cosine
     */
    public static double directionCosine(Vector3D v1, Vector3D v2) {
        return Vector3D.dotProduct(v1, v2) / (v1.getNorm() * v2.getNorm());
    }



    /**
     * Returns the triple product of the three vectors. The triple product is
     * the cross product of two vectors followed by the dot product of the
     * resultant vector by the third vector.
     * 
     * @param v1
     *            the first vector
     * @param v2
     *            the second vector
     * @param v3
     *            the third vector
     * @return triple product
     */
    public static double tripleProduct(Vector3D v1, Vector3D v2, Vector3D v3) {
        return Vector3D.dotProduct(Vector3D.crossProduct(v1, v2), v3);
    }



    /**
     * Checks whether three vectors are coplanar (i.e. they lie inside the same
     * plane). To be coplanar, the triple product of the vector must be equal to
     * zero.
     * 
     * @param v1
     *            the first vector
     * @param v2
     *            the second vector
     * @param v3
     *            the third vector
     * @param precision
     *            precision of the verification
     * @return <code>true</code> if the vectors are coplanar, <code>false</code>
     *         otherwise
     */
    public static boolean areCoplanar(Vector3D v1, Vector3D v2, Vector3D v3,
            double precision) {
        return java.lang.Math.abs(tripleProduct(v1, v2, v3)) < precision;
    }



    /**
     * Checks whether two vectors are parallel. To be parallel, the cross
     * product of two vectors must be the null vector.
     * 
     * @param v1
     *            first vector
     * @param v2
     *            second vector
     * @param precision
     *            precision of the verification
     * @return <code>true</code> if the vectors are parallel, <code>false</code>
     *         otherwise
     */
    public static boolean areParallel(Vector3D v1, Vector3D v2, double precision) {
        return Vector3D.crossProduct(v1, v2).getNorm() < precision;
    }

}
