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
package ptpshared.geom;

import org.apache.commons.math.geometry.Vector3D;

/**
 * Utilities to deal with <code>Plane</code>'s.
 * 
 * @author Philippe T. Pinard
 */
public class PlaneUtil {

    /**
     * Returns the <code>Point3D</code> where the specified line intersects the
     * specified plane.
     * <p/>
     * References:
     * http://www.softsurfer.com/Archive/algorithm_0104/algorithm_0104B.htm
     * 
     * @param line
     *            a line
     * @param plane
     *            a plane
     * @return point of intersection
     * @throws ArithmeticException
     *             if the line and the plane are parallel
     * @throws ArithmeticException
     *             if the line is inside the plane
     */
    public static Vector3D linePlaneIntersection(Line3D line, Plane plane) {
        Vector3D w = line.p.subtract(plane.p);

        double numerator = Vector3D.dotProduct(plane.n, w) * -1;
        double denominator = Vector3D.dotProduct(plane.n, line.v);
        double s = numerator / denominator;

        // Check for intersection
        // If s is infinite, the line and the plane are parallel
        if (Double.isInfinite(s))
            throw new ArithmeticException("The line (" + line
                    + ") does not intersect the plane (" + plane + ").");
        // If s is NaN, the line lies within the plane
        if (Double.isNaN(s))
            throw new ArithmeticException("The line (" + line
                    + ") is parallel and inside the plane (" + plane + ").");

        // Return point of intersection
        return line.getPointFromS(s);
    }



    /**
     * Returns the intersection line of two planes.
     * <p/>
     * References:
     * http://www.softsurfer.com/Archive/algorithm_0104/algorithm_0104B.htm
     * 
     * @param plane0
     *            first plane
     * @param plane1
     *            second plane
     * @return intersecting line
     * @throws ArithmeticException
     *             if the two planes are parallel
     */
    public static Line3D planesIntersection(Plane plane0, Plane plane1) {
        Vector3D n3 = Vector3D.crossProduct(plane0.n, plane1.n);

        // Check if the planes are parallel
        if (n3.getNorm() < 1e-6)
            throw new ArithmeticException("The planes (" + plane0 + " and "
                    + plane1 + ") are parallel.");

        // Calculate point along the intersecting line
        // (d2n1 - d1n2) x n3 / (n3^2)
        Vector3D p =
                plane0.n.scalarMultiply(plane1.getD()).subtract(
                        plane1.n.scalarMultiply(plane0.getD()));
        p = Vector3D.crossProduct(p, n3);
        p = p.scalarMultiply(1.0 / n3.getNormSq());

        return new Line3D(p, n3);
    }
}
