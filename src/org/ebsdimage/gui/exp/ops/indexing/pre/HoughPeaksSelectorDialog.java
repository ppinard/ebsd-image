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
package org.ebsdimage.gui.exp.ops.indexing.pre;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.indexing.pre.HoughPeaksSelector;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>SelectHoughPeaks</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeaksSelectorDialog extends OperationDialog {

    /** Field for the minimum number of Hough peaks. */
    private IntField minimumField;

    /** Field for the maximum number of Hough peaks. */
    private IntField maximumField;



    /**
     * Creates a new <code>SelectHoughPeaksDialog</code>.
     */
    public HoughPeaksSelectorDialog() {
        super("Select Hough Peaks");

        minimumField = new IntField("Minimum", HoughPeaksSelector.DEFAULT.min);
        minimumField.setRange(3, Integer.MAX_VALUE);

        maximumField = new IntField("Maximum", HoughPeaksSelector.DEFAULT.max);
        maximumField.setRange(3, Integer.MAX_VALUE);

        Panel panel = new ColumnPanel(2);

        panel.add(new JLabel("Minimum number of Hough peaks: "));
        panel.add(minimumField);
        panel.add(new JLabel("Maximum number of Hough peaks: "));
        panel.add(maximumField);

        setMainComponent(panel);
    }



    @Override
    public boolean isCorrect() {
        if (!super.isCorrect())
            return false;

        if (minimumField.getValue() > maximumField.getValue()) {
            ErrorDialog.show("The maximum number of Hough peaks "
                    + "cannot be greater than the minimum number.");
            return false;
        }

        return true;
    }



    @Override
    public String getDescription() {
        return "Operation to select a certain number of Hough peaks. "
                + "Three cases are possible: "
                + "(1) If the number of Hough peaks is less than the minimum "
                + "value, an empty array is returned; "
                + "(2) If the number of Hough peaks is less than the maximum "
                + "value, all the Hough peaks are returned; "
                + "(3) If the number of Hough peaks is greater than the "
                + "maximum value, the most intense Hough peaks up to the "
                + "maximum value are returned.";
    }



    @Override
    public ExpOperation getOperation() {
        return new HoughPeaksSelector(minimumField.getValueBFR(),
                maximumField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Hough Peaks Selector";
    }

}
