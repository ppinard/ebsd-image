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
package org.ebsdimage.gui.exp.ops.detection.pre;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ops.detection.pre.Butterfly;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>Butterfly</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class ButterflyDialog extends OperationDialog {

    /** Field for the flatten lower limit. */
    private DoubleField flattenLowerLimitField;

    /** Field for the flatten upper limit. */
    private DoubleField flattenUpperLimitField;

    /** Field for the kernel size. */
    private IntField kernelSizeField;



    /**
     * Creates a new <code>ButterflyDialog</code>.
     */
    public ButterflyDialog() {
        super("Butterfly");

        flattenLowerLimitField =
                new DoubleField("Flatten Lower Limit",
                        Butterfly.DEFAULT_FLATTEN_LOWER_LIMIT);
        flattenUpperLimitField =
                new DoubleField("Flatten Upper Limit",
                        Butterfly.DEFAULT_FLATTEN_UPPER_LIMIT);
        kernelSizeField =
                new IntField("Kernel Size", Butterfly.DEFAULT_KERNEL_SIZE);
        kernelSizeField.setEnabled(false);
        // FIXME: Test range

        Panel panel = new ColumnPanel(2);

        panel.add(new JLabel("Flatten (lower limit): "));
        panel.add(flattenLowerLimitField);

        panel.add(new JLabel("Flatten (upper limit): "));
        panel.add(flattenUpperLimitField);

        panel.add(new JLabel("Kernel size: "));
        panel.add(kernelSizeField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Apply a convolution filter on the Hough map to increase the "
                + "intensity of the peaks. The convolution kernel has the "
                + "shape of a buttefly to match the shape of the peaks in "
                + "the Hough map";
    }



    @Override
    public Operation getOperation() {
        return new Butterfly(kernelSizeField.getValueBFR(),
                flattenLowerLimitField.getValueBFR().floatValue(),
                flattenUpperLimitField.getValueBFR().floatValue());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Butterfly";
    }

}
