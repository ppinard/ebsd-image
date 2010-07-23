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

import rmlimage.core.Result;

/**
 * Results consisting of the centroid of Hough peaks. The object contains an
 * array of positions of the peaks (rho and theta).
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughPoint extends Result {

    /** Id for the rho values. */
    public static final int ID_RHO = 0;

    /** Id for the theta values. */
    public static final int ID_THETA = 1;

    /** Rho values of the centroids. */
    public final float[] rho;

    /** Theta values of the centroids. */
    public final float[] theta;



    /**
     * Creates a new <code>Centroid</code> containing a number of centroids.
     * 
     * The units for the rho values are in pixels and for the theta values in
     * radians.
     * 
     * @param nbValues
     *            number of centroids
     */
    public HoughPoint(int nbValues) {
        super(2, nbValues);

        name[ID_RHO] = "Centroid R";
        name[ID_THETA] = "Centroid Theta";

        units[ID_RHO] = "pix";
        units[ID_THETA] = "rad";

        rho = values[ID_RHO];
        theta = values[ID_THETA];
    }



    @Override
    public HoughPoint duplicate() {
        HoughPoint dup = new HoughPoint(rho.length);
        System.arraycopy(rho, 0, dup.rho, 0, rho.length);
        System.arraycopy(theta, 0, dup.theta, 0, theta.length);
        return dup;
    }

}
