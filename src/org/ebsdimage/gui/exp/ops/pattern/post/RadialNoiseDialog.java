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

import org.ebsdimage.core.exp.ops.pattern.post.RadialNoise;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>RadialNoise</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class RadialNoiseDialog extends OperationDialog {

    /** Field for the center in x. */
    private IntField xField;

    /** Field for the center in y. */
    private IntField yField;

    /** Field for the standard deviation in x. */
    private DoubleField stdDevXField;

    /** Field for the standard deviation in y. */
    private DoubleField stdDevYField;

    /** Field for the initial noise level at (x0, y0). */
    private DoubleField initialNoiseStdDevField;

    /** Field for the final noise level at (x0, y0). */
    private DoubleField finalNoiseStdDevField;



    /**
     * Creates a new <code>RadialNoiseDialog</code>.
     */
    public RadialNoiseDialog() {
        super("Radial Noise");

        xField = new IntField("Center X", RadialNoise.DEFAULT.x);
        xField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

        yField = new IntField("Center Y", RadialNoise.DEFAULT.y);
        yField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

        stdDevXField =
                new DoubleField("Standard deviation in X",
                        RadialNoise.DEFAULT.stdDevX);
        stdDevXField.setRange(-Double.MAX_VALUE, Double.MAX_VALUE);

        stdDevYField =
                new DoubleField("Standard deviation in Y",
                        RadialNoise.DEFAULT.stdDevY);
        stdDevYField.setRange(-Double.MAX_VALUE, Double.MAX_VALUE);

        initialNoiseStdDevField =
                new DoubleField("Initial Noise Standard Deviation",
                        RadialNoise.DEFAULT.initialNoiseStdDev);
        initialNoiseStdDevField.setRange(0.1, Double.MAX_VALUE);

        finalNoiseStdDevField =
                new DoubleField("Final Noise Standard Deviation",
                        RadialNoise.DEFAULT.finalNoiseStdDev);
        finalNoiseStdDevField.setRange(0.1, Double.MAX_VALUE);

        Panel panel = new ColumnPanel(3);

        panel.add(new JLabel("x0: "));
        panel.add(xField);
        panel.add(new JLabel("px"));

        panel.add(new JLabel("y0: "));
        panel.add(yField);
        panel.add(new JLabel("px"));

        panel.add(new JLabel("Standard deviation in X: "));
        panel.add(stdDevXField);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Standard deviation in Y: "));
        panel.add(stdDevYField);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Standard deviation at (x0, y0): "));
        panel.add(initialNoiseStdDevField);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Standard deviation the image edges: "));
        panel.add(finalNoiseStdDevField);
        panel.add(new JLabel(""));

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Applies a 2D Gaussian noise from a point on the pattern map.";
    }



    @Override
    public Operation getOperation() {
        return new RadialNoise(xField.getValueBFR(), yField.getValueBFR(),
                stdDevXField.getValueBFR(), stdDevYField.getValueBFR(),
                initialNoiseStdDevField.getValueBFR(),
                finalNoiseStdDevField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Radial Noise";
    }

}
