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

import rmlimage.core.Pixel;

/**
 * Representation of a pixel in a <code>HoughMap<</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPixel extends Pixel {
    /** Rho position. */
    private double r;

    /** Theta position. */
    private double theta;

    /** Intensity value. */
    private int value;



    /**
     * Creates a new <code>HoughPixel</code> with the rho and theta coordinates
     * and the intensity of the pixel.
     * 
     * @param r
     *            rho coordinate
     * @param theta
     *            theta coordinate
     * @param value
     *            intensity
     */
    public HoughPixel(double r, double theta, byte value) {
        this.r = r;
        this.theta = theta;
        this.value = (value & 0xff);
    }



    @Override
    public String getValueLabel() {
        StringBuilder str = new StringBuilder(128);
        str.append("r = ");
        str.append(r);
        str.append("  theta = ");
        str.append(rmlshared.math.Double.round(Math.toDegrees(theta), 1));
        str.append("\u00b0  value = ");
        str.append(value);
        return str.toString();
    }

}
