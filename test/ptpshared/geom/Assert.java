package ptpshared.geom;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

import rmlshared.geom.LineUtil;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class Assert {

    /**
     * Asserts that two 2D-arrays are equal.
     * 
     * @param expected
     *            expected array
     * @param actual
     *            actual array
     * @param delta
     *            the maximum delta between <code>expected</code> and
     *            <code>actual</code> for which both arrays are still considered
     *            equal.
     */
    public static void assertEquals(double[][] expected, double[][] actual,
            double delta) {
        if (expected.length != actual.length)
            fail("Arrays length are different (expected=" + expected.length
                    + "; actual=" + actual.length + ").");

        for (int i = 0; i < expected.length; i++)
            assertArrayEquals(expected[i], actual[i], delta);
    }



    /**
     * Asserts that two <code>Line2D</code> are equal.
     * 
     * @param expected
     *            expected line
     * @param actual
     *            actual line
     * @param delta
     *            the maximum delta between <code>expected</code> and
     *            <code>actual</code> for which both <code>Line2D</code> are
     *            still considered equal.
     */
    public static void assertEquals(Line2D expected, Line2D actual, double delta) {
        if (!Line2DUtils.equals(expected, actual, delta))
            fail(LineUtil.toString(expected) + " != "
                    + LineUtil.toString(actual));
    }



    /**
     * Asserts that two <code>Point2D</code> are equal.
     * 
     * @param expected
     *            expected point
     * @param actual
     *            actual point
     * @param delta
     *            the maximum delta between <code>expected</code> and
     *            <code>actual</code> for which both <code>Point2D</code> are
     *            still considered equal.
     */
    public static void assertEquals(Point2D expected, Point2D actual,
            double delta) {
        if (!Point2DUtils.equals(expected, actual, delta))
            fail(expected + " != " + actual);
    }



    /**
     * Asserts that two <code>Rotation</code> are equal.
     * 
     * @param expected
     *            expected rotation
     * @param actual
     *            actual rotation
     * @param delta
     *            the maximum delta between <code>expected</code> and
     *            <code>actual</code> for which both <code>Rotation</code> are
     *            still considered equal.
     */
    public static void assertEquals(Rotation expected, Rotation actual,
            double delta) {
        if (!RotationUtils.equals(expected, actual, delta))
            fail(RotationUtils.toString(expected) + " != "
                    + RotationUtils.toString(actual));
    }



    /**
     * Asserts that two <code>Vector3D</code> are equal.
     * 
     * @param expected
     *            expected vector
     * @param actual
     *            actual vector
     * @param delta
     *            the maximum delta between <code>expected</code> and
     *            <code>actual</code> for which both <code>Vector3D</code> are
     *            still considered equal.
     */
    public static void assertEquals(Vector3D expected, Vector3D actual,
            double delta) {
        if (!Vector3DUtils.equals(expected, actual, delta))
            fail(expected + " != " + actual);
    }
}
