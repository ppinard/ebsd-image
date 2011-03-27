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

import junittools.core.AlmostEquable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Representation of the metadata held in the <code>EbsdMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
@Root(name = "metadata")
public class EbsdMetadata implements AlmostEquable {

    /** Default EBSD metadata. */
    public static final EbsdMetadata DEFAULT = new EbsdMetadata(
            new Microscope());

    /** Microscope configuration. */
    @Element(name = "microscope")
    private Microscope microscope;



    /**
     * Creates a new <code>EbsdMetadata</code>.
     * 
     * @param microscope
     *            microscope parameters
     */
    public EbsdMetadata(@Element(name = "microscope") Microscope microscope) {
        setMicroscope(microscope);
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        EbsdMetadata other = (EbsdMetadata) obj;
        if (!microscope.equals(other.microscope, precision))
            return false;

        return true;
    }



    /**
     * Returns the microscope configuration.
     * 
     * @return microscope configuration
     */
    public Microscope getMicroscope() {
        return microscope;
    }



    /**
     * Sets the microscope configuration.
     * 
     * @param microscope
     *            microscope configuration.
     */
    public void setMicroscope(Microscope microscope) {
        if (microscope == null)
            throw new NullPointerException("Microscope cannot be null.");
        this.microscope = microscope;
    }

}
