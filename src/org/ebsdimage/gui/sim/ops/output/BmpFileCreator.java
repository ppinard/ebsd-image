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

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.output.BmpFile;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * Creator for the operation <code>BmpFile</code>.
 * 
 * @author Philippe T. Pinard
 */
public class BmpFileCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Saves all simulated patterns as BMPs";
    }



    @Override
    public String toString() {
        return "Bmp File";
    }



    @Override
    public Operation getOperation() {
        return new BmpFile();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}