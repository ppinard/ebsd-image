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
package org.ebsdimage.gui.sim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.io.sim.SimLoader;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import ptpshared.gui.WizardPage;
import rmlimage.gui.FileDialog;
import rmlshared.gui.CheckBox;
import rmlshared.gui.FileNameField;

/**
 * Template for the start page of the wizard.
 * 
 * @author Philippe T. Pinard
 */
public class StartWizardPage extends WizardPage {

    @Override
    public WizardPanelNavResult allowNext(String stepName,
            @SuppressWarnings("rawtypes") Map settings, Wizard wizard) {

        put(ParamsWizardPage.KEY_LOADED, 0);

        return super.allowNext(stepName, settings, wizard);
    }

    /**
     * Listener to enable/disable the operations file browser field.
     */
    private class ParamsCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            paramsFileField.setEnabled(paramsCBox.isSelected());
        }
    }

    /** Map key for the temporary simulation. */
    public static final String KEY_TEMP_SIM = "start.sim";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }

    /** Field for the parameters file. */
    private FileNameField paramsFileField;

    /** Check box for importing the parameters. */
    private CheckBox paramsCBox;



    /**
     * Creates a new <code>StartWizardPage</code>.
     */
    public StartWizardPage() {

        setLayout(new MigLayout("", "[grow,fill]", ""));

        // Welcome
        String text =
                "<html>This wizard is to simulate diffraction pattern using "
                        + "kinematic intensity calculations. The next pages guides you "
                        + "in the selection of the parameters and the outputs. The "
                        + "simulated patterns can then be used with the analysis engine."
                        + "</html>";
        add(new JLabel(text), "grow, wrap 40");

        paramsCBox = new CheckBox("Import parameters");
        paramsCBox.addActionListener(new ParamsCBoxListener());
        add(paramsCBox, "wrap");

        paramsFileField = new FileNameField("OPS_FILE", 32, true);
        paramsFileField.setFileSelectionMode(FileNameField.FILES_ONLY);
        FileDialog.addFilter("*.xml");
        paramsFileField.setFileFilter("*.xml");
        paramsFileField.setEnabled(false);
        add(paramsFileField, "gapleft 35, wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        Sim sim = null;

        if (paramsCBox.isSelected()) {
            if (paramsFileField.getFile() == null) {
                showErrorDialog("Specify the file for the operations.");
                return false;
            } else {
                try {
                    sim = new SimLoader().load(paramsFileField.getFile());
                } catch (IOException ex) {
                    showErrorDialog("Could not import operations from "
                            + "the specified file.");
                    return false;
                }
            }

        }

        if (buffer) {
            put(KEY_TEMP_SIM, sim);
        }

        return true;
    }
}
