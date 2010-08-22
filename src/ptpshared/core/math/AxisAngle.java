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

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;
import net.jcip.annotations.Immutable;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Represents a rotation using an angle and an axis. A rotation between two
 * frames can be represented by an axis (a unit 3D vector) and an rotation angle
 * around that axis. By definition, the rotation axis cannot be a zero vector
 * and the rotation angle must be between [0, 2PI[. For a rotation angle of 0,
 * any rotation axis are technically possible.
 * <p/>
 * The axis and angle can be accessed via the public final variables
 * <code>axis</code> and <code>angle</code> respectively.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class AxisAngle {

    /** Axis of rotation. */
    @NonNull
    public final Vector3D axis;

    /** Rotation angle (in radians). */
    public final double angle;



    /**
     * Creates a new <code>AxisAngle</code> from an angle and the coordinates of
     * the rotation axis.
     * 
     * @param angle
     *            angle of rotation (in radians)
     * @param x
     *            x coordinate of the rotation axis
     * @param y
     *            y coordinate of the rotation axis
     * @param z
     *            z coordinate of the rotation axis
     */
    public AxisAngle(double angle, double x, double y, double z) {
        this(angle, new Vector3D(x, y, z));
    }



    /**
     * Creates a new <code>AxisAngle</code> from an angle and the rotation axis
     * vector.
     * 
     * @param angle
     *            angle of rotation (in radians)
     * @param axis
     *            axis of rotation
     * @throws NullPointerException
     *             axis cannot be null
     * @throws IllegalArgumentException
     *             axis cannot be a zero vector
     * @throws IllegalArgumentException
     *             angle must be between [0, 2PI[.
     */
    public AxisAngle(double angle, Vector3D axis) {
        if (axis == null)
            throw new NullPointerException("Axis cannot be null");
        if (axis.norm() == 0)
            throw new IllegalArgumentException("Axis cannot be a zero vector");
        if (angle < 0 || angle > 2 * PI)
            throw new IllegalArgumentException(
                    "Angle has to be between [0, 2PI[");

        this.axis = axis.normalize();
        this.angle = angle;
    }



    /**
     * Returns a copy.
     * 
     * @return copy of this <code>AxisAngle</code>.
     */
    public AxisAngle duplicate() {
        return new AxisAngle(angle, axis);
    }



    /**
     * Checks if this <code>AxisAngle</code> is almost equal to the specified
     * one with the given precision.
     * 
     * @param other
     *            other <code>AxisAngle</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>AxisAngle</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(AxisAngle other, double precision) {
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

        if (abs(angle - other.angle) >= precision)
            return false;
        if (!(axis.equals(other.axis, precision)))
            return false;

        return true;
    }



    /**
     * Checks if this <code>AxisAnle</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>AxisAnle</code> to check equality
     * @return whether the two <code>AxisAnle</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AxisAngle other = (AxisAngle) obj;
        if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle))
            return false;
        if (!axis.equals(other.axis))
            return false;
        return true;
    }



    /**
     * Returns the hash code for this <code>AxisAngle</code>.
     * 
     * @return hash code.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(angle);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + axis.hashCode();
        return result;
    }



    /**
     * Returns a representation of this <code>AxisAngle</code>, suitable for
     * debugging.
     * 
     * @return information about the <code>AxisAngle</code>.
     */
    @Override
    public String toString() {
        return "AxisAngle [angle=" + toDegrees(angle) + ", axis=" + axis + "]";
    }
}
