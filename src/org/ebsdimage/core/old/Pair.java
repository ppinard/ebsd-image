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

import static java.lang.Math.toDegrees;
import net.jcip.annotations.Immutable;
import ptpshared.geom.Vector3DUtils;
import ptpshared.math.old.Vector3D;

/**
 * Abstract class to store a pair of normals and the direction cosine between
 * them.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public abstract class Pair {

    /** Direction cosine between the two normals. */
    public final double directionCosine;

    /** First normal. */
    public final Vector3D normal0;

    /** Second normal. */
    public final Vector3D normal1;



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
    protected Pair(Vector3D normal0, Vector3D normal1, double directionCosine) {
        if (normal0 == null)
            throw new NullPointerException("The first normal cannot be null.");
        if (normal1 == null)
            throw new NullPointerException("The second normal cannot be null.");
        if (Double.isNaN(directionCosine))
            throw new IllegalArgumentException(
                    "The direction cosine cannot be not a number (NaN).");
        if (Double.isInfinite(directionCosine))
            throw new IllegalArgumentException(
                    "The direction cosine cannot be infinite.");

        this.normal0 = normal0.normalize();
        this.normal1 = normal1.normalize();
        this.directionCosine = directionCosine;
    }



    /**
     * Returns a representation of this pair (two normals and direction cosine),
     * suitable for debugging.
     * 
     * @return info about this pair
     */
    @Override
    public String toString() {
        return normal0.toString() + "\t" + normal1.toString() + "\t"
                + toDegrees(Vector3DUtils.angle(directionCosine)) + " deg";
    }

}
