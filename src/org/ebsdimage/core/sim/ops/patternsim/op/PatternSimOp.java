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
package org.ebsdimage.core.sim.ops.patternsim.op;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Pattern;
import org.ebsdimage.core.sim.Sim;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;

/**
 * Superclass of operation to simulate a pattern.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PatternSimOp extends Operation {

    /** Simulated pattern. */
    protected Pattern pattern;



    /**
     * Simulates a new pattern with the specified variables.
     * 
     * @param sim
     *            simulation executing this method
     * @param crystal
     *            crystal of the pattern
     * @param camera
     *            camera parameters
     * @param energy
     *            energy object for the beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     */
    public abstract void simulate(Sim sim, Camera camera, Crystal crystal,
            Energy energy, Quaternion rotation);



    /**
     * Returns the pattern that was simulated. If no pattern was simulated , a
     * <code>RuntimeException</code> is thrown.
     * 
     * @return simulated patter
     * @throws RuntimeException
     *             if no pattern was simulated
     */
    public Pattern getPattern() {
        if (pattern == null)
            throw new RuntimeException(
                    "No pattern created. Execute create() first.");
        return pattern;
    }

}
