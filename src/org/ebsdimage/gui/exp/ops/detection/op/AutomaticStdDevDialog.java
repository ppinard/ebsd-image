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

import org.ebsdimage.core.exp.ops.detection.op.AutomaticStdDev;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;

/**
 * GUI dialog for the <code>AutomaticStdDev</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class AutomaticStdDevDialog extends OperationDialog {

    /** Double field for the sigma factor. */
    private DoubleField sigmaFactorField;



    /**
     * Creates a new <code>AutomaticStdDevDialog</code>.
     */
    public AutomaticStdDevDialog() {
        super("Automatic Std Dev");

        ColumnPanel panel = new ColumnPanel(2);

        panel.add("Sigma factor");
        sigmaFactorField = new DoubleField("Sigma factor", 2);
        sigmaFactorField.setRange(0, Double.MAX_VALUE);
        panel.add(sigmaFactorField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Detects peaks using an automatic standard deviation"
                + " thresholding.";
    }



    @Override
    public Operation getOperation() {
        return new AutomaticStdDev(sigmaFactorField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Automatic Std Dev";
    }

}
