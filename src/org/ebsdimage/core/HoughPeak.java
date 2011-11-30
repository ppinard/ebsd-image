/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import junittools.core.AlmostEquable;
import net.sf.magnitude.core.Magnitude;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
    public final double rho;

    /** Units of the rho coordinate. */
    @Element(name = "rhoUnits")
    public final String rhoUnits;

    /** Theta coordinate. */
    @Element(name = "theta")
    public final double theta;

    /** Intensity of the peak. */
    @Element(name = "intensity")
    public final double intensity;



    /**
     * Creates a new Hough peak. The units of rho are set to be pixels.
     * 
     * @param theta
     *            value of the theta coordinate
     * @param rho
     *            value of the rho coordinate
     * @param intensity
     *            value for the intensity of the peak
     * @throws IllegalArgumentException
     *             if rho, theta or intensity are NaN or infinity
     */
    public HoughPeak(double theta, double rho, double intensity) {
        this(theta, rho, "px", intensity);
    }



    /**
     * Creates a new Hough peak.
     * 
     * @param theta
     *            value of the theta coordinate
     * @param rho
     *            value of the rho coordinate
     * @param rhoUnits
     *            units of the rho coordinate
     * @param intensity
     *            value for the intensity of the peak
     * @throws IllegalArgumentException
     *             if rho, theta or intensity are NaN or infinity
     * @throws NullPointerException
     *             if rhoUnits is null
     */
    public HoughPeak(@Element(name = "theta") double theta,
            @Element(name = "rho") double rho,
            @Element(name = "rhoUnits") String rhoUnits,
            @Element(name = "intensity") double intensity) {
        if (Double.isNaN(rho) || Double.isInfinite(rho))
            throw new IllegalArgumentException("Rho cannot be NaN or infinite.");
        if (Double.isNaN(theta) || Double.isInfinite(theta))
            throw new IllegalArgumentException(
                    "Theta cannot be NaN or infinite.");
        if (rhoUnits == null)
            throw new NullPointerException("Rho units cannot be null.");
        if (Double.isNaN(intensity) || Double.isInfinite(intensity))
            throw new IllegalArgumentException(
                    "Intensity cannot be NaN or infinite.");

        // Bring theta within the [0,PI[ interval
        this.theta = theta % Math.PI;

        // Adjust rho based on the location of theta
        this.rho = rho * Math.pow(-1, (int) (theta / Math.PI));
        this.rhoUnits = rhoUnits;

        this.intensity = intensity;
    }



    /**
     * Creates a new Hough peak. The units of rho are set to be pixels.
     * 
     * @param theta
     *            value of the theta coordinate
     * @param rho
     *            value of the rho coordinate
     * @param intensity
     *            value for the intensity of the peak
     * @throws IllegalArgumentException
     *             if rho, theta or intensity are NaN or infinity
     */
    public HoughPeak(Magnitude theta, Magnitude rho, double intensity) {
        this(theta.getValue("rad"), rho.getPreferredUnitsValue(),
                rho.getPreferredUnitsLabel(), intensity);
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
        if (Math.abs(rho - other.rho) > delta)
            return false;
        if (Math.abs(theta - other.theta) > delta)
            return false;
        if (Math.abs(intensity - other.intensity) > delta)
            return false;
        if (!rhoUnits.equals(other.rhoUnits))
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
    public boolean equivalent(HoughPeak other, Object precisionTheta,
            Object precisionRho) {
        double deltaTheta = ((Number) precisionTheta).doubleValue();
        if (deltaTheta < 0)
            throw new IllegalArgumentException(
                    "The theta precision has to be greater or equal to 0.0.");
        if (Double.isNaN(deltaTheta))
            throw new IllegalArgumentException(
                    "The theta precision must be a number.");

        double deltaRho = ((Number) precisionRho).doubleValue();
        if (deltaRho < 0)
            throw new IllegalArgumentException(
                    "The rho precision has to be greater or equal to 0.0.");
        if (Double.isNaN(deltaRho))
            throw new IllegalArgumentException(
                    "The rho precision must be a number.");

        if (this == other)
            return true;
        if (other == null)
            return false;

        if (rhoUnits != other.rhoUnits)
            return false;
        if (Math.abs(theta - other.theta) > deltaTheta)
            return false;
        if (Math.abs(rho - other.rho) > deltaRho)
            return false;

        return true;
    }



    /**
     * Returns a <code>Magnitude</code> object of the rho coordinate.
     * 
     * @return rho coordinate
     */
    public Magnitude getRho() {
        return new Magnitude(rho, rhoUnits);
    }



    /**
     * Returns a representation of this <code>HoughPeak</code>, suitable for
     * debugging.
     * 
     * @return information about this <code>HoughPeak</code>
     */
    @Override
    public String toString() {
        return "(" + rmlshared.math.Double.format(Math.toDegrees(theta), 2)
                + " deg, " + rmlshared.math.Double.format(rho, 2) + " px): "
                + intensity;
    }

}
