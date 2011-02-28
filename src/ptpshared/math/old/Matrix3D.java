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
package ptpshared.math.old;

import static java.lang.Math.abs;
import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Attribute;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Defines a 3x3 matrix. This object can be used to represent a rotation or any
 * other 3x3 matrix. Matrix operations of inversion, transpose and
 * multiplication are implemented as well as calculations of the determinant and
 * the trace.
 * <p/>
 * <b>References:</b>
 * <ul>
 * <li><a href="http://www.dr-lex.be/random/matrix_inv.html">Algorithm from
 * Alexander Thomas</a></li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Matrix3D implements AlmostEquable, Cloneable {

    /** Zero matrix. */
    public static final Matrix3D ZERO = new Matrix3D(0, 0, 0, 0, 0, 0, 0, 0, 0);

    /** Identity matrix. */
    public static final Matrix3D IDENTITY = new Matrix3D(1, 0, 0, 0, 1, 0, 0,
            0, 1);

    /** Array that holds the actual data. */
    @NonNull
    private final double[][] m = new double[3][3];



    /**
     * Creates a new <code>Matrix3D</code> with the specified values.
     * 
     * @param m00
     *            value of in the first row and first column
     * @param m01
     *            value of in the first row and second column
     * @param m02
     *            value of in the first row and third column
     * @param m10
     *            value of in the second row and first column
     * @param m11
     *            value of in the second row and second column
     * @param m12
     *            value of in the second row and third column
     * @param m20
     *            value of in the third row and first column
     * @param m21
     *            value of in the third row and second column
     * @param m22
     *            value of in the third row and third column
     */
    public Matrix3D(@Attribute(name = "m00") double m00,
            @Attribute(name = "m01") double m01,
            @Attribute(name = "m02") double m02,
            @Attribute(name = "m10") double m10,
            @Attribute(name = "m11") double m11,
            @Attribute(name = "m12") double m12,
            @Attribute(name = "m20") double m20,
            @Attribute(name = "m21") double m21,
            @Attribute(name = "m22") double m22) {
        this(new double[] { m00, m01, m02, m10, m11, m12, m20, m21, m22 });
    }



    /**
     * Creates a new <code>Matrix3D</code> from the specified 1D array. The
     * array must have 9 items representing the matrix values sequentially from
     * the top left corner to the bottom right corner.
     * <p/>
     * For example, the following array:<br/>
     * <code>double[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};</code><br/>
     * will given the following matrix:<br/>
     * 
     * <pre>
     *     _     _
     *    | 1 2 3 |
     *    | 4 5 6 |
     *    | 7 8 9 |
     *     -     -
     * </pre>
     * 
     * @param data
     *            array containing 9 values
     * @throws IllegalArgumentException
     *             if the array does not have 9 items
     */
    public Matrix3D(double[] data) {
        if (data.length != 9)
            throw new IllegalArgumentException("data array length ("
                    + data.length + ") must be 9.");

        int index = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++, index++) {
                if (Double.isNaN(data[index]) || Double.isInfinite(data[index]))
                    throw new IllegalArgumentException(
                            "NaN and Infinite at position " + i + ", " + j
                                    + " is not accepted in Matrix3D.");
                m[i][j] = data[index];
            }
    }



    /**
     * Creates a new <code>Matrix3D</code> from the specified 2D array.
     * <p/>
     * If <code>i</code> represents the row index and <code>j</code> represents
     * the column index, the 2D array must be coded as such:
     * <code>data[i][j]</code>. The following array:<br/>
     * <code>double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};</code><br/>
     * will given the following matrix:<br/>
     * 
     * <pre>
     *     _     _
     *    | 1 2 3 |
     *    | 4 5 6 |
     *    | 7 8 9 |
     *     -     -
     * </pre>
     * 
     * @param data
     *            2d array containing the values
     * @throws IllegalArgumentException
     *             if the array is not 3x3
     */
    public Matrix3D(double[][] data) {
        if (data.length != 3)
            throw new IllegalArgumentException("The number of matrix rows ("
                    + data.length + ") must be 3.");
        if (data[0].length != 3)
            throw new IllegalArgumentException("The number of matrix columns ("
                    + data[0].length + ") must be 3.");

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (Double.isNaN(data[i][j]) || Double.isInfinite(data[i][j]))
                    throw new IllegalArgumentException(
                            "NaN and Infinite at position " + i + ", " + j
                                    + " is not accepted in Matrix3D.");
                m[i][j] = data[i][j];
            }
    }



    /**
     * Clears all the items in the matrix. All values in the matrix are set to
     * 0.
     */
    public void clear() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                m[i][j] = 0.0;
    }



    /**
     * Creates a copy.
     * 
     * @return copy of the current <code>Matrix3D</code>
     */
    @Override
    @CheckReturnValue
    public Matrix3D clone() {
        double[][] data = new double[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                data[i][j] = m[i][j];

        return new Matrix3D(data);
    }



    /**
     * Returns the determinant of the matrix.
     * 
     * @return determinant
     */
    public double det() {
        double a =
                m[0][0] * m[1][1] * m[2][2] + m[0][1] * m[1][2] * m[2][0]
                        + m[0][2] * m[2][1] * m[1][0];
        double b =
                m[0][0] * m[1][2] * m[2][1] + m[0][1] * m[2][2] * m[1][0]
                        + m[0][2] * m[2][0] * m[1][1];

        return a - b;
    }



    /**
     * Checks if this <code>Matrix3D</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>Matrix3D</code> to check equality
     * @return whether the two <code>Matrix3D</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Matrix3D other = (Matrix3D) obj;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (Double.doubleToLongBits(m[i][j]) != Double.doubleToLongBits(other.m[i][j]))
                    return false;
        return true;
    }



    /**
     * Checks if this <code>Matrix3D</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param obj
     *            other <code>Matrix3D</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Matrix3D</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    @Override
    public boolean equals(Object obj, Object precision) {
        double delta = ((Number) precision).doubleValue();
        if (delta < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(delta))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Matrix3D other = (Matrix3D) obj;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (abs(m[i][j] - other.m[i][j]) > delta)
                    return false;

        return true;
    }



    /**
     * Returns a specific value from the <code>Matrix3D</code>.
     * 
     * @param row
     *            index of the row (between 0 and 2)
     * @param col
     *            index of the column (between 0 and 2)
     * @return value from the <code>Matrix3D</code>
     */
    public double get(int row, int col) {
        if (row < 0 || row > 2)
            throw new IllegalArgumentException(
                    "Row index has to be between [0, 2].");
        if (col < 0 || col > 2)
            throw new IllegalArgumentException(
                    "Row index has to be between [0, 2].");

        return m[row][col];
    }



    /**
     * Returns the element (0,0).
     * 
     * @return element (0,0).
     */
    @Attribute(name = "m00")
    public double getM00() {
        return m[0][0];
    }



    /**
     * Returns the element (0,1).
     * 
     * @return element (0,1).
     */
    @Attribute(name = "m01")
    public double getM01() {
        return m[0][1];
    }



    /**
     * Returns the element (0,2).
     * 
     * @return element (0,2).
     */
    @Attribute(name = "m02")
    public double getM02() {
        return m[0][2];
    }



    /**
     * Returns the element (1,0).
     * 
     * @return element (1,0).
     */
    @Attribute(name = "m10")
    public double getM10() {
        return m[1][0];
    }



    /**
     * Returns the element (1,1).
     * 
     * @return element (1,1.
     */
    @Attribute(name = "m11")
    public double getM11() {
        return m[1][1];
    }



    /**
     * Returns the element (1,2).
     * 
     * @return element (1,2).
     */
    @Attribute(name = "m12")
    public double getM12() {
        return m[1][2];
    }



    /**
     * Returns the element (2,0).
     * 
     * @return element (2,0).
     */
    @Attribute(name = "m20")
    public double getM20() {
        return m[2][0];
    }



    /**
     * Returns the element (2,1).
     * 
     * @return element (2,1).
     */
    @Attribute(name = "m21")
    public double getM21() {
        return m[2][1];
    }



    /**
     * Returns the element (2,2).
     * 
     * @return element (2,2).
     */
    @Attribute(name = "m22")
    public double getM22() {
        return m[2][2];
    }



    /**
     * Returns the hash code for this <code>Matrix3D</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                long temp;
                temp = Double.doubleToLongBits(m[i][j]);
                result = prime * result + (int) (temp ^ (temp >>> 32));
            }
        return result;
    }



    /**
     * Returns the inverse of the current matrix. <b>References:</b>
     * <ul>
     * <li><a href="http://www.dr-lex.be/random/matrix_inv.html">Algorithm from
     * Alexander Thomas</a></li>
     * </ul>
     * 
     * @return the inverse matrix
     */
    @CheckReturnValue
    public Matrix3D inverse() {
        double determinant = det();
        if (determinant == 0)
            throw new ArithmeticException(
                    "Matrices with a determinant equal to 0 cannot be inverted");

        double a11 = (m[2][2] * m[1][1] - m[2][1] * m[1][2]) / determinant;
        double a12 = (-(m[2][2] * m[0][1] - m[2][1] * m[0][2])) / determinant;
        double a13 = (m[1][2] * m[0][1] - m[1][1] * m[0][2]) / determinant;

        double a21 = (-(m[2][2] * m[1][0] - m[2][0] * m[1][2])) / determinant;
        double a22 = (m[2][2] * m[0][0] - m[2][0] * m[0][2]) / determinant;
        double a23 = (-(m[1][2] * m[0][0] - m[1][0] * m[0][2])) / determinant;

        double a31 = (m[2][1] * m[1][0] - m[2][0] * m[1][1]) / determinant;
        double a32 = (-(m[2][1] * m[0][0] - m[2][0] * m[0][1])) / determinant;
        double a33 = (m[1][1] * m[0][0] - m[1][0] * m[0][1]) / determinant;

        return new Matrix3D(a11, a12, a13, a21, a22, a23, a31, a32, a33);
    }



    /**
     * Checks if the matrix is a special orthogonal matrix with a precision of
     * 1e-7.
     * 
     * @return <code>true</code> if the <code>Matrix3D</code> is a special
     *         orthogonal matrix
     */
    public boolean isSpecialOrthogonal() {
        return isSpecialOrthogonal(1e-7);
    }



    /**
     * Checks if the matrix can be considered as a special orthogonal matrix.
     * <p/>
     * Conditions:
     * <ul>
     * <li>abs(det) == 1.0</li>
     * <li>AA^T=I (Wolfram MathWorld)</li>
     * </ul>
     * 
     * @param precision
     *            precision of the comparison
     * @return <code>true</code> if the <code>Matrix3D</code> is a special
     *         orthogonal matrix
     */
    public boolean isSpecialOrthogonal(double precision) {
        return (abs(det() - 1.0) < precision && multiply(transpose()).equals(
                IDENTITY, precision));
    }



    /**
     * Returns a new matrix resulting from the multiplication of the current
     * matrix by a scalar. Every value of the matrix is multiplied by the
     * scalar.
     * 
     * @param value
     *            scalar
     * @return resultant matrix
     */
    @CheckReturnValue
    public Matrix3D multiply(double value) {
        double[][] data = new double[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                data[i][j] = m[i][j] * value;

        return new Matrix3D(data);
    }



    /**
     * Returns a new matrix resulting from the multiplication of the current
     * matrix by the specified matrix.
     * 
     * @param other
     *            matrix
     * @return resultant matrix
     */
    @CheckReturnValue
    public Matrix3D multiply(Matrix3D other) {
        double[][] data = new double[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    data[i][j] += m[i][k] * other.m[k][j];

        return new Matrix3D(data);
    }



    /**
     * Returns a new vector resulting from the multiplication of the matrix by
     * the specified vector. The vector is assumed to be a column vector.
     * 
     * @param other
     *            column vector
     * @return resultant <code>Vector3D</code>
     */
    @CheckReturnValue
    public Vector3D multiply(Vector3D other) {
        double[] data = new double[3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                data[i] += m[i][j] * other.v[j];

        return new Vector3D(data);
    }



    /**
     * Returns a representation of the <code>Matrix3D</code> as a 2d array.
     * 
     * @return a 2d array
     */
    public double[][] toArray() {
        return m.clone();
    }



    /**
     * Returns a representation of the <code>Matrix3D</code> , suitable for
     * debugging.
     * 
     * @return information about the <code>Matrix3D</code>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                str.append(m[i][j]);
                if (j < 2)
                    str.append(", ");
            }
            if (i < 2)
                str.append('\n');
        }

        return str.toString();
    }



    /**
     * Returns the trace of the matrix.
     * 
     * @return trace
     */
    public double trace() {
        return m[0][0] + m[1][1] + m[2][2];
    }



    /**
     * Returns a the transpose of the current matrix.
     * 
     * @return transpose matrix
     */
    @CheckReturnValue
    public Matrix3D transpose() {
        double[][] data = new double[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                data[i][j] = m[j][i];

        return new Matrix3D(data);
    }
}
