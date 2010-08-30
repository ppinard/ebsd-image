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
package org.ebsdimage.plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.gui.sim.SimWizard;
import org.ebsdimage.io.sim.SimSaver;

import ptpshared.core.math.Quaternion;
import ptpshared.utility.LoggerUtil;
import rmlimage.plugin.PlugIn;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Plug-in for the simulation engine.
 * 
 * @author Philippe T. Pinard
 */
public class Sim extends PlugIn implements Monitorable {

    /** Simulation. */
    private org.ebsdimage.core.sim.Sim sim;

    /** Simulation wizard. */
    private SimWizard wizard;



    /**
     * Creates a new <code>Sim</code> plug-in.
     * 
     * @throws IOException
     *             if an error occurs while creating the wizard
     */
    public Sim() throws IOException {
        setInterruptable(true);

        sim = null;
        wizard = new SimWizard();
        wizard.setPreferences(getPreferences());
    }



    @Override
    public double getTaskProgress() {
        if (sim != null)
            return sim.getTaskProgress();
        else
            return super.getTaskProgress();
    }



    @Override
    public String getTaskStatus() {
        if (sim != null)
            return sim.getTaskStatus();
        else
            return super.getTaskStatus();
    }



    @Override
    public void interrupt() {
        if (sim != null) {
            super.interrupt();
            sim.interrupt();
        }
    }



    /**
     * Saves the experiment by asking the user where to save the file.
     * 
     * @throws IOException
     *             if an error occurs while saving
     */
    private void saveSim() throws IOException {
        File file = new File(sim.getDir(), sim.getName() + ".xml");
        new SimSaver().save(sim, file);
    }



    /**
     * Creates a new simulation from the results in the wizard.
     * 
     * @param wizard
     *            simulation engine wizard dialog
     */
    private void createSim(SimWizard wizard) {
        Operation[] ops = wizard.getOperations();
        Camera[] cameras = wizard.getCameras();
        Energy[] energies = wizard.getEnergies();
        Quaternion[] rotations = wizard.getRotations();
        Crystal[] phases = wizard.getPhases();
        String name = wizard.getName();
        File dir = wizard.getDir();

        sim =
                new org.ebsdimage.core.sim.Sim(ops, cameras, phases, energies,
                        rotations);
        sim.setName(name);
        sim.setDir(dir);
    }



    @Override
    protected void xRun() throws Exception {
        if (!wizard.show())
            return;

        // Create sim
        createSim(wizard);

        // Save
        saveSim();

        // Run
        // Turn off the logger
        LoggerUtil.turnOffLogger(Logger.getLogger("ebsd"));

        sim.run();
    }
}
