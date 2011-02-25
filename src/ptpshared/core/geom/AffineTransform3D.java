package ptpshared.core.geom;

import java.util.Arrays;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;
import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Vector3D;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import static java.lang.Math.abs;

/**
 * Affine transformation (rotation + translation) in Euclidean 3D space.
 * <p/>
 * Based on:
 * http://www.euclideanspace.com/maths/geometry/affine/matrix4x4/index.htm
 * 
 * @author ppinard
 */
@Immutable
public class AffineTransform3D implements Cloneable, AlmostEquable {

    /** 4x4 matrix. */
    private double m[][] = new double[4][4];



    /**
     * Creates a new affine transformation from a 4x4 array.
     * 
     * @param m
     *            4x4 array representing the affine transformation matrix.
     */
    public AffineTransform3D(double[][] m) {
        if (m == null)
            throw new NullPointerException("Array cannot be null.");
        if (m.length != 4)
            throw new IllegalArgumentException("The number of matrix rows ("
                    + m.length + ") must be 4.");
        if (m[0].length != 4)
            throw new IllegalArgumentException("The number of matrix columns ("
                    + m[0].length + ") must be 4.");

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (Double.isNaN(m[i][j]) || Double.isInfinite(m[i][j]))
                    throw new IllegalArgumentException(
                            "NaN and Infinite at position " + i + ", " + j
                                    + " is not accepted in Matrix3D.");
                this.m[i][j] = m[i][j];
            }
    }



    /**
     * Creates a new affine transformation from a rotation matrix and a
     * translation vector.
     * 
     * @param rotation
     *            rotation matrix (must be special orthogonal)
     * @param translation
     *            translation vector
     */
    public AffineTransform3D(Matrix3D rotation, Vector3D translation) {
        if (rotation == null)
            throw new NullPointerException("Rotation matrix cannot be null.");
        if (!rotation.isSpecialOrthogonal())
            throw new IllegalArgumentException(
                    "The matrix must be special orthogonal to represent a rotation.");
        if (translation == null)
            throw new NullPointerException("Translation vector cannot be null.");

        m[0][0] = rotation.get(0, 0);
        m[0][1] = rotation.get(0, 1);
        m[0][2] = rotation.get(0, 2);
        m[0][3] = translation.getX();

        m[1][0] = rotation.get(1, 0);
        m[1][1] = rotation.get(1, 1);
        m[1][2] = rotation.get(1, 2);
        m[1][3] = translation.getY();

        m[2][0] = rotation.get(2, 0);
        m[2][1] = rotation.get(2, 1);
        m[2][2] = rotation.get(2, 2);
        m[2][3] = translation.getZ();

        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;
    }



    @Override
    public AffineTransform3D clone() {
        return new AffineTransform3D(m);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AffineTransform3D other = (AffineTransform3D) obj;
        if (!Arrays.equals(m, other.m))
            return false;

        return true;
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

        AffineTransform3D other = (AffineTransform3D) obj;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (abs(m[i][j] - other.m[i][j]) > delta)
                    return false;

        return true;
    }



    /**
     * Returns the rotation matrix of this affine transformation.
     * 
     * @return rotation matrix
     */
    public Matrix3D getRotation() {
        return new Matrix3D(m[0][0], m[0][1], m[0][2], m[1][0], m[1][1],
                m[1][2], m[2][0], m[2][1], m[2][2]);
    }



    /**
     * Returns the translation vector of this affine transformation.
     * 
     * @return translation vector
     */
    public Vector3D getTranslation() {
        return new Vector3D(m[0][3], m[1][3], m[2][3]);
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(m);
        return result;
    }



    /**
     * Returns the inverse affine transformation.
     * 
     * @return inserve affine transformation
     */
    @CheckReturnValue
    public AffineTransform3D inverse() {
        // Use transpose() instead of inverse() since the rotation is SO3
        Matrix3D rotation = getRotation().transpose();
        Vector3D translation = rotation.multiply(getTranslation()).negate();
        return new AffineTransform3D(rotation, translation);
    }



    /**
     * Multiply this affine transform by the specified one.
     * 
     * @param other
     *            other affine transform
     * @return product of two affine transforms (this times other)
     */
    @CheckReturnValue
    public AffineTransform3D multiply(AffineTransform3D other) {
        double[][] data = new double[4][4];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                for (int k = 0; k < 4; k++)
                    data[i][j] += m[i][k] * other.m[k][j];

        return new AffineTransform3D(data);
    }



    /**
     * Returns a representation of the <code>AffineTransformation3D</code> ,
     * suitable for debugging.
     * 
     * @return information about the <code>AffineTransformation3D</code>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                str.append(m[i][j]);
                if (j < 3)
                    str.append(", ");
            }
            if (i < 3)
                str.append('\n');
        }

        return str.toString();
    }



    /**
     * Returns the transformed point after applying this affine transformation
     * on the specified point. The point is transformed by the rotation and
     * translation components of the affine transformation.
     * 
     * @param p
     *            a point
     * @return transformed point
     */
    public Vector3D transformPoint(Vector3D p) {
        double px = p.getX();
        double py = p.getY();
        double pz = p.getZ();

        double x = m[0][0] * px + m[0][1] * py + m[0][2] * pz + m[0][3];
        double y = m[1][0] * px + m[1][1] * py + m[1][2] * pz + m[1][3];
        double z = m[2][0] * px + m[2][1] * py + m[2][2] * pz + m[2][3];

        return new Vector3D(x, y, z);
    }



    /**
     * Returns the transformed vector after applying this affine transformation
     * on the specified vector. The vector is only transformed by the rotation
     * component of this affine transformation.
     * 
     * @param v
     *            a vector
     * @return transformed vector
     */
    @CheckReturnValue
    public Vector3D transformVector(Vector3D v) {
        double vx = v.getX();
        double vy = v.getY();
        double vz = v.getZ();

        double x = m[0][0] * vx + m[0][1] * vy + m[0][2] * vz;
        double y = m[1][0] * vx + m[1][1] * vy + m[1][2] * vz;
        double z = m[2][0] * vx + m[2][1] * vy + m[2][2] * vz;

        return new Vector3D(x, y, z);
    }

}
