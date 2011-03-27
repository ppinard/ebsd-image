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
package org.ebsdimage.core;

import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import rmlshared.util.Labeled;

/**
 * Error code used in <code>ErrorMap</code> to keep track of errors in an
 * experiment. An error code is defined by an index, a type of error and a
 * descripton of the error.
 * 
 * @author Philippe T. Pinard
 */
@Root
@Immutable
public class ErrorCode implements Labeled {

    /** Name of the error code. */
    @Attribute(name = "name")
    public final String name;

    /** Description of the error code. */
    @Attribute(name = "description")
    public final String description;



    /**
     * Creates a new <code>ErrorCode</code>. The description is empty.
     * 
     * @param name
     *            name of the error code
     * @throws IllegalArgumentException
     *             if the id is outside the [0, 255] range.
     * @throws NullPointerException
     *             if the type or the description is null
     * @throws IllegalArgumentException
     *             if the type is empty
     */
    public ErrorCode(String name) {
        this(name, "");
    }



    /**
     * Creates a new <code>ErrorCode</code>.
     * 
     * @param name
     *            name of the error code
     * @param description
     *            description of the error code
     * @throws IllegalArgumentException
     *             if the id is outside the [0, 255] range.
     * @throws NullPointerException
     *             if the type or the description is null
     * @throws IllegalArgumentException
     *             if the type is empty
     */
    public ErrorCode(@Attribute(name = "name") String name,
            @Attribute(name = "description") String description) {
        if (name == null)
            throw new NullPointerException("Error type cannot be null.");
        if (name.isEmpty())
            throw new IllegalArgumentException("Error type cannot be empty.");
        if (description == null)
            throw new NullPointerException("Error description cannot be null.");

        this.name = name;
        this.description = description;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        ErrorCode other = (ErrorCode) obj;
        if (!description.equals(other.description))
            return false;
        if (!name.equals(other.name))
            return false;

        return true;
    }



    @Override
    public String getLabel() {
        if (!description.isEmpty())
            return name + " (" + description + ")";
        else
            return name;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + description.hashCode();
        result = prime * result + name.hashCode();
        return result;
    }



    @Override
    public String toString() {
        return "ErrorCode [name=" + name + ", description=" + description + "]";
    }

}
