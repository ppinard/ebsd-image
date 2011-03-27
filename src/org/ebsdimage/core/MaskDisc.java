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
package org.ebsdimage.core;

import rmlimage.core.BinMap;
import rmlimage.core.Drawing;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * A circular mask. The central pixel is counted in the radius. This means that
 * a disc of <code>radius = 1</code> is a single pixel and a disc of
 * <code>radius = 2</code> will be 3 pixels wide
 */
public class MaskDisc extends BinMap {

    /** X coordinate of the center of the disc. */
    public final int centroidX;

    /** Y coordinate of the center of the disc. */
    public final int centroidY;

    /** Radius of the disc. */
    public final int radius;

    /** Property key for the radius value. */
    public static final String KEY_RADIUS = "maskdisc.radius";

    /** Property key for the centroid x coordinate value. */
    public static final String KEY_CENTROID_X = "maskdisc.centroid.x";

    /** Property key for the centroid y coordinate value. */
    public static final String KEY_CENTROID_Y = "maskdisc.centroid.y";



    /**
     * Constructs a circular mask.
     * 
     * @param width
     *            width of the mask map
     * @param height
     *            height of the mask map
     * @param centroidX
     *            x position in pixels of the center of the disc
     * @param centroidY
     *            y position in pixels of the center of the disc
     * @param radius
     *            radius of the disc in pixels
     * @throws IllegalArgumentException
     *             if either <code>width</code>, <code>height</code> or
     *             <code>radius</code> are <= 0
     */
    public MaskDisc(int width, int height, int centroidX, int centroidY,
            int radius) {
        super(width, height); // Create the BinMap

        // Validate the radius
        if (radius <= 0)
            throw new IllegalArgumentException("radius (" + radius
                    + ") must be > 0");

        // Save the parameters
        this.centroidX = centroidX;
        this.centroidY = centroidY;
        this.radius = radius;

        // Save the properties
        setProperty(KEY_CENTROID_X, centroidX);
        setProperty(KEY_CENTROID_Y, centroidY);
        setProperty(KEY_RADIUS, radius);

        // Draw the disc
        double squareRoot;
        double xMin;
        double xMax;

        // Cycle through every pixel in the BinMap
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Skip if above or below the disc
                if (abs(y - centroidY) >= radius)
                    continue;

                // Calculate the two endpoints of the scan line that defines the
                // disc
                /*
                 * (x-centroidX)^2 + (y-centroidY)^2 < radius^2 (x-centroidX)^2
                 * < radius^2 - (y-centroidY)^2 abs(x-centroidX) < sqrt(radius^2
                 * - (y-centroidY)^2) so: x > centroidX - sqrt(radius^2 -
                 * (y-centroidY)^2) and x < centroidX + sqrt(radius^2 -
                 * (y-centroidY)^2)
                 */

                squareRoot =
                        sqrt(radius * radius - (y - centroidY)
                                * (y - centroidY));
                xMin = centroidX - squareRoot + 1; // +1 because we have > not
                // >=
                xMax = centroidX + squareRoot - 1; // -1 because we have < not
                // <=

                // Trim the line to the left and right edges
                // if (xMin < 0) xMin = 0;
                // if (xMax >= width) xMax = width - 1;

                Drawing.horizontalLine(this, (int) (xMin + 0.5),
                        (int) (xMax + 0.5), y, true);
            }
        }
    }

}
