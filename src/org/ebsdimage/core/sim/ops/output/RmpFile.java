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
package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.sim.Sim;

import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.io.IO;

/**
 * Operation to save the simulated pattern as a rmp file (<code>RealMap</code>).
 * 
 * @author Philippe T. Pinard
 * 
 */
public class RmpFile extends OutputOps {

    /**
     * Saves the simulated pattern as a rmp file (<code>RealMap</code>).
     * 
     * @param sim
     *            simulation executing this method
     * @throws IOException
     *             if an error occurs while saving the rmp file
     */
    @Override
    public void save(Sim sim) throws IOException {
        // Get RealMap
        RealMap patternMap = sim.getPatternSimOp().getPattern()
                .getPatternRealMap();

        patternMap.setFile(new File(sim.getDir(), sim.getName() + "_"
                + sim.getCurrentIndex() + ".rmp"));

        IO.save(patternMap);
    }

}
