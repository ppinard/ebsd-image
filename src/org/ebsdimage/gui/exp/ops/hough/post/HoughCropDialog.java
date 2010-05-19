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
package org.ebsdimage.gui.exp.ops.hough.post;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ops.hough.post.HoughCrop;
import org.ebsdimage.gui.exp.ops.OperationDialog;

import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * GUI Dialog for the <code>HoughCrop</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HoughCropDialog extends OperationDialog {

    /** Field for the radius. */
    private IntField radiusField;



    /**
     * Creates a new <code>HoughCropDialog</code>.
     */
    public HoughCropDialog() {
        super("Hough Crop");

        radiusField = new IntField("Radius", HoughCrop.DEFAULT_RADIUS);
        radiusField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

        Panel panel = new Panel();
        panel.add(new JLabel("Radius: "));
        panel.add(radiusField);
        panel.add(new JLabel(" px"));

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Crop of the Hough map to remove unwanted peaks near the edges.";
    }



    @Override
    public HoughCrop getOperation() {
        return new HoughCrop(radiusField.getValueBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Hough Crop";
    }

}
