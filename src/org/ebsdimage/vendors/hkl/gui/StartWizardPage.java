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
package org.ebsdimage.vendors.hkl.gui;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.vendors.hkl.io.CprLoader;
import org.ebsdimage.vendors.hkl.io.CtfLoader;

import ptpshared.gui.SingleFileBrowserField;
import ptpshared.gui.WizardPage;
import rmlshared.io.FileUtil;

/**
 * First page for the import wizard.
 * 
 * @author Philippe T. Pinard
 */
public class StartWizardPage extends WizardPage {

    /** Map key for the CPR file. */
    public static final String KEY_CPR_FILE = "cprFile";

    /** Map key for the CTF file. */
    public static final String KEY_CTF_FILE = "ctfFile";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }

    /** Field for the CPR file. */
    private SingleFileBrowserField cprFileField;



    /**
     * Creates a new <code>StartWizardPage</code>.
     */
    public StartWizardPage() {

        setLayout(new MigLayout("", "[][grow,fill][]", ""));

        String text =
                "<html>This importer converts the acquisition parameters "
                        + "and data from a CPR/CTF files into EBSD-Image's EBSD multimap "
                        + "format. The user can select whether if wants to import "
                        + "the diffraction patterns of the acquisition to a SMP file."
                        + "<br/><br/>"
                        + "For more information, please visit: http://ebsd-image.sourceforge.net/wiki/ImportFromHKLChannel5"
                        + "</html>";
        add(new JLabel(text), "grow, span 3, wrap 40");

        add(new JLabel("CPR file"));

        FileFilter[] filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "Channel 5 project file (*.cpr)", "cpr") };
        cprFileField = new SingleFileBrowserField("CPR file", true, filters);
        add(cprFileField, "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        File cprFile = cprFileField.getFile();
        if (cprFile == null) {
            showErrorDialog("Please specify a CPR file.");
            return false;
        }

        if (!cprFile.exists()) {
            showErrorDialog("CPR file (" + cprFile + ") does not exist.");
            return false;
        }

        if (!new CprLoader().canLoad(cprFile)) {
            showErrorDialog("The CPR file is not valid.");
            return false;
        }

        File ctfFile = FileUtil.setExtension(cprFile, "ctf");
        if (!ctfFile.exists()) {
            showErrorDialog("No CTF file (" + ctfFile
                    + ") for the specified CPR file.");
            return false;
        }

        if (!new CtfLoader().canLoad(ctfFile)) {
            showErrorDialog("The CTF file is not valid.");
            return false;
        }

        if (buffer) {
            put(KEY_CPR_FILE, cprFile);
            put(KEY_CTF_FILE, ctfFile);
        }

        return true;
    }
}
