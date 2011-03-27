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
import org.junit.Before;
import org.junit.Test;

import static ptpshared.geom.Assert.assertEquals;

public class PlaneUtilTest {

    @Before
    public void setUp() throws Exception {
    }



    @Test
    public void testLinePlaneIntersection() {
        // From
        // http://www.jtaylor1142001.net/calcjat/Solutions/VPlanes/VPtIntLPlane.htm

        Line3D line =
                new Line3D(new Vector3D(2, -3, 1), new Vector3D(-3, 1, -2));
        Plane plane = new Plane(4, -2, 2, -5);

        Vector3D actual = PlaneUtil.linePlaneIntersection(line, plane);
        Vector3D expected = new Vector3D(1.0 / 6.0, -43.0 / 18.0, -2.0 / 9.0);
        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testPlanesIntersection() {
        // From
        // http://www.math.umn.edu/~nykamp/m2374/readings/planeintex/

        Plane plane0 = new Plane(1, 1, 1, 1);
        Plane plane1 = new Plane(1, 2, 3, 4);

        Line3D actual = PlaneUtil.planesIntersection(plane0, plane1);
        Line3D expected =
                new Line3D(new Vector3D(2, -3, 0), new Vector3D(1, -2, 1));
        assertEquals(expected.v, actual.v, 1e-6);
        // Translation required for the point
        assertEquals(expected.p, actual.getPointFromS(4.0 / 3.0), 1e-6);
    }



    @Test(expected = ArithmeticException.class)
    public void testPlanesIntersectionException() {
        Plane plane0 = new Plane(1, 1, 1, 1);

        PlaneUtil.planesIntersection(plane0, plane0);
    }
}
