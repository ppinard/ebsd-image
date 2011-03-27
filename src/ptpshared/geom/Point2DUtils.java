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

import java.awt.geom.Point2D;

/**
 * Operations on <code>Point2D</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Point2DUtils {
    /**
     * Checks whether two <code>Point2D</code> are equal to a certain precision.
     * 
     * @param p1
     *            point 1
     * @param p2
     *            point 2
     * @param precision
     *            precision
     * @return <code>true</code> if the two points are equal to the specified
     *         precision, <code>false</code> otherwise
     */
    public static boolean equals(Point2D p1, Point2D p2, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "Precision cannot be less than zero.");

        if (p1 == null || p2 == null)
            return false;

        if (Math.abs(p1.getX() - p2.getX()) > precision)
            return false;
        if (Math.abs(p1.getY() - p2.getY()) > precision)
            return false;

        return true;
    }



    /**
     * Returns a string representation of a <code>Point2D</code>.
     * 
     * @param point
     *            a point
     * @return string representation
     */
    public static String toString(Point2D point) {
        return "(" + point.getX() + ";" + point.getY() + ")";
    }
}
