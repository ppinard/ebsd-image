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

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.util.Arrays;

import net.jcip.annotations.Immutable;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Abstract class for an algebraic Euclidean vector. The class implements vector
 * addition and subtraction as well as scalar multiplication and division. Other
 * functions such as the dot product and the normalization are also implemented.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public abstract class BaseVector {
    /** Array that holds the actual data. */
    @NonNull
    protected final double[] v;



    /**
     * Creates a new vector with the specified values.
     * 
     * @param data
     *            values
     */
    public BaseVector(double... data) {
        this(data.length);

        for (int i = 0; i < data.length; i++) {
            if (Double.isNaN(data[i]) || Double.isInfinite(data[i]))
                throw new IllegalArgumentException(
                        "NaN or infinite at position " + i
                                + " is not accepted in Vector.");
        }

        System.arraycopy(data, 0, v, 0, data.length);
    }



    /**
     * Creates a new empty vector (all values are 0.0) of the specified
     * dimension.
     * 
     * @param size
     *            length of the vector
     */
    public BaseVector(int size) {
        v = new double[size];
    }



    /**
     * Clears all the items in this vector. All the items are assigned the value
     * <code>0</code>.
     */
    public void clear() {
        for (int n = 0; n < size(); n++)
            v[n] = 0.0;
    }



    /**
     * Returns a new vector resulting from the division of this vector by a
     * scalar.
     * 
     * @param scalar
     *            scalar value
     * @return resultant vector
     */
    @CheckReturnValue
    public BaseVector div(double scalar) {
        if (scalar == 0.0)
            throw new ArithmeticException("Zero division");

        return multiply(1.0 / scalar);
    }



    /**
     * Returns the dot product between this vector and the specified one.
     * 
     * @param other
     *            other vector of the same size
     * @return dot product
     */
    public Number dot(BaseVector other) {
        if (other == null)
            throw new NullPointerException("Cannot dot a null.");
        if (size() != other.size())
            throw new ArithmeticException("The size of this vector (" + size()
                    + ") must be the same as the size of the other ("
                    + other.size() + ").");

        double product = 0.0;

        for (int n = 0; n < size(); n++)
            product += get(n).doubleValue() * other.get(n).doubleValue();

        return product;
    }



    /**
     * Creates a copy.
     * 
     * @return copy of the current vector
     */
    @CheckReturnValue
    public abstract BaseVector duplicate();



    /**
     * Checks if this vector is almost equal to the specified one with the given
     * precision.
     * 
     * @param other
     *            other vector to check equality
     * @param precision
     *            level of precision
     * @return whether the two vectors are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(BaseVector other, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == other)
            return true;
        if (other == null)
            return false;

        for (int n = 0; n < size(); n++)
            if (abs(v[n] - other.v[n]) >= precision)
                return false;

        return true;
    }



    /**
     * Checks if this vector is exactly equal to the specified one.
     * 
     * @param obj
     *            other vector to check equality
     * @return whether the two vectors are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        BaseVector other = (BaseVector) obj;
        if (!Arrays.equals(v, other.v))
            return false;
        return true;
    }



    /**
     * Returns the value of this vector at the specified index.
     * 
     * @param index
     *            index of the desired value
     * @return value
     */
    public Number get(int index) {
        if (index < 0 || index >= size())
            throw new IllegalArgumentException("Index outside the limit: [0,"
                    + size() + "[");

        return v[index];
    }



    /**
     * Returns the hash code for this vector.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(v);
        return result;
    }



    /**
     * Returns a new vector resulting from the subtraction of this vector by the
     * specified one.
     * 
     * @param other
     *            other vector
     * @return resultant vector
     */
    @CheckReturnValue
    public BaseVector minus(BaseVector other) {
        if (other == null)
            throw new NullPointerException("Cannot substract a null.");

        BaseVector dup = other.duplicate();
        return plus(dup.negate());
    }



    /**
     * Returns a new vector resulting from the multiplication of this vector by
     * a scalar.
     * 
     * @param scalar
     *            scalar value
     * @return resultant vector
     */
    @CheckReturnValue
    public BaseVector multiply(double scalar) {
        BaseVector output = duplicate(); // To get vector of correct subclass

        for (int n = 0; n < size(); n++)
            output.v[n] = v[n] * scalar;

        return output;
    }



    /**
     * Returns the negative vector of this vector.
     * 
     * @return negative vector
     */
    @CheckReturnValue
    public BaseVector negate() {
        BaseVector output = duplicate();

        for (int n = 0; n < size(); n++)
            output.v[n] = -1 * v[n];

        return output;
    }



    /**
     * Returns the norm of this vector.
     * 
     * @return norm
     */
    public double norm() {
        return sqrt(square());
    }



    /**
     * Returns the dot product of this vector with itself.
     * 
     * @return square of the vector's norm
     */
    public double square() {
        return dot(this).doubleValue();
    }



    /**
     * Returns the normalized vector (<code>norm() == 1</code>) of this vector.
     * 
     * @return resultant vector
     */
    @CheckReturnValue
    public BaseVector normalize() {
        double norm = norm();
        if (norm == 0.0)
            throw new ArithmeticException("Cannot normalize a null vector.");

        return div(norm);
    }



    /**
     * Returns a new vector resulting from the addition of this vector to the
     * specified one.
     * 
     * @param other
     *            other vector
     * @return resultant vector
     */
    @CheckReturnValue
    public BaseVector plus(BaseVector other) {
        if (other == null)
            throw new NullPointerException("Cannot add a null.");
        if (other.size() != size())
            throw new ArithmeticException("The size of this vector (" + size()
                    + ") must be the same as the size of the other ("
                    + other.size() + ").");

        BaseVector output = duplicate(); // To get vector of correct subclass

        for (int n = 0; n < size(); n++)
            output.v[n] = get(n).doubleValue() + other.get(n).doubleValue();

        return output;
    }



    /**
     * Returns the positive vector of this vector. A positive vector is a vector
     * where its first non-zero component is positive.
     * 
     * @return resultant vector
     */
    @CheckReturnValue
    public BaseVector positive() {
        BaseVector output = duplicate();

        for (int n = 0; n < size(); n++)
            if (output.v[n] == 0.0)
                continue;
            else if (output.v[n] < 0.0) {
                output = output.negate();
                break;
            } else
                break;

        return output;
    }



    /**
     * Returns the number of coordinates of this vector (i.e. length of the
     * array).
     * 
     * @return size
     */
    public int size() {
        return v.length;
    }



    /**
     * Returns the sum of all the components of this vector.
     * 
     * @return sum of components
     */
    public Number sum() {
        double result = 0.0;

        for (int n = 0; n < size(); n++)
            result += v[n];

        return result;
    }



    /**
     * Returns the array representation of this vector.
     * 
     * @return an array
     */
    public double[] toArray() {
        return v.clone();
    }



    /**
     * Returns a representation of this vector, suitable for debugging.
     * 
     * @return values of the vector
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);
        str.append('(');
        for (int n = 0; n < size(); n++) {
            str.append(v[n]);
            str.append(';');
        }
        if (size() > 0)
            str.setLength(str.length() - 1);
        str.append(')');
        return str.toString();
    }

}
