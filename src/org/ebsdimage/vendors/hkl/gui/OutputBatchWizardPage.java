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

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.DirBrowserField;
import ptpshared.gui.WizardPage;
import rmlshared.io.FileUtil;

/**
 * Wizard page for the output of the import.
 * 
 * @author Philippe T. Pinard
 */
public class OutputBatchWizardPage extends WizardPage {

    /** Map key for the output file. */
    public static final String KEY_OUTPUT_DIR = "outputDir";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Output";
    }

    /** Field for the output directory. */
    private DirBrowserField outputDirField;



    /**
     * Creates a new <code>OutputBatchWizardPage</code>.
     */
    public OutputBatchWizardPage() {
        setLayout(new MigLayout("", "[][grow, fill][]"));

        add(new JLabel("Multimaps output directory"));
        outputDirField = new DirBrowserField("Output dir", false);
        add(outputDirField);
        add(outputDirField.getBrowseButton(), "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        File outputdir = outputDirField.getDir();

        if (outputdir == null) {
            showErrorDialog("Please specify an output direcotry.");
            return false;
        }

        if (buffer)
            put(KEY_OUTPUT_DIR, outputdir);

        return true;
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        File[] cprFiles = (File[]) get(StartBatchWizardPage.KEY_CPR_FILES);

        if (cprFiles == null)
            return;

        outputDirField.setDir(FileUtil.getParentFile(cprFiles[0]));
    }

}
