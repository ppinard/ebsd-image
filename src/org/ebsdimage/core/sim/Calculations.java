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

import java.util.Arrays;

import magnitude.core.Magnitude;
import magnitude.geom.Line2D;
import magnitude.geom.Line2DUtil;
import magnitude.geom.Vector2D;
import ptpshared.geom.Line;
import ptpshared.geom.Plane;
import ptpshared.geom.Vector3DUtils;
import ptpshared.math.old.Quaternion;
import ptpshared.math.old.QuaternionMath;
import ptpshared.math.old.Vector3D;
import static crystallography.core.Calculations.diffractionAngle;
import static crystallography.core.Calculations.electronWavelength;

/**
 * Calculations to draw band in a pattern.
 * 
 * @author Philippe T. Pinard
 */
public class Calculations {

    /**
     * Returns the width of the band above and below the centre line.
     * 
     * @param planeSpacing
     *            spacing between the plane corresponding to the band
     * @param line
     *            middle line of the band
     * @param detectorDistance
     *            distance between the sample and the detector window
     * @param energy
     *            beam energy (in eV)
     * @return two-item array for the half widths
     */
    public static Line2D[] getEdgesIntercepts(double planeSpacing, Line2D line,
            Magnitude detectorDistance, double energy) {
        /*
         * Calculate the alpha angle: angle between the diffraction plane normal
         * and the screen.
         */
        if (!line.p.getX().areSameUnits(detectorDistance)
                || !line.p.getY().areSameUnits(detectorDistance))
            throw new IllegalArgumentException("The detector distance ("
                    + detectorDistance.getBaseUnitsLabel()
                    + ") must have the same units as the line ("
                    + Arrays.toString(line.p.getBaseUnitsLabels()) + ").");
        if (!line.v.getX().areSameUnits(detectorDistance)
                || !line.v.getY().areSameUnits(detectorDistance))
            throw new IllegalArgumentException("The detector distance ("
                    + detectorDistance.getBaseUnitsLabel()
                    + ") must have the same units as the line ("
                    + Arrays.toString(line.v.getBaseUnitsLabels()) + ").");

        // Vector from the origin to a point along the line
        double px = line.p.getX().getBaseUnitsValue();
        double py = line.p.getY().getBaseUnitsValue();
        double dd = detectorDistance.getBaseUnitsValue();
        Vector3D v0 = new Vector3D(px, dd, py);

        // Vector of the line (Vector2D) express in 3D
        double vx = line.v.getX().getBaseUnitsValue();
        double vy = line.v.getY().getBaseUnitsValue();
        Vector3D v1 = new Vector3D(vx, 0.0, vy);

        // Vector normal to the plane from by x0 and x1
        Vector3D n = v1.cross(v0);

        // Vector parallel to the screen
        Vector3D s = new Vector3D(n.getX(), 0.0, n.getZ());

        // Shortest distance between the origin and the line
        double d = n.norm() / v1.norm();

        // Angle between the vector n and s
        double alpha = Vector3DUtils.angle(n, s);

        /* Calculate the diffraction angle */
        double wavelength = electronWavelength(energy);
        double theta = diffractionAngle(planeSpacing, wavelength);

        /*
         * Calculate the half-widths of the band (top and bottom or left and
         * right)
         */
        Vector2D t = Line2DUtil.getPerpendicular(line).v;

        double width = Math.abs(d * Math.sin(theta) / Math.cos(alpha - theta));
        Line2D top = Line2DUtil.translate(line, t.multiply(width));

        width = Math.abs(d * Math.sin(theta) / Math.cos(alpha + theta));
        Line2D bottom = Line2DUtil.translate(line, t.multiply(-width));

        return new Line2D[] { top, bottom };
    }



    /**
     * Returns the full width of a band based on the half widths.
     * 
     * @param halfWidths
     *            two-item array of the half widths
     * @return estimation of the full width of the band
     */
    public static Magnitude getBandWidth(Magnitude[] halfWidths) {
        Magnitude halfWidth =
                magnitude.core.Math.max(halfWidths[0], halfWidths[1]);
        return halfWidth.multiply(2.0);
    }



    // /**
    // * Returns the top and bottom intercepts of the lines at the edges of a
    // * band.
    // *
    // * @param line
    // * middle line of the band
    // * @param halfWidths
    // * two-item array of the half widths
    // * @return two-item array for the intercept values of the band edges
    // */
    // public static Line2D[] getEdgesIntercepts(Line2D line,
    // Magnitude[] halfWidths) {
    // // Calculate the beta angle: angle between the band and the x-axis
    // double cosBeta;
    //
    // if (line.isVertical())
    // cosBeta = 1.0;
    // else
    // cosBeta = Math.cos(Math.atan(line.getSlope().getValue("")));
    //
    // Magnitude x = line.p.getX();
    // Magnitude y = line.p.getY().add(halfWidths[0].div(cosBeta));
    // Line2D top = new Line2D(new Vector2D(x, y), line.v);
    //
    // y = line.p.getY().minus(halfWidths[1].div(cosBeta));
    // Line2D bottom = new Line2D(new Vector2D(x, y), line.v);
    //
    // return new Line2D[] { top, bottom };
    // }

    /**
     * Returns the slope and intercept of the projection of a plane (hkl) on a
     * detector located at <code>detectorDistance</code>. A pattern center of
     * <code>(0.0, 0.0)</code> is centered.
     * 
     * @param plane
     *            diffracted plane
     * @param detectorDistance
     *            distance between the sample and the detector window
     * @return line middle line of the band
     */
    public static Line getLineFromPlane(Vector3D plane, double detectorDistance) {
        double m;
        double k;

        double nx = plane.get(0);
        double ny = plane.get(1);
        double nz = plane.get(2);

        if (abs(nz) > 1e-7) { // nz != 0
            m = -(nx / nz);
            k = -detectorDistance * ny / nz;
        } else if (!(abs(nx) < 1e-7)) {
            m = Double.POSITIVE_INFINITY;
            k = -detectorDistance * ny / nx;
        } else { // Plane parallel to the screen
            m = Double.POSITIVE_INFINITY;
            k = Double.POSITIVE_INFINITY;
        }

        return new Line(m, k);
    }



    /**
     * Returns the slope and intercept for a given plane after its rotation.
     * 
     * @param plane
     *            diffracted plane
     * @param detectorDistance
     *            distance between the sample and the detector window
     * @param rotation
     *            <code>Quaternion</code> representing the rotation
     * @return middle line of the band
     */
    public static Line getLineFromPlane(Plane plane, double detectorDistance,
            Quaternion rotation) {
        // Apply rotation
        Vector3D reflRotated = QuaternionMath.rotate(plane, rotation);

        // Calculate slope and intercept
        return Calculations.getLineFromPlane(reflRotated, detectorDistance);
    }

}
