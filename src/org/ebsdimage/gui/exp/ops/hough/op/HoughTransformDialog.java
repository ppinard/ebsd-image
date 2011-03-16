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

import org.ebsdimage.core.exp.ops.hough.op.HoughTransform;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import ptpshared.gui.CalibratedDoubleField;
import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>HoughTransformDialog</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class HoughTransformDialog extends OperationDialog {

    /** Field for the theta resolution. */
    private CalibratedDoubleField deltaThetaField;

    /** Field for the rho resolution. */
    private DoubleField deltaRhoField;



    /**
     * Creates a new <code>HoughTransformDialog</code>.
     */
    public HoughTransformDialog() {
        super("Hough Transform");

        String[] units = new String[] { "rad", "deg" };
        Magnitude value =
                new Magnitude(HoughTransform.DEFAULT.deltaTheta, "rad");
        value.setPreferredUnits("deg");
        Magnitude min = new Magnitude(0.1, "deg");
        Magnitude max = new Magnitude(180, "deg");

        deltaThetaField = new CalibratedDoubleField("deltaTheta", value, units);
        deltaThetaField.setRange(min, max);

        deltaRhoField =
                new DoubleField("deltaRho", HoughTransform.DEFAULT.deltaRho);
        deltaRhoField.setRange(0.0001, Double.MAX_VALUE);

        Panel panel = new Panel();

        panel.add(new JLabel("Theta resolution"));
        panel.add(deltaThetaField);
        panel.add("");

        panel.add(new JLabel("Rho resolution"));
        panel.add(deltaRhoField);
        panel.add(new JLabel("px/px"));

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Operation to perform a Hough transform. "
                + "The theta and rho resolution must be specified.";
    }



    @Override
    public Operation getOperation() {
        return new HoughTransform(
                deltaThetaField.getValueBFR().getValue("rad"),
                deltaRhoField.getValueBFR());
    }



    @Override
    public String toString() {
        return "Hough Transform";
    }

}
