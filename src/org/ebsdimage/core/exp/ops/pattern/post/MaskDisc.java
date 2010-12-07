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
package org.ebsdimage.core.exp.ops.pattern.post;

import static java.lang.Math.min;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.MapMath;

/**
 * Operation to apply a disc mask on the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class MaskDisc extends PatternPostOps {

    /** Default operation. */
    public static final MaskDisc DEFAULT = new MaskDisc(-1, -1, -1);

    /** Horizontal position of the centroid of the disc mask. */
    @Attribute(name = "x")
    public final int centroidX;

    /** Vertical position of the centroid of the disc mask. */
    @Attribute(name = "y")
    public final int centroidY;

    /** Radius of the disc mask. */
    @Attribute(name = "r")
    public final int radius;



    /**
     * Creates a new <code>MaskDisc</code> operation from the specified
     * parameters.
     * <p/>
     * If the centroid x is negative, the centroid x position will be
     * automatically calculated as half of the pattern map's width.
     * <p/>
     * If the centroid y is negative, the centroid y position will be
     * automatically calculated as half of the pattern map's height.
     * <p/>
     * If the radius is negative, the radius will be automatically calculated as
     * the radius of the largest circle that can be inscribed in the pattern
     * map.
     * 
     * @param centroidX
     *            horizontal position of the centroid of the disc mask
     * @param centroidY
     *            vertical position of the centroid of the disc mask
     * @param radius
     *            radius of the disc mask
     */
    public MaskDisc(@Attribute(name = "x") int centroidX,
            @Attribute(name = "y") int centroidY,
            @Attribute(name = "r") int radius) {
        // No restriction on arguments since they can be negative to force the
        // auto-computation.
        this.centroidX = centroidX;
        this.centroidY = centroidY;
        this.radius = radius;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        MaskDisc other = (MaskDisc) obj;
        if (centroidX != other.centroidX)
            return false;
        if (centroidY != other.centroidY)
            return false;
        if (radius != other.radius)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        MaskDisc other = (MaskDisc) obj;
        if (Math.abs(centroidX - other.centroidX) > delta)
            return false;
        if (Math.abs(centroidY - other.centroidY) > delta)
            return false;
        if (Math.abs(radius - other.radius) > delta)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + centroidX;
        result = prime * result + centroidY;
        result = prime * result + radius;
        return result;
    }



    /**
     * Creates a mask disc and applies it to the pattern map.
     * <p/>
     * If the centroid x is negative, the centroid x position will be
     * automatically calculated as half of the pattern map's width.
     * <p/>
     * If the centroid y is negative, the centroid y position will be
     * automatically calculated as half of the pattern map's height.
     * <p/>
     * If the radius is negative, the radius will be automatically calculated as
     * the radius of the largest circle that can be inscribed in the pattern
     * map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input pattern map.
     * @return output pattern map
     * @see org.ebsdimage.core.MaskDisc#MaskDisc(int, int, int, int, int)
     */
    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        // Set default if needed
        int centroidX = this.centroidX;
        int centroidY = this.centroidY;
        int radius = this.radius;

        if (centroidX < 0)
            centroidX = srcMap.width / 2;
        if (centroidY < 0)
            centroidY = srcMap.height / 2;
        if (radius < 0) {
            radius = min(srcMap.width / 2, srcMap.height / 2);

            // Decrease the radius by how much its value is negative
            // e.g radius = -1 => radius = maskdisc.radius
            // e.g. radius = -2 => radius = maskdisc.radius - 1
            // etc.
            radius += this.radius + 1;
        }

        // Create MaskDisc
        BinMap maskDisc =
                new org.ebsdimage.core.MaskDisc(srcMap.width, srcMap.height,
                        centroidX, centroidY, radius);

        // Apply mask
        ByteMap destMap = new ByteMap(srcMap.width, srcMap.height);
        MapMath.and(srcMap, maskDisc, destMap);

        destMap.setProperties(maskDisc);
        destMap.setProperties(srcMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Mask Disc [centroid X=" + centroidX + " px, centroid Y="
                + centroidY + " px, radius=" + radius + " px]";
    }

}