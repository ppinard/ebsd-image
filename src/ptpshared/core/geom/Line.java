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

import static java.lang.Math.abs;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

import net.jcip.annotations.Immutable;
import ptpshared.core.math.Vector3D;
import rmlshared.geom.LineUtil;

/**
 * Represents a 2D line using a slope and intercept. This is a more general
 * representation of a line in two dimensions than the definition of
 * <code>java.awt.geom.Line2D</code> object which use two points to represent a
 * line. Use {@link #toLine2D} to convert this object to a
 * <code>Line2D.Double</code>.
 * <p/>
 * The slope and intercept can be accessed via the public final variables
 * <code>m</code> and <code>k</code> respectively.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Line {

    /** Slope. */
    public final double m;

    /** Intercept. */
    public final double k;



    /**
     * Creates a new line and stores the slope and intercept.
     * 
     * @param m
     *            slope
     * @param k
     *            intercept
     * @throws IllegalArgumentException
     *             if the slope is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the intercept is not a number (NaN)
     */
    public Line(double m, double k) {
        if (Double.isNaN(m))
            throw new IllegalArgumentException("The slope cannot be NaN.");
        if (Double.isNaN(k))
            throw new IllegalArgumentException("The intercept cannot be NaN.");

        this.m = m;
        this.k = k;
    }



    /**
     * Checks if this <code>Line</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param other
     *            other <code>Line</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Line</code> are almost equal
     */
    public boolean equals(Line other, double precision) {
        if (this == other)
            return true;
        if (other == null)
            return false;

        if (abs(m - other.m) >= precision)
            return false;
        if (abs(k - other.k) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>Line</code> is exactly equal to the specified one.
     * 
     * @param obj
     *            other <code>Line</code> to check equality
     * @return whether the two <code>Line</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Line other = (Line) obj;
        if (Double.doubleToLongBits(k) != Double.doubleToLongBits(other.k))
            return false;
        if (Double.doubleToLongBits(m) != Double.doubleToLongBits(other.m))
            return false;
        return true;
    }



    /**
     * Returns a <code>Line2D.Double</code> from this line. Instead of begin
     * defined as a slope and intercept, the analytical line is defined by two
     * points.
     * 
     * @param width
     *            width of the image
     * @return a <code>Line2D.Double</code>
     */
    private Line2D.Double getLine2D(int width) {
        Line2D.Double line2D = new Line2D.Double();

        Point2D.Double center;

        if (Double.isInfinite(m))
            // Vertical line
            center = new Point2D.Double(k * width, 0.0);
        else
            // Horizontal and oblique line
            center = new Point2D.Double(0.0, k * width);

        LineUtil.setAnalyticalLine(line2D, m, center, width);

        return line2D;
    }



    /**
     * Returns the hash code for this <code>Line</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(k);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Converts the line to two points (x1, y1) and (x2, y2) saved in a Line2D.
     * The line with slope m and k uses an origin at width/2 and height/2 where
     * the y axis is pointing up and x axis pointing to the right of the image.
     * The line is extended to 10% outside the image.
     * 
     * @param width
     *            width of the image
     * @param height
     *            height of the image
     * @return <code>Line2D</code> containing the two points
     */
    public Line2D.Double toLine2D(int width, int height) {
        Line2D.Double line2D = getLine2D(width);

        // Translate the origin from the center of the image
        // to the upper left corner
        LineUtil.translate(line2D, width / 2, height / 2);

        // Flip y axis
        line2D.y1 = height - 1 - line2D.y1;
        line2D.y2 = height - 1 - line2D.y2;

        // Extends the line to fit the whole map
        Rectangle.Double frame =
                new Rectangle.Double(-0.1 * width, -0.1 * height, 1.2 * width,
                        1.2 * height);

        try {
            LineUtil.extendTo(line2D, frame);
        } catch (IllegalArgumentException e) {
            // Ignore line outside the image
            Logger logger = Logger.getLogger("ptpshared.core.geom");
            logger.warning("Line (slope=" + m + ", intercept=" + k
                    + ") is outside the image.");
            return null;
        }

        return line2D;
    }



    /**
     * Converts the line to a <code>Line3D</code>.
     * 
     * @param plane
     *            plane containing the line
     * @return a <code>Line3D</code>
     */
    public Line3D toLine3D(LinePlane plane) {
        Line2D.Double line2D = getLine2D(1);

        double deltax = line2D.x2 - line2D.x1;
        double deltay = line2D.y2 - line2D.y1;

        Vector3D vector;
        Vector3D point;
        switch (plane) {
        case XY:
            vector = new Vector3D(deltax, deltay, 0);
            point = new Vector3D(line2D.x1, line2D.y1, 0);
            return new Line3D(point, vector);
        case XZ:
            vector = new Vector3D(deltax, 0, deltay);
            point = new Vector3D(line2D.x1, 0, line2D.y1);
            return new Line3D(point, vector);
        case YZ:
            vector = new Vector3D(0, deltax, deltay);
            point = new Vector3D(0, line2D.x1, line2D.y1);
            return new Line3D(point, vector);
        default:
            throw new IllegalArgumentException(plane.toString()
                    + " is invalid.");
        }
    }



    /**
     * Returns a <code>String</code> representation of the <code>Line</code>,
     * suitable for debugging.
     * 
     * @return information about the <code>Line</code>
     */
    @Override
    public String toString() {
        return "(m=" + m + ", k=" + k + ")";
    }
}
