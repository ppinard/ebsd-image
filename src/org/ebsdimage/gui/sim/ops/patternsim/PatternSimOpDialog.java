/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package org.ebsdimage.gui.sim.ops.patternsim;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.IntField;
import rmlshared.gui.Panel;

/**
 * Abstract class common to the pattern simulation operations.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PatternSimOpDialog extends OperationDialog {

    /** Field for the width of the simulated pattern. */
    protected IntField widthField;

    /** Field for the height of the simulated pattern. */
    protected IntField heightField;



    /**
     * Creates a new <code>PatternSimOpDialog</code>.
     * 
     * @param title
     *            title of the dialog
     */
    public PatternSimOpDialog(String title) {
        super(title);

        Panel panel = new Panel(new MigLayout());

        // Width
        panel.add("Width of the pattern");

        widthField = new IntField("Width", 400);
        widthField.setRange(1, Integer.MAX_VALUE);
        panel.add(widthField);

        panel.add("px", "align left, wrap");

        // Height
        panel.add("Height of the pattern");

        heightField = new IntField("Height", 400);
        heightField.setRange(1, Integer.MAX_VALUE);
        panel.add(heightField);

        panel.add("px", "align left, wrap");

        setMainComponent(panel);
    }

}
