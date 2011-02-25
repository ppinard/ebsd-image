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

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;

/**
 * Represents a 3D line using a point and a vector. The line is passing by the
 * point and pointing in the vector direction.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Line3D implements AlmostEquable, Transformable {

    /** A point on the line. */
    public final Vector3D p;

    /** Vector of the line. */
    public final Vector3D v;



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

        this.p = point;
        this.v = vector;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        double delta = ((Number) precision).doubleValue();
        if (delta < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(delta))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Line3D other = (Line3D) obj;
        if (Math.abs(p.getX() - other.p.getX()) > delta)
            return false;
        if (Math.abs(p.getY() - other.p.getY()) > delta)
            return false;
        if (Math.abs(p.getZ() - other.p.getZ()) > delta)
            return false;

        if (Math.abs(v.getX() - other.v.getX()) > delta)
            return false;
        if (Math.abs(v.getY() - other.v.getY()) > delta)
            return false;
        if (Math.abs(v.getZ() - other.v.getZ()) > delta)
            return false;

        return true;
    }



    /**
     * Returns the point along the line at a distance <code>s</code> from
     * <code>p</code>.
     * 
     * @param s
     *            distance from point <code>p</code>
     * @return point along the line
     */
    public Vector3D getPointFromS(double s) {
        return v.scalarMultiply(s).add(p);
    }



    /**
     * Returns the point on the line when <code>x</code> equals the specified
     * coordinate.
     * 
     * @param x
     *            coordinate
     * @return point on the line
     */
    public Vector3D getPointFromX(double x) {
        double t = (x - p.getX()) / v.getX();

        if (Double.isInfinite(t))
            throw new ArithmeticException("The line does not pass through x="
                    + x + ".");

        double y = p.getY() + v.getY() * t;
        double z = p.getZ() + v.getZ() * t;

        return new Vector3D(x, y, z);
    }



    /**
     * Returns the point on the line when <code>y</code> equals the specified
     * coordinate.
     * 
     * @param y
     *            coordinate
     * @return point on the line
     */
    public Vector3D getPointFromY(double y) {
        double t = (y - p.getY()) / v.getY();

        if (Double.isInfinite(t))
            throw new ArithmeticException("The line does not pass through y="
                    + y + ".");

        double x = p.getX() + v.getX() * t;
        double z = p.getZ() + v.getZ() * t;

        return new Vector3D(x, y, z);
    }



    /**
     * Returns the point on the line when <code>z</code> equals the specified
     * coordinate.
     * 
     * @param z
     *            coordinate
     * @return point on the line
     */
    public Vector3D getPointFromZ(double z) {
        double t = (z - p.getZ()) / v.getZ();

        if (Double.isInfinite(t))
            throw new ArithmeticException("The line does not pass through z="
                    + z + ".");

        double x = p.getX() + v.getX() * t;
        double y = p.getY() + v.getY() * t;

        return new Vector3D(x, y, z);
    }



    @Override
    public String toString() {
        return p + " + " + v + "t";
    }



    @Override
    public Line3D transform(AffineTransform3D t) {
        return new Line3D(t.transformPoint(p), t.transformVector(v));
    }

}
