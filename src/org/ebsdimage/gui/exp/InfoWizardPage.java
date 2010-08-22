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
package org.ebsdimage.gui.exp;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.run.Run;

import ptpshared.gui.WizardPage;
import rmlshared.gui.FileNameField;
import rmlshared.gui.TextField;

/**
 * Template for the info's wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class InfoWizardPage extends WizardPage {

    /** Map key for the experiment's name. */
    public static final String KEY_NAME = "exp.name";

    /** Map key for the experiment's working directory. */
    public static final String KEY_DIR = "exp.dir";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Info";
    }

    /** Field for the experiment's name. */
    private TextField nameField;

    /** Field for the experiment's working directory. */
    private FileNameField workingDirField;



    /**
     * Creates a new <code>InfoWizardPage</code>.
     */
    public InfoWizardPage() {

        setLayout(new MigLayout("", "[][grow,fill][]"));

        // Name
        add(new JLabel("Name"));

        nameField = new TextField("NAME", 32, Run.DEFAULT_NAME);
        add(nameField, "wrap 20");

        // Create the working directory field label
        add(new JLabel("Working directory"));

        workingDirField = new FileNameField("DIR", 32, false);
        workingDirField.setFileSelectionMode(FileNameField.DIRECTORIES_ONLY);
        workingDirField.setFile(Run.DEFAULT_DIR);
        add(workingDirField);
        add(workingDirField.getBrowseButton(), "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (nameField.getText().trim().length() == 0) {
            showErrorDialog("Please specify an experiment name");
            return false;
        }

        if (!workingDirField.isCorrect())
            return false;

        if (buffer) {
            put(KEY_NAME, nameField.getText());
            put(KEY_DIR, workingDirField.getFile());
        }

        return true;
    }

}
