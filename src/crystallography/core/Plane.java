/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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
package crystallography.core;

import static java.lang.Math.abs;
import static ptpshared.core.math.Fractions.gcd;
import ptpshared.core.math.BaseVector;
import ptpshared.core.math.Vector;
import ptpshared.core.math.Vector3D;
import ptpshared.utility.xml.ObjectXml;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * Defines a crystallographic plane. A crystallographic planes is defined by
 * three integer indices given the intersection of this plane with the crystal
 * axes (Miller indices).
 * <p/>
 * The class extends the <code>BaseVector</code> class, so it can be used in
 * some vectorial operations that keep the integer values of the plane. To
 * further vectorial calculations, the plane should be converted to a
 * {@link Vector3D} using the method {@link #toVector3D}.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Plane extends BaseVector implements ObjectXml {

    /**
     * Creates a plane from h, k and l indices.
     * 
     * @param h
     *            first index
     * @param k
     *            second index
     * @param l
     *            third index
     * 
     * @throws InvalidPlaneException
     *             if the indices are all equal to zero
     */
    public Plane(int h, int k, int l) {
        super(h, k, l);

        if (h == 0 && k == 0 && l == 0)
            throw new InvalidPlaneException("A plane cannot be a zero vector.");
    }



    /**
     * Creates a plane from a 3d vector containing Miller indices.
     * 
     * @param vector
     *            indices
     * @throws InvalidPlaneException
     *             if the indices are all equal to zero
     */
    public Plane(Vector3D vector) {
        this(vector.get(0).intValue(), vector.get(1).intValue(), vector.get(2)
                .intValue());
    }



    /**
     * Returns the cross product between this <code>Plane</code> and the
     * specified one.
     * 
     * @param other
     *            other plane
     * @return resultant plane
     * @throws InvalidPlaneException
     *             the resultant plane is invalid.
     */
    @CheckReturnValue
    public Plane cross(Plane other) {
        return new Plane((int) (v[1] * other.v[2] - v[2] * other.v[1]),
                (int) (v[2] * other.v[0] - v[0] * other.v[2]), (int) (v[0]
                        * other.v[1] - v[1] * other.v[0]));
    }



    @Override
    public Double dot(BaseVector other) {
        return super.dot(other).doubleValue();
    }



    /**
     * Returns the dot product between this <code>Plane</code> and the specified
     * one.
     * 
     * @param other
     *            plane
     * 
     * @return dot product
     */
    public Integer dot(Plane other) {
        return super.dot(other).intValue();
    }



    /**
     * Creates a duplicate of the plane.
     * 
     * @return a plane
     * @throws RuntimeException
     *             if the plane is invalid
     */
    @Override
    public Plane duplicate() {
        try {
            return new Plane((int) v[0], (int) v[1], (int) v[2]);
        } catch (InvalidPlaneException e) {
            throw new RuntimeException("Cannot duplicate an invalid plane.", e);
        }
    }



    /**
     * Checks if this <code>Plane</code> is exactly equal to the specified one.
     * 
     * @param obj
     *            other <code>Plane</code> to check equality
     * 
     * @return whether the two <code>Plane</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Plane other = (Plane) obj;
        for (int i = 0; i < 3; i++)
            if ((int) v[i] != (int) other.v[i])
                return false;

        return true;
    }



    @Override
    public Integer get(int index) {
        return super.get(index).intValue();
    }



    /**
     * {@inheritDoc}
     * <p/>
     * Overrides {@link Vector3D#hashCode()} to get hash code from integer
     * components instead of double.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) v[0];
        result = prime * result + (int) v[1];
        result = prime * result + (int) v[2];
        return result;
    }



    @Override
    public Plane negate() {
        return (Plane) super.negate();
    }



    @Override
    public Plane positive() {
        return (Plane) super.positive();
    }



    /**
     * Simplifies the indices so that the largest common integer of the plane's
     * indices is 1.
     * 
     * @return a simplified plane
     */
    @CheckReturnValue
    public Plane simplify() {
        int commonDenominator = gcd((int) v[0], gcd((int) v[1], (int) v[2]));

        if (v[0] < 0)
            commonDenominator = -abs(commonDenominator);
        else
            commonDenominator = abs(commonDenominator);

        BaseVector simplifiedVector = div(commonDenominator);

        return new Plane(simplifiedVector.get(0).intValue(), simplifiedVector
                .get(1).intValue(), simplifiedVector.get(2).intValue());
    }



    @Override
    public Integer sum() {
        return super.sum().intValue();
    }



    /**
     * Returns the Bravais indices of the plane.
     * 
     * @return Bravais indices
     */
    public Vector toBravais() {
        return PlaneConversion.millerToBravais(this);
    }



    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(128);
        str.append('(');
        for (int n = 0; n < size(); n++) {
            str.append((int) v[n]);
            str.append(';');
        }
        if (size() > 0)
            str.setLength(str.length() - 1);
        str.append(')');
        return str.toString();
    }



    /**
     * Returns the <code>Vector3D</code> equivalent to the plane.
     * 
     * @return a <code>Vector3D</code>
     */
    public Vector3D toVector3D() {
        return new Vector3D(v[0], v[1], v[2]);
    }
}
