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

import java.awt.Shape;

import net.jcip.annotations.Immutable;
import crystallography.core.Reflector;

/**
 * Band in a diffraction pattern. A band consists of a <code>Reflector</code>
 * plus the information to draw this reflector in a simulated pattern (center
 * line, width, etc.).
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Band {

    public final Reflector reflector;

    /** Line representing the middle of this band. */
    public final Shape middle;

    public final Shape edge1;

    public final Shape edge2;



    /**
     * Creates a new <code>Band</code> from a reflector, a rotation, a camera
     * and an energy.
     * 
     * @param reflector
     *            reflector of the band
     */
    public Band(Reflector reflector, Shape middle, Shape edge1, Shape edge2) {
        this.reflector = reflector;
        this.middle = middle;
        this.edge1 = edge1;
        this.edge2 = edge2;
    }
}
