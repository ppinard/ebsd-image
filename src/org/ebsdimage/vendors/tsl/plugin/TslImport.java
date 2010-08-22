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
package org.ebsdimage.vendors.tsl.plugin;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.ebsdimage.vendors.tsl.gui.TslImportWizard;
import org.ebsdimage.vendors.tsl.io.AngLoader;
import org.ebsdimage.vendors.tsl.io.TslMMapSaver;

import ptpshared.core.math.Quaternion;
import rmlimage.plugin.PlugIn;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Import an ang file by converting it to a multimap.
 * 
 * @author Philippe T. Pinard
 */
public class TslImport extends PlugIn implements Monitorable {

    /** Loader for the ang file. */
    private AngLoader angLoader;

    /** Saver for the MMap. */
    private TslMMapSaver mmapSaver;

    /** Progress status. */
    private String status;



    /**
     * Creates a new <code>TslImport</code> plug-in.
     */
    public TslImport() {
        setInterruptable(true);
    }



    /**
     * Displays a dialog and performs the import of the ang file.
     * 
     * @return a <code>HklMMap</code>
     */
    private TslMMap doImport() {
        TslImportWizard dialog = new TslImportWizard();

        if (!dialog.show())
            return null;

        // Load ctf
        double beamEnergy = dialog.getBeamEnergy();
        double magnification = dialog.getMagnification();
        double tiltAngle = dialog.getTiltAngle();
        Quaternion rotation = dialog.getSampleRotation();
        Crystal[] phases = dialog.getPhases();
        File angFile = dialog.getAngFile();

        angLoader = new AngLoader();
        status = "Loading ang file.";
        TslMMap mmap = null;

        try {
            mmap =
                    angLoader.load(angFile, beamEnergy, magnification,
                            tiltAngle, rotation, phases);
        } catch (IOException e) {
            showErrorDialog("While loading the ang:" + e.getMessage());
        }

        angLoader = null;

        // Save MMap
        File outputFile = dialog.getOutputFile();

        mmapSaver = new TslMMapSaver();
        status = "Saving multimap.";

        try {
            mmapSaver.save(mmap, outputFile);
        } catch (IOException e) {
            showErrorDialog("While saving the multimap:" + e.getMessage());
        }

        mmapSaver = null;

        // Display MMap in the GUI
        if (dialog.getDisplayGUI())
            add(mmap);

        return mmap;
    }



    @Override
    public double getTaskProgress() {
        if (angLoader != null)
            return angLoader.getTaskProgress();
        else if (mmapSaver != null)
            return mmapSaver.getTaskProgress();
        else
            return 0;
    }



    @Override
    public String getTaskStatus() {
        if (angLoader != null)
            return status + " - " + angLoader.getTaskStatus();
        else
            return status;
    }



    @Override
    public void interrupt() {
        if (angLoader != null) {
            super.interrupt();
            angLoader.interrupt();
        }
    }



    @Override
    protected void xRun() throws Exception {
        doImport();
    }

}
