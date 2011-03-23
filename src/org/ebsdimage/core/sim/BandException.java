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
public class BandException extends Exception {

    /**
     * Creates a <code>BandException</code> with no detail message.
     */
    public BandException() {
    }



    /**
     * Creates a <code>BandException</code> with the specified message.
     * 
     * @param message
     *            the message.
     */
    public BandException(String message) {
        super(message);
    }



    /**
     * Creates a <code>BandException</code> with the specified message and cause
     * for this exception.
     * 
     * @param message
     *            message
     * @param cause
     *            cause for this exception
     */
    public BandException(String message, Throwable cause) {
        super(message, cause);
    }



    /**
     * Creates a <code>BandException</code> with the cause for this exception.
     * 
     * @param cause
     *            cause for this exception
     */
    public BandException(Throwable cause) {
        super(cause);
    }

}
