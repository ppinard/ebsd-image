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
package org.ebsdimage.vendors.hkl.plugin;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.Microscope;
import org.ebsdimage.io.SmpCreator;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.ebsdimage.vendors.hkl.gui.HklBatchImportWizard;
import org.ebsdimage.vendors.hkl.io.CprLoader;
import org.ebsdimage.vendors.hkl.io.CtfLoader;
import org.ebsdimage.vendors.hkl.io.HklMMapSaver;
import org.ebsdimage.vendors.hkl.io.Utils;

import rmlimage.plugin.PlugIn;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Import several CTF files at once by converting them to multimaps.
 * 
 * @author Philippe T. Pinard
 */
public class HklBatchImport extends PlugIn implements Monitorable {

    /** Loader for the CPR file. */
    private CprLoader cprLoader;

    /** Loader for the CTF file. */
    private CtfLoader ctfLoader;

    /** Saver for the MMap. */
    private HklMMapSaver mmapSaver;

    /** Creator for the SMP file. */
    private SmpCreator smpCreator;

    /** Progress: index of current CTF file. */
    private int index = 0;

    /** Progress: total number of CTF files. */
    private int total = 1;

    /** Progress status. */
    private String status = "";



    /**
     * Creates a new <code>HklImport</code> plug-in.
     */
    public HklBatchImport() {
        setInterruptable(true);
    }



    @Override
    public double getTaskProgress() {
        double progress = (double) index / total;

        if (ctfLoader != null)
            progress += ctfLoader.getTaskProgress() / total;
        else if (mmapSaver != null)
            progress += mmapSaver.getTaskProgress() / total;
        else if (smpCreator != null)
            progress += smpCreator.getTaskProgress() / total;

        return progress;
    }



    @Override
    public String getTaskStatus() {
        if (ctfLoader != null)
            return status + " - " + ctfLoader.getTaskStatus();
        else if (smpCreator != null)
            return status + " - " + smpCreator.getTaskStatus();
        else
            return status;
    }



    @Override
    public void interrupt() {
        if (ctfLoader != null) {
            super.interrupt();
            ctfLoader.interrupt();
        } else if (smpCreator != null) {
            super.interrupt();
            smpCreator.interrupt();
        }
    }



    @Override
    protected void xRun() throws Exception {
        HklBatchImportWizard wizard = new HklBatchImportWizard();
        wizard.setPreferences(getPreferences());

        if (!wizard.show())
            return;

        // Constant parameters
        Crystal[] phases = wizard.getPhases();
        File outputDir = wizard.getOutputDir();

        File[] cprFiles = wizard.getCprFiles();
        total = cprFiles.length;

        File cprFile;
        File ctfFile;
        HklMetadata metadata;
        HklMMap mmap;
        File outputFile;
        for (int i = 0; i < cprFiles.length; i++) {
            // Progress
            index = i;

            // Load CPR
            status = "Loading ctf file.";
            cprLoader = new CprLoader();

            cprFile = cprFiles[i];

            try {
                metadata = cprLoader.load(cprFile, new Microscope());
            } catch (IOException e) {
                showErrorDialog("While loading the CPR (" + cprFile + "):"
                        + e.getMessage());
                return;
            }

            cprLoader = null;

            // Load CTF
            status = "Loading ctf file.";
            ctfLoader = new CtfLoader();

            ctfFile = FileUtil.setExtension(cprFile, "ctf");

            try {
                mmap = ctfLoader.load(ctfFile, metadata, phases);
            } catch (IOException e) {
                showErrorDialog("While loading the CTF (" + ctfFile + "):"
                        + e.getMessage());
                return;
            }

            ctfLoader = null;

            // Save multimap
            status = "Saving multimap";
            mmapSaver = new HklMMapSaver();

            outputFile = new File(outputDir, FileUtil.getBaseName(ctfFile));
            outputFile = FileUtil.setExtension(outputFile, "zip");

            try {
                mmapSaver.save(mmap, outputFile);
            } catch (IOException e) {
                showErrorDialog("While saving the multimap:" + e.getMessage());
                return;
            }

            mmapSaver = null;

            // Load patterns
            status = "Saving patterns as a SMP";

            if (wizard.getSavePatterns()) {
                File[] patternFiles =
                        mmap.getPatternFiles(Utils.getPatternImagesDir(ctfFile));

                File smpFile = FileUtil.setExtension(outputFile, "smp");
                smpCreator = new SmpCreator();

                try {
                    smpCreator.create(smpFile, patternFiles);
                } catch (IOException e) {
                    showErrorDialog("While creating the SMP file (" + smpFile
                            + "):" + e.getMessage());
                    return;
                }
            }

            smpCreator = null;
        }
    }

}
