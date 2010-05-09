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

import static ptpshared.core.math.Math.acos;

/**
 * Operations on vectors.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Vector3DMath {

    /**
     * Returns the angle in radians between two vectors. The angle is calculated
     * from the dot product of two unit vectors.
     * 
     * @param v1
     *            the first vector
     * @param v2
     *            the second vector
     * 
     * @return angle in radians
     */
    public static double angle(Vector3D v1, Vector3D v2) {
        return angle(directionCosine(v1, v2));
    }



    /**
     * Returns the angle in radians for the given direction cosine.
     * <p/>
     * <code>angle = acos(directionCosine)</code>
     * 
     * @param directionCosine
     *            direction cosine value
     * @return angle in radians
     */
    public static double angle(double directionCosine) {
        return acos(directionCosine);
    }



    /**
     * Returns the direction cosine between two vectors. The direction cosine is
     * calculated from the dot product of two unit vectors.
     * 
     * @param v1
     *            the first vector
     * @param v2
     *            the second vector
     * 
     * @return direction cosine
     */
    public static double directionCosine(Vector3D v1, Vector3D v2) {
        return v1.dot(v2) / (v1.norm() * v2.norm());
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
     * 
     * @return triple product
     */
    public static double tripleProduct(Vector3D v1, Vector3D v2, Vector3D v3) {
        return v1.cross(v2).dot(v3);
    }

}
