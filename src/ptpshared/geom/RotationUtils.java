package ptpshared.geom;

import org.apache.commons.math.geometry.Rotation;

/**
 * Operations on rotations.
 * 
 * @author Philippe T. Pinard
 */
public class RotationUtils {

    /**
     * Checks whether two <code>Rotation</code> are equal to a certain
     * precision. This is done using the
     * {@link Rotation#distance(Rotation, Rotation)} method of the
     * <code>Rotation</code> object.
     * 
     * @param r1
     *            rotation 1
     * @param r2
     *            rotation 2
     * @param precision
     *            precision (angular deviation allowed)
     * @return <code>true</code> if the two rotations are equal to the specified
     *         precision, <code>false</code> otherwise
     */
    public static boolean equals(Rotation r1, Rotation r2, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "Precision cannot be less than zero.");

        if (r1 == null || r2 == null)
            return false;

        if (Rotation.distance(r1, r2) > precision)
            return false;

        return true;
    }



    /**
     * Returns a string representation of a <code>Rotation</code>.
     * 
     * @param r
     *            a rotation
     * @return string representation
     */
    public static String toString(Rotation r) {
        return "[[" + r.getQ0() + "; " + r.getQ1() + "; " + r.getQ2() + "; "
                + r.getQ3() + "]]";
    }
}
