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
package org.ebsdimage.core.run;

import java.io.IOException;

import net.jcip.annotations.Immutable;

import org.ebsdimage.core.ErrorCode;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import ptpshared.util.AlmostEquable;

/**
 * Superclass for all operations.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
@Root
public abstract class Operation implements AlmostEquable {

    /**
     * Position of the operation in the array of operations. Used only in
     * serialization and deserialization.
     */
    private int index = 0;



    /**
     * Checks if this <code>Operation</code> is equal to the specified one. If
     * no other argument is defined, two operations are equal if they have the
     * same tag name.
     * 
     * @param obj
     *            an <code>Operation</code>
     * @return <code>true</code> if two operations are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Operation other = (Operation) obj;
        if (!getName().equals(other.getName()))
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Operation other = (Operation) obj;
        if (!getName().equals(other.getName()))
            return false;

        return true;
    }



    /**
     * Returns the error codes associated with the operation. By default, no
     * error codes are defined.
     * 
     * @return error codes
     */
    public ErrorCode[] getErrorCodes() {
        return new ErrorCode[0];
    }



    /**
     * Returns the position of the operation in the array of operations. Used
     * only in serialization and deserialization.
     * 
     * @return position of the operation
     */
    @Attribute(name = "opIndex")
    public int getIndex() {
        return this.index;
    }



    /**
     * Returns the name of this operation which corresponds to the simple name
     * of the class.
     * 
     * @return name
     */
    public String getName() {
        return getClass().getSimpleName();
    }



    /**
     * Returns the hash code of this operation which is calculated using
     * {@link #getName()}.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getName().hashCode();
        return result;
    }



    /**
     * Sets the position of the operation in the array of operations. Used only
     * in serialization and deserialization.
     * 
     * @param index
     *            position of the operation
     */
    @Attribute(name = "opIndex")
    public void setIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException(
                    "Index must be greater or equal to zero.");
        this.index = index;
    }



    /**
     * Initialises the operation before running an experiment.
     * 
     * @param run
     *            run executing this method
     * @throws IOException
     *             if an error occurs during the initialization
     */
    public void setUp(Run run) throws IOException {

    }



    /**
     * Flushes the operation after running an experiment.
     * 
     * @param run
     *            run executing this method
     * @throws IOException
     *             if an error occurs during the flush
     */
    public void tearDown(Run run) throws IOException {

    }



    /**
     * Returns a representation of this <code>Operation</code>, suitable for
     * debugging.
     * 
     * @return information about this <code>Operation</code>
     */
    @Override
    public String toString() {
        return getName() + " []";
    }

}
