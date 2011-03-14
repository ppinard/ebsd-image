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
package org.ebsdimage.core.old;

import ptpshared.math.old.Vector3D;
import crystallography.core.Reflector;

/**
 * Pair of interplanar angles between two theoretical planes.
 * 
 * @author Philippe T. Pinard
 */
public class InterplanarAnglePair extends Pair {

    /**
     * Creates a new <code>InterplanarAnglePair</code> from two reflectors.
     * 
     * @param refl0
     *            first reflector
     * @param refl1
     *            second reflector
     * @param directionCosine
     *            direction cosine between the two reflectors' plane
     * @throws NullPointerException
     *             if a normal is null
     * @throws IllegalArgumentException
     *             if the direction cosine is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is infinite
     */
    protected InterplanarAnglePair(Reflector refl0, Reflector refl1,
            double directionCosine) {
        super(refl0.plane.toVector3D(), refl1.plane.toVector3D(),
                directionCosine);
    }



    /**
     * Creates a new pair.
     * 
     * @param normal0
     *            first normal
     * @param normal1
     *            second normal
     * @param directionCosine
     *            direction cosine between the two normals
     * @throws NullPointerException
     *             if a normal is null
     * @throws IllegalArgumentException
     *             if the direction cosine is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is infinite
     */
    public InterplanarAnglePair(Vector3D normal0, Vector3D normal1,
            double directionCosine) {
        super(normal0, normal1, directionCosine);
    }

}
