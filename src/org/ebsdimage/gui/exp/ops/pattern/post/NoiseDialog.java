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

import org.ebsdimage.core.exp.ops.pattern.post.Noise;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.DoubleField;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>Noise</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class NoiseDialog extends OperationDialog {

    /** Field for the standard deviation. */
    private DoubleField stdDevField;

    /** Field for the number of iterations. */
    private IntField countField;



    /**
     * Creates a new <code>NoiseDialog</code>.
     */
    public NoiseDialog() {
        super("Noise");

        stdDevField =
                new DoubleField("Standard Deviation", Noise.DEFAULT_STDDEV);
        stdDevField.setRange(0.1, Double.MAX_VALUE);

        countField = new IntField("Iterations", 1);
        countField.setRange(1, Integer.MAX_VALUE);

        Panel panel = new Panel();

        panel.add(new JLabel("Standard deviation"));
        panel.add(stdDevField);

        panel.add(new JLabel("Nb of iterations"));
        panel.add(countField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Applies a Gaussian noise on the pattern map."
                + " Can be used to test algorithms.";
    }



    @Override
    public Operation getOperation() {
        return new Noise(stdDevField.getValueBFR(), countField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Noise";
    }

}
