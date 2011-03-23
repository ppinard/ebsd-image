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

import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.SingleFileBrowserField;
import ptpshared.gui.WizardPage;
import rmlshared.gui.CheckBox;
import rmlshared.io.FileUtil;

/**
 * Wizard page for the output of the import.
 * 
 * @author Philippe T. Pinard
 */
public class OutputWizardPage extends WizardPage {

    /** Map key for the output file. */
    public static final String KEY_OUTPUT_FILE = "outputFile";

    /** Map key for whether to display the multimap in the GUI. */
    public static final String KEY_DISPLAY_GUI = "displayGUI";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Output";
    }

    /** Field for the output file. */
    private SingleFileBrowserField outputFileField;

    /** Check box to display the MMap after being loaded. */
    private CheckBox displayCBox;



    /**
     * Creates a new <code>OutputWizardPage</code>.
     */
    public OutputWizardPage() {
        setLayout(new MigLayout("", "[][grow, fill][]"));

        add(new JLabel("Multimap output file"));
        FileFilter[] filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "HKL multimap (*.zip)", "zip") };
        outputFileField =
                new SingleFileBrowserField("Output file", false, filters);
        add(outputFileField);
        add(outputFileField.getBrowseButton(), "wrap");

        displayCBox = new CheckBox("Display multimap after loading");
        add(displayCBox, "span 3");
        displayCBox.setSelected(true);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (outputFileField.getFile() == null) {
            showErrorDialog("Please specify an output file.");
            return false;
        }

        if (buffer) {
            put(KEY_OUTPUT_FILE, outputFileField.getFile());
            put(KEY_DISPLAY_GUI, displayCBox.isSelected());
        }

        return true;
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        File file = (File) get(StartWizardPage.KEY_ANG_FILE);

        if (file == null)
            return;

        file = FileUtil.setExtension(file, "zip");
        outputFileField.setFile(file);
    }
}
