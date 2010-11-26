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
package org.ebsdimage.gui.sim.ops.patternsim;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ComboBox;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;
import crystallography.core.ScatteringFactorsEnum;

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

    /** Field for the maximum index of the reflectors. */
    protected IntField maxIndexfield;

    /** Field for the type of scattering factors. */
    protected ComboBox<ScatteringFactorsEnum> scatterTypeField;



    /**
     * Creates a new <code>PatternSimOpDialog</code>.
     * 
     * @param title
     *            title of the dialog
     * @param width
     *            default width
     * @param height
     *            default height
     * @param maxIndex
     *            default maximum index of the reflectors to use in the pattern
     *            simulate
     * @param scatterType
     *            default type of scattering factors
     */
    public PatternSimOpDialog(String title, int width, int height,
            int maxIndex, ScatteringFactorsEnum scatterType) {
        super(title);

        widthField = new IntField("Width", width);
        widthField.setRange(1, Integer.MAX_VALUE);

        heightField = new IntField("Height", height);
        heightField.setRange(1, Integer.MAX_VALUE);

        maxIndexfield = new IntField("Maximum index", maxIndex);
        maxIndexfield.setRange(1, Integer.MAX_VALUE);

        scatterTypeField =
                new ComboBox<ScatteringFactorsEnum>(
                        ScatteringFactorsEnum.values());
        scatterTypeField.setSelectedItem(scatterType);

        Panel panel = new Panel(new MigLayout());

        panel.add("Width of the pattern");
        panel.add(widthField);
        panel.add("px", "align left, wrap");

        panel.add("Height of the pattern");
        panel.add(heightField);
        panel.add("px", "align left, wrap");

        panel.add("Maximum index of the reflectors");
        panel.add(maxIndexfield, "wrap");

        panel.add("Type of scattering factors");
        panel.add(scatterTypeField, "wrap");

        setMainComponent(panel);
    }

}
