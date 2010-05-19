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

import org.ebsdimage.core.exp.ops.pattern.post.Smoothing;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationDialog;

import rmlshared.gui.OddIntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>Smoothing</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class SmoothingDialog extends OperationDialog {

    /** Field for the kernel size. */
    private OddIntField kernelSizeField;



    /**
     * Creates a new <code>SmoothingDialog</code>.
     */
    public SmoothingDialog() {
        super("Smoothing");

        kernelSizeField =
                new OddIntField("Kernel Size", Smoothing.DEFAULT_KERNEL_SIZE);
        kernelSizeField.setRange(1, Integer.MAX_VALUE);

        Panel panel = new Panel();

        panel.add(new JLabel("Kernel size"));
        panel.add(kernelSizeField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Applies a smoothing filter on the pattern map."
                + " Can be used to test algorithms.";
    }



    @Override
    public Operation getOperation() {
        return new Smoothing(kernelSizeField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Smoothing";
    }

}
