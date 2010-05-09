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
package org.ebsdimage.vendors.hkl.gui;

import javax.swing.JLabel;

import org.ebsdimage.vendors.hkl.io.CtfLoader;

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

    /** Map key for the ctf file. */
    public static final String KEY_CTF_FILE = "ctfFile";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }



    /** Field for the ctf file. */
    private FileNameField ctfFileField;



    /**
     * Creates a new <code>StartWizardPage</code>.
     */
    public StartWizardPage() {

        setLayout(new MigLayout("", "[][grow,fill][]", ""));

        String text =
                "<html>This importer converts the acquisition parameters "
                        + "and data from a ctf file into EBSD-Image's EBSD multimap "
                        + "format. The user can select whether if wants to import "
                        + "the diffraction patterns of the acquisition to a smp file."
                        + "<br/><br/>"
                        + "For more information, please visit: http://ebsd-image.sourceforge.net/wiki/ImportFromHKLChannel5"
                        + "</html>";
        add(new JLabel(text), "grow, span 3, wrap 40");

        add(new JLabel("Ctf file"));
        ctfFileField = new FileNameField("Ctf file", 32, false);
        ctfFileField.setFileSelectionMode(FileNameField.FILES_ONLY);
        FileDialog.addFilter("*.ctf");
        ctfFileField.setFileFilter("*.ctf");
        add(ctfFileField);
        add(ctfFileField.getBrowseButton(), "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (ctfFileField.getFile() == null) {
            showErrorDialog("Please specify a ctf file.");
            return false;
        }

        if (!CtfLoader.isCtf(ctfFileField.getFile())) {
            showErrorDialog("The ctf file is not valid.");
            return false;
        }

        if (buffer)
            put(KEY_CTF_FILE, ctfFileField.getFile());

        return true;
    }
}
