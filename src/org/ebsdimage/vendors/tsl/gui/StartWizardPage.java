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

import javax.swing.JLabel;

import org.ebsdimage.vendors.tsl.io.AngLoader;

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.WizardPage;
import rmlimage.gui.FileDialog;
import rmlshared.gui.FileNameField;

/**
 * First page for the import wizard.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class StartWizardPage extends WizardPage {

    /** Map key for the ang file. */
    public static final String KEY_ANG_FILE = "angFile";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }



    /** Field for the ang file. */
    private FileNameField angFileField;



    /**
     * Creates a new <code>StartWizardPage</code>.
     */
    public StartWizardPage() {

        setLayout(new MigLayout("", "[][grow,fill][]", ""));

        String text =
                "<html>This importer converts the acquisition parameters "
                        + "and data from a ang file into EBSD-Image's EBSD multimap "
                        + "format."
                        + "<br/><br/>"
                        + "For more information, please visit: http://ebsd-image.sourceforge.net/wiki/ImportFromTSLOIM"
                        + "</html>";
        add(new JLabel(text), "grow, span 3, wrap 40");

        add(new JLabel("Ang file"));
        angFileField = new FileNameField("Ang file", 32, false);
        angFileField.setFileSelectionMode(FileNameField.FILES_ONLY);
        FileDialog.addFilter("*.ang");
        angFileField.setFileFilter("*.ang");
        add(angFileField);
        add(angFileField.getBrowseButton(), "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (angFileField.getFile() == null) {
            showErrorDialog("Please specify a ang file.");
            return false;
        }

        if (!AngLoader.isAng(angFileField.getFile())) {
            showErrorDialog("The ang file is not valid.");
            return false;
        }

        if (buffer)
            put(KEY_ANG_FILE, angFileField.getFile());

        return true;
    }
}
