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
package org.ebsdimage.vendors.tsl.plugin;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.io.SmpCreator;
import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.ebsdimage.vendors.tsl.core.TslMetadata;
import org.ebsdimage.vendors.tsl.gui.TslImportWizard;
import org.ebsdimage.vendors.tsl.io.AngLoader;
import org.ebsdimage.vendors.tsl.io.TslMMapSaver;

import rmlimage.plugin.PlugIn;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Import an ANG file by converting it to a multimap.
 * 
 * @author Philippe T. Pinard
 */
public class TslImport extends PlugIn implements Monitorable {

    /** Loader for the ANG file. */
    private AngLoader angLoader;

    /** Saver for the MMap. */
    private TslMMapSaver mmapSaver;

    /** Creator for the SMP file. */
    private SmpCreator smpCreator;

    /** Progress status. */
    private String status;



    /**
     * Creates a new <code>TslImport</code> plug-in.
     */
    public TslImport() {
        setInterruptable(true);
    }



    @Override
    public double getTaskProgress() {
        if (angLoader != null)
            return angLoader.getTaskProgress();
        else if (mmapSaver != null)
            return mmapSaver.getTaskProgress();
        else if (smpCreator != null)
            return smpCreator.getTaskProgress();
        else
            return 0;
    }



    @Override
    public String getTaskStatus() {
        if (angLoader != null)
            return status + " - " + angLoader.getTaskStatus();
        else if (smpCreator != null)
            return status + " - " + smpCreator.getTaskStatus();
        else
            return status;
    }



    @Override
    public void interrupt() {
        if (angLoader != null) {
            super.interrupt();
            angLoader.interrupt();
        } else if (smpCreator != null) {
            super.interrupt();
            smpCreator.interrupt();
        }
    }



    @Override
    protected void xRun() throws Exception {
        TslImportWizard wizard = new TslImportWizard();
        wizard.setPreferences(getPreferences());

        if (!wizard.show())
            return;

        // Load ANG
        status = "Loading ANG file.";
        angLoader = new AngLoader();

        AcquisitionConfig acqConfig = wizard.getAcquisitionConfig();
        Crystal[] phases = wizard.getPhases();
        File angFile = wizard.getAngFile();

        TslMetadata metadata;
        try {
            metadata = angLoader.loadMetadata(angFile, Microscope.DEFAULT);
        } catch (IOException e) {
            showErrorDialog("While loading the ANG:" + e.getMessage());
            return;
        }
        metadata = new TslMetadata(acqConfig);

        TslMMap mmap;
        try {
            mmap = angLoader.load(angFile, metadata, phases);
        } catch (IOException e) {
            showErrorDialog("While loading the ANG:" + e.getMessage());
            return;
        }

        angLoader = null;

        // Save multimap
        status = "Saving multimap.";
        mmapSaver = new TslMMapSaver();

        File outputFile = wizard.getOutputFile();

        try {
            mmapSaver.save(mmap, outputFile);
        } catch (IOException e) {
            showErrorDialog("While saving the multimap:" + e.getMessage());
            return;
        }

        mmapSaver = null;

        // Load patterns
        status = "Saving patterns in smp";

        File patternsDir = wizard.getPatternsDir();
        if (patternsDir != null) {
            File[] patternFiles = mmap.getPatternFiles(patternsDir);

            File smpFile = FileUtil.setExtension(outputFile, "smp");
            smpCreator = new SmpCreator();

            try {
                smpCreator.create(smpFile, patternFiles);
            } catch (IOException e) {
                showErrorDialog("While creating the smp file:" + e.getMessage());
                return;
            }
        }

        smpCreator = null;

        // Display multimap in the GUI
        if (wizard.getDisplayGUI())
            add(mmap);
    }

}
