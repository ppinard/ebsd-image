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
import org.ebsdimage.vendors.hkl.gui.HklImportWizard;
import org.ebsdimage.vendors.hkl.io.CtfLoader;
import org.ebsdimage.vendors.hkl.io.HklMMapSaver;

import rmlimage.plugin.PlugIn;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Import a ctf file by converting it to a multimap.
 * 
 * @author Philippe T. Pinard
 */
public class HklImport extends PlugIn implements Monitorable {

    /** Loader for the ctf file. */
    private CtfLoader ctfLoader;

    /** Saver for the MMap. */
    private HklMMapSaver mmapSaver;

    /** Creator for the smp file. */
    private SmpCreator smpCreator;

    /** Progress status. */
    private String status;



    /**
     * Creates a new <code>HklImport</code> plug-in.
     */
    public HklImport() {
        setInterruptable(true);
    }



    /**
     * Displays a dialog and performs the import of the ctf file.
     * 
     * @return a <code>HklMMap</code>
     */
    private HklMMap doImport() {
        HklImportWizard wizard = new HklImportWizard();
        wizard.setPreferences(getPreferences());

        if (!wizard.show())
            return null;

        // Load ctf
        Camera calibration = wizard.getCalibration();
        double workingDistance = wizard.getWorkingDistance();
        Crystal[] phases = wizard.getPhases();
        File ctfFile = wizard.getCtfFile();

        ctfLoader = new CtfLoader();
        status = "Loading ctf file.";
        HklMMap mmap = null;

        try {
            mmap =
                    ctfLoader.load(ctfFile, workingDistance, calibration,
                            phases);
        } catch (IOException e) {
            showErrorDialog("While loading the ctf:" + e.getMessage());
        }

        ctfLoader = null;

        // Save MMap
        File outputFile = wizard.getOutputFile();

        mmapSaver = new HklMMapSaver();
        status = "Saving multimap";

        try {
            mmapSaver.save(mmap, outputFile);
        } catch (IOException e) {
            showErrorDialog("While saving the multimap:" + e.getMessage());
        }

        mmapSaver = null;

        // Load patterns
        status = "Saving patterns in smp";

        File imagesDir = wizard.getPatternsDir();
        if (imagesDir != null) {
            File[] patternFiles = mmap.getPatternFiles(imagesDir);

            File smpFile = FileUtil.setExtension(outputFile, "smp");
            smpCreator = new SmpCreator();

            try {
                smpCreator.create(smpFile, patternFiles);
            } catch (IOException e) {
                showErrorDialog("While creating the smp file:" + e.getMessage());
            }
        }

        smpCreator = null;

        // Display MMap in the GUI
        if (wizard.getDisplayGUI())
            add(mmap);

        return mmap;
    }



    @Override
    public double getTaskProgress() {
        if (ctfLoader != null)
            return ctfLoader.getTaskProgress();
        else if (mmapSaver != null)
            return mmapSaver.getTaskProgress();
        else if (smpCreator != null)
            return smpCreator.getTaskProgress();
        else
            return 0;
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
        doImport();
    }

}
