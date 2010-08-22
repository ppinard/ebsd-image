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

import static java.lang.Math.abs;
import net.jcip.annotations.Immutable;
import ptpshared.utility.xml.ObjectXml;

/**
 * Calibration of the EBSD camera.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Camera implements ObjectXml {

    /** Location of the pattern center in the horizontal direction. */
    public final double patternCenterH;

    /** Location of the pattern center in the vertical direction. */
    public final double patternCenterV;

    /** Distance between the sample and the detector window. */
    public final double detectorDistance;



    /**
     * Creates a new EBSD camera.
     * <p/>
     * A pattern center of <code>(0.0, 0.0)</code> is centered.
     * 
     * @param patternCenterH
     *            location of the pattern center in the horizontal direction
     * @param patternCenterV
     *            location of the pattern center in the vertical direction
     * @param detectorDistance
     *            distance between the sample and the detector window
     * @throws IllegalArgumentException
     *             if a value is not a number (NaN)
     * @throws IllegalArgumentException
     *             if a value is infinite
     * @throws IllegalArgumentException
     *             if the pattern center is out of range
     * @throws IllegalArgumentException
     *             if the detector distance is out of range
     */
    public Camera(double patternCenterH, double patternCenterV,
            double detectorDistance) {
        if (Double.isNaN(patternCenterH))
            throw new IllegalArgumentException(
                    "The horizontal coordinate of the pattern center cannot be NaN.");
        if (Double.isInfinite(patternCenterH))
            throw new IllegalArgumentException(
                    "The horizontal coordinate of the pattern center cannot be infinite.");

        if (Double.isNaN(patternCenterV))
            throw new IllegalArgumentException(
                    "The vertical coordinate of the pattern center cannot be NaN.");
        if (Double.isInfinite(patternCenterV))
            throw new IllegalArgumentException(
                    "The vertical coordinate of the pattern center cannot be infinite.");

        if (Double.isNaN(detectorDistance))
            throw new IllegalArgumentException(
                    "The detector distance cannot be NaN.");
        if (Double.isInfinite(detectorDistance))
            throw new IllegalArgumentException(
                    "The detector distance cannot be infinite.");

        // Check limits
        if (patternCenterH < -0.5 || patternCenterH > 0.5)
            throw new IllegalArgumentException("The horizontal coordinate ("
                    + patternCenterH
                    + ") of the pattern center must be with the range"
                    + "[-0.5, 0.5].");

        if (patternCenterV < -0.5 || patternCenterV > 0.5)
            throw new IllegalArgumentException("The vertical coordinate ("
                    + patternCenterV
                    + ") of the pattern center must be with the range"
                    + "[-0.5, 0.5].");

        if (detectorDistance < 1e-6)
            throw new IllegalArgumentException("The detector distance ("
                    + detectorDistance + ") must greater than zero.");

        this.patternCenterH = patternCenterH;
        this.patternCenterV = patternCenterV;
        this.detectorDistance = detectorDistance;
    }



    /**
     * Checks if this <code>Camera</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param other
     *            other <code>Camera</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Camera</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(Camera other, double precision) {
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

        if (abs(patternCenterH - other.patternCenterH) >= precision)
            return false;
        if (abs(patternCenterV - other.patternCenterV) >= precision)
            return false;
        if (abs(detectorDistance - other.detectorDistance) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>Camera</code> is exactly equal to the specified one.
     * 
     * @param obj
     *            other <code>Camera</code> to check equality
     * @return whether the two <code>Camera</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Camera other = (Camera) obj;
        if (Double.doubleToLongBits(detectorDistance) != Double.doubleToLongBits(other.detectorDistance))
            return false;
        if (Double.doubleToLongBits(patternCenterH) != Double.doubleToLongBits(other.patternCenterH))
            return false;
        if (Double.doubleToLongBits(patternCenterV) != Double.doubleToLongBits(other.patternCenterV))
            return false;
        return true;
    }



    /**
     * Returns the hash code for this <code>Camera</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(detectorDistance);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(patternCenterH);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(patternCenterV);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Returns a <code>String</code> representation of the <code>Camera</code>,
     * suitable for debugging.
     * 
     * @return information about the <code>Camera</code>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);

        str.append("PCh:" + patternCenterH + " ");
        str.append("PCv:" + patternCenterV + " ");
        str.append("DD:" + detectorDistance);

        return str.toString();
    }

}
