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

import Jama.Matrix;
import static ptpshared.core.math.Math.acos;

/**
 * Operations on vectors.
 * 
 * @author Philippe T. Pinard
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
     * @return angle in radians
     */
    public static double angle(Vector3D v1, Vector3D v2) {
        return acos(directionCosine(v1, v2));
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
     * @return triple product
     */
    public static double tripleProduct(Vector3D v1, Vector3D v2, Vector3D v3) {
        return v1.cross(v2).dot(v3);
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
        return v1.cross(v2).norm() < precision;
    }



    /**
     * Finds the coefficients (t, s) to express the vector <code>v</code> as a
     * linear combination of vectors <code>a</code> and <code>b</code>.
     * 
     * @param v
     *            a vector
     * @param a
     *            first vector of the linear combination
     * @param b
     *            second vector of the linear combination
     * @return coefficients t and s
     * @throws ArithmeticException
     *             if the vector <code>v</code> cannot be decomposed by the
     *             vectors <code>a</code> and <code>b</code>
     */
    public static double[] linearDecomposition(Vector3D v, Vector3D a,
            Vector3D b) {
        // Find a solution with x and y
        // AX = B
        Matrix mA =
                new Matrix(new double[][] { { a.getX(), b.getX() },
                        { a.getY(), b.getY() } });
        Matrix mB = new Matrix(new double[][] { { v.getX() }, { v.getY() } });

        // X = A^{-1}B
        mA = mA.inverse();
        Matrix mX = mA.times(mB);

        double t = mX.get(0, 0);
        double s = mX.get(1, 0);

        // Check that this solution applies to z
        if (java.lang.Math.abs(t * a.getZ() + s * b.getZ() - v.getZ()) < 1e-6)
            return new double[] { t, s };
        else
            throw new ArithmeticException(
                    "The linear decomposition of vector (" + v
                            + ") with the vectors (" + a + " and " + b
                            + ") is not possible");
    }
}
