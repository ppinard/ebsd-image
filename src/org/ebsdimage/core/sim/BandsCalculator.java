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
package org.ebsdimage.core.sim;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.AcquisitionConfig;

import crystallography.core.Reflectors;

/**
 * Calculates the representation of a crystal's reflectors in a diffraction
 * pattern.
 * 
 * @author Philippe T. Pinard
 */
public interface BandsCalculator {

    /**
     * Calculates all the bands from the specified reflectors in the given
     * experimental conditions.
     * 
     * @param width
     *            width of the diffraction pattern image
     * @param height
     *            height of the diffraction pattern image
     * @param acqConfig
     *            acquisition configuration
     * @param reflectors
     *            list of reflectors
     * @param rotation
     *            rotation of the crystal
     * @return bands
     */
    public Band[] calculate(int width, int height, AcquisitionConfig acqConfig,
            Reflectors reflectors, Rotation rotation);
}
