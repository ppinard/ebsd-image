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
package ptpshared.core.geom;

import ptpshared.core.math.Vector3D;

/**
 * Represents a 3D line using a point and a vector. The line is passing by the
 * point and pointing in the vector direction.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Line3D {

    /** A point on the line. */
    public final Vector3D point;

    /** Vector of the line. */
    public final Vector3D vector;



    /**
     * Creates a new <code>Line3D</code>.
     * 
     * @param point
     *            a point on the line
     * @param vector
     *            vector representing the line
     * @throws NullPointerException
     *             if the point is null
     * @throws NullPointerException
     *             if the vector is null
     */
    public Line3D(Vector3D point, Vector3D vector) {
        if (point == null)
            throw new NullPointerException("Point cannot be null.");
        if (vector == null)
            throw new NullPointerException("Vector cannot be null.");

        this.point = point;
        this.vector = vector;
    }



    @Override
    public String toString() {
        return point + "+" + vector + "t";
    }
}
