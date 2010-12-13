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

import java.util.ArrayList;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.OperationGenerator;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;

/**
 * Utilities to create simulations.
 * 
 * @author Philippe T. Pinard
 */
public class SimUtil {

    /**
     * Returns an array of <code>Sim</code>s created from the specified
     * <code>SimsGenerator</code> and arrays of parameters (camera, crystal,
     * energy, rotation).
     * 
     * @param crystals
     *            array of <code>Crystal</code>s
     * @param cameras
     *            array of <code>Camera</code>s
     * @param energies
     *            array of <code>Energy</code>s
     * @param rotations
     *            array of rotations
     * @param generator
     *            simulation generator to generate combinations of
     *            <code>Operation</code>
     * @return an array of <code>Sim</code>
     */
    public static Sim[] createSimulations(OperationGenerator generator,
            Crystal[] crystals, Camera[] cameras, Energy[] energies,
            Quaternion[] rotations) {
        if (crystals.length < 1)
            throw new IllegalArgumentException(
                    "At least one crystal must be defined.");
        if (cameras.length < 1)
            throw new IllegalArgumentException(
                    "At least one camera must be defined.");
        if (energies.length < 1)
            throw new IllegalArgumentException(
                    "At least one energy must be defined.");
        if (rotations.length < 1)
            throw new IllegalArgumentException(
                    "At least one rotation must be defined.");

        ArrayList<Operation[]> combsOps = generator.getCombinationsOperations();

        Sim[] sims = new Sim[combsOps.size()];

        int i = 0;
        for (Operation[] combOps : combsOps) {
            sims[i] = new Sim(combOps, cameras, crystals, energies, rotations);
            i++;
        }

        return sims;
    }

}
