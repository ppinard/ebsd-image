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

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.ebsdimage.core.Microscope;

import ptpshared.gui.Wizard;
import ptpshared.gui.WizardPage;
import crystallography.core.Crystal;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import static rmlshared.io.FileUtil.getURL;

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
        super("Import from HKL", new WizardPage[] { new StartWizardPage(),
                new MicroscopeWizardPage(), new PhasesWizardPage(),
                new PatternsWizardPage(), new OutputWizardPage() });

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
     * Returns the CTF file to load.
     * 
     * @return CTF file
     */
    public File getCtfFile() {
        File ctfFile = (File) results.get(StartWizardPage.KEY_CTF_FILE);

        if (ctfFile == null)
            throw new NullPointerException(
                    "Could not get CTF file from wizard.");

        return ctfFile;
    }



    /**
     * Returns the CPR file to load.
     * 
     * @return CPR file
     */
    public File getCprFile() {
        File cprFile = (File) results.get(StartWizardPage.KEY_CPR_FILE);

        if (cprFile == null)
            throw new NullPointerException(
                    "Could not get CPR file from wizard.");

        return cprFile;
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
     * Returns the output file for the multimap.
     * 
     * @return output file
     */
    public File getOutputFile() {
        File outputFile = (File) results.get(OutputWizardPage.KEY_OUTPUT_FILE);

        if (outputFile == null)
            throw new NullPointerException(
                    "Could not get output file from wizard.");

        return outputFile;
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

        return patternsDir;
    }



    /**
     * Returns the phases specified in this dialog.
     * 
     * @return phases
     */
    public Crystal[] getPhases() {
        Crystal[] phases = (Crystal[]) results.get(PhasesWizardPage.KEY_PHASES);

        if (phases == null)
            throw new NullPointerException(
                    "Could not get the phases from wizard.");

        return phases;
    }



    /**
     * Returns the microscope with the CTF parameters.
     * 
     * @return microscope
     */
    public Microscope getMicroscope() {
        Microscope microscope =
                (Microscope) results.get(MicroscopeWizardPage.KEY_MICROSCOPE);

        if (microscope == null)
            throw new NullPointerException(
                    "Could not get the microscope from wizard.");

        return microscope;
    }

}
