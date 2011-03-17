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
package org.ebsdimage.gui.exp.ops.identification.post;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.identification.post.DoublePeaksCleanUp;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>Butterfly</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class DoublePeaksCleanUpDialog extends OperationDialog {

    /** Field for the minimum number of pixels in rho. */
    private IntField spacingRhoField;

    /** Field for the minimum number of pixels in rho. */
    private IntField spacingThetaField;



    /**
     * Creates a new <code>ButterflyDialog</code>.
     */
    public DoublePeaksCleanUpDialog() {
        super("Double Peaks Clean-Up");

        spacingRhoField =
                new IntField("Spacing in rho",
                        DoublePeaksCleanUp.DEFAULT.spacingRho);
        spacingRhoField.setRange(1, Integer.MAX_VALUE);

        spacingThetaField =
                new IntField("Spacing in theta",
                        DoublePeaksCleanUp.DEFAULT.spacingTheta);
        spacingThetaField.setRange(1, Integer.MAX_VALUE);

        Panel panel = new ColumnPanel(3);

        panel.add(new JLabel("Spacing between two peaks in \u03c1"));
        panel.add(spacingRhoField);
        panel.add(new JLabel("px"));

        panel.add(new JLabel("Spacing between two peaks in \u03f4"));
        panel.add(spacingThetaField);
        panel.add(new JLabel("px"));

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Clean-up peaks that appears twice in the list of Hough peaks.";
    }



    @Override
    public ExpOperation getOperation() {
        return new DoublePeaksCleanUp(spacingRhoField.getValueBFR(),
                spacingThetaField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Double Peaks Clean-Up";
    }

}
