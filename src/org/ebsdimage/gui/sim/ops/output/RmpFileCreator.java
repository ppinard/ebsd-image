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
package org.ebsdimage.gui.sim.ops.output;

import org.ebsdimage.core.sim.SimOperation;
import org.ebsdimage.core.sim.ops.output.RmpFile;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * Creator for the operation <code>RmpFile</code>.
 * 
 * @author Philippe T. Pinard
 */
public class RmpFileCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Saves all simulated patterns as RMPs";
    }



    @Override
    public SimOperation getOperation() {
        return new RmpFile();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }



    @Override
    public String toString() {
        return "RMP File";
    }

}
