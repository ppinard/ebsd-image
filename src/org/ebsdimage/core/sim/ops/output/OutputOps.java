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

import java.io.IOException;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.op.PatternSimOp;

/**
 * Superclass of operation to save the output(s).
 * 
 * @author Philippe T. Pinard
 */
public abstract class OutputOps extends Operation {

    /**
     * Saves results, operations or parameters of the <code>Sim</code>.
     * 
     * @param sim
     *            simulation executing this method
     * @param patternSimOp
     *            pattern simulation operation
     * @throws IOException
     *             if an error occurs while saving
     */
    public abstract void save(Sim sim, PatternSimOp patternSimOp)
            throws IOException;

}
