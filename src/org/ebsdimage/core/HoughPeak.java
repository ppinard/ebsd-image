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
import junittools.core.AlmostEquable;
import magnitude.core.Magnitude;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Peak in the Hough Transform.
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class HoughPeak implements AlmostEquable {

    /** Rho coordinate. */
    @Element(name = "rho")
    @NonNull
    public final Magnitude rho;

    /** Theta coordinate. */
    @Element(name = "theta")
    @NonNull
    public final Magnitude theta;

    /** Intensity of the peak. */
    @Element(name = "intensity")
    @NonNull
    public final double intensity;



    /**
     * Creates a new Hough peak.
     * 
     * @param theta
     *            value of the theta coordinate
     * @param rho
     *            value of the rho coordinate
     * @param intensity
     *            value for the intensity of the peak
     * @throws IllegalArgumentException
     *             if rho or theta are NaN
     * @throws IllegalArgumentException
     *             if rho, theta or the intensity are infinite
     * @throws IllegalArgumentException
     *             if theta is outside [0, PI]
     * @throws NullPointerException
     *             if rho, theta or intensity are null
     * @throws IllegalArgumentException
     *             if the units of theta can not be expressed as radians
     */
    public HoughPeak(@Element(name = "theta") Magnitude theta,
            @Element(name = "rho") Magnitude rho,
            @Element(name = "intensity") double intensity) {
        if (rho == null)
            throw new NullPointerException("Rho cannot be null.");
        if (theta == null)
            throw new NullPointerException("Theta cannot be null.");

        if (rho.isNaN())
            throw new IllegalArgumentException("Rho cannot be NaN.");
        if (theta.isNaN())
            throw new IllegalArgumentException("Theta cannot be NaN.");

        if (rho.isInfinite())
            throw new IllegalArgumentException("Rho cannot be infinite.");
        if (theta.isInfinite())
            throw new IllegalArgumentException("Theta cannot be infinite.");
        if (Double.isInfinite(intensity))
            throw new IllegalArgumentException("Intensity cannot be infinite.");

        if (!theta.areUnits("rad"))
            throw new IllegalArgumentException("The units of theta ("
                    + theta.getBaseUnitsLabel()
                    + ") cannot be expressed as radians.");

        Magnitude piMag = new Magnitude(PI, "rad");

        // Bring theta within the [0,PI[ interval
        this.theta = theta.modulo(new Magnitude(PI, "rad"));

        if (this.theta.getBaseUnitsValue() < 0
                || this.theta.compareTo(piMag) >= 0)
            throw new IllegalArgumentException("Theta (" + this.theta
                    + ") must be between [0,PI[");

        // Adjust rho based on the location of theta
        // factor = -1 ^ (theta / PI)
        double factor = Math.pow(-1, (int) (theta.div(piMag).getValue("")));
        this.rho = rho.multiply(factor);

        this.intensity = intensity;
    }



    // /**
    // * Creates a new Hough peak.
    // *
    // * @param theta
    // * value of the theta coordinate in radians
    // * @param rho
    // * value of the rho coordinate in pixel
    // * @param intensity
    // * value for the intensity of the peak
    // * @throws IllegalArgumentException
    // * if rho or theta are NaN
    // * @throws IllegalArgumentException
    // * if rho, theta or the intensity are infinite
    // * @throws IllegalArgumentException
    // * if theta is outside [0, PI]
    // * @throws NullPointerException
    // * if rho, theta or intensity are null
    // * @throws IllegalArgumentException
    // * if the units of theta can not be expressed as radians
    // * @throws IllegalArgumentException
    // * if the intensity is not dimensionless
    // */
    // public HoughPeak(double theta, double rho, double intensity) {
    // this(new Magnitude(theta, "rad"), new Magnitude(rho, "px"), intensity);
    // }

    /**
     * Creates a new Hough peak. The intensity is set to NaN.
     * 
     * @param rho
     *            value of the rho coordinate
     * @param theta
     *            value of the theta coordinate
     * @throws IllegalArgumentException
     *             if rho or theta are NaN
     * @throws IllegalArgumentException
     *             if rho or theta are infinite
     * @throws IllegalArgumentException
     *             if theta is outside [0, PI]
     */
    public HoughPeak(Magnitude theta, Magnitude rho) {
        this(theta, rho, Double.NaN);
    }



    /**
     * Checks if this <code>HoughPeak</code> is almost equal to the specified
     * one with the given precision.
     * 
     * @param obj
     *            other <code>HoughPeak</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>HoughPeak</code> are almost equal
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

        HoughPeak other = (HoughPeak) obj;
        if (!rho.equals(other.rho, precision))
            return false;
        if (!theta.equals(other.theta, precision))
            return false;
        if (Math.abs(intensity - other.intensity) > delta)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        HoughPeak other = (HoughPeak) obj;
        if (Double.doubleToLongBits(intensity) != Double.doubleToLongBits(other.intensity))
            return false;
        if (!rho.equals(other.rho))
            return false;
        if (!theta.equals(other.theta))
            return false;

        return true;
    }



    /**
     * Checks if this <code>HoughPeak</code> is at the same position as the
     * specified one with the given precision. In other words, this method is
     * equivalent to {@link #equals(Object, double)} expected that the intensity
     * of the two <code>HoughPeal</code> does not need to be equal.
     * 
     * @param other
     *            other <code>HoughPeak</code> to check equality
     * @param precisionRho
     *            level of precision in rho
     * @param precisionTheta
     *            level of precision in theta
     * @return whether the two <code>HoughPeak</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equivalent(HoughPeak other, Magnitude precisionRho,
            Magnitude precisionTheta) {
        if (precisionRho.getBaseUnitsValue() < 0)
            throw new IllegalArgumentException(
                    "The precision in rho has to be greater or equal to 0.0.");
        if (precisionRho.isNaN())
            throw new IllegalArgumentException(
                    "The precision in rho must be a number.");
        if (precisionTheta.getBaseUnitsValue() < 0)
            throw new IllegalArgumentException(
                    "The precision in theta has to be greater or equal to 0.0.");
        if (precisionTheta.isNaN())
            throw new IllegalArgumentException(
                    "The precision in theta must be a number.");

        if (this == other)
            return true;
        if (other == null)
            return false;

        if (!rho.equals(other.rho, precisionRho))
            return false;
        if (!theta.equals(other.theta, precisionTheta))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(intensity);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + rho.hashCode();
        result = prime * result + theta.hashCode();
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
        String str = "(" + theta.toString(2, "deg") + ", " + rho + ")";

        if (!Double.isNaN(intensity))
            str += ": " + intensity;

        return str;
    }

}
