package ptpshared.core.geom;

import net.jcip.annotations.Immutable;
import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Vector3D;
import ptpshared.core.math.Vector3DMath;

/**
 * Representation of an Euclidean space.
 * 
 * @author ppinard
 */
@Immutable
public class EuclideanSpace {

    /** Euclidean space of the origin. */
    public static final EuclideanSpace ORIGIN =
            new EuclideanSpace(new Vector3D(1, 0, 0), new Vector3D(0, 1, 0),
                    new Vector3D(0, 0, 1));

    /** Vector of basis i. */
    public final Vector3D i;

    /** Vector of basis j. */
    public final Vector3D j;

    /** Vector of basis k. */
    public final Vector3D k;

    /** Translation vector. */
    public final Vector3D translation;

    /** Matrix of the basis vectors. */
    public final Matrix3D basisMatrix;

    /** Affine transform representing the Euclidean space. */
    public final AffineTransform3D affineTransformation;



    /**
     * Creates a new Euclidean space with three orthonormal basis vectors and a
     * translation vector.
     * 
     * @param i
     *            first basis vector
     * @param j
     *            second basis vector
     * @param k
     *            third basis vector
     * @param t
     *            translation vector
     */
    public EuclideanSpace(Vector3D i, Vector3D j, Vector3D k, Vector3D t) {
        if (i == null)
            throw new NullPointerException("Basic vector i cannot be null.");
        if (i.norm() == 0.0)
            throw new IllegalArgumentException(
                    "Basic vector i cannot be a null vector.");
        this.i = i.normalize();

        if (j == null)
            throw new NullPointerException("Basic vector j cannot be null.");
        if (j.norm() == 0.0)
            throw new IllegalArgumentException(
                    "Basic vector j cannot be a null vector.");
        this.j = j.normalize();

        if (k == null)
            throw new NullPointerException("Basic vector k cannot be null.");
        if (k.norm() == 0.0)
            throw new IllegalArgumentException(
                    "Basic vector k cannot be a null vector.");
        this.k = k.normalize();

        if (Vector3DMath.tripleProduct(i, j, k) == 0)
            throw new IllegalArgumentException(
                    "Vectors i, j and k must be orthogonal.");

        if (t == null)
            throw new NullPointerException("Translation vector cannot be null.");
        translation = t;

        basisMatrix =
                new Matrix3D(i.getX(), j.getX(), k.getX(), i.getY(), j.getY(),
                        k.getY(), i.getZ(), j.getZ(), k.getZ());
        if (!basisMatrix.isSpecialOrthogonal())
            throw new AssertionError("The matrix is not orthogonal.");

        affineTransformation = new AffineTransform3D(basisMatrix, t);
    }



    /**
     * Creates a new Euclidean space from the basis' matrix (columns are the
     * basis vectors) and the translation vector.
     * 
     * @param m
     *            basis' matrix
     * @param t
     *            translation vector
     */
    public EuclideanSpace(Matrix3D m, Vector3D t) {
        if (m == null)
            throw new NullPointerException("basis' matrix cannot be null.");
        if (!m.isSpecialOrthogonal())
            throw new IllegalArgumentException("The matrix is not orthogonal.");
        basisMatrix = m;

        if (t == null)
            throw new NullPointerException("Translation vector cannot be null.");
        translation = t;

        i = new Vector3D(m.get(0, 0), m.get(1, 0), m.get(2, 0));
        j = new Vector3D(m.get(0, 1), m.get(1, 1), m.get(2, 1));
        k = new Vector3D(m.get(0, 2), m.get(1, 2), m.get(2, 2));

        if (Vector3DMath.tripleProduct(i, j, k) == 0)
            throw new AssertionError("Vectors i, j and k must be orthogonal.");

        affineTransformation = new AffineTransform3D(m, t);
    }



    /**
     * Creates a new Euclidean space with three orthonormal basis vectors.
     * 
     * @param i
     *            first basis vector
     * @param j
     *            second basis vector
     * @param k
     *            third basis vector
     */
    public EuclideanSpace(Vector3D i, Vector3D j, Vector3D k) {
        this(i, j, k, new Vector3D());
    }



    /**
     * Returns the transformation from the specified referential frame to the
     * current one. For a point or a vector expressed in the specified
     * referential frame, the transformation will convert this point or vector
     * to the current referential frame.
     * 
     * @param e
     *            source
     * @return transformation from the specified referential frame to the
     *         current one
     */
    public AffineTransform3D getTransformationFrom(EuclideanSpace e) {
        return affineTransformation.inverse().multiply(e.affineTransformation);
    }



    /**
     * Returns the transformation from the current referential frame to the
     * specified one. For a point or a vector expressed in the current
     * referential frame, the transformation will convert this point or vector
     * to the specified referential frame.
     * 
     * @param e
     *            destination
     * @return transformation from this referential frame to the specified one
     */
    public AffineTransform3D getTransformationTo(EuclideanSpace e) {
        return e.affineTransformation.inverse().multiply(affineTransformation);
    }
}
