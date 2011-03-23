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

import org.ebsdimage.vendors.tsl.io.AngLoader;

import ptpshared.gui.SingleFileBrowserField;
import ptpshared.gui.WizardPage;

/**
 * First page for the import wizard.
 * 
 * @author Philippe T. Pinard
 */
public class StartWizardPage extends WizardPage {

    /** Map key for the ANG file. */
    public static final String KEY_ANG_FILE = "angFile";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }

    /** Field for the ANG file. */
    private SingleFileBrowserField angFileField;



    /**
     * Creates a new <code>StartWizardPage</code>.
     */
    public StartWizardPage() {

        setLayout(new MigLayout("", "[][grow,fill][]", ""));

        String text =
                "<html>This importer converts the acquisition parameters "
                        + "and data from a ANG file into EBSD-Image's EBSD multimap "
                        + "format."
                        + "<br/><br/>"
                        + "For more information, please visit: http://ebsd-image.sourceforge.net/wiki/ImportFromTSLOIM"
                        + "</html>";
        add(new JLabel(text), "grow, span 3, wrap 40");

        add(new JLabel("Ang file"));

        FileFilter[] filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "TSL OIM ANG file (*.ang)", "ang") };
        angFileField = new SingleFileBrowserField("Ang file", false, filters);
        add(angFileField);
        add(angFileField.getBrowseButton(), "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        File angFile = angFileField.getFile();

        if (angFile == null) {
            showErrorDialog("Please specify a ANG file.");
            return false;
        }

        if (!new AngLoader().canLoad(angFile)) {
            showErrorDialog("The ANG file is not valid.");
            return false;
        }

        if (buffer)
            put(KEY_ANG_FILE, angFileField.getFile());

        return true;
    }
}
