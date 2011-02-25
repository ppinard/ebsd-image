package ptpshared.core.geom;

import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class PlaneTest {

    Plane p1;

    Plane p2;



    @Before
    public void setUp() throws Exception {
        p1 = new Plane(new Vector3D(1, 2, 3), new Vector3D(4, 5, 6));
        p2 = new Plane(3, 4, 0, 5);
    }



    @Test
    public void testDistanceFromOrigin() {
        assertEquals(32.0 / p1.n.norm(), p1.distanceFromOrigin(), 1e-6);
        assertEquals(5 / p2.n.norm(), p2.distanceFromOrigin(), 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(p1.equals(p1));

        assertFalse(p1.equals(null));

        assertFalse(p1.equals(new Object()));

        assertTrue(p1.equals(new Plane(new Vector3D(1, 2, 3), new Vector3D(4,
                5, 6))));

        assertFalse(p1.equals(new Plane(new Vector3D(99, 2, 3), new Vector3D(4,
                5, 6))));

        assertFalse(p1.equals(new Plane(new Vector3D(1, 2, 3), new Vector3D(99,
                5, 6))));
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(p1.equals(p1, 1e-3));

        assertFalse(p1.equals(null, 1e-3));

        assertFalse(p1.equals(new Object(), 1e-3));

        assertTrue(p1.equals(new Plane(new Vector3D(1.0001, 2, 3),
                new Vector3D(4, 5, 6)), 1e-3));

        assertTrue(p1.equals(new Plane(new Vector3D(1, 2, 3), new Vector3D(
                4.0001, 5, 6)), 1e-3));

        assertFalse(p1.equals(new Plane(new Vector3D(1.1, 2, 3), new Vector3D(
                4, 5, 6))));

        assertFalse(p1.equals(new Plane(new Vector3D(1, 2, 3), new Vector3D(
                4.1, 5, 6))));
    }



    @Test
    public void testGetA() {
        assertEquals(p1.n.getX(), p1.getA(), 1e-6);
        assertEquals(p2.n.getX(), p2.getA(), 1e-6);
    }



    @Test
    public void testGetB() {
        assertEquals(p1.n.getY(), p1.getB(), 1e-6);
        assertEquals(p2.n.getY(), p2.getB(), 1e-6);
    }



    @Test
    public void testGetC() {
        assertEquals(p1.n.getZ(), p1.getC(), 1e-6);
        assertEquals(p2.n.getZ(), p2.getC(), 1e-6);
    }



    @Test
    public void testGetD() {
        assertEquals(-32, p1.getD(), 1e-6);
        assertEquals(5, p2.getD(), 1e-6);
    }



    @Test
    public void testGetInterceptX() {
        assertEquals(32.0 / 4.0, p1.getInterceptX(), 1e-6);
        assertEquals(-5.0 / 3.0, p2.getInterceptX(), 1e-6);
    }



    @Test
    public void testGetInterceptY() {
        assertEquals(32.0 / 5.0, p1.getInterceptY(), 1e-6);
        assertEquals(-5.0 / 4.0, p2.getInterceptY(), 1e-6);
    }



    @Test
    public void testGetInterceptZ() {
        assertEquals(32.0 / 6.0, p1.getInterceptZ(), 1e-6);
    }



    @Test(expected = ArithmeticException.class)
    public void testGetInterceptZException() {
        p2.getInterceptZ();
    }



    @Test
    public void testPlaneDoubleDoubleDoubleDouble() {
        assertEquals(3, p2.getA(), 1e-6);
        assertEquals(4, p2.getB(), 1e-6);
        assertEquals(0, p2.getC(), 1e-6);
        assertEquals(5, p2.getD(), 1e-6);

        assertEquals(new Vector3D(3, 4, 0), p2.n, 1e-6);
        assertEquals(new Vector3D(-(5 + 4 + 0) / 3, 1, 1), p2.p, 1e-6);
    }



    @Test
    public void testPlaneVector3DVector3D() {
        assertEquals(new Vector3D(1, 2, 3), p1.p, 1e-6);
        assertEquals(new Vector3D(4, 5, 6), p1.n, 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "4.0x + 5.0y + 6.0z + -32.0";
        assertEquals(expected, p1.toString());
    }

}
