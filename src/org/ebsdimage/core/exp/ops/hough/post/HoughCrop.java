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
package org.ebsdimage.core.exp.ops.hough.post;

import org.ebsdimage.core.Edit;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.MaskDisc;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import static java.lang.Math.min;

/**
 * Operation to crop the Hough map to a specified radius.
 * 
 * @author Philippe T. Pinard
 */
public class HoughCrop extends HoughPostOps {

    /** Radius cropping limit. */
    @Attribute(name = "radius")
    public final int radius;

    /** Default operation. */
    public static final HoughCrop DEFAULT = new HoughCrop(-1);



    /**
     * Creates a new Hough crop operation sing the specified radius. If the
     * radius is negative, the radius will be automatically calculated as the
     * radius of the largest circle that can be inscribed in the source map.
     * 
     * @param radius
     *            radius cropping limit
     */
    public HoughCrop(@Attribute(name = "radius") int radius) {
        // No restriction on radius since it can be negative to force the
        // auto-computation.
        this.radius = radius;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        HoughCrop other = (HoughCrop) obj;
        if (radius != other.radius)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + radius;
        return result;
    }



    /**
     * Crops the source map to the specified radius. If the radius is negative,
     * the class tries to find the property "Crop.radius" to find the cropping
     * radius. If the property cannot be found, the radius is recalculated to be
     * half of the hough map's height.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input Hough map
     * @return output Hough map
     * @see Edit#crop(HoughMap, double)
     */
    @Override
    public HoughMap process(Exp exp, HoughMap srcMap) {
        ByteMap patternMap = exp.getCurrentPatternMap();

        // Set default if needed
        double radius = this.radius;
        if (radius < 0) {
            // The radius is either the radius read from the property of the
            // pattern map or the minimum between half the pattern map's height
            // or half the pattern map's width
            radius =
                    patternMap.getProperty(MaskDisc.KEY_RADIUS,
                            min(patternMap.width / 2, patternMap.height / 2));

            System.out.println(radius);

            // Decrease the radius by how much its value is negative
            // e.g radius = -1 => radius = maskdisc.radius
            // e.g. radius = -2 => radius = maskdisc.radius - 1
            // etc.
            radius += this.radius + 1;
        }

        System.out.println(radius);

        // Adjust radius value to be in deltaRho units
        radius *= patternMap.getCalibration().dx; // dx == dy

        System.out.println(radius);

        HoughMap destMap = Edit.crop(srcMap, radius);

        return destMap;
    }



    @Override
    public String toString() {
        return "Hough Crop [radius=" + radius + " px]";
    }

}
