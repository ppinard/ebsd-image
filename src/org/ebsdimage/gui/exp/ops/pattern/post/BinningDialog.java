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
package org.ebsdimage.gui.exp.ops.pattern.post;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ops.pattern.post.Binning;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationDialog;

import ptpshared.gui.PowerOfTwoIntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>Binning</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class BinningDialog extends OperationDialog {

    /** Field for the binning size. */
    private PowerOfTwoIntField binningSizeField;



    /**
     * Creates a new <code>BinningDialog</code>.
     */
    public BinningDialog() {
        super("Binning");

        binningSizeField =
                new PowerOfTwoIntField("Binning Size",
                        Binning.DEFAULT_BINNING_SIZE);

        Panel panel = new Panel();

        panel.add(new JLabel("Binning size"));
        panel.add(binningSizeField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Reduce the size of the pattern map by applying a "
                + "binning compression.";
    }



    @Override
    public Operation getOperation() {
        return new Binning(binningSizeField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Binning";
    }

}
