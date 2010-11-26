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
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;

import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.io.IO;

/**
 * Operation to save the simulated pattern as a RMP file (<code>RealMap</code>).
 * 
 * @author Philippe T. Pinard
 */
public class RmpFile extends OutputOps {

    /** Default operation. */
    public static final RmpFile DEFAULT = new RmpFile();



    /**
     * Saves the simulated pattern as a RMP file (<code>RealMap</code>).
     * 
     * @param sim
     *            simulation executing this method
     * @param patternSimOp
     *            pattern simulation operation
     * @throws IOException
     *             if an error occurs while saving the RMP file
     */
    @Override
    public void save(Sim sim, PatternSimOp patternSimOp) throws IOException {
        // Get RealMap
        RealMap patternMap = patternSimOp.getPatternRealMap();

        patternMap.setFile(new File(sim.getDir(), sim.getName() + "_"
                + sim.getCurrentIndex() + ".rmp"));

        IO.save(patternMap);
    }

}
