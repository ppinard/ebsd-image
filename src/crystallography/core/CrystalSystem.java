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

import rmlshared.util.Labeled;

/**
 * Enumeration of the 7 crystal systems.
 * 
 * @author Philippe T. Pinard
 * 
 */
public enum CrystalSystem implements Labeled {

    /** Triclinic crystal system (a != b != c and alpha != beta != gamma). */
    TRICLINIC("triclinic"),

    /** Monoclinic crystal system (a != b != c and alpha = gamma = PI/2). */
    MONOCLINIC("monoclinic"),

    /**
     * Orthorhombic crystal system (a != b != c and alpha = beta = gamma =
     * PI/2).
     */
    ORTHORHOMBIC("orthorhombic"),

    /** Tetragonal crystal system (a = b and alpha = beta = gamma = PI/2). */
    TETRAGONAL("tetragonal"),

    /**
     * Tetragonal crystal system (a = b and alpha = beta = PI/2, gamma = 2PI/3).
     */
    HEXAGONAL("hexagonal"),

    /** Trigonal crystal system (a = b = c and alpha = beta = gamma). */
    TRIGONAL("trigonal"),

    /** Cubic crystal system (a = b = c and alpha = beta = gamma = PI/2). */
    CUBIC("cubic");

    /** Label for the crystal system. */
    private final String label;



    /**
     * Creates a new crystal system with its label.
     * 
     * @param label
     *            label
     */
    private CrystalSystem(String label) {
        this.label = label;
    }



    @Override
    public String getLabel() {
        return label;
    }

}
