/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ptpshared.geom;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.apache.commons.math.geometry.Vector3D;

/**
 * Representation of an Euclidean space.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class EuclideanSpace implements AlmostEquable {

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
    public final double[][] basisMatrix;

    /** Affine transform representing the Euclidean space. */
    public final AffineTransform3D affineTransformation;



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
        this(i, j, k, new Vector3D(0.0, 0.0, 0.0));
    }



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
        if (i.getNorm() == 0.0)
            throw new IllegalArgumentException(
                    "Basic vector i cannot be a null vector.");
        this.i = i.normalize();

        if (j == null)
            throw new NullPointerException("Basic vector j cannot be null.");
        if (j.getNorm() == 0.0)
            throw new IllegalArgumentException(
                    "Basic vector j cannot be a null vector.");
        this.j = j.normalize();

        if (k == null)
            throw new NullPointerException("Basic vector k cannot be null.");
        if (k.getNorm() == 0.0)
            throw new IllegalArgumentException(
                    "Basic vector k cannot be a null vector.");
        this.k = k.normalize();

        if (Vector3DUtils.tripleProduct(i, j, k) == 0)
            throw new IllegalArgumentException(
                    "Vectors i, j and k must be orthogonal.");

        if (t == null)
            throw new NullPointerException("Translation vector cannot be null.");
        translation = t;

        basisMatrix =
                new double[][] { { i.getX(), j.getX(), k.getX() },
                        { i.getY(), j.getY(), k.getY() },
                        { i.getZ(), j.getZ(), k.getZ() } };

        affineTransformation =
                new AffineTransform3D(basisMatrix, Vector3DUtils.toArray(t));
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        EuclideanSpace other = (EuclideanSpace) obj;
        if (!affineTransformation.equals(other.affineTransformation, precision))
            return false;

        return true;
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
