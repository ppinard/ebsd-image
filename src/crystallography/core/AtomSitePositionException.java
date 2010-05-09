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
 * Exception thrown when an <code>AtomSite</code> already exists in the list of
 * <code>AtomSites</code>. This exception can be catch by the user or simply
 * ignored since the atoms are equivalent.
 * 
 * @author Philippe T. Pinard
 */
public class AtomSitePositionException extends IllegalArgumentException {

    /**
     * Constructs a <dfn>AtomSitePositionException</dfn> with no detail message.
     */
    public AtomSitePositionException() {
    }



    /**
     * Constructs a <dfn>AtomSitePositionException</dfn> with the specified
     * detail message.
     * 
     * @param message
     *            the detail message.
     */
    public AtomSitePositionException(String message) {
        super(message);
    }

}
