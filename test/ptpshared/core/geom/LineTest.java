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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Line2D;

import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Vector3D;

public class LineTest {

    private Line line1;



    private void compareLine2Ds(Line2D.Double a, Line2D.Double b) {
        assertEquals(a.x1, b.x1, 1e-7);
        assertEquals(a.y1, b.y1, 1e-7);
        assertEquals(a.x2, b.x2, 1e-7);
        assertEquals(a.y2, b.y2, 1e-7);
    }



    @Before
    public void setUp() throws Exception {
        line1 = new Line(1.0, 2.0);
    }



    @Test
    public void testEqualsLineDouble() {
        Line line = new Line(1.01, 2.01);
        assertTrue(line1.equals(line, 0.1));
    }



    @Test
    public void testEqualsObject() {
        Line expected = new Line(1.0, 2.0);
        assertEquals(expected, line1);
    }



    @Test
    public void testLineDoubleDouble() {
        assertEquals(1.0, line1.m, 1e-7);
        assertEquals(2.0, line1.k, 1e-7);

        Line line = new Line(Double.POSITIVE_INFINITY, 2.0);
        assertEquals(Double.POSITIVE_INFINITY, line.m, 1e-7);
        assertEquals(2.0, line.k, 1e-7);
    }



    @Test
    public void testToLine2D() {
        int width = 21;
        int height = 11;
        Line line;
        Line2D.Double points;
        Line2D.Double expected;

        // Horizontal at origin
        line = new Line(0.0, 0.0);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(-2.1, 5, 23.1, 5);
        compareLine2Ds(expected, points);

        // Horizontal above origin
        line = new Line(0.0, 0.1);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(-2.1, 2.9, 23.1, 2.9);
        compareLine2Ds(expected, points);

        // Horizontal below origin
        line = new Line(0.0, -0.2);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(-2.1, 9.2, 23.1, 9.2);
        compareLine2Ds(expected, points);

        // Vertical at origin
        line = new Line(Double.POSITIVE_INFINITY, 0.0);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(10, -1.1, 10, 12.1);
        compareLine2Ds(expected, points);

        // Vertical right of the origin
        line = new Line(Double.POSITIVE_INFINITY, 0.2);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(14.2, -1.1, 14.2, 12.1);
        compareLine2Ds(expected, points);

        // Vertical left of the origin
        line = new Line(Double.POSITIVE_INFINITY, -0.2);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(5.8, -1.1, 5.8, 12.1);
        compareLine2Ds(expected, points);

        // Oblique bottom-right corner to top-left corner
        line = new Line(1.0, 0.0);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(2.9, 12.1, 16.1, -1.1);
        compareLine2Ds(expected, points);

        // Oblique top-right corner to bottom-left corner
        line = new Line(-1.0, 0.0);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(3.9, -1.1, 17.1, 12.1);
        compareLine2Ds(expected, points);

        // Oblique (|m| < 1) bottom-right corner to top-left corner
        line = new Line(0.1, 0.0);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(-2.1, 6.21, 23.1, 3.69);
        compareLine2Ds(expected, points);

        // Oblique (|m| < 1) top-right corner to bottom-left corner
        line = new Line(-0.1, 0.0);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(-2.1, 3.79, 23.1, 6.31);
        compareLine2Ds(expected, points);

        // Oblique (k != 0) bottom-right corner to top-left corner
        line = new Line(1.0, -0.2);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(7.1, 12.1, 20.3, -1.1);
        compareLine2Ds(expected, points);

        // Oblique (k != 0) top-right corner to bottom-left corner
        line = new Line(-1.0, 0.2);
        points = line.toLine2D(width, height);
        expected = new Line2D.Double(8.1, -1.1, 21.3, 12.1);
        compareLine2Ds(expected, points);
    }



    @Test
    public void testToString() {
        String string = line1.toString();
        String expected = "(m=1.0, k=2.0)";
        assertEquals(expected, string);
    }



    @Test
    public void testToParametrizedLine() {
        Line3D paramLine;

        paramLine = line1.toLine3D(LinePlane.XY);
        assertTrue(paramLine.vector.equals(new Vector3D(0.70710, 0.70710, 0.0),
                1e-5));
        assertTrue(paramLine.point.equals(new Vector3D(-0.35355, 1.64644, 0.0),
                1e-5));

        paramLine = line1.toLine3D(LinePlane.XZ);
        assertTrue(paramLine.vector.equals(new Vector3D(0.70710, 0.0, 0.70710),
                1e-5));
        assertTrue(paramLine.point.equals(new Vector3D(-0.35355, 0.0, 1.64644),
                1e-5));

        paramLine = line1.toLine3D(LinePlane.YZ);
        assertTrue(paramLine.vector.equals(new Vector3D(0.0, 0.70710, 0.70710),
                1e-5));
        assertTrue(paramLine.point.equals(new Vector3D(0.0, -0.35355, 1.64644),
                1e-5));

    }

}
