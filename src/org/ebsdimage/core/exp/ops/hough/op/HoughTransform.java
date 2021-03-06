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
package org.ebsdimage.core.exp.ops.hough.op;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.Filter;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

/**
 * Operation to perform the Hough transform.
 * 
 * @author Philippe T. Pinard
 */
public class HoughTransform extends HoughOp {

    /** Resolution in theta of the Hough transform (in radians/px). */
    @Attribute(name = "deltaTheta")
    public final double deltaTheta;

    /** Resolution in rho of the Hough transform (in px/px). */
    @Attribute(name = "deltaRho")
    public final double deltaRho;

    /** Default operation. */
    public static final HoughTransform DEFAULT = new HoughTransform(
            Math.toRadians(0.5), 1.0);



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
    public HoughTransform(@Attribute(name = "deltaTheta") double deltaTheta,
            @Attribute(name = "deltaRho") double deltaRho) {
        if (deltaTheta <= 0)
            throw new IllegalArgumentException("Resolution in theta ("
                    + deltaTheta + ") must be > 0");
        if (deltaRho <= 0)
            throw new IllegalArgumentException("Resolution in rho (" + deltaRho
                    + ") must be > 0");

        this.deltaTheta = deltaTheta;
        this.deltaRho = deltaRho;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        HoughTransform other = (HoughTransform) obj;
        if (abs(deltaTheta - other.deltaTheta) > delta)
            return false;
        if (abs(deltaRho - other.deltaRho) > delta)
            return false;

        return true;
    }



    @Override
    public String toString() {
        return "Hough Transform [deltaTheta=" + toDegrees(deltaTheta)
                + " deg/px, deltaRho=" + deltaRho + " px/px]";
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

}
