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
package org.ebsdimage.gui.exp.ops.detection.op;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.detection.op.AutomaticTopHat;
import org.ebsdimage.gui.run.ops.OperationCreator;

import rmlshared.gui.OkCancelDialog;

/**
 * GUI creator for the <code>AutomaticTopHat</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class AutomaticTopHatCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Detects peaks using an automatic top hat thresholding.";
    }



    @Override
    public ExpOperation getOperation() {
        return new AutomaticTopHat();
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
        return "Automatic Top Hat";
    }

}
