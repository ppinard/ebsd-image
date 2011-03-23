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
package org.ebsdimage.vendors.tsl.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

import org.ebsdimage.vendors.tsl.io.AngLoader;

/**
 * Wizard page to create phases based on the information in the ANG file.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesWizardPage extends org.ebsdimage.gui.PhasesWizardPage {

    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Phases";
    }

    /** Field for the description of phases in the ANG file. */
    private JLabel descField;



    /**
     * Creates a new <code>PhasesWizardPage</code> with a description field.
     */
    public PhasesWizardPage() {
        super();

        descField = new JLabel("");
        add(descField, "grow, wrap");
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        File angFile = (File) get(StartWizardPage.KEY_ANG_FILE);

        if (angFile == null) {
            showErrorDialog("No ANG file was defined in the Start wizard page.");
            return;
        }

        // Load phases name
        String[] phasesName = new String[0];
        try {
            phasesName = new AngLoader().loadPhaseNames(angFile);
        } catch (IOException e) {
            showErrorDialog(e.getMessage());
            return;
        }

        // Set minimum number of phases
        int phasesCount = phasesName.length;
        setMinimumPhasesCount(phasesCount);

        // New text box specifying the names
        StringBuilder desc = new StringBuilder();
        desc.append("<html><u>Warning</u>: ");
        desc.append(phasesCount + " phase(s) need(s) to be defined. "
                + "In the ANG file, the following names "
                + "were given to the phases: ");
        for (int i = 0; i < phasesCount; i++)
            desc.append("(" + (i + 1) + ") " + phasesName[i] + ",");
        desc.append("</html>");

        descField.setText(desc.toString());
    }

}
