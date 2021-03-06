/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package org.ebsdimage.gui.exp.ops.positioning.results;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.positioning.results.StandardDeviation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.IntField;

/**
 * GUI creator for the <code>StandardDeviation</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class StandardDeviationDialog extends OperationDialog {

    /** Field for the maximum number of peaks to consider in the calculations. */
    private IntField maxField;



    /**
     * Creates a new <code>StandardDeviationDialog</code>.
     */
    public StandardDeviationDialog() {
        super("Standard Deviation");

        maxField =
                new IntField("Maximum number of peaks",
                        StandardDeviation.DEFAULT.max);
        maxField.setRange(-1, Integer.MAX_VALUE);

        ColumnPanel panel = new ColumnPanel(2);

        panel.add("Maximum number of peaks in calculations");
        panel.add(maxField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Calculates the standard deviation between the Hough "
                + "peaks' intensities.";
    }



    @Override
    public ExpOperation getOperation() {
        return new StandardDeviation(maxField.getValueBFR());
    }



    @Override
    public boolean isCorrect() {
        if (!super.isCorrect())
            return false;

        if (maxField.getValue() == 0) {
            ErrorDialog.show("Maximum number of peaks cannot be equal to zero.");
            return false;
        }

        return true;
    }



    @Override
    public String toString() {
        return "Standard Deviation";
    }

}
