package ptpshared.geom;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.NotARotationMatrixException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

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
public class AffineTransform3D implements AlmostEquable {

    /** Threshold to consider the rotation matrix is special orthogonal. */
    private static final double THRESHOLD = 1e-7;



    /**
     * Creates a 4x4 matrix from a rotation matrix and a translation vector.
     * 
     * @param r
     *            a 3x3 matrix representing the rotation
     * @param t
     *            a 3x1 vector representing the translation
     * @return a 4x4 matrix
     */
    private static double[][] create4x4Matrix(double[][] r, double[] t) {
        // dimension check
        if ((r.length != 3) || (r[0].length != 3) || (r[1].length != 3)
                || (r[2].length != 3))
            throw new IllegalArgumentException(
                    "The rotation matrix must be a 3x3 matrix.");
        if (t.length != 3)
            throw new IllegalArgumentException("The translation array ("
                    + t.length + ") must have a length of 3.");

        double[][] out = new double[4][4];

        for (int i = 0; i < 3; i++)
            System.arraycopy(r[i], 0, out[i], 0, 3);
        out[3] = new double[] { 0, 0, 0, 1 };

        out[0][3] = t[0];
        out[1][3] = t[1];
        out[2][3] = t[2];

        return out;
    }

    /** 4x4 matrix. */
    private final double[][] m;



    /**
     * Creates a new affine transformation from a 4x4 array.
     * 
     * @param d
     *            4x4 array representing the affine transformation matrix.
     */
    public AffineTransform3D(double[][] d) {
        // dimension check
        if ((d.length != 4) || (d[0].length != 4) || (d[1].length != 4)
                || (d[2].length != 4) || (d[3].length != 4))
            throw new IllegalArgumentException(
                    "The matrix must be a 4x4 matrix.");

        // orthogonality
        if (!isSpecialOrthogonal(getRotationMatrix(d)))
            throw new IllegalArgumentException(
                    "The rotation matrix is not a special orthogonal matrix.");

        this.m = d;
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
    public AffineTransform3D(double[][] rotation, double[] translation) {
        this(create4x4Matrix(rotation, translation));
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
    public AffineTransform3D(Rotation rotation, Vector3D translation) {
        this(rotation.getMatrix(), Vector3DUtils.toArray(translation));
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
     * Returns a copy of the internal 4x4 matrix representing this affine
     * transformation.
     * 
     * @return 4x4 matrix
     */
    public double[][] getMatrix() {
        return m.clone();
    }



    /**
     * Returns the rotation matrix of this affine transformation.
     * 
     * @return rotation
     */
    public Rotation getRotation() {
        try {
            return new Rotation(getRotationMatrix(), THRESHOLD);
        } catch (NotARotationMatrixException e) {
            throw new RuntimeException("Cannot create rotation from matrix", e);
        }
    }



    /**
     * Returns the rotation matrix of this affine transformation.
     * 
     * @return rotation matrix
     */
    public double[][] getRotationMatrix() {
        return getRotationMatrix(m);
    }



    /**
     * Extracts and returns the rotation matrix from the specified 4x4 array.
     * 
     * @param m
     *            4x4 array
     * @return rotation matrix
     */
    private double[][] getRotationMatrix(double[][] m) {
        double[][] out = new double[3][3];

        for (int i = 0; i < 3; i++)
            System.arraycopy(m[i], 0, out[i], 0, 3);

        return out;

    }



    /**
     * Returns the translation vector of this affine transformation.
     * 
     * @return translation vector
     */
    public Vector3D getTranslation() {
        return new Vector3D(m[0][3], m[1][3], m[2][3]);
    }



    /**
     * Returns the transpose of a 3x3 matrix.
     * 
     * @param m
     *            3x3 matrix
     * @return transpose matrix
     */
    private double[][] getTranspose(double[][] m) {
        double[][] out = new double[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                out[i][j] = m[j][i];

        return out;
    }



    /**
     * Returns the inverse affine transformation.
     * 
     * @return inverse affine transformation
     */
    @CheckReturnValue
    public AffineTransform3D inverse() {
        // Use transpose() instead of inverse() since the rotation is SO3
        double[][] oldRotation = getRotationMatrix();
        double[][] newRotation = getTranspose(oldRotation);

        double[] oldTranslation = new double[] { m[0][3], m[1][3], m[2][3] };
        double[] newTranslation = new double[3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                newTranslation[i] -= newRotation[i][j] * oldTranslation[j];

        return new AffineTransform3D(newRotation, newTranslation);
    }



    /**
     * Returns a 3x3 matrix is a special orthogonal matrix.
     * <p/>
     * Conditions:
     * <ul>
     * <li>abs(det) == 1.0</li>
     * <li>AA^T=I (Wolfram MathWorld)</li>
     * </ul>
     * 
     * @param m
     *            a 3x3 matrix
     * @return <code>true</code> if the matrix is special orthogonal,
     *         <code>false</code> otherwise
     */
    private boolean isSpecialOrthogonal(double[][] m) {
        // determinant == 1.0
        double det =
                m[0][0] * (m[1][1] * m[2][2] - m[2][1] * m[1][2]) - m[1][0]
                        * (m[0][1] * m[2][2] - m[2][1] * m[0][2]) + m[2][0]
                        * (m[0][1] * m[1][2] - m[1][1] * m[0][2]);
        if (abs(det - 1.0) > THRESHOLD)
            return false;

        // AA^T = I
        double[][] transpose = getTranspose(m);
        double[][] p = new double[3][3]; // product

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    p[i][j] += m[i][k] * transpose[k][j];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (i == j && abs(p[i][j] - 1.0) > THRESHOLD)
                    return false;
                else if (i != j && abs(p[i][j]) > THRESHOLD)
                    return false;

        return true;
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
    @CheckReturnValue
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
