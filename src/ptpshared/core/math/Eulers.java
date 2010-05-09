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
import net.jcip.annotations.Immutable;
import ptpshared.utility.xml.ObjectXml;

/**
 * Represents a rotation using set of 3 Euler angles (in radians) as defined by
 * the Bunge convention. The Bunge convention stipulates:
 * <ul>
 * <li>First rotation around the z-axis</li>
 * <li>Second rotation around the z-axis</li>
 * <li>Third rotation around the new z-axis</li>
 * </ul>
 * 
 * Also by convention, the angle of rotation for the first and third angle are
 * between ]-PI,PI] and the second angle between [0, PI[.
 * <p/>
 * <b>References:</b>
 * <ul>
 * <li>Martin Baker</li>
 * <li>Wikipedia</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Eulers implements ObjectXml {

    /**
     * Convert the first theta angle to be between ]-PI, PI], following the
     * Bunge convention.
     * 
     * @param theta1
     *            first Euler angle (in radians)
     * @return valid angle
     */
    private static double theta1ToBunge(double theta1) {
        while (theta1 > PI)
            theta1 -= 2 * PI;
        while (theta1 <= -PI)
            theta1 += 2 * PI;
        return theta1;
    }



    /**
     * Verify that theta2 is between [0, PI], following the Bunge convention.
     * 
     * @param theta2
     *            second Euler angle (in radians)
     * @return valid angle
     */
    private static double theta2ToBunge(double theta2) {
        if (theta2 < 0 || theta2 > PI)
            throw new IllegalArgumentException("Theta 2 (" + theta2
                    + ") is outside " + "the range (0 <= theta2 <= pi)");

        return theta2;
    }



    /**
     * Convert the third theta angle to be between ]-PI, PI], following the
     * Bunge convention.
     * 
     * @param theta3
     *            third Euler angle (in radians)
     * @return valid angle
     */
    private static double theta3ToBunge(double theta3) {
        return theta1ToBunge(theta3);
    }



    /** First rotation angle about the x-axis (in radians). */
    public final double theta1;

    /** Rotation angle about the z-axis (in radians). */
    public final double theta2;

    /** Second rotation angle about the x-axis (in radians). */
    public final double theta3;



    /**
     * Creates a new <code>Eulers</code> from three Euler angles. To be valid:
     * <ul>
     * <li>-PI < theta1 <= PI</li>
     * <li>0 <= theta2 <= PI</li>
     * <li>-PI < theta3 <= PI</li>
     * </ul>
     * 
     * @param theta1
     *            first rotation about the x-axis (in radians)
     * @param theta2
     *            rotation about the z-axis (in radians)
     * @param theta3
     *            second rotation about the x-axis (in radians)
     */
    public Eulers(double theta1, double theta2, double theta3) {
        this.theta1 = theta1ToBunge(theta1);
        this.theta2 = theta2ToBunge(theta2);
        this.theta3 = theta3ToBunge(theta3);
    }



    /**
     * Creates a copy.
     * 
     * @return copy of the current <code>Eulers</code>
     */
    public Eulers duplicate() {
        return new Eulers(theta1, theta2, theta3);
    }



    /**
     * Checks if this <code>Eulers</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param other
     *            other <code>Eulers</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Eulers</code> are almost equal
     * 
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(Eulers other, double precision) {
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

        if (abs(theta1 - other.theta1) >= precision)
            return false;
        if (abs(theta2 - other.theta2) >= precision)
            return false;
        if (abs(theta3 - other.theta3) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>Eulers</code> is exactly equal to the specified one.
     * 
     * @param obj
     *            other <code>Eulers</code> to check equality
     * 
     * @return whether the two <code>Eulers</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Eulers other = (Eulers) obj;
        if (Double.doubleToLongBits(theta1) != Double
                .doubleToLongBits(other.theta1))
            return false;
        if (Double.doubleToLongBits(theta2) != Double
                .doubleToLongBits(other.theta2))
            return false;
        if (Double.doubleToLongBits(theta3) != Double
                .doubleToLongBits(other.theta3))
            return false;
        return true;
    }



    /**
     * Returns the hash code for this <code>Eulers</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(theta1);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(theta2);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(theta3);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Returns a positive <code>Eulers</code>.
     * <p/>
     * Convert eulers from
     * <ul>
     * <li>-PI < theta1 < PI</li>
     * <li>0 < theta2 < PI</li>
     * <li>-PI < theta3 < PI</li>
     * </ul>
     * to
     * <ul>
     * <li>0 < theta1 < 2PI</li>
     * <li>0 < theta2 < PI</li>
     * <li>0 < theta3 < 2PI</li>
     * </ul>
     * 
     * @return an array containing the three Euler angles
     */
    public double[] positive() {
        double theta1 = this.theta1;
        double theta3 = this.theta3;

        while (theta1 < 0)
            theta1 += 2 * PI;

        while (theta3 < 0)
            theta3 += 2 * PI;

        return new double[] { theta1, theta2, theta3 };
    }



    /**
     * Returns the array representation of the <code>Eulers</code>.
     * 
     * @return an array
     */
    public double[] toArray() {
        return new double[] { theta1, theta2, theta3 };
    }



    /**
     * Returns a representation of the <code>Eulers</code>, suitable for
     * debugging.
     * 
     * @return values of the <code>Eulers</code>
     */
    @Override
    public String toString() {
        return "(" + theta1 + ", " + theta2 + ", " + theta3 + ')';
    }



    /**
     * Returns a representation of the Euler angles in degrees.
     * 
     * @return value of the <code>Eulers</code> in degrees
     */
    public String toStringDegs() {
        double theta1deg = java.lang.Math.toDegrees(theta1);
        double theta2deg = java.lang.Math.toDegrees(theta2);
        double theta3deg = java.lang.Math.toDegrees(theta3);

        return "(" + theta1deg + ", " + theta2deg + ", " + theta3deg + ')';
    }

}
