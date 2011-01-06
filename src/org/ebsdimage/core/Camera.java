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

import junittools.core.AlmostEquable;
import magnitude.core.Magnitude;
import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Root;

import ptpshared.core.math.Vector3D;

/**
 * Calibration of the EBSD camera.
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class Camera implements AlmostEquable {

    /** Translation of the camera. */
    public final Vector3D t;

    /** Normal of the camera plane. */
    public final Vector3D n;



    /**
     * Creates a new EBSD camera.
     * <p/>
     * A pattern centre of <code>(0.0, 0.0)</code> is centred.
     * 
     * @param patternCenterH
     *            location of the pattern centre in the horizontal direction
     * @param patternCenterV
     *            location of the pattern centre in the vertical direction
     * @param detectorDistance
     *            distance between the sample and the detector window
     * @throws IllegalArgumentException
     *             if a value is not a number (NaN)
     * @throws IllegalArgumentException
     *             if a value is infinite
     */
    public Camera(Magnitude patternCenterH, Magnitude patternCenterV,
            Magnitude detectorDistance) {
        if (Magnitude.isNaN(patternCenterH))
            throw new IllegalArgumentException(
                    "The horizontal coordinate of the pattern center cannot be NaN.");
        if (Magnitude.isInfinite(patternCenterH))
            throw new IllegalArgumentException(
                    "The horizontal coordinate of the pattern center cannot be infinite.");

        if (Magnitude.isNaN(patternCenterV))
            throw new IllegalArgumentException(
                    "The vertical coordinate of the pattern center cannot be NaN.");
        if (Magnitude.isInfinite(patternCenterV))
            throw new IllegalArgumentException(
                    "The vertical coordinate of the pattern center cannot be infinite.");

        if (Magnitude.isNaN(detectorDistance))
            throw new IllegalArgumentException(
                    "The detector distance cannot be NaN.");
        if (Magnitude.isInfinite(detectorDistance))
            throw new IllegalArgumentException(
                    "The detector distance cannot be infinite.");

    }



    /**
     * Checks if this <code>Camera</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param obj
     *            other <code>Camera</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Camera</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    @Override
    public boolean equals(Object obj, Object precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Camera other = (Camera) obj;
        if (!detectorDistance.equals(other.detectorDistance, precision))
            return false;
        if (!patternCenterH.equals(other.patternCenterH, precision))
            return false;
        if (!patternCenterV.equals(other.patternCenterV, precision))
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

        Camera other = (Camera) obj;
        if (!detectorDistance.equals(other.detectorDistance))
            return false;
        if (!patternCenterH.equals(other.patternCenterH))
            return false;
        if (!patternCenterV.equals(other.patternCenterV))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + detectorDistance.hashCode();
        result = prime * result + patternCenterH.hashCode();
        result = prime * result + patternCenterV.hashCode();
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
