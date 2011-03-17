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
import org.ebsdimage.core.exp.ops.pattern.post.MaskDisc;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>MaskDisc</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class MaskDiscDialog extends OperationDialog {

    /** Field for the centroid X. */
    private IntField centroidXField;

    /** Field for the centroid Y. */
    private IntField centroidYField;

    /** Field for the radius. */
    private IntField radiusField;



    /**
     * Creates a new <code>MaskDiscDialog</code>.
     */
    public MaskDiscDialog() {
        super("Crop");

        centroidXField = new IntField("Centroid X", MaskDisc.DEFAULT.centroidX);
        centroidXField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

        centroidYField = new IntField("Centroid Y", MaskDisc.DEFAULT.centroidY);
        centroidYField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

        radiusField = new IntField("Radius", MaskDisc.DEFAULT.radius);
        radiusField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

        Panel panel = new ColumnPanel(3);

        panel.add(new JLabel("Centroid X: "));
        panel.add(centroidXField);
        panel.add(new JLabel("px"));

        panel.add(new JLabel("Centroid Y: "));
        panel.add(centroidYField);
        panel.add(new JLabel("px"));

        panel.add(new JLabel("Radius: "));
        panel.add(radiusField);
        panel.add(new JLabel("px"));

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Applies a disk mask on the pattern map. "
                + "The disk mask is defined by its center position and radius.";
    }



    @Override
    public ExpOperation getOperation() {
        return new MaskDisc(centroidXField.getValueBFR(),
                centroidYField.getValueBFR(), radiusField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Mask Disc";
    }

}
