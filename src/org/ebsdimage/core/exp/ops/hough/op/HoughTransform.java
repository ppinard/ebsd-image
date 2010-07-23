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
package org.ebsdimage.core.exp.ops.hough.op;

import static java.lang.Math.PI;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.ByteMap;
import rmlimage.core.Filter;

/**
 * Operation to perform the Hough transform.
 * 
 * @author Philippe T. Pinard
 */
public class HoughTransform extends HoughOp {

    /** Resolution of the Hough transform (in radians). */
    public final double resolution;

    /** Default resolution of the Hough transform (0.5 deg). */
    public static final double DEFAULT_RESOLUTION = 0.5 / 180 * PI;



    /**
     * Creates a new Hough transform operation with the default values.
     */
    public HoughTransform() {
        this(DEFAULT_RESOLUTION);
    }



    /**
     * Creates a new Hough transform operation using the specified resolution.
     * 
     * @param resolution
     *            resolution of the Hough transform (in radians)
     */
    public HoughTransform(double resolution) {
        if (resolution <= 0)
            throw new IllegalArgumentException("Resolution (" + resolution
                    + ") must be > 0");

        this.resolution = resolution;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        HoughTransform other = (HoughTransform) obj;
        if (Double.doubleToLongBits(resolution) != Double.doubleToLongBits(other.resolution))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(resolution);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Performs a Hough transform on the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            pattern map
     * @return Hough map
     * @see Transform#hough(ByteMap, double)
     */
    @Override
    public HoughMap transform(Exp exp, ByteMap srcMap) {
        HoughMap houghMap = Transform.hough(srcMap, resolution);

        // Apply median to remove gap at theta = 90 deg
        Filter.median(houghMap);

        return houghMap;
    }



    @Override
    public String toString() {
        return "Hough Transform [resolution=" + Math.toDegrees(resolution)
                + " deg]";
    }

}
