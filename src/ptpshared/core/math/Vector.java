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

/**
 * Algebraic Euclidean vector. Defines a vector of any length. The class
 * implements vector addition and subtraction as well as scalar multiplication
 * and division. Other functions such as the dot product and the normalization
 * are also implemented.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Vector extends BaseVector {

    /**
     * Creates a new <code>Vector</code>.
     * 
     * @param data
     *            array of double values
     */
    public Vector(double... data) {
        super(data);
    }



    /**
     * Creates a new null <code>Vector</code> (all values are equal to 0.0).
     * 
     * @param size
     *            number of coordinates
     */
    public Vector(int size) {
        super(size);
    }



    @Override
    public Vector div(double scalar) {
        return (Vector) super.div(scalar);
    }



    @Override
    public Double dot(BaseVector other) {
        return super.dot(other).doubleValue();
    }



    @Override
    public Vector duplicate() {
        Vector output = new Vector(size());

        for (int i = 0; i < size(); i++)
            output.v[i] = v[i];

        return output;
    }



    @Override
    public Double get(int index) {
        return super.get(index).doubleValue();
    }



    @Override
    public Vector minus(BaseVector other) {
        return (Vector) super.minus(other);
    }



    @Override
    public Vector multiply(double scalar) {
        return (Vector) super.multiply(scalar);
    }



    @Override
    public Vector negate() {
        return (Vector) super.negate();
    }



    @Override
    public Vector normalize() {
        return (Vector) super.normalize();
    }



    @Override
    public Vector plus(BaseVector other) {
        return (Vector) super.plus(other);
    }



    @Override
    public Vector positive() {
        return (Vector) super.positive();
    }



    @Override
    public Double sum() {
        return super.sum().doubleValue();
    }

}
