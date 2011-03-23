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
