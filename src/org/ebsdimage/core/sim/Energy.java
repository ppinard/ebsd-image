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
package org.ebsdimage.core.sim;

import static java.lang.Math.abs;
import net.jcip.annotations.Immutable;
import ptpshared.utility.xml.ObjectXml;

/**
 * Stores the beam energy.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Energy implements ObjectXml {

    /** Value of the beam energy (in eV). */
    public final double value;



    /**
     * Creates a new <code>Energy</code> using the specified beam energy value.
     * 
     * @param value
     *            value of the beam energy (in eV)
     * @throws IllegalArgumentException
     *             if the energy is NaN
     * @throws IllegalArgumentException
     *             if the energy is infinite.
     */
    public Energy(double value) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException(
                    "Energy cannot be NaN (not a number).");
        if (Double.isInfinite(value))
            throw new IllegalArgumentException("Energy cannot be infinite.");

        this.value = value;
    }



    /**
     * Checks if this <code>Energy</code> is almost equal to the specified one
     * with the given precision.
     * 
     * @param other
     *            other <code>Energy</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>Energy</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean equals(Energy other, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (this == other)
            return true;
        if (other == null)
            return false;

        if (abs(value - other.value) >= precision)
            return false;

        return true;
    }



    /**
     * Checks if this <code>Energy</code> is exactly equal to the specified one.
     * 
     * @param obj
     *            other <code>Energy</code> to check equality
     * @return whether the two <code>Energy</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Energy other = (Energy) obj;
        if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
            return false;
        return true;
    }



    /**
     * Returns the value of the beam energy (in eV).
     * 
     * @return beam energy (in eV)
     */
    public Double getValue() {
        return value;
    }



    /**
     * Returns the hash code of this <code>Energy</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Returns a representation of this <code>Energy</code>, suitable for
     * debugging.
     * 
     * @return information about this <code>Energy</code>
     */
    @Override
    public String toString() {
        return value + " eV";
    }

}
