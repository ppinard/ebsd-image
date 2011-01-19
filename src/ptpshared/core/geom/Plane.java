package ptpshared.core.geom;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;
import ptpshared.core.math.Quaternion;
import ptpshared.core.math.QuaternionMath;
import ptpshared.core.math.Vector3D;

/**
 * Representation of a 3D plane.
 * 
 * @author ppinard
 */
@Immutable
public class Plane implements Rotatable, Translatable, Cloneable, AlmostEquable {

    /** Normal of the plane. */
    public final Vector3D n;

    /** Point on the plane. */
    public final Vector3D p;



    /**
     * Creates a new <code>Plane</code> from the
     * <code>ax + by + cz + d = 0</code> representation.
     * 
     * @param a
     *            first coordinate
     * @param b
     *            second coordinate
     * @param c
     *            third coordinate
     * @param d
     *            forth coordinate
     */
    public Plane(double a, double b, double c, double d) {
        this.n = new Vector3D(a, b, c);

        // Calculate the point
        if (Math.abs(a) > 1e-6)
            this.p = new Vector3D(-(d + b + c) / a, 1, 1);
        else if (Math.abs(b) > 1e-6)
            this.p = new Vector3D(1, -(d + a + c) / b, 1);
        else if (Math.abs(c) > 1e-6)
            this.p = new Vector3D(1, 1, -(d + a + b) / c);
        else
            throw new IllegalArgumentException(
                    "A plane cannot have a null normal vector.");
    }



    /**
     * Creates a new <code>Plane</code>.
     * 
     * @param p
     *            point on the plane
     * @param n
     *            normal of the plane
     */
    public Plane(Vector3D p, Vector3D n) {
        if (p == null)
            throw new NullPointerException("The point cannot be null.");
        if (n == null)
            throw new NullPointerException("The normal cannot be null.");
        if (n.norm() == 0.0)
            throw new IllegalArgumentException(
                    "A plane cannot have a null normal vector.");

        this.p = p;
        this.n = n;
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Plane(p, n);
    }



    /**
     * Returns the distance of this plane from the origin.
     * 
     * @return distance of this plane from the origin
     */
    public double distanceFromOrigin() {
        return Math.abs(getD()) / n.norm();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Plane other = (Plane) obj;
        if (!n.equals(other.n))
            return false;
        if (!p.equals(other.p))
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Plane other = (Plane) obj;
        if (!n.equals(other.n, precision))
            return false;
        if (!p.equals(other.p, precision))
            return false;

        return true;
    }



    /**
     * Returns the <code>a</code> coordinate in the
     * <code>ax + by + cz + d = 0</code> representation.
     * 
     * @return <code>a</code> coordinate
     */
    public double getA() {
        return n.getX();
    }



    /**
     * Returns the <code>b</code> coordinate in the
     * <code>ax + by + cz + d = 0</code> representation.
     * 
     * @return <code>b</code> coordinate
     */
    public double getB() {
        return n.getY();
    }



    /**
     * Returns the <code>c</code> coordinate in the
     * <code>ax + by + cz + d = 0</code> representation.
     * 
     * @return <code>c</code> coordinate
     */
    public double getC() {
        return n.getZ();
    }



    /**
     * Returns the <code>d</code> coordinate in the
     * <code>ax + by + cz + d = 0</code> representation.
     * 
     * @return <code>d</code> coordinate
     */
    public double getD() {
        return -n.getX() * p.getX() - n.getY() * p.getY() - n.getZ() * p.getZ();
    }



    /**
     * Returns the intercept of this plane with the x-axis.
     * 
     * @return intercept of this plane with the x-axis
     */
    public double getInterceptX() {
        double x = -getD() / getA();

        if (Double.isInfinite(x))
            throw new ArithmeticException(
                    "The plane does not intersect the x-axis.");

        return x;
    }



    /**
     * Returns the intercept of this plane with the y-axis.
     * 
     * @return intercept of this plane with the y-axis
     */
    public double getInterceptY() {
        double y = -getD() / getB();

        if (Double.isInfinite(y))
            throw new ArithmeticException(
                    "The plane does not intersect the y-axis.");

        return y;
    }



    /**
     * Returns the intercept of this plane with the z-axis.
     * 
     * @return intercept of this plane with the z-axis
     */
    public double getInterceptZ() {
        double z = -getD() / getC();

        if (Double.isInfinite(z))
            throw new ArithmeticException(
                    "The plane does not intersect the z-axis.");

        return z;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((n == null) ? 0 : n.hashCode());
        result = prime * result + ((p == null) ? 0 : p.hashCode());
        return result;
    }



    @Override
    public Plane rotate(Quaternion r) {
        Vector3D newNormal = QuaternionMath.rotate(n, r);
        return new Plane(p, newNormal);
    }



    @Override
    public String toString() {
        return getA() + "x + " + getB() + "y + " + getC() + "z + " + getD();
    }



    @Override
    public Plane translate(Vector3D t) {
        Vector3D newPoint = p.plus(t);
        return new Plane(newPoint, n);
    }
}
