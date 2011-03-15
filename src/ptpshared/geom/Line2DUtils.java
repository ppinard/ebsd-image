package ptpshared.geom;

import java.awt.geom.Line2D;

/**
 * Operations on <code>Line2D</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Line2DUtils {
    /**
     * Checks whether two <code>Line2D</code> are equal to a certain precision.
     * 
     * @param l1
     *            line 1
     * @param l2
     *            line 2
     * @param precision
     *            precision
     * @return <code>true</code> if the two lines are equal to the specified
     *         precision, <code>false</code> otherwise
     */
    public static boolean equals(Line2D l1, Line2D l2, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "Precision cannot be less than zero.");

        if (l1 == null || l2 == null)
            return false;

        if (Point2DUtils.equals(l1.getP1(), l2.getP1(), precision)) {
            if (Point2DUtils.equals(l1.getP2(), l2.getP2(), precision))
                return true;

            // try crossing p1 and p2
        } else if (Point2DUtils.equals(l1.getP1(), l2.getP2(), precision)) {
            if (Point2DUtils.equals(l1.getP2(), l2.getP1(), precision))
                return true;
        }

        return false;
    }
}
