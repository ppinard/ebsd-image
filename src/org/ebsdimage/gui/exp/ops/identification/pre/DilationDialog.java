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

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.detection.post.Opening;
import org.ebsdimage.core.exp.ops.identification.pre.Dilation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.IntField;

/**
 * GUI creator for the <code>Dilation</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class DilationDialog extends OperationDialog {

    /**
     * Field for the minimum number of <code>OFF</code> neighbour to an
     * <code>ON</code> pixel for it to be turned to <code>OFF</code>.
     */
    private IntField minField;

    /**
     * Field for the maximum number of <code>OFF</code> neighbour to an
     * <code>ON</code> pixel for it to be turned to <code>OFF</code>.
     */
    private IntField maxField;



    @Override
    public String getDescription() {
        return "Performs a dilation on the peaks in the peaks map to "
                + "increase their size.";
    }



    /**
     * Creates a new <code>DilationDialog</code>.
     */
    public DilationDialog() {
        super("Dilation");

        ColumnPanel panel = new ColumnPanel(2);

        panel.add("Min");
        minField = new IntField("Min", Opening.DEFAULT.min);
        minField.setRange(0, 8);
        panel.add(minField);

        panel.add("Max");
        maxField = new IntField("Max", Opening.DEFAULT.max);
        maxField.setRange(0, 8);
        panel.add(maxField);

        setMainComponent(panel);
    }



    @Override
    public ExpOperation getOperation() {
        return new Dilation(minField.getValueBFR(), maxField.getValueBFR());
    }



    @Override
    public boolean isCorrect() {
        if (!super.isCorrect())
            return false;

        if (minField.getValue() > maxField.getValue()) {
            ErrorDialog.show("Minimum value (" + minField.getValue()
                    + ") cannot be greater than the maximum value ("
                    + maxField.getValue() + ").");
            return false;
        }

        return true;
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
