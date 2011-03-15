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
package org.ebsdimage.core.exp.ops.pattern.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.MapStats;
import rmlimage.core.ROI;
import rmlimage.module.real.core.RealMap;

/**
 * Result operation to calculate the total intensity of a region of the original
 * diffraction pattern. The region is selected using a rectangular area.
 * 
 * @author Philippe T. Pinard
 */
public class Sum extends PatternResultsOps {

    /** Default operation. */
    public static final Sum DEFAULT = new Sum(0.0, 1.0, 0.0, 1.0);

    /** Lower x limit as a fraction of the image width. */
    @Attribute(name = "xmin")
    public final double xmin;

    /** Upper x limit as a fraction of the image width. */
    @Attribute(name = "xmax")
    public final double xmax;

    /** Lower y limit as a fraction of the image height. */
    @Attribute(name = "ymin")
    public final double ymin;

    /** Upper y limit as a fraction of the image height. */
    @Attribute(name = "ymax")
    public final double ymax;



    /**
     * Creates a new <code>Sum</code> with the specified values.
     * 
     * @param xmin
     *            lower x limit as a fraction of the image width
     * @param xmax
     *            upper x limit as a fraction of the image width
     * @param ymin
     *            lower y limit as a fraction of the image height
     * @param ymax
     *            upper y limit as a fraction of the image height
     * @throws IllegalArgumentException
     *             if the value of one of the parameter is outside [0, 1]
     * @throws IllegalArgumentException
     *             if the lower limit is greater than the upper limit
     */
    public Sum(@Attribute(name = "xmin") double xmin,
            @Attribute(name = "ymin") double ymin,
            @Attribute(name = "xmax") double xmax,
            @Attribute(name = "ymax") double ymax) {
        if (xmin < 0 || xmin > 1)
            throw new IllegalArgumentException(
                    "Lower x limit must be between [0,1].");
        if (xmax < 0 || xmax > 1)
            throw new IllegalArgumentException(
                    "Upper x limit must be between [0,1].");
        if (ymin < 0 || ymin > 1)
            throw new IllegalArgumentException(
                    "Lower y limit must be between [0,1].");
        if (ymax < 0 || ymax > 1)
            throw new IllegalArgumentException(
                    "Upper y limit must be between [0,1].");

        if (xmin > xmax)
            throw new IllegalArgumentException(
                    "Lower x limit cannot be greater than the upper x limit.");
        if (ymin > ymax)
            throw new IllegalArgumentException(
                    "Lower y limit cannot be greater than the upper y limit.");

        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }



    /**
     * Calculate the total intensity of the specified region of interest. The
     * original source diffraction pattern is used.
     * 
     * @param exp
     *            experiment executing this operation
     * @param srcMap
     *            pattern map
     * @return a result named "AverageRegion"
     */
    @Override
    public OpResult[] calculate(Exp exp, ByteMap srcMap) {
        String name =
                "Pattern Sum" + "[(" + xmin + "," + ymin + ")-(" + xmax + ","
                        + ymax + ")]";

        OpResult result =
                new OpResult(name, calculateSum(srcMap), RealMap.class);

        return new OpResult[] { result };
    }



    /**
     * Calculate the total intensity of the specified region of interest.
     * 
     * @param map
     *            diffraction pattern
     * @return a result named "AverageRegion"
     */
    protected double calculateSum(ByteMap map) {
        int width = map.width;
        int height = map.height;

        ROI roi =
                new ROI((int) (xmin * width), (int) (ymin * height),
                        (int) (xmax * width) - 1, (int) (ymax * height) - 1);

        return MapStats.sum(map, roi);
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        Sum other = (Sum) obj;
        if (Math.abs(xmin - other.xmin) > delta)
            return false;
        if (Math.abs(xmax - other.xmax) > delta)
            return false;
        if (Math.abs(ymin - other.ymin) > delta)
            return false;
        if (Math.abs(ymax - other.ymax) > delta)
            return false;

        return true;
    }



    @Override
    public String toString() {
        return "Sum [xmin=" + xmin + ", ymin=" + ymin + ", xmax=" + xmax
                + ", ymax=" + ymax + "]";
    }
}
