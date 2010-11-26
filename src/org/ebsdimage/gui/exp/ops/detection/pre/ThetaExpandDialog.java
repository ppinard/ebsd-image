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

import org.ebsdimage.core.exp.ops.detection.pre.ThetaExpand;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>ThetaExpand</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class ThetaExpandDialog extends OperationDialog {

    /** Field for the angular increment. */
    private DoubleField incrementField;



    /**
     * Creates a new <code>ThetaExpandDialog</code>.
     */
    public ThetaExpandDialog() {
        super("Theta Expand");

        incrementField =
                new DoubleField("increment",
                        Math.toDegrees(ThetaExpand.DEFAULT.increment));
        incrementField.setRange(0.0, Double.MAX_VALUE);

        Panel panel = new ColumnPanel(3);

        panel.add(new JLabel("Increment"));
        panel.add(incrementField);
        panel.add(new JLabel("\u00b0")); // deg

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Increase the size of the Hough map on the right side by an "
                + "angular increment. This is used to better detect peaks near "
                + "0 deg and 180 deg.";
    }



    @Override
    public Operation getOperation() {
        return new ThetaExpand(Math.toRadians(incrementField.getValueBFR()));
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Theta Expand";
    }

}
