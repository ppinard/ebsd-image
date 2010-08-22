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
package ptpshared.core.math;

import ptpshared.utility.xml.ObjectXml;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * Algebraic vector in 3 dimensions (size = 3). It implements all the operations
 * from {@link BaseVector} as well as the cross product.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3D extends BaseVector implements ObjectXml {

    /**
     * Creates a new <code>Vector3D</code> with all coordinates are 0.
     */
    public Vector3D() {
        super(0, 0, 0);
    }



    /**
     * Creates a new <code>Vector3D</code> from a <code>Vector</code>. Only the
     * first three coordinates are considered.
     * 
     * @param vector
     *            vector of any length
     */
    public Vector3D(BaseVector vector) {
        super(3);

        for (int i = 0; i < 3; i++)
            v[i] = vector.v[i];
    }



    /**
     * Creates a new <code>Vector3D</code> from three coordinates.
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @param z
     *            z coordinate
     */
    public Vector3D(double x, double y, double z) {
        super(x, y, z);
    }



    /**
     * Creates a new <code>Vector3D</code> from an array of double.
     * 
     * @param data
     *            an array containing three coordinates
     */
    public Vector3D(double[] data) {
        super(3);

        if (data.length != 3)
            throw new IllegalArgumentException("Data must contains 3 numbers.");

        for (int i = 0; i < 3; i++)
            v[i] = data[i];
    }



    /**
     * Returns a new <code>Vector3D</code> resulting from the cross product
     * between this vector and the specified one.
     * 
     * @param other
     *            other vector
     * @return resultant vector
     */
    @CheckReturnValue
    public Vector3D cross(Vector3D other) {
        return new Vector3D(v[1] * other.v[2] - v[2] * other.v[1], v[2]
                * other.v[0] - v[0] * other.v[2], v[0] * other.v[1] - v[1]
                * other.v[0]);
    }



    @Override
    public Vector3D div(double scalar) {
        return (Vector3D) super.div(scalar);
    }



    @Override
    public Double dot(BaseVector other) {
        return super.dot(other).doubleValue();
    }



    @Override
    public Vector3D duplicate() {
        return new Vector3D(v[0], v[1], v[2]);
    }



    @Override
    public Double get(int index) {
        return super.get(index).doubleValue();
    }



    @Override
    public Vector3D minus(BaseVector other) {
        return (Vector3D) super.minus(other);
    }



    @Override
    public Vector3D multiply(double value) {
        return (Vector3D) super.multiply(value);
    }



    @Override
    public Vector3D negate() {
        return (Vector3D) super.negate();
    }



    @Override
    public Vector3D normalize() {
        return (Vector3D) super.normalize();
    }



    @Override
    public Vector3D plus(BaseVector other) {
        return (Vector3D) super.plus(other);
    }



    @Override
    public Vector3D positive() {
        return (Vector3D) super.positive();
    }



    @Override
    public Double sum() {
        return super.sum().doubleValue();
    }

}
