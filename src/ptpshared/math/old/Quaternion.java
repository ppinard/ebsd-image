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

import static java.lang.Math.*;
import static ptpshared.math.Math.acos;
import static ptpshared.math.Math.sqrt;
import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import rmlshared.math.Stats;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Represents a rotation using a quaternion. A quaternion consists of a scalar
 * value and a unit 3D vector. Quaternions are an easy and effective way to
 * represent a rotation since only four values are required and the algebra to
 * combine rotations is easier and faster.
 * <p/>
 * For a rotation, the positive quaternion is equivalent to the negative
 * quaternion. A positive quaternion is a quaternion where its first non-zero
 * component is positive. By definition, a quaternion is always positive.
 * <p/>
 * <b>References:</b>
 * <ul>
 * <li>Altmann</li>
 * <li>Orilib</li>
 * <li>Martin Baker</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class Quaternion implements AlmostEquable, Cloneable {

    /** Null quaternion (all coefficients are equal to 0.0). */
    public static final Quaternion ZERO = new Quaternion(0);

    /** Identity quaternion (zero rotation). */
    public static final Quaternion IDENTITY = new Quaternion(1);

    /** The scalar component of the quaternion. */
    public final double scalar;

    /** The vector component of the quaternion. */
    @NonNull
    public final Vector3D vector;



    /**
     * Creates a new <code>Quaternion</code> from an axis angle.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li><a href="http://www.euclideansplace.com">Martin Baker (2008)
     * Euclidean Space</a></li>
     * </ul>
     * 
     * @param axisAngle
     *            axis-angle representing a rotation.
     * @throws NullPointerException
     *             axis angle cannot be null
     */
    public Quaternion(AxisAngle axisAngle) {
        if (axisAngle == null)
            throw new NullPointerException("Axis angle cannot be null.");

        double halfAngle = 0.5 * axisAngle.angle;
        // TODO: Verify since apache common math is using -0.5 * axisAngle.angle
        double multiplier = sin(halfAngle);

        double q0 = cos(halfAngle);
        double q1 = axisAngle.axis.get(0) * multiplier;
        double q2 = axisAngle.axis.get(1) * multiplier;
        double q3 = axisAngle.axis.get(2) * multiplier;

        // Create positive quaternion
        Vector q = new Vector(q0, q1, q2, q3).positive();

        // Store values
        scalar = q.get(0);
        vector = new Vector3D(q.get(1), q.get(2), q.get(3));
    }



    /**
     * Creates a new scalar <code>Quaternion</code>. All the values of the
     * vector are 0.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * 
     * @param a
     *            scalar
     */
    public Quaternion(double a) {
        this(a, new Vector3D(0, 0, 0));
    }



    /**
     * Creates a new <code>Quaternion</code> with the specified scalar and
     * vector coordinates.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * 
     * @param q0
     *            scalar value
     * @param q1
     *            first coordinate of the vector
     * @param q2
     *            second coordinate of the vector
     * @param q3
     *            third coordinate of the vector
     */
    public Quaternion(@Attribute(name = "q0") double q0,
            @Attribute(name = "q1") double q1,
            @Attribute(name = "q2") double q2, @Attribute(name = "q3") double q3) {
        this(q0, new Vector3D(q1, q2, q3));
    }



    /**
     * Creates a new <code>Quaternion</code> from the specified scalar and
     * vector.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * 
     * @param a
     *            scalar
     * @param v
     *            vector
     * @throws NullPointerException
     *             vector cannot be null
     */
    public Quaternion(double a, Vector3D v) {
        this(a, v, true);
    }



    /**
     * Creates a new <code>Quaternion</code> from a scalar and a
     * <code>Vector3D</code>. The quaternion is only force to be positive if the
     * <code>positive</code> argument is equal to <code>true</code>.
     * 
     * @param a
     *            scalar
     * @param v
     *            vector
     * @param positive
     *            <code>true</code> if the quaternion should be made positive
     * @throws NullPointerException
     *             vector cannot be null
     */
    protected Quaternion(double a, Vector3D v, boolean positive) {
        if (v == null)
            throw new NullPointerException("Vector cannot be null.");

        // Create positive quaternion
        Vector q;
        if (positive)
            q = new Vector(a, v.get(0), v.get(1), v.get(2)).positive();
        else
            q = new Vector(a, v.get(0), v.get(1), v.get(2));

        // Store values
        scalar = q.get(0);
        vector = new Vector3D(q.get(1), q.get(2), q.get(3));
    }



    /**
     * Creates a new <code>Quaternion</code> from a <code>Eulers</code> angles.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * <li>Rollett, Tony (2008) Advanced Characterization and Microstructural
     * Analysis</li>
     * </ul>
     * 
     * @param angles
     *            euler angles
     * @throws NullPointerException
     *             euler angles cannot be null
     */
    public Quaternion(Eulers angles) {
        if (angles == null)
            throw new NullPointerException("Euler angles cannot be null.");

        // For clarity
        double t1 = angles.theta1;
        double t2 = angles.theta2;
        double t3 = angles.theta3;

        double q0 = cos(t2 / 2.0) * cos((t1 + t3) / 2.0);
        double q1 = sin(t2 / 2.0) * cos((t1 - t3) / 2.0);
        double q2 = sin(t2 / 2.0) * sin((t1 - t3) / 2.0);
        double q3 = cos(t2 / 2.0) * sin((t1 + t3) / 2.0);

        // Create positive quaternion
        Vector q = new Vector(q0, q1, q2, q3).positive();

        // Store values
        scalar = q.get(0);
        vector = new Vector3D(q.get(1), q.get(2), q.get(3));
    }



    /**
     * Creates a new <code>Quaternion</code> from a SO3 matrix. The matrix is
     * checked to be SO3 with the specified precision.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * 
     * @param so3matrix
     *            a special orthogonal matrix
     * @param precision
     *            precision to check for SO3
     * @see #Quaternion(Matrix3D)
     * @throws NullPointerException
     *             SO3 matrix cannot be null
     * @throws IllegalArgumentException
     *             SO3 matrix is not a special orthogonal matrix
     */
    public Quaternion(Matrix3D so3matrix, double precision) {
        if (so3matrix == null)
            throw new NullPointerException("SO3 matrix cannot be null.");
        if (!so3matrix.isSpecialOrthogonal(precision))
            throw new IllegalArgumentException(
                    "so3matrix is not a special orthogonal matrix.");

        double[][] m = so3matrix.toArray(); // For less verbosity

        double q0 = 0.5 * sqrt(so3matrix.trace() + 1);
        double[] v = new double[3];

        if (q0 >= precision && !Double.isNaN(q0)) { // Morawiec & orilib
            v[0] = (m[1][2] - m[2][1]) / (4 * q0);
            v[1] = (m[2][0] - m[0][2]) / (4 * q0);
            v[2] = (m[0][1] - m[1][0]) / (4 * q0);
        } else {
            q0 = 0.0;
            v[0] = sqrt((m[0][0] + 1) / 2.0);
            v[1] = sqrt((m[1][1] + 1) / 2.0);
            v[2] = sqrt((m[2][2] + 1) / 2.0);

            int s = Stats.maxIndex(v);
            for (int i = 0; i < 3; i++)
                if (i != s)
                    if (m[i][s] < 0)
                        v[i] *= -1;
        }

        // Create positive quaternion
        Vector q = new Vector(q0, v[0], v[1], v[2]).positive();

        // Store values
        scalar = q.get(0);
        vector = new Vector3D(q.get(1), q.get(2), q.get(3));
    }



    /**
     * Creates a new <code>Quaternion</code> from a SO3 matrix.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li><a href="http://www.euclideansplace.com"> Martin Baker (2008)
     * Euclidean Space</a></li>
     * <li><a href=
     * "http://en.wikipedia.org/wiki/Rotation_representation_(mathematics)_">
     * Wikipedia, Rotation representation (mathematics)</a></li>
     * </ul>
     * 
     * @param so3matrix
     *            a SO3 matrix
     */
    public Quaternion(Matrix3D so3matrix) {
        this(so3matrix, 1e-7);
    }



    /**
     * Creates a new <code>Quaternion</code> from the specified vector. The
     * scalar component will be set to 0.
     * <p/>
     * <em>The <code>Quaternion</code> will be converted to be a positive
     * <code>Quaternion</code>.</em>
     * 
     * @param v
     *            vector
     * @throws NullPointerException
     *             vector cannot be null
     */
    public Quaternion(Vector3D v) {
        this(0, v.clone());
    }



    /**
     * Returns the conjugate of this <code>Quaternion</code>. The conjugate is
     * defined by the inverse of the vector part of the quaternion.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @return conjugate quaternion
     */
    @CheckReturnValue
    public Quaternion conjugate() {
        return conjugate(true);
    }



    /**
     * Returns the conjugate of this <code>Quaternion</code>. The conjugate is
     * defined by the inverse of the vector part of the quaternion. The
     * quaternion is only force to be positive if the <code>positive</code>
     * argument is equal to <code>true</code>.
     * 
     * @param positive
     *            <code>true</code> if the quaternion should be made positive
     * @return conjugate quaternion
     */
    @CheckReturnValue
    protected Quaternion conjugate(boolean positive) {
        return new Quaternion(scalar, vector.negate(), positive);
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from the division of this
     * quaternion by the specified scalar value.
     * 
     * @param value
     *            scalar
     * @return resultant quaternion
     * @throws ArithmeticException
     *             if the value is 0.0
     */
    @CheckReturnValue
    public Quaternion div(double value) {
        if (value == 0)
            throw new ArithmeticException("Zero division");

        return multiply(1 / value);
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from the division of this
     * quaternion by the specified one.
     * 
     * @param other
     *            other <code>Quaternion</code>
     * @return resultant quaternion
     * @throws NullPointerException
     *             if the other quaternion is null
     */
    @CheckReturnValue
    public Quaternion div(Quaternion other) {
        if (other == null)
            throw new NullPointerException("Cannot divide a null.");

        return multiply(other.invert());
    }



    /**
     * Returns a duplicate of this quaternion.
     * 
     * @return duplicate
     */
    @Override
    @CheckReturnValue
    public Quaternion clone() {
        return new Quaternion(scalar, vector);
    }



    /**
     * Checks if this <code>Quaternion</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>Quaternion</code> to check equality
     * @return whether the two <code>Quaternion</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Quaternion other = (Quaternion) obj;
        if (Double.doubleToLongBits(scalar) != Double.doubleToLongBits(other.scalar))
            return false;
        if (!vector.equals(other.vector))
            return false;

        return true;
    }



    /**
     * Checks if this <code>Quaternion</code> is almost equal to the specified
     * one with the given precision.
     * 
     * @param obj
     *            other <code>Quaternion</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Quaternion</code> are almost equal
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

        Quaternion other = (Quaternion) obj;
        if (abs(scalar - other.scalar) > delta)
            return false;
        if (!(vector.equals(other.vector, precision)))
            return false;

        return true;
    }



    /**
     * Returns the first coordinate of this quaternion.
     * 
     * @return value of the first coordinate
     */
    @Attribute(name = "q0")
    public double getQ0() {
        return scalar;
    }



    /**
     * Returns the second coordinate of this quaternion.
     * 
     * @return value of the second coordinate
     */
    @Attribute(name = "q1")
    public double getQ1() {
        return vector.get(0);
    }



    /**
     * Returns the third coordinate of this quaternion.
     * 
     * @return value of the third coordinate
     */
    @Attribute(name = "q2")
    public double getQ2() {
        return vector.get(1);
    }



    /**
     * Returns the fourth coordinate of this quaternion.
     * 
     * @return value of the fourth coordinate
     */
    @Attribute(name = "q3")
    public double getQ3() {
        return vector.get(2);
    }



    /**
     * Returns the hash code for this <code>Quaternion</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(scalar);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + vector.hashCode();
        return result;
    }



    /**
     * Returns the hash code for this <code>Quaternion</code> limiting the
     * precision of the quaternion's values. It is equivalent to
     * {@link Quaternion#equals(Quaternion, double)}.
     * 
     * @param precision
     *            level of precision of the quaternion's value
     * @return limited hash code
     * @throws IllegalArgumentException
     *             if the precision if less than 0.0
     */
    public int hashCode(double precision) {
        if (precision <= 0.0)
            throw new IllegalArgumentException(
                    "Precision has to be greater than 0.0.");

        double multiplier = 1.0 / precision;

        final int prime = 31;
        int result = 1;

        long temp;
        temp = (long) (getQ0() * multiplier);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = (long) (getQ1() * multiplier);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = (long) (getQ2() * multiplier);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = (long) (getQ3() * multiplier);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from inverting this
     * quaternion. <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @return inverted quaternion
     */
    @CheckReturnValue
    public Quaternion invert() {
        if (isNormalized())
            return conjugate();
        else
            return conjugate().div(norm());
    }



    /**
     * Checks if this <code>Quaternion</code> is normalized to a precision of
     * 1e-7.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @return <code>true</code> if the quaternion is normalized.
     */
    public boolean isNormalized() {
        return isNormalized(1e-7);
    }



    /**
     * Checks if this <code>Quaternion</code> is normalized meaning that the
     * <code>norm == 1</code>.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @param precision
     *            level of precision
     * @see #norm
     * @return <code>true</code> if the quaternion is normalized.
     */
    public boolean isNormalized(double precision) {
        return (abs(norm() - 1.0) < precision);
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from the subtraction of
     * this quaternion by the specified quaternion. <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @param other
     *            other quaternion
     * @return resultant quaternion
     * @throws NullPointerException
     *             if the other quaternion is null
     */
    @CheckReturnValue
    public Quaternion minus(Quaternion other) {
        if (other == null)
            throw new NullPointerException("Cannot substract a null.");

        return new Quaternion(scalar - other.scalar, vector.v[0]
                - other.vector.v[0], vector.v[1] - other.vector.v[1],
                vector.v[2] - other.vector.v[2]);
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from the multiplication
     * of this quaternion by a scalar value.
     * 
     * @param value
     *            scalar
     * @return resultant quaternion
     */
    @CheckReturnValue
    public Quaternion multiply(double value) {
        double scalar = this.scalar * value;
        Vector3D vector = this.vector.multiply(value);

        Quaternion q = new Quaternion(scalar, vector);
        return q;
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from the multiplication
     * of this quaternion by the specified one. Multiplication of quaternions is
     * not commutative.
     * <p/>
     * <code>q1.multiply(q2) != q2.multiply(q1)</code>
     * 
     * @param other
     *            other quaternion
     * @return resultant quaternion
     */
    @CheckReturnValue
    public Quaternion multiply(Quaternion other) {
        return multiply(other, true);
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from the multiplication
     * of this quaternion by the specified one. Multiplication of quaternions is
     * not commutative. The quaternion is only force to be positive if the
     * <code>positive</code> argument is equal to <code>true</code>.
     * <p/>
     * <code>q1.multiply(q2) != q2.multiply(q1)</code>
     * 
     * @param other
     *            other quaternion
     * @param positive
     *            if <code>true</code> the quaternion is force to be positive
     * @return resultant quaternion
     */
    @CheckReturnValue
    protected Quaternion multiply(Quaternion other, boolean positive) {
        if (other == null)
            throw new NullPointerException("Cannot multiply a null.");

        double scalar = this.scalar * other.scalar - vector.dot(other.vector);

        Vector3D v3 = vector.cross(other.vector);

        Vector3D vector = new Vector3D();
        for (int i = 0; i < 3; i++)
            vector.v[i] =
                    this.scalar * other.vector.v[i] + other.scalar
                            * this.vector.v[i] + v3.v[i];

        return new Quaternion(scalar, vector, positive);
    }



    /**
     * Returns the norm of this <code>Quaternion</code>. <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * <li><a href="http://www.euclideansplace.com">Martin Baker (2008)
     * Euclidean Space</a></li>
     * </ul>
     * 
     * @return norm
     */
    public double norm() {
        double[] v = vector.v; // For clarity

        return sqrt(scalar * scalar + v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }



    /**
     * Returns a new <code>Quaternion</code> resulting from normalizing this
     * quaternion. The norm of the new quaternion is equal to 1.0.
     * <b>References:</b>
     * <ul>
     * <li>"<a
     * href="http://cpprogramming.com/tutorial/3d/quaternions.html">Confuted
     * (2008) Rotations in Three Dimensions, Part V: Quaternions</a></li>
     * </ul>
     * 
     * @return resultant quaternion
     */
    @CheckReturnValue
    public Quaternion normalize() {
        double norm = norm();
        return div(norm);
    }



    /**
     * Return a new <code>Quaternion</code> resulting from the addition of this
     * quaternion with the specified one. <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @param other
     *            other quaternion
     * @return resultant quaternion
     */
    @CheckReturnValue
    public Quaternion plus(Quaternion other) {
        if (other == null)
            throw new NullPointerException("Cannot add a null.");

        return new Quaternion(scalar + other.scalar, vector.v[0]
                + other.vector.v[0], vector.v[1] + other.vector.v[1],
                vector.v[2] + other.vector.v[2]);
    }



    /**
     * Returns the 4 coefficients of this <code>Quaternion</code> as an array.
     * 
     * @return an array
     */
    public double[] toArray() {
        double[] array = new double[4];
        array[0] = scalar;
        System.arraycopy(vector.v, 0, array, 1, vector.v.length);
        return array;
    }



    /**
     * Returns the axis angle representation of this quaternion.
     * <b>References:</b>
     * <ul>
     * <li><a href="http://www.euclideansplace.com">Martin Baker (2008)
     * Euclidean Space</a></li>
     * </ul>
     * 
     * @return equivalent axis angle
     */
    public AxisAngle toAxisAngle() {
        Quaternion qcalc = normalize();

        double phi = 2.0 * acos(qcalc.scalar);

        double denominator = sqrt(1 - qcalc.scalar * qcalc.scalar);

        Vector3D n = new Vector3D();
        for (int i = 0; i < 3; i++)
            if (abs(denominator) < 1e-7)
                return new AxisAngle(0.0, 1, 0, 0);
            else
                n.v[i] = vector.v[i] / denominator;

        return new AxisAngle(phi, n);
    }



    /**
     * Returns the Euler angles for this quaternion. <b>References:</b>
     * <ul>
     * <li>"Altmann, Simon (1986) Rotations, Quaternions, and Double Groups"</li>
     * </ul>
     * 
     * @return equivalent eulers
     */
    public Eulers toEuler() {
        Quaternion qcalc = normalize();

        double q0 = qcalc.scalar;
        double q1 = qcalc.vector.v[0];
        double q2 = qcalc.vector.v[1];
        double q3 = qcalc.vector.v[2];

        double x = (q0 * q0 + q3 * q3) * (q1 * q1 + q2 * q2);

        double theta1 = 0;
        double theta2 = 0;
        double theta3 = 0;

        if (abs(x) < 1e-14) {
            if (abs(q1) < 1e-7 && abs(q2) < 1e-7) {
                theta1 = atan2(2 * q0 * q3, q0 * q0 - q3 * q3);
                theta2 = 0;
                theta3 = 0;
            } else if (abs(q0) < 1e-7 && abs(q3) < 1e-7) {
                theta1 = atan2(2 * q1 * q2, q1 * q1 - q2 * q2);
                theta2 = PI;
                theta3 = 0;
            }
        } else {
            theta1 = atan2(q3, q0) + atan2(q2, q1);
            theta2 = acos(1 - 2 * q1 * q1 - 2 * q2 * q2);
            theta3 = atan2(q3, q0) - atan2(q2, q1);
        }

        return new Eulers(theta1, theta2, theta3);
    }



    /**
     * Returns the SO3 matrix for this quaternion. <b>References:</b>
     * <ul>
     * <li><a href="http://www.euclideansplace.com">Martin Baker (2008)
     * Euclidean Space</a></li>
     * </ul>
     * 
     * @return equivalent matrix
     */
    public Matrix3D toSO3matrix() {
        Quaternion qcalc = normalize();

        double q0 = qcalc.scalar;
        double q1 = qcalc.vector.v[0];
        double q2 = qcalc.vector.v[1];
        double q3 = qcalc.vector.v[2];

        // Orilib
        double m11 = q0 * q0 + q1 * q1 - q2 * q2 - q3 * q3;
        double m12 = 2 * (q1 * q2 + q0 * q3);
        double m13 = 2 * (q1 * q3 - q0 * q2);
        double m21 = 2 * (q1 * q2 - q0 * q3);
        double m22 = q0 * q0 - q1 * q1 + q2 * q2 - q3 * q3;
        double m23 = 2 * (q2 * q3 + q0 * q1);
        double m31 = 2 * (q1 * q3 + q0 * q2);
        double m32 = 2 * (q2 * q3 - q0 * q1);
        double m33 = q0 * q0 - q1 * q1 - q2 * q2 + q3 * q3;

        Matrix3D m = new Matrix3D(m11, m12, m13, m21, m22, m23, m31, m32, m33);
        assert m.isSpecialOrthogonal();
        return m;
    }



    /**
     * Returns a representation of this <code>Quaternion</code> , suitable for
     * debugging.
     * 
     * @return information about the <code>Quaternion</code>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);
        str.append("[[");
        str.append(scalar);
        for (int n = 0; n < 3; n++) {
            str.append("; ");
            str.append(vector.v[n]);
        }
        str.append("]]");

        return str.toString();
    }

}
