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
package org.ebsdimage.gui.exp.ops.hough.op;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ops.hough.op.AutoHoughTransform;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>AutoHoughTransform</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class AutoHoughTransformDialog extends OperationDialog {

    /** Field for the resolution. */
    private DoubleField resolutionField;



    /**
     * Creates a new <code>AutoHoughTransformDialog</code>.
     */
    public AutoHoughTransformDialog() {
        super("Auto Hough Transform");

        resolutionField =
                new DoubleField("Resolution",
                        toDegrees(AutoHoughTransform.DEFAULT.deltaTheta));
        resolutionField.setRange(0.1, 90.0);

        Panel panel = new Panel();
        panel.add(new JLabel("Theta resolution"));
        panel.add(resolutionField);
        panel.add(new JLabel("\u00b0/px")); // deg

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Operation to perform a Hough transform. "
                + "The theta resolution must be specified. "
                + "The rho resolution is automatically calculated with the "
                + "theta resolution to ensure that most of the peaks have an "
                + "aspect ratio close to unity.";
    }



    @Override
    public Operation getOperation() {
        return new AutoHoughTransform(toRadians(resolutionField.getValueBFR()));
    }



    @Override
    public String toString() {
        return "Auto Hough Transform";
    }

}
