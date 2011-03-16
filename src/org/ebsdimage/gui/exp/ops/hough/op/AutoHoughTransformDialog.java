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

import javax.swing.JLabel;

import magnitude.core.Magnitude;

import org.ebsdimage.core.exp.ops.hough.op.AutoHoughTransform;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import ptpshared.gui.CalibratedDoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>AutoHoughTransform</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class AutoHoughTransformDialog extends OperationDialog {

    /** Field for the resolution. */
    private CalibratedDoubleField resolutionField;



    /**
     * Creates a new <code>AutoHoughTransformDialog</code>.
     */
    public AutoHoughTransformDialog() {
        super("Auto Hough Transform");

        String[] units = new String[] { "rad", "deg" };
        Magnitude value =
                new Magnitude(AutoHoughTransform.DEFAULT.deltaTheta, "rad");
        value.setPreferredUnits("deg");
        Magnitude min = new Magnitude(0.1, "deg");
        Magnitude max = new Magnitude(180, "deg");

        resolutionField = new CalibratedDoubleField("Resolution", value, units);
        resolutionField.setRange(min, max);

        Panel panel = new Panel();
        panel.add(new JLabel("Theta resolution"));
        panel.add(resolutionField);

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
        return new AutoHoughTransform(resolutionField.getValueBFR().getValue(
                "rad"));
    }



    @Override
    public String toString() {
        return "Auto Hough Transform";
    }

}
