package ptpshared.geom;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;

/**
 * Representation of a 3D plane.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Plane implements AlmostEquable, Transformable {

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
        if (n.getNorm() == 0.0)
            throw new IllegalArgumentException(
                    "A plane cannot have a null normal vector.");

        this.p = p;
        this.n = n;
    }



    /**
     * Returns the distance of this plane from the origin.
     * 
     * @return distance of this plane from the origin
     */
    public double distanceFromOrigin() {
        return Math.abs(getD()) / n.getNorm();
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        double delta = ((Number) precision).doubleValue();
        if (delta < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(delta))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Plane other = (Plane) obj;
        if (Math.abs(p.getX() - other.p.getX()) > delta)
            return false;
        if (Math.abs(p.getY() - other.p.getY()) > delta)
            return false;
        if (Math.abs(p.getZ() - other.p.getZ()) > delta)
            return false;

        if (Math.abs(n.getX() - other.n.getX()) > delta)
            return false;
        if (Math.abs(n.getY() - other.n.getY()) > delta)
            return false;
        if (Math.abs(n.getZ() - other.n.getZ()) > delta)
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
    public String toString() {
        return getA() + "x + " + getB() + "y + " + getC() + "z + " + getD();
    }



    @Override
    public Plane transform(AffineTransform3D t) {
        return new Plane(t.transformPoint(p), t.transformVector(n));
    }
}
