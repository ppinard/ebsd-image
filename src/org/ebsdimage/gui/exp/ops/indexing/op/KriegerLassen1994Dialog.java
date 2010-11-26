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
package org.ebsdimage.gui.exp.ops.indexing.op;

import javax.swing.JLabel;

import org.ebsdimage.core.exp.ops.indexing.op.KriegerLassen1994;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ComboBox;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;
import crystallography.core.ScatteringFactorsEnum;

/**
 * GUI Dialog for the <code>KriegerLassen1994</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class KriegerLassen1994Dialog extends OperationDialog {

    /** Field for the maximum index. */
    private IntField maxIndexField;

    /** ComboBox for the type of scattering factors. */
    private ComboBox<ScatteringFactorsEnum> scatterTypeField;



    /**
     * Creates a new <code>KriegerLassen1994Dialog</code>.
     */
    public KriegerLassen1994Dialog() {
        super("Krieger Lassen (1994)");

        maxIndexField =
                new IntField("Maximum Index",
                        KriegerLassen1994.DEFAULT.maxIndex);
        maxIndexField.setRange(1, Integer.MAX_VALUE);

        scatterTypeField =
                new ComboBox<ScatteringFactorsEnum>(
                        ScatteringFactorsEnum.values());
        scatterTypeField.setSelectedItem(KriegerLassen1994.DEFAULT.scatterType);

        Panel panel = new ColumnPanel(2);
        panel.add(new JLabel("Maximum index of the reflectors"));
        panel.add(maxIndexField);

        panel.add(new JLabel("Type of scattering factors"));
        panel.add(scatterTypeField);

        setMainComponent(panel);
    }



    @Override
    public String getDescription() {
        return "Operation to index the Hough peaks using the algorithm "
                + "described in Krieger Lassen (1994).";
    }



    @Override
    public Operation getOperation() {
        return new KriegerLassen1994(maxIndexField.getValueBFR(),
                scatterTypeField.getSelectedItemBFR());
    }



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString() {
        return "Krieger Lassen (1994)";
    }

}
