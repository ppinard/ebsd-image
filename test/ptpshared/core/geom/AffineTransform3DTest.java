package ptpshared.core.geom;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class AffineTransform3DTest extends TestCase {

    private AffineTransform3D transform1;



    @Before
    public void setUp() throws Exception {
        Matrix3D m1 = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        Vector3D t1 = new Vector3D(1, 2, 3);
        transform1 = new AffineTransform3D(m1, t1);
    }



    @Test
    public void testAffineTransform3DDoubleArrayArray() {
        Matrix3D mExpected = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        Vector3D tExpected = new Vector3D(1, 2, 3);
        AffineTransform3D transform =
                new AffineTransform3D(new double[][] { { 0, 0, 1, 1 },
                        { 0, 1, 0, 2 }, { -1, 0, 0, 3 }, { 0, 0, 0, 1 } });

        assertEquals(mExpected, transform.getRotation(), 1e-6);
        assertEquals(tExpected, transform.getTranslation(), 1e-6);
    }



    @Test
    public void testAffineTransform3DMatrix3DVector3D() {
        Matrix3D mExpected = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        Vector3D tExpected = new Vector3D(1, 2, 3);

        assertEquals(mExpected, transform1.getRotation(), 1e-6);
        assertEquals(tExpected, transform1.getTranslation(), 1e-6);
    }



    @Test
    public void testClone() {
        AffineTransform3D clone = transform1.clone();
        assertEquals(transform1, clone, 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(transform1.equals(transform1));
        assertFalse(transform1.equals(null));
        assertFalse(transform1.equals(new Object()));

        Matrix3D m1 = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        Vector3D t1 = new Vector3D(99, 99, 99);
        AffineTransform3D diff = new AffineTransform3D(m1, t1);
        assertFalse(transform1.equals(diff));
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(transform1.equals(transform1, 1e-6));
        assertFalse(transform1.equals(null, 1e-6));
        assertFalse(transform1.equals(new Object(), 1e-6));

        Matrix3D m1 = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        Vector3D t1 = new Vector3D(99, 99, 99);
        AffineTransform3D diff = new AffineTransform3D(m1, t1);
        assertFalse(transform1.equals(diff, 1e-6));

        m1 = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        t1 = new Vector3D(1.01, 2.01, 3.01);
        AffineTransform3D same = new AffineTransform3D(m1, t1);
        assertTrue(transform1.equals(same, 1e-1));
    }



    @Test
    public void testGetRotation() {
        Matrix3D expected = new Matrix3D(0, 0, 1, 0, 1, 0, -1, 0, 0);
        assertEquals(expected, transform1.getRotation(), 1e-6);
    }



    @Test
    public void testGetTranslation() {
        Vector3D expected = new Vector3D(1, 2, 3);
        assertEquals(expected, transform1.getTranslation(), 1e-6);
    }



    @Test
    public void testInverse() {
        Matrix3D mexpected = new Matrix3D(0, 0, -1, 0, 1, 0, 1, 0, 0);
        Vector3D texpected = new Vector3D(3, -2, -1);

        AffineTransform3D actual = transform1.inverse();
        assertEquals(mexpected, actual.getRotation());
        assertEquals(texpected, actual.getTranslation());
    }



    @Test
    public void testInverse2() {
        Matrix3D m = new Matrix3D(0, 1, 0, -1, 0, 0, 0, 0, 1);
        Vector3D t = new Vector3D(2, 5, -3);

        AffineTransform3D actual = new AffineTransform3D(m, t).inverse();

        Matrix3D mexpected = new Matrix3D(0, -1, 0, 1, 0, 0, 0, 0, 1);
        Vector3D texpected = new Vector3D(5, -2, 3);

        assertEquals(mexpected, actual.getRotation());
        assertEquals(texpected, actual.getTranslation());
    }



    @Test
    public void testInverse3() {
        AffineTransform3D actual = transform1.inverse().inverse();
        assertEquals(transform1.getRotation(), actual.getRotation());
        assertEquals(transform1.getTranslation(), actual.getTranslation());
    }



    @Test
    public void testToString() {
        String expected =
                "0.0, 0.0, 1.0, 1.0\n0.0, 1.0, 0.0, 2.0\n-1.0, 0.0, 0.0, 3.0\n0.0, 0.0, 0.0, 1.0";
        assertEquals(expected, transform1.toString());
    }



    @Test
    public void testTransformPoint() {
        Vector3D point = new Vector3D(1, 0, 0);
        Vector3D expected = new Vector3D(1, 2, 2);
        Vector3D actual = transform1.transformPoint(point);
        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testTransformVector() {
        Vector3D vector = new Vector3D(1, 0, 0);
        Vector3D expected = new Vector3D(0, 0, -1);
        Vector3D actual = transform1.transformVector(vector);
        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testMultiply() {
        AffineTransform3D a =
                new AffineTransform3D(new double[][] { { 0, 0, -1, 1 },
                        { 1, 0, 0, 10 }, { 0, 1, 0, 0 }, { 0, 0, 0, 1 } });
        AffineTransform3D b =
                new AffineTransform3D(new double[][] { { 0, 1, 0, 0 },
                        { -1, 0, 0, 5 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });

        AffineTransform3D expected =
                new AffineTransform3D(new double[][] { { 0, 0, -1, 1 },
                        { 0, 1, 0, 10 }, { -1, 0, 0, 5 }, { 0, 0, 0, 1 } });
        AffineTransform3D actual = a.multiply(b);

        assertEquals(expected, actual, 1e-6);
    }

}
