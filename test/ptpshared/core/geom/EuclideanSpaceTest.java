package ptpshared.core.geom;

import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static ptpshared.core.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class EuclideanSpaceTest {

    private EuclideanSpace e0;

    private EuclideanSpace e1;

    private EuclideanSpace e2;



    @Before
    public void setUp() throws Exception {
        e0 = EuclideanSpace.ORIGIN;

        // x'=-y, y'=x, z'=z
        e1 =
                new EuclideanSpace(new Vector3D(0, -1, 0),
                        new Vector3D(1, 0, 0), new Vector3D(0, 0, 1),
                        new Vector3D(0, 5, 0));

        // x'=-y, y'=z, z'=-x
        e2 =
                new EuclideanSpace(new Vector3D(0, -1, 0),
                        new Vector3D(0, 0, 1), new Vector3D(-1, 0, 0),
                        new Vector3D(1, 10, 0));
    }



    @Test
    public void testEuclideanSpaceVector3DVector3DVector3D() {
        Vector3D iExpected = new Vector3D(0, -1, 0);
        Vector3D jExpected = new Vector3D(1, 0, 0);
        Vector3D kExpected = new Vector3D(0, 0, 1);
        Vector3D tExpected = new Vector3D(0, 5, 0);
        double[][] mExpected =
                new double[][] { { 0, 1, 0 }, { -1, 0, 0 }, { 0, 0, 1 } };
        AffineTransform3D transformExpected =
                new AffineTransform3D(mExpected, tExpected);

        assertEquals(iExpected, e1.i, 1e-6);
        assertEquals(jExpected, e1.j, 1e-6);
        assertEquals(kExpected, e1.k, 1e-6);
        assertEquals(tExpected, e1.translation, 1e-6);
        assertEquals(mExpected, e1.basisMatrix, 1e-6);
        assertEquals(transformExpected.getMatrix(),
                e1.affineTransformation.getMatrix(), 1e-6);
    }



    @Test
    public void testEuclideanSpaceVector3DVector3D() {
        Vector3D i = new Vector3D(0, 0, -1);
        Vector3D j = new Vector3D(0, 1, 0);
        Vector3D k = new Vector3D(1, 0, 0);
        EuclideanSpace e = new EuclideanSpace(i, j, k);

        double[][] mExpected =
                new double[][] { { 0, 0, 1 }, { 0, 1, 0 }, { -1, 0, 0 } };
        AffineTransform3D transformExpected =
                new AffineTransform3D(mExpected, new Vector3D(0.0, 0.0, 0.0));

        assertEquals(i, e.i, 1e-6);
        assertEquals(j, e.j, 1e-6);
        assertEquals(k, e.k, 1e-6);
        assertEquals(new Vector3D(0.0, 0.0, 0.0), e.translation, 1e-6);
        assertEquals(mExpected, e.basisMatrix, 1e-6);
        assertEquals(transformExpected.getMatrix(),
                e.affineTransformation.getMatrix(), 1e-6);
    }



    @Test
    public void testGetTransformationEquivalence() {
        AffineTransform3D f = e0.getTransformationTo(e1);
        AffineTransform3D b = e1.getTransformationFrom(e0);
        assertEquals(f.getMatrix(), b.getMatrix(), 1e-6);

        f = e0.getTransformationTo(e2);
        b = e2.getTransformationFrom(e0);
        assertEquals(f.getMatrix(), b.getMatrix(), 1e-6);

        f = e2.getTransformationTo(e1);
        b = e1.getTransformationFrom(e2);
        assertEquals(f.getMatrix(), b.getMatrix(), 1e-6);
    }



    @Test
    public void testGetTransformationE0toE1() {
        Line3D line = new Line3D(new Vector3D(1, 5, 0), new Vector3D(-1, 0, 1));

        AffineTransform3D f = e0.getTransformationTo(e1);

        Line3D expected =
                new Line3D(new Vector3D(0, 1, 0), new Vector3D(0, -1, 1));
        Line3D actual = line.transform(f);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testGetTransformationE0toE2() {
        Line3D line = new Line3D(new Vector3D(1, 5, 0), new Vector3D(-1, 0, 1));

        AffineTransform3D f = e0.getTransformationTo(e2);

        Line3D expected =
                new Line3D(new Vector3D(5, 0, 0), new Vector3D(0, 1, 1));
        Line3D actual = line.transform(f);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testGetTransformationE1toE2() {
        Line3D line = new Line3D(new Vector3D(0, 1, 0), new Vector3D(0, -1, 1));

        AffineTransform3D f = e1.getTransformationTo(e2);

        Line3D expected =
                new Line3D(new Vector3D(5, 0, 0), new Vector3D(0, 1, 1));
        Line3D actual = line.transform(f);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testGetTransformationE1toE0() {
        Line3D line = new Line3D(new Vector3D(0, 1, 0), new Vector3D(0, -1, 1));

        AffineTransform3D f = e1.getTransformationTo(e0);

        Line3D expected =
                new Line3D(new Vector3D(1, 5, 0), new Vector3D(-1, 0, 1));
        Line3D actual = line.transform(f);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testGetTransformationE2toE0() {
        Line3D line = new Line3D(new Vector3D(5, 0, 0), new Vector3D(0, 1, 1));

        AffineTransform3D f = e2.getTransformationTo(e0);

        Line3D expected =
                new Line3D(new Vector3D(1, 5, 0), new Vector3D(-1, 0, 1));

        Line3D actual = line.transform(f);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testGetTransformationE2toE1() {
        Line3D line = new Line3D(new Vector3D(5, 0, 0), new Vector3D(0, 1, 1));

        AffineTransform3D f = e2.getTransformationTo(e1);

        Line3D expected =
                new Line3D(new Vector3D(0, 1, 0), new Vector3D(0, -1, 1));

        Line3D actual = line.transform(f);

        assertEquals(expected, actual, 1e-6);
    }

}
