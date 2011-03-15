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

/**
 * Operations on vectors.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3DUtils {

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
     * Returns the positive vector of the specified vector. A positive vector is
     * a vector where its first non-zero component is positive.
     * 
     * @param v
     *            a vector
     * @return resultant vector
     */
    public static Vector3D positive(Vector3D v) {
        double[] data = new double[] { v.getX(), v.getY(), v.getZ() };

        for (int i = 0; i < 3; i++)
            if (data[i] == 0.0)
                continue;
            else if (data[i] < 0.0) {
                for (int j = i; j < 3; j++)
                    data[j] = -1 * data[j];
                break;
            } else
                break;

        return new Vector3D(data[0], data[1], data[2]);
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
     * Converts a <code>Vector3D</code> to a <code>RealVector</code>.
     * 
     * @param v
     *            a vector 3D
     * @return a real vector
     */
    public static RealVector toRealVector(Vector3D v) {
        return new ArrayRealVector(
                new double[] { v.getX(), v.getY(), v.getZ() });
    }



    /**
     * Converts a <code>RealVector</code> to a <code>Vector3D</code>.
     * 
     * @param v
     *            a real vector
     * @return a vector 3D
     */
    public static Vector3D toVector3D(RealVector v) {
        if (v.getDimension() != 3)
            throw new IllegalArgumentException("The vector length ("
                    + v.getDimension() + ") must be equal to 3.");

        return new Vector3D(v.getEntry(0), v.getEntry(1), v.getEntry(2));
    }



    /**
     * Returns an array representation of the specified vector.
     * 
     * @param v
     *            a vector
     * @return a array of length 3
     */
    public static double[] toArray(Vector3D v) {
        return new double[] { v.getX(), v.getY(), v.getZ() };
    }



    /**
     * Checks whether two <code>Vector3D</code> are equal to a certain
     * precision.
     * 
     * @param u
     *            vector 1
     * @param v
     *            vector 2
     * @param precision
     *            precision
     * @return <code>true</code> if the two vectors are equal to the specified
     *         precision, <code>false</code> otherwise
     */
    public static boolean equals(Vector3D u, Vector3D v, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "Precision cannot be less than zero.");

        if (u == null || v == null)
            return false;

        if (!(Math.abs(u.getX() - v.getX()) <= precision))
            return false;
        if (!(Math.abs(u.getY() - v.getY()) <= precision))
            return false;
        if (!(Math.abs(u.getZ() - v.getZ()) <= precision))
            return false;

        return true;
    }

}
