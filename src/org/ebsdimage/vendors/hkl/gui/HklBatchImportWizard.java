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

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ptpshared.gui.Wizard;
import ptpshared.gui.WizardPage;
import crystallography.core.Crystal;
import static rmlshared.io.FileUtil.getURL;

/**
 * Wizard to import several HKL data at once.
 * 
 * @author Philippe T. Pinard
 */
public class HklBatchImportWizard extends Wizard {

    /**
     * Creates a new <code>HklBatchImportWizard</code>.
     */
    public HklBatchImportWizard() {
        super("Import from HKL", new WizardPage[] { new StartBatchWizardPage(),
                new org.ebsdimage.gui.PhasesWizardPage(),
                new OutputBatchWizardPage() });

        BufferedImage image;
        try {
            image = ImageIO.read(getURL("org/ebsdimage/gui/sidepanel.png"));
        } catch (Exception ex) {
            image = null;
        }

        setSidePanelBackground(image);

        setPreferredWidth(800);
        setPreferredHeight(600);
    }



    /**
     * Returns the CTF files to load.
     * 
     * @return CTF files
     */
    public File[] getCprFiles() {
        File[] ctfFiles =
                (File[]) results.get(StartBatchWizardPage.KEY_CPR_FILES);

        if (ctfFiles == null)
            throw new NullPointerException(
                    "Could not get CTF files from wizard.");

        return ctfFiles;
    }



    /**
     * Returns the output directory for the MMap.
     * 
     * @return output file
     */
    public File getOutputDir() {
        File outputDir =
                (File) results.get(OutputBatchWizardPage.KEY_OUTPUT_DIR);

        if (outputDir == null)
            throw new NullPointerException(
                    "Could not get output file from wizard.");

        return outputDir;
    }



    /**
     * Returns the phases specified in this dialog.
     * 
     * @return phases
     */
    public Crystal[] getPhases() {
        Crystal[] phases =
                (Crystal[]) results.get(org.ebsdimage.gui.PhasesWizardPage.KEY_PHASES);

        if (phases == null)
            throw new NullPointerException(
                    "Could not get the phases from wizard.");

        return phases;
    }



    /**
     * Returns whether to import diffraction patterns.
     * 
     * @return <code>true</code> if the diffraction patterns are imported,
     *         <code>false</code> otherwise
     */
    public boolean getSavePatterns() {
        Boolean answer =
                (Boolean) results.get(StartBatchWizardPage.KEY_IMPORT_PATTERNS);

        if (answer == null)
            throw new NullPointerException(
                    "Could not get the import patterns value from wizard.");

        return answer;
    }

}
