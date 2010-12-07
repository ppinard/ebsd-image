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
package org.ebsdimage.core.exp.ops.detection.pre;

import static java.lang.Math.abs;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.Edit;
import rmlimage.core.ROI;
import rmlimage.core.Transform;

/**
 * Operation to increase the theta range of the Hough by copying the left side
 * of the Hough to the right side. The left side appears as a mirror on the
 * right side of the Hough map. The result is a expanded Hough ranging from 0
 * deg to 180 deg + the increment.
 * 
 * @author Philippe T. Pinard
 */
public class ThetaExpand extends DetectionPreOps {

    /** Angular increment (in radians) to the Hough theta range. */
    @Attribute(name = "increment")
    public final double increment;

    /** Default operation. */
    public static final ThetaExpand DEFAULT =
            new ThetaExpand(Math.toRadians(2));



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        ThetaExpand other = (ThetaExpand) obj;
        if (abs(increment - other.increment) > delta)
            return false;

        return true;
    }



    /**
     * Creates a new <code>ThetaExpand</code> with the specified increment.
     * 
     * @param increment
     *            angular increment (in radians). This increment is added to the
     *            theta range of the Hough map.
     */
    public ThetaExpand(@Attribute(name = "increment") double increment) {
        if (increment < 0.0 || increment > Math.PI)
            throw new IllegalArgumentException("Increment (" + increment
                    + ") must be between [0, PI].");

        this.increment = increment;
    }



    /**
     * The process performs the following steps:
     * <ul>
     * <li>Create a new Hough map with a new width equals to the width of the
     * original Hough plus the angular increment</li>
     * <li>Copy old Hough map in new Hough map</li>
     * <li>Select a slice from the left side of the original Hough map</li>
     * <li>Copy and performs a vertical flip of the slice</li>
     * <li>Paste the slice in the new Hough map</li>.
     * </ul> {@inheritDoc}
     */
    @Override
    public HoughMap process(Exp exp, HoughMap srcMap) {
        int incrementWidth =
                (int) Math.floor(increment
                        / srcMap.getDeltaTheta().getValue("rad"));
        int width = srcMap.width;
        int height = srcMap.height;

        // Destination
        int destWidth = width + incrementWidth;
        HoughMap destMap =
                new HoughMap(destWidth, height, srcMap.getDeltaTheta(),
                        srcMap.getDeltaRho());

        // Copy srcMap in destMap
        Edit.copy(srcMap, srcMap.getROI(), destMap, 0, 0);

        // Slice
        ROI roi = new ROI(0, 0, incrementWidth - 1, height - 1);
        ByteMap slice = Edit.crop(srcMap, roi);
        Transform.verticalFlip(slice);

        Edit.copy(slice, slice.getROI(), destMap, width, 0);

        return destMap;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(increment);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        ThetaExpand other = (ThetaExpand) obj;
        if (Double.doubleToLongBits(increment) != Double.doubleToLongBits(other.increment))
            return false;

        return true;
    }



    @Override
    public String toString() {
        return "ThetaExpand [increment=" + Math.toDegrees(increment) + " deg]";
    }
}
