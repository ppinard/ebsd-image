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
import static java.lang.Math.toDegrees;

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

    /** Resolution in theta of the Hough transform (in radians/px). */
    public final double deltaTheta;

    /** Default resolution in theta of the Hough transform (0.5 deg). */
    public static final double DEFAULT_DELTA_THETA = 0.5 / 180 * PI;

    /** Resolution in rho of the Hough transform (in px/px). */
    public final double deltaRho;

    /** Default resolution in rho of the Hough transform (1 px/px). */
    public static final double DEFAULT_DELTA_RHO = 1.0;



    /**
     * Creates a new Hough transform operation with the default values.
     */
    public HoughTransform() {
        this(DEFAULT_DELTA_THETA, DEFAULT_DELTA_RHO);
    }



    /**
     * Creates a new Hough transform operation using the specified resolutions.
     * 
     * @param deltaTheta
     *            resolution in theta (radians/px)
     * @param deltaRho
     *            resolution in rho (px/px)
     * @throws IllegalArgumentException
     *             if the theta resolution is less or equal to zero
     * @throws IllegalArgumentException
     *             if the rho resolution is less or equal to zero
     */
    public HoughTransform(double deltaTheta, double deltaRho) {
        if (deltaTheta <= 0)
            throw new IllegalArgumentException("Resolution in theta ("
                    + deltaTheta + ") must be > 0");
        if (deltaRho <= 0)
            throw new IllegalArgumentException("Resolution in rho (" + deltaRho
                    + ") must be > 0");

        this.deltaTheta = deltaTheta;
        this.deltaRho = deltaRho;
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
        HoughMap houghMap = Transform.hough(srcMap, deltaTheta, deltaRho);

        // Apply median to remove gap at theta = 90 deg
        Filter.median(houghMap);

        return houghMap;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(deltaRho);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(deltaTheta);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        HoughTransform other = (HoughTransform) obj;
        if (Double.doubleToLongBits(deltaRho) != Double.doubleToLongBits(other.deltaRho))
            return false;
        if (Double.doubleToLongBits(deltaTheta) != Double.doubleToLongBits(other.deltaTheta))
            return false;

        return true;
    }



    @Override
    public String toString() {
        return "Hough Transform [deltaTheta=" + toDegrees(deltaTheta)
                + " deg/px, deltaRho=" + deltaRho + " px/px]";
    }

}
