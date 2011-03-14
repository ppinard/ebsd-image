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

import magnitude.core.Magnitude;
import magnitude.geom.Line2D;

import org.junit.Test;

import ptpshared.geom.Line;
import ptpshared.math.old.AxisAngle;
import ptpshared.math.old.Quaternion;
import crystallography.core.Plane;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.PI;

public class CalculationsTest {

    @Test
    public void testComputeLineFromPlanePlaneDouble() {
        Line line;
        Line expected;
        Plane plane;

        // Horizontal and vertical
        plane = new Plane(0, 0, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(0.0, 0.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(1, 0, 0);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(Double.POSITIVE_INFINITY, 0.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(0, 1, 0);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(line.equals(expected, 1e-7));

        // Oblique
        plane = new Plane(1, 0, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(-1.0, 0.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(-1, 0, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(1.0, 0.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(1, 0, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 0.5);
        expected = new Line(-1.0, 0.0);
        assertTrue(line.equals(expected, 1e-7));

        // Intercept != 0.0
        plane = new Plane(0, 1, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(0.0, -1.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(0, 1, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 0.5);
        expected = new Line(0.0, -0.5);
        assertTrue(line.equals(expected, 1e-7));

        // Oblique + Intercept != 0.0
        plane = new Plane(1, 1, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(-1.0, -1.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(-1, 1, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(1.0, -1.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(-1, -1, 1);
        line = Calculations.getLineFromPlane(plane.toVector3D(), 1.0);
        expected = new Line(1.0, 1.0);
        assertTrue(line.equals(expected, 1e-7));
    }



    @Test
    public void testComputeLineFromPlanePlaneDoubleQuaternion() {
        Plane plane;
        Quaternion rotation = new Quaternion(new AxisAngle(PI / 2, 0, 0, 1));
        double detectorDistance = 1.0;
        Line line;
        Line expected;

        plane = new Plane(1, 0, 0);
        line =
                Calculations.getLineFromPlane(plane.toVector3D(),
                        detectorDistance, rotation);
        expected = new Line(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(0, 1, 0);
        line =
                Calculations.getLineFromPlane(plane.toVector3D(),
                        detectorDistance, rotation);
        expected = new Line(Double.POSITIVE_INFINITY, 0.0);
        assertTrue(line.equals(expected, 1e-7));

        plane = new Plane(0, 0, 1);
        line =
                Calculations.getLineFromPlane(plane.toVector3D(),
                        detectorDistance, rotation);
        expected = new Line(0.0, 0.0);
        assertTrue(line.equals(expected, 1e-7));
    }



    @Test
    public void testGetBandHalfWidths() {
        Line2D line;
        double planeSpacing = 3.0;
        double energy = 20e3;
        Magnitude dd = new Magnitude(1.0, "m"); // Detector distance
        Magnitude[] halfWidths;

        line = Line2D.fromSlopeIntercept(0.0, new Magnitude(0.0, "m"));
        halfWidths =
                Calculations.getBandHalfWidths(planeSpacing, line, dd, energy);
        assertEquals(0.0139204, halfWidths[0].getValue("m"), 1e-7);
        assertEquals(0.0139204, halfWidths[1].getValue("m"), 1e-7);

        line =
                Line2D.fromSlopeIntercept(Double.POSITIVE_INFINITY,
                        new Magnitude(0.0, "m"));
        halfWidths =
                Calculations.getBandHalfWidths(planeSpacing, line, dd, energy);
        assertEquals(0.0139204, halfWidths[0].getValue("m"), 1e-7);
        assertEquals(0.0139204, halfWidths[1].getValue("m"), 1e-7);
    }



    @Test
    public void testGetBandWidth() {
        Magnitude[] halfWidths =
                { new Magnitude(0.001, "m"), new Magnitude(0.0139204, "m") };

        Magnitude width = Calculations.getBandWidth(halfWidths);
        assertEquals(0.0139204 * 2, width.getValue("m"), 1e-7);
    }



    @Test
    public void testGetEdgesIntercepts() {
        Line2D line;
        double planeSpacing = 3.0;
        double energy = 20e3;
        Magnitude dd = new Magnitude(1.0, "m"); // Detector distance
        Line2D[] edgeIntercepts;

        line = Line2D.fromSlopeIntercept(0.0, new Magnitude(0.0, "m"));
        edgeIntercepts =
                Calculations.getEdgesIntercepts(planeSpacing, line, dd, energy);
        assertEquals(0.0139204,
                edgeIntercepts[0].getInterceptY().getValue("m"), 1e-7);
        assertEquals(-0.0139204,
                edgeIntercepts[1].getInterceptY().getValue("m"), 1e-7);

        line =
                Line2D.fromSlopeIntercept(Double.POSITIVE_INFINITY,
                        new Magnitude(0.0, "m"));
        edgeIntercepts =
                Calculations.getEdgesIntercepts(planeSpacing, line, dd, energy);
        assertEquals(0.0139204,
                edgeIntercepts[1].getInterceptX().getValue("m"), 1e-7);
        assertEquals(-0.0139204,
                edgeIntercepts[0].getInterceptX().getValue("m"), 1e-7);
    }
}
