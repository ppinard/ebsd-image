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
package org.ebsdimage.core.sim;

import static java.lang.Math.abs;

import org.ebsdimage.core.Camera;

import ptpshared.core.geom.Line;
import ptpshared.core.math.Quaternion;
import crystallography.core.Reflector;

/**
 * Band in a diffraction pattern. A band consists of a <code>Reflector</code>
 * plus the information to draw this reflector in a simulated pattern (center
 * line, width, etc.).
 * 
 * @author Philippe T. Pinard
 */
public class Band extends Reflector {

    /** Line representing the middle of this band. */
    public final Line line;

    /** Two-item array for the half widths above and below the middle line. */
    public final double[] halfWidths;

    /** Two-item array for the intercept values of this band's edges. */
    public final Line[] edgeIntercepts;

    /** Estimated width of this band from the half widths. */
    public final double width;



    /**
     * Creates a new <code>Band</code> from a reflector, a rotation, a camera
     * and an energy.
     * 
     * @param reflector
     *            reflector of the band
     * @param rotation
     *            rotation of the simulated pattern
     * @param camera
     *            definition of the camera's position
     * @param energy
     *            accelerating energy of the electron beam in the simulated
     *            pattern
     */
    public Band(Reflector reflector, Quaternion rotation, Camera camera,
            double energy) {
        super(reflector, reflector.normalizedIntensity);

        // Slope and intercept as of a PC = (0.0, 0.0).
        Line tmpLine =
                Calculations.getLineFromPlane(plane.toVector3D(),
                        camera.detectorDistance, rotation);

        // Exception for line parallel to camera's plane
        if (tmpLine.m == Double.POSITIVE_INFINITY
                && tmpLine.k == Double.POSITIVE_INFINITY)
            throw new BandException(
                    "The band is parallel to the camera's plane. "
                            + "It cannot be calculated.");

        // Half widths
        halfWidths =
                Calculations.getBandHalfWidths(planeSpacing, tmpLine,
                        camera.detectorDistance, energy);

        // Slope and intercept translated to the correct PC.
        double newK;
        if (tmpLine.m == Double.POSITIVE_INFINITY) // Vertical line
            newK = tmpLine.k + camera.patternCenterH;
        else
            // Horizontal and oblique
            newK =
                    tmpLine.k - tmpLine.m * camera.patternCenterH
                            + camera.patternCenterV;

        line = new Line(tmpLine.m, newK);

        // Edge intercepts
        edgeIntercepts = Calculations.getEdgesIntercepts(line, halfWidths);

        // Full width
        width = Calculations.getBandWidth(halfWidths);
    }



    /**
     * Checks if this <code>Band</code> is almost equal to the specified one
     * with the given precision. To be equal two bands must have the same
     * reflector and the same width.
     * 
     * @param other
     *            other <code>Band</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Band</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(Band other, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == other)
            return true;
        if (other == null)
            return false;

        if (!super.equals(other))
            return false;
        if (abs(width - other.width) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>Band</code> is exactly equal to the specified one.
     * To be equal two bands must have the same reflector and the same width.
     * 
     * @param obj
     *            other <code>Band</code> to check equality
     * @return whether the two <code>Band</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        Band other = (Band) obj;
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width))
            return false;

        return true;
    }



    /**
     * Returns the hash code for this <code>Band</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(width);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Returns a representation of this <code>Band</code>, suitable for
     * debugging.
     * 
     * @return information about this <code>Band</code>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);

        str.append("Band " + plane + "\n");
        str.append("  Plane spacing: " + planeSpacing + "\n");
        str.append("  Intensity: " + planeSpacing + "\n");
        str.append("  Normalized intensity: " + planeSpacing + "\n");
        str.append("  Line: " + line.toString() + "\n");
        str.append("  Half widths: " + halfWidths[0] + "," + halfWidths[1]
                + "\n");
        str.append("  Edge intercepts: " + edgeIntercepts[0] + ","
                + edgeIntercepts[1] + "\n");
        str.append("  Width: " + width);

        return str.toString();
    }

}
