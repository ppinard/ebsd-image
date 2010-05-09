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

import static crystallography.core.Calculations.diffractionAngle;
import static crystallography.core.Calculations.electronWavelength;
import static java.lang.Math.*;
import ptpshared.core.geom.Line;
import ptpshared.core.math.Quaternion;
import ptpshared.core.math.QuaternionMath;
import ptpshared.core.math.Vector3D;
import ptpshared.core.math.Vector3DMath;

/**
 * Calculations to draw band in a pattern.
 * 
 * @author Philippe T. Pinard
 */
public class Calculations {

    /**
     * Returns the width of the band above and below the center line.
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
    public static double[] getBandHalfWidths(double planeSpacing, Line line,
            double detectorDistance, double energy) {
        /*
         * Calculate the alpha angle: angle between the diffraction plane normal
         * and the screen.
         */
        Vector3D x0;
        Vector3D x1;
        Vector3D x2;

        x0 = new Vector3D(0, 0, 0);

        // Vertical line
        if (line.m == Double.POSITIVE_INFINITY) {
            x1 = new Vector3D(line.k, detectorDistance, 0.0);
            x2 = new Vector3D(line.k, detectorDistance, 0.1);
        }

        // Horizontal line
        else if (abs(line.m) <= 1e-7) {
            x1 = new Vector3D(0.0, detectorDistance, line.k);
            x2 = new Vector3D(0.1, detectorDistance, line.k);
        }

        // Oblique line
        else {
            x1 = new Vector3D(0.0, detectorDistance, line.k);

            // Intercept greater than 0.0
            if (abs(line.k) > 1e-7)
                x2 = new Vector3D(-line.k / line.m, detectorDistance, 0.0);

            // Intercept less than 0.0
            else
                x2 = new Vector3D((1 - line.k) / line.m, detectorDistance, 1.0);
        }

        Vector3D n = x2.minus(x1).cross(x1.minus(x0));
        Vector3D s = new Vector3D(n.get(0), 0, n.get(2));
        double d = n.norm() / x2.minus(x1).norm();
        double alpha = Vector3DMath.angle(n, s);

        /* Calculate the diffraction angle */
        double wavelength = electronWavelength(energy);
        double theta = diffractionAngle(planeSpacing, wavelength);

        /*
         * Calculate the half-widths of the band (top and bottom or left and
         * right)
         */
        double widthTop = abs(d * sin(theta) / cos(alpha - theta));
        double widthBottom = abs(d * sin(theta) / cos(alpha + theta));

        return new double[] { widthTop, widthBottom };
    }



    /**
     * Returns the full width of a band based on the half widths.
     * 
     * @param halfWidths
     *            two-item array of the half widths
     * @return estimation of the full width of the band
     */
    public static double getBandWidth(double[] halfWidths) {
        double halfWidth = max(halfWidths[0], halfWidths[1]);
        return 2 * halfWidth;
    }



    /**
     * Returns the top and bottom intercepts of the lines at the edges of a
     * band.
     * 
     * @param line
     *            middle line of the band
     * @param halfWidths
     *            two-item array of the half widths
     * @return two-item array for the intercept values of the band edges
     */
    public static Line[] getEdgesIntercepts(Line line, double[] halfWidths) {
        // Calculate the beta angle: angle between the band and the x-axis
        double beta;

        if (line.m == Double.POSITIVE_INFINITY)
            beta = 0.0;
        else
            beta = atan(line.m);

        double kTop = line.k + halfWidths[0] / cos(beta);
        double kBottom = line.k - halfWidths[1] / cos(beta);

        return new Line[] { new Line(line.m, kTop), new Line(line.m, kBottom) };
    }



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
    public static Line getLineFromPlane(Vector3D plane,
            double detectorDistance, Quaternion rotation) {
        // Apply rotation
        Vector3D reflRotated = QuaternionMath.rotate(plane, rotation);

        // Calculate slope and intercept
        return Calculations.getLineFromPlane(reflRotated, detectorDistance);
    }

}
