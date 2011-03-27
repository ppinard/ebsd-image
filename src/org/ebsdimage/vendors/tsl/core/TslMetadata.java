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
package org.ebsdimage.vendors.tsl.core;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.Microscope;
import org.simpleframework.xml.Element;

/**
 * Metadata held in a <code>TslMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class TslMetadata extends EbsdMetadata {

    /**
     * Creates a <code>HklMetadata</code> with the required parameters.
     * 
     * @param microscope
     *            microscope configuration
     */
    public TslMetadata(@Element(name = "microscope") Microscope microscope) {
        super(microscope);
    }

}
