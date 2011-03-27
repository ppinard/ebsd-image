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
 * Operation to perform the Hough transform. The resolution in rho is
 * automatically calculated from the resolution in theta to ensure that most of
 * the peaks have an aspect ratio close to unity.
 * 
 * @author Philippe T. Pinard
 */
public class AutoHoughTransform extends HoughOp {

    /** Resolution in theta of the Hough transform (in radians/px). */
    @Attribute(name = "deltaTheta")
    public final double deltaTheta;

    /** Default operation. */
    public static final AutoHoughTransform DEFAULT = new AutoHoughTransform(
            Math.toRadians(0.5));



    /**
     * Creates a new Hough transform operation using the specified resolution in
     * theta. The resolution in rho is automatically calculated to ensure that
     * the aspect ratio of the peaks is close to unity (square peaks).
     * 
     * @param deltaTheta
     *            resolution of the Hough transform (in radians)
     * @throws IllegalArgumentException
     *             if the resolution is less or equal to zero
     */
    public AutoHoughTransform(@Attribute(name = "deltaTheta") double deltaTheta) {
        if (deltaTheta <= 0)
            throw new IllegalArgumentException("Resolution (" + deltaTheta
                    + ") must be > 0");

        this.deltaTheta = deltaTheta;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        AutoHoughTransform other = (AutoHoughTransform) obj;
        if (abs(deltaTheta - other.deltaTheta) > delta)
            return false;

        return true;
    }



    @Override
    public String toString() {
        return "Auto Hough Transform [resolution=" + toDegrees(deltaTheta)
                + " deg/px]";
    }



    @Override
    public HoughMap transform(Exp exp, ByteMap srcMap) {
        HoughMap houghMap = Transform.hough(srcMap, deltaTheta);

        // Apply median to remove gap at theta = 90 deg
        Filter.median(houghMap);

        return houghMap;
    }

}
