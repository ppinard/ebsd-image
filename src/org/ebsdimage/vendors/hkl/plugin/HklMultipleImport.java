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
package org.ebsdimage.vendors.hkl.plugin;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.Camera;
import org.ebsdimage.io.SmpCreator;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.gui.HklMultipleImportWizard;
import org.ebsdimage.vendors.hkl.io.CtfLoader;
import org.ebsdimage.vendors.hkl.io.HklMMapSaver;
import org.ebsdimage.vendors.hkl.io.Util;

import rmlimage.plugin.PlugIn;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Import several CTF files at once by converting them to multimaps.
 * 
 * @author Philippe T. Pinard
 */
public class HklMultipleImport extends PlugIn implements Monitorable {

    /** Loader for the ctf file. */
    private CtfLoader ctfLoader = new CtfLoader();

    /** Saver for the MMap. */
    private HklMMapSaver mmapSaver = new HklMMapSaver();

    /** Creator for the smp file. */
    private SmpCreator smpCreator = new SmpCreator();

    /** Progress. */
    private double progress = 0.0;

    /** Progress status. */
    private String status = "";



    /**
     * Creates a new <code>HklImport</code> plug-in.
     */
    public HklMultipleImport() {
        setInterruptable(true);
    }



    /**
     * Displays a dialog and performs the import of the CTF files.
     */
    private void doImport() {
        HklMultipleImportWizard wizard = new HklMultipleImportWizard();
        wizard.setPreferences(getPreferences());

        if (!wizard.show())
            return;

        // Constant parameters
        Camera calibration = wizard.getCalibration();
        double workingDistance = wizard.getWorkingDistance();
        Crystal[] phases = wizard.getPhases();
        File outputDir = wizard.getOutputDir();

        File[] ctfFiles = wizard.getCtfFiles();

        File ctfFile;
        File outputFile;
        for (int i = 0; i < ctfFiles.length; i++) {
            progress = (double) (i + 1) / ctfFiles.length;

            // Load ctf
            ctfFile = ctfFiles[i];
            status = "Loading ctf file.";
            HklMMap mmap = null;

            try {
                mmap =
                        ctfLoader.load(ctfFile, workingDistance, calibration,
                                phases);
            } catch (IOException e) {
                showErrorDialog("While loading the ctf:" + e.getMessage());
            }

            // Save MMap
            mmapSaver = new HklMMapSaver();
            status = "Saving multimap";
            outputFile = new File(outputDir, FileUtil.getBaseName(ctfFile));
            outputFile = FileUtil.setExtension(outputFile, "zip");

            try {
                mmapSaver.save(mmap, outputFile);
            } catch (IOException e) {
                showErrorDialog("While saving the multimap:" + e.getMessage());
            }

            // Load patterns
            status = "Saving patterns in smp";

            if (wizard.getSavePatterns()) {
                File[] patternFiles =
                        mmap.getPatternFiles(Util.getPatternImagesDir(ctfFile));

                File smpFile = FileUtil.setExtension(outputFile, "smp");
                smpCreator = new SmpCreator();

                try {
                    smpCreator.create(smpFile, patternFiles);
                } catch (IOException e) {
                    showErrorDialog("While creating the smp file:"
                            + e.getMessage());
                }
            }

        }
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    @Override
    public void interrupt() {
        super.interrupt();
        ctfLoader.interrupt();
        smpCreator.interrupt();
    }



    @Override
    protected void xRun() throws Exception {
        doImport();
    }

}
