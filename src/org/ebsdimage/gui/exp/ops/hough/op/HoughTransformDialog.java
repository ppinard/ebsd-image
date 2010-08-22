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

import org.ebsdimage.core.exp.ops.hough.op.HoughTransform;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>HoughTransform</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughTransformDialog extends OperationDialog {

    /** Field for the resolution. */
    private DoubleField resolutionField;



    /**
     * Creates a new <code>HoughTransformDialog</code>.
     */
    public HoughTransformDialog() {
        super("Hough Transform");

        resolutionField =
                new DoubleField("Resolution",
                        toDegrees(HoughTransform.DEFAULT_RESOLUTION));
        resolutionField.setRange(0.1, 90.0);

        Panel panel = new Panel();
        panel.add(new JLabel("Theta resolution"));
        panel.add(resolutionField);
        panel.add(new JLabel("\u00b0")); // deg

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Operation to perform a Hough transform. "
                + "The theta resolution must be specified. "
                + "The rho resolution is automatically calculated with the "
                + "theta resolution. The dimension of the Hough map are "
                + "adjusted such thtat the region of the biggest inscribed "
                + "circular mask will give a square region in the Hough map.";
    }



    @Override
    public Operation getOperation() {
        return new HoughTransform(toRadians(resolutionField.getValueBFR()));
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Hough Transform";
    }

}
