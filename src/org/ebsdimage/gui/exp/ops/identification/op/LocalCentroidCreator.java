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
package org.ebsdimage.gui.exp.ops.identification.op;

import org.ebsdimage.core.exp.ops.identification.op.LocalCentroid;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationCreator;

import rmlshared.gui.OkCancelDialog;

/**
 * GUI creator for the <code>LocalCentroid</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class LocalCentroidCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Identifies the Hough peaks from a peaks map by calculating"
                + " the centroid of each peak. The intensity of the Hough "
                + "peak is set as the maximum value of the peak.";
    }



    @Override
    public Operation getOperation() {
        return new LocalCentroid();
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
        return "Local Centroid";
    }

}
