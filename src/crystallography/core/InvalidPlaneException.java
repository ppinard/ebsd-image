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

/**
 * Exception thrown when an invalid plane is defined.
 * 
 * @author Philippe T. Pinard
 */
public class InvalidPlaneException extends RuntimeException {

    /**
     * Constructs a <code>InvalidPlaneException</code> with no detail message.
     */
    public InvalidPlaneException() {
    }



    /**
     * Constructs a <code>InvalidPlaneException</code> with the specified detail
     * message.
     * 
     * @param message
     *            the detail message.
     */
    public InvalidPlaneException(String message) {
        super(message);
    }

}
