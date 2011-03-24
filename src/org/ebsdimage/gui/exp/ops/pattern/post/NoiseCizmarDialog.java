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

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.pattern.post.NoiseCizmar;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>NoiseCizmar</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class NoiseCizmarDialog extends OperationDialog {

    /** Field for the standard deviation of the Gaussian noise. */
    private DoubleField gaussianField;

    /** Field for the amplitude of the Poisson noise. */
    private DoubleField poissonField;



    /**
     * Creates a new <code>NoiseCizmarDialog</code>.
     */
    public NoiseCizmarDialog() {
        super("Noise Cizmar");

        gaussianField =
                new DoubleField("Gaussian noise", NoiseCizmar.DEFAULT.gaussian);
        gaussianField.setRange(0.1, Double.MAX_VALUE);

        poissonField =
                new DoubleField("Poisson noise", NoiseCizmar.DEFAULT.poisson);
        poissonField.setRange(0.1, Double.MAX_VALUE);

        Panel panel = new ColumnPanel(2);

        panel.add(new JLabel("Standard deviation of Gaussian noise"));
        panel.add(gaussianField);

        panel.add(new JLabel("Amplitude of Poisson noise"));
        panel.add(poissonField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Applies Gaussian and Poisson noise to a diffraction pattern "
                + "based on Cizmar et al.(2008) equation.";
    }



    @Override
    public ExpOperation getOperation() {
        return new NoiseCizmar(gaussianField.getValueBFR(),
                poissonField.getValueBFR(), -1);
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Noise Cizmar";
    }

}
