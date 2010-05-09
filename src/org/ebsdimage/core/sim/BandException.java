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

/**
 * Exception raised when information about a band (center line, width, etc.)
 * cannot be calculated.
 * 
 * @author Philippe T. Pinard
 */
public class BandException extends RuntimeException {

    /**
     * Constructs a <code>BandException</code> with no detail message.
     */
    public BandException() {
    }



    /**
     * Constructs a <code>BandException</code> with the specified detail
     * message.
     * 
     * @param message
     *            the detail message.
     */
    public BandException(String message) {
        super(message);
    }

}
