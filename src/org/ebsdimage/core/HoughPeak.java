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
package org.ebsdimage.core;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import net.jcip.annotations.Immutable;
import ptpshared.utility.xml.ObjectXml;

/**
 * Peak in the Hough Transform.
 * 
 * @author Philippe T. Pinard
 * 
 */
@Immutable
public class HoughPeak implements ObjectXml {

    /** Rho coordinate. */
    public final double rho;

    /** Theta coordinate. */
    public final double theta;

    /** Intensity of the peak. */
    public final double intensity;



    /**
     * Creates a new Hough peak.
     * 
     * @param rho
     *            value of the rho coordinate
     * @param theta
     *            value of the theta coordinate
     * @param intensity
     *            value for the intensity of the peak
     * 
     * @throws IllegalArgumentException
     *             if rho or theta are NaN
     * @throws IllegalArgumentException
     *             if rho, theta or the intensity are infinite
     * @throws IllegalArgumentException
     *             if theta is outside [0, PI]
     */
    public HoughPeak(double rho, double theta, double intensity) {
        if (Double.isNaN(rho))
            throw new IllegalArgumentException("Rho cannot be NaN.");
        if (Double.isNaN(theta))
            throw new IllegalArgumentException("Rho cannot be NaN.");

        if (Double.isInfinite(rho))
            throw new IllegalArgumentException("Rho cannot be infinite.");
        if (Double.isInfinite(theta))
            throw new IllegalArgumentException("Rho cannot be infinite.");
        if (Double.isInfinite(intensity))
            throw new IllegalArgumentException("Rho cannot be infinite.");

        if (theta < 0 || theta > PI)
            throw new IllegalArgumentException("Theta (" + theta
                    + ") must be between [0,PI[");

        this.rho = rho;
        this.theta = theta;
        this.intensity = intensity;
    }



    /**
     * Creates a new Hough peak. The intensity is set to NaN.
     * 
     * @param rho
     *            value of the rho coordinate
     * @param theta
     *            value of the theta coordinate
     * 
     * @throws IllegalArgumentException
     *             if rho or theta are NaN
     * @throws IllegalArgumentException
     *             if rho or theta are infinite
     * @throws IllegalArgumentException
     *             if theta is outside [0, PI]
     */
    public HoughPeak(double rho, double theta) {
        this(rho, theta, Double.NaN);
    }



    /**
     * Checks if this <code>HoughPeak</code> is almost equal to the specified
     * one with the given precision.
     * 
     * @param other
     *            other <code>HoughPeak</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>HoughPeak</code> are almost equal
     * 
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(HoughPeak other, double precision) {
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

        if (abs(rho - other.rho) >= precision)
            return false;
        if (abs(theta - other.theta) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>HoughPeak</code> is exactly equal to the specified
     * one.
     * 
     * @param obj
     *            other <code>HoughPeak</code> to check equality
     * 
     * @return whether the two <code>HoughPeak</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        HoughPeak other = (HoughPeak) obj;

        if (Double.doubleToLongBits(rho) != Double.doubleToLongBits(other.rho))
            return false;
        if (Double.doubleToLongBits(theta) != Double
                .doubleToLongBits(other.theta))
            return false;

        return true;
    }



    /**
     * Hash code of this <code>HoughPeak</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(rho);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(theta);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Returns a representation of this <code>HoughPeak</code>, suitable for
     * debugging.
     * 
     * @return information about this <code>HoughPeak</code>
     */
    @Override
    public String toString() {
        return "(" + rho + ", " + theta + ")";
    }

}
