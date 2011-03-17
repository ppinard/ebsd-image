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
package org.ebsdimage.gui.exp.ops.detection.post;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.detection.post.ShapeFactor;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>ShapeFactor</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class ShapeFactorDialog extends OperationDialog {

    /** Field for the critical aspect ratio. */
    private DoubleField aspectRatioField;



    /**
     * Creates a new <code>ShapeFactorDialog</code>.
     */
    public ShapeFactorDialog() {
        super("Shape Factor");

        aspectRatioField =
                new DoubleField("Aspect Ratio", ShapeFactor.DEFAULT.aspectRatio);
        aspectRatioField.setRange(1.0, Double.MAX_VALUE);

        Panel panel = new ColumnPanel(2);

        panel.add(new JLabel("Critical aspect ratio"));
        panel.add(aspectRatioField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Remove peaks which have an aspect ratio greater than "
                + "the critical aspect ratio.";
    }



    @Override
    public ExpOperation getOperation() {
        return new ShapeFactor(aspectRatioField.getValueBFR());
    }



    @Override
    public String toString() {
        return "Shape Factor";
    }

}
