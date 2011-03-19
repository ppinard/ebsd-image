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
package org.ebsdimage.gui.sim.ops.patternsim;

import org.ebsdimage.core.sim.SimOperation;
import org.ebsdimage.core.sim.ops.patternsim.PatternBandEdges;

/**
 * Dialog for the <code>PatternBandEdges</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PatternBandEdgesDialog extends PatternSimOpDialog {

    /**
     * Creates a new <code>PatternBandEdgesDialog</code>.
     */
    public PatternBandEdgesDialog() {
        super("Pattern Band Edges");
    }



    @Override
    public String toString() {
        return "Pattern Band Edges";
    }



    @Override
    public String getDescription() {
        return "Simulate a pattern where the edges of the bands are drawn.";
    }



    @Override
    public SimOperation getOperation() {
        return new PatternBandEdges(widthField.getValueBFR(),
                heightField.getValueBFR());
    }

}
