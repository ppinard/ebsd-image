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
import static java.lang.Math.toDegrees;
import ptpshared.core.math.Vector3D;
import ptpshared.core.math.Vector3DMath;

/**
 * Pair of Hough peaks.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughPeakPair extends Pair {

    /** First Hough peak. */
    public final HoughPeak peak0;

    /** Second Hough peak. */
    public final HoughPeak peak1;



    /**
     * Creates a new <code>HoughPeakPair</code> from two Hough peaks and their
     * respective normals. The direction cosine is automatically calculated from
     * the specified normals.
     * 
     * @param peak0
     *            first Hough peak
     * @param normal0
     *            normal of the first Hough peak
     * @param peak1
     *            second Hough peak
     * @param normal1
     *            normal of the second Hough peak
     * 
     * @throws NullPointerException
     *             if a normal is null
     * @throws NullPointerException
     *             if a Hough peak is null
     * @throws IllegalArgumentException
     *             if the direction cosine is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is infinite
     */
    protected HoughPeakPair(HoughPeak peak0, Vector3D normal0, HoughPeak peak1,
            Vector3D normal1) {
        super(normal0, normal1, abs(Vector3DMath.directionCosine(normal0,
                normal1)));

        if (peak0 == null)
            throw new NullPointerException(
                    "The first Hough peak cannot be null.");
        if (peak1 == null)
            throw new NullPointerException(
                    "The second Hough peak cannot be null.");

        this.peak0 = peak0;
        this.peak1 = peak1;
    }



    /**
     * Returns a representation of this pair (two Hough peaks and direction
     * cosine), suitable for debugging.
     * 
     * @return info about this pair
     */
    @Override
    public String toString() {
        return peak0.toString() + "\t" + peak1.toString() + "\t"
                + toDegrees(Vector3DMath.angle(directionCosine)) + " deg";
    }

}
