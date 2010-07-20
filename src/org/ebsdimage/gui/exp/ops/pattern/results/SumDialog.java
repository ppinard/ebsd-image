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
package org.ebsdimage.gui.exp.ops.pattern.results;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ops.pattern.results.Sum;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>Sum</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class SumDialog extends OperationDialog {

    /** Field for lower x limit. */
    private DoubleField xminField;

    /** Field for upper x limit. */
    private DoubleField xmaxField;

    /** Field for lower y limit. */
    private DoubleField yminField;

    /** Field for upper y limit. */
    private DoubleField ymaxField;



    /**
     * Creates a new <code>SumDialog</code>.
     */
    public SumDialog() {
        super("Sum");

        xminField = new DoubleField("Lower x limit", Sum.DEFAULT_XMIN * 100.0);
        xminField.setRange(0.0, 100.0);

        yminField = new DoubleField("Lower y limit", Sum.DEFAULT_YMIN * 100.0);
        yminField.setRange(0.0, 100.0);

        xmaxField = new DoubleField("Upper x limit", Sum.DEFAULT_XMAX * 100.0);
        xmaxField.setRange(0.0, 100.0);

        ymaxField = new DoubleField("Upper y limit", Sum.DEFAULT_YMAX * 100.0);
        ymaxField.setRange(0.0, 100.0);

        Panel panel = new ColumnPanel(3);

        panel.add(new JLabel("Lower x limit"));
        panel.add(xminField);
        panel.add(new JLabel("% of the pattern's width"));

        panel.add(new JLabel("Lower y limit"));
        panel.add(yminField);
        panel.add(new JLabel("% of the pattern's height"));

        panel.add(new JLabel("Upper x limit"));
        panel.add(xmaxField);
        panel.add(new JLabel("% of the pattern's width"));

        panel.add(new JLabel("Upper y limit"));
        panel.add(ymaxField);
        panel.add(new JLabel("% of the pattern's height"));

        setMainComponent(panel);
    }



    @Override
    public boolean isCorrect() {
        if (!super.isCorrect())
            return false;

        if (xminField.getValue() > xmaxField.getValue()) {
            ErrorDialog.show("Lower x limit cannot be greater than the upper x limit.");
            return false;
        }

        if (yminField.getValue() > ymaxField.getValue()) {
            ErrorDialog.show("Lower y limit cannot be greater than the upper y limit.");
            return false;
        }

        return true;
    }



    @Override
    public String getDescription() {
        return "Calculate the total intensity of a rectangular region of the "
                + "original diffraction pattern";
    }



    @Override
    public Operation getOperation() {
        return new Sum(xminField.getValueBFR() / 100.0,
                yminField.getValueBFR() / 100.0,
                xmaxField.getValueBFR() / 100.0,
                ymaxField.getValueBFR() / 100.0);
    }



    @Override
    public String toString() {
        return "Sum";
    }

}
