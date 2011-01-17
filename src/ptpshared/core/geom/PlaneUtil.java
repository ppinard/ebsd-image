package ptpshared.core.geom;

import ptpshared.core.math.Vector3D;

/**
 * Utilities to deal with <code>Plane</code>'s.
 * 
 * @author ppinard
 */
public class PlaneUtil {

    /**
     * Returns the intersection line of two planes.
     * <p/>
     * References:
     * http://www.softsurfer.com/Archive/algorithm_0104/algorithm_0104B.htm
     * 
     * @param plane0
     *            first plane
     * @param plane1
     *            second plane
     * @return intersecting line
     * @throws ArithmeticException
     *             if the two planes are parallel
     */
    public static Line3D planesIntersection(Plane plane0, Plane plane1) {
        Vector3D n3 = plane0.n.cross(plane1.n);

        // Check if the planes are parallel
        if (n3.norm() < 1e-6)
            throw new ArithmeticException("The planes (" + plane0 + " and "
                    + plane1 + ") are parallel.");

        // Calculate point along the intersecting line
        // (d2n1 - d1n2) x n3 / (n3^2)
        Vector3D p =
                plane0.n.multiply(plane1.getD()).minus(
                        plane1.n.multiply(plane0.getD()));
        p = p.cross(n3);
        p = p.div(n3.square());

        return new Line3D(p, n3);
    }



    /**
     * Returns the <code>Point3D</code> where the specified line intersects the
     * specified plane.
     * <p/>
     * References:
     * http://www.softsurfer.com/Archive/algorithm_0104/algorithm_0104B.htm
     * 
     * @param line
     *            a line
     * @param plane
     *            a plane
     * @return point of intersection
     * @throws ArithmeticException
     *             if the line and the plane are parallel
     * @throws ArithmeticException
     *             if the line is inside the plane
     */
    public static Vector3D linePlaneIntersection(Line3D line, Plane plane) {
        Vector3D w = line.p.minus(plane.p);

        double numerator = plane.n.dot(w) * -1;
        double denominator = plane.n.dot(line.v);
        double s = numerator / denominator;

        // Check for intersection
        // If s is infinite, the line and the plane are parallel
        if (Double.isInfinite(s))
            throw new ArithmeticException("The line (" + line
                    + ") does not intersect the plane (" + plane + ").");
        // If s is NaN, the line lies within the plane
        if (Double.isNaN(s))
            throw new ArithmeticException("The line (" + line
                    + ") is parallel and inside the plane (" + plane + ").");

        // Return point of intersection
        return line.getPointFromS(s);
    }
}
