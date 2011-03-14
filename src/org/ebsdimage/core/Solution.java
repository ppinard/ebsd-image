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

import org.apache.commons.math.geometry.Rotation;

import crystallography.core.Crystal;

/**
 * Solution from the indexing.
 * 
 * @author Philippe T. Pinard
 */
public class Solution {

    /** Indexed phase. */
    public final Crystal phase;

    /** Lattice orientation. */
    public final Rotation rotation;

    /** Evaluation of the goodness of a solution. */
    public final double fit;



    /**
     * Creates a new solution from an indexed phase, lattice orientation and
     * fit.
     * 
     * @param phase
     *            indexed phase
     * @param rotation
     *            lattice orientation
     * @param fit
     *            goodness of a solution
     * @throws IllegalArgumentException
     *             if the fit is less than 0 or greater than 1
     */
    public Solution(Crystal phase, Rotation rotation, double fit) {
        if (fit < -1e-6 || (fit - 1) > 1e-6)
            throw new IllegalArgumentException("Fit must be between [0, 1].");

        this.phase = phase;
        this.rotation = rotation;
        this.fit = fit;
    }



    /**
     * Returns a representation of this solution, suitable for debugging.
     * 
     * @return info about this solution
     */
    @Override
    public String toString() {
        return phase.name + "\t" + rotation.getAxis().toString() + "\t"
                + Math.toDegrees(rotation.getAngle()) + "\t" + fit;
    }

}
