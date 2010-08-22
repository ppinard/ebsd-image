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

import static rmlshared.io.FileUtil.getURL;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.ebsdimage.core.Camera;

import ptpshared.gui.Wizard;
import ptpshared.gui.WizardPage;
import crystallography.core.Crystal;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Wizard to import HKL data.
 * 
 * @author Philippe T. Pinard
 */
public class HklImportWizard extends Wizard {

    /**
     * Creates a new <code>ImportWizard</code>.
     */
    public HklImportWizard() {
        super(new WizardPage[] { new StartWizardPage(),
                new MissingDataWizardPage(), new PhasesWizardPage(),
                new PatternsWizardPage(), new OutputWizardPage() });

        setTitle("Import from HKL");

        BufferedImage image;
        try {
            image = ImageIO.read(getURL("org/ebsdimage/gui/exp/sidepanel.png"));
        } catch (Exception ex) {
            image = null;
        }

        setSidePanelBackground(image);

        setPreferredWidth(800);
        setPreferredHeight(600);
    }



    /**
     * Returns the calibration specified in this dialog.
     * 
     * @return calibration
     */
    public Camera getCalibration() {
        Camera calibration =
                (Camera) results.get(MissingDataWizardPage.KEY_CALIBRATION);

        if (calibration == null)
            throw new NullPointerException(
                    "Could not get calibration from wizard.");

        return calibration;
    }



    /**
     * Returns the ctf file to load.
     * 
     * @return ctf file
     */
    public File getCtfFile() {
        File ctfFile = (File) results.get(StartWizardPage.KEY_CTF_FILE);

        if (ctfFile == null)
            throw new NullPointerException(
                    "Could not get ctf file from wizard.");

        return ctfFile;
    }



    /**
     * Returns whether the multimap must be displayed in the GUI after the
     * loading.
     * 
     * @return <code>true</code> if the multimap must be displayed,
     *         <code>false</code> otherwise
     */
    public boolean getDisplayGUI() {
        Boolean displayGUI =
                (Boolean) results.get(OutputWizardPage.KEY_DISPLAY_GUI);

        if (displayGUI == null)
            throw new NullPointerException(
                    "Could not get the display in GUI value from wizard.");

        return displayGUI;
    }



    /**
     * Returns the output file for the MMap.
     * 
     * @return outptu file
     */
    public File getOutputFile() {
        File outputFile = (File) results.get(OutputWizardPage.KEY_OUTPUT_FILE);

        if (outputFile == null)
            throw new NullPointerException(
                    "Could not get output file from wizard.");

        return outputFile;
    }



    /**
     * Returns the working distance specified in this dialog.
     * 
     * @return working distance
     */
    public double getWorkingDistance() {
        Double workingDistance =
                (Double) results.get(MissingDataWizardPage.KEY_WORKING_DISTANCE);

        if (workingDistance == null)
            throw new NullPointerException(
                    "Could not get working distance from wizard.");

        return workingDistance;
    }



    /**
     * Returns the directory where the patterns are located.
     * 
     * @return directory
     */
    @CheckForNull
    public File getPatternsDir() {
        File patternsDir =
                (File) results.get(PatternsWizardPage.KEY_PATTERNS_FOLDER);

        if (patternsDir == null)
            throw new NullPointerException(
                    "Could not get pattern directory from wizard.");

        return patternsDir;
    }



    /**
     * Returns whether to import the patterns.
     * 
     * @return <code>true</code> if the patterns should be imported,
     *         <code>false</code> otherwise
     */
    public boolean getImportPatterns() {
        Boolean importPatterns =
                (Boolean) results.get(PatternsWizardPage.KEY_IMPORT_PATTERNS);

        if (importPatterns == null)
            throw new NullPointerException(
                    "Could not get the import patterns value from wizard.");

        return importPatterns;
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

}
