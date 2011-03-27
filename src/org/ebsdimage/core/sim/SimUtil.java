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

import java.util.ArrayList;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.OperationGenerator;

import crystallography.core.Crystal;

/**
 * Utilities to create simulations.
 * 
 * @author Philippe T. Pinard
 */
public class SimUtil {

    /**
     * Returns an array of <code>Sim</code>s created from the specified
     * <code>SimsGenerator</code> and arrays of parameters (crystal and
     * rotation).
     * 
     * @param generator
     *            simulation generator to generate combinations of
     *            <code>Operation</code>
     * @param metadata
     *            simulation metadata
     * @param phases
     *            array of <code>Crystal</code>s
     * @param rotations
     *            array of rotations
     * @return an array of <code>Sim</code>
     */
    public static Sim[] createSimulations(OperationGenerator generator,
            SimMetadata metadata, Crystal[] phases, Rotation[] rotations) {
        if (phases.length < 1)
            throw new IllegalArgumentException(
                    "At least one crystal must be defined.");
        if (rotations.length < 1)
            throw new IllegalArgumentException(
                    "At least one rotation must be defined.");

        ArrayList<Operation[]> combsOps = generator.getCombinationsOperations();

        Sim[] sims = new Sim[combsOps.size()];

        int i = 0;
        for (Operation[] combOps : combsOps) {
            sims[i] = new Sim(metadata, combOps, phases, rotations);
            i++;
        }

        return sims;
    }

}
