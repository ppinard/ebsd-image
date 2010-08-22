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
package org.ebsdimage.gui.exp.ops.identification.pre;

import org.ebsdimage.core.exp.ops.identification.pre.Dilation;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationCreator;

import rmlshared.gui.OkCancelDialog;

/**
 * GUI creator for the <code>Dilation</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class DilationCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Performs a dilation on the peaks in the peaks map to "
                + "increase their size.";
    }



    @Override
    public Operation getOperation() {
        return new Dilation();
    }



    @Override
    public int show() {
        return OkCancelDialog.OK;
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Dilation";
    }

}
