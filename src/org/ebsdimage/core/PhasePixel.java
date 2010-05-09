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
package org.ebsdimage.core;

import rmlimage.core.Pixel;
import crystallography.core.Crystal;

/**
 * Representation of a pixel in a <code>PhasesMap</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PhasePixel extends Pixel {

    /** Phase id. */
    private final int id;

    /** Phase. */
    private final Crystal phase;



    /**
     * Creates a new <code>PhasePixel</code>.
     * 
     * @param id
     *            id of the phase
     * @param phase
     *            crystal representing the phase
     * 
     * @throws IllegalArgumentException
     *             if the id is outside [0, 255].
     * @throws NullPointerException
     *             if the phase is null
     */
    public PhasePixel(int id, Crystal phase) {
        if (id <= 0 || id > 255)
            throw new IllegalArgumentException("Id has to be between ]0, 255[.");
        if (phase == null)
            throw new IllegalArgumentException("Phase cannot be null.");

        this.id = id;
        this.phase = phase;
    }



    /**
     * Creates a <em>null</em> <code>PhasePixel</code>. The id is 0 and the
     * phase is <code>null</code>.
     */
    public PhasePixel() {
        this.id = 0;
        this.phase = null;
    }



    @Override
    public String getValueLabel() {
        if (phase == null)
            return id + ": Not indexed";
        else
            return id + ": " + phase.name;
    }

}
