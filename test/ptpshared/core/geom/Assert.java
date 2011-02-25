package ptpshared.core.geom;

import org.apache.commons.math.geometry.Vector3D;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class Assert {
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
        if (!(Math.abs(expected.getX() - actual.getX()) <= delta))
            fail(expected + " != " + actual);
        if (!(Math.abs(expected.getY() - actual.getY()) <= delta))
            fail(expected + " != " + actual);
        if (!(Math.abs(expected.getZ() - actual.getZ()) <= delta))
            fail(expected + " != " + actual);
    }



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
}
