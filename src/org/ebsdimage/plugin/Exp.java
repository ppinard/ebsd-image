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
package org.ebsdimage.plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.gui.exp.MapsGUIListener;
import org.ebsdimage.gui.exp.wizard.ExpWizard;
import org.ebsdimage.io.exp.ExpMMapSaver;
import org.ebsdimage.io.exp.ExpSaver;

import ptpshared.util.LoggerUtil;
import rmlimage.plugin.PlugIn;
import rmlimage.plugin.builtin.CloseAll;
import rmlshared.gui.Panel;
import rmlshared.gui.YesNoCancelDialog;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Plug-in for the experiment's engine.
 * 
 * @author Philippe T. Pinard
 */
public class Exp extends PlugIn implements Monitorable {

    /**
     * Dialog displayed after the preview to decided the next action.
     * 
     * @author Philippe T. Pinard
     */
    private static class PreviewDialog extends YesNoCancelDialog {

        /**
         * Creates a new <code>PreviewDialog</code>.
         */
        public PreviewDialog() {
            super(null, "Preview");

            Panel panel = new Panel(new MigLayout());

            panel.add(new JLabel("\"Yes\": Save XML and run the experiment"),
                    "wrap");
            panel.add(new JLabel(
                    "\"No\": Go back in the wizard to modify the operations"),
                    "wrap");
            panel.add(new JLabel("\"Cancel\": Cancel the experiment"), "wrap");

            setMainComponent(panel);
        }
    }

    /** Experiment. */
    private org.ebsdimage.core.exp.Exp exp;

    /** Experiment wizard. */
    private ExpWizard wizard;



    /**
     * Creates a new <code>Exp</code> plug-in.
     * 
     * @throws IOException
     *             if an error occurs while creating the wizard
     */
    public Exp() throws IOException {
        setInterruptable(true);

        exp = null;
        wizard = new ExpWizard();
        wizard.setPreferences(getPreferences());
    }



    /**
     * Creates a new experiment from the results in the wizard.
     * 
     * @param wizard
     *            engine wizard dialog
     */
    private void createExp(ExpWizard wizard) {
        ExpMMap mmap = new ExpMMap(wizard.getWidth(), wizard.getHeight());

        EbsdMetadata metadata = new EbsdMetadata(wizard.getAcquisitionConfig());
        mmap.setMetadata(metadata);

        mmap.setCalibration(wizard.getCalibration());

        for (Crystal crystal : wizard.getPhases())
            mmap.getPhaseMap().register(crystal);

        exp = new org.ebsdimage.core.exp.Exp(mmap, wizard.getOperations());
        exp.setName(wizard.getName());
        exp.setDir(wizard.getDir());
    }



    /**
     * Creates a new experiment from the results in the wizard to be launched in
     * the preview mode.
     * 
     * @param wizard
     *            engine wizard dialog
     */
    private void createExpPreview(ExpWizard wizard) {
        ExpMMap mmap = new ExpMMap(wizard.getWidth(), wizard.getHeight());

        EbsdMetadata metadata = new EbsdMetadata(wizard.getAcquisitionConfig());
        mmap.setMetadata(metadata);

        mmap.setCalibration(wizard.getCalibration());

        for (Crystal crystal : wizard.getPhases())
            mmap.getPhaseMap().register(crystal);

        exp =
                new org.ebsdimage.core.exp.Exp(mmap,
                        wizard.getPreviewOperations());
        exp.setName(wizard.getName());
        exp.setDir(wizard.getDir());

        exp.addExpListener(new MapsGUIListener());
    }



    @Override
    public double getTaskProgress() {
        if (exp != null)
            return exp.getTaskProgress();
        else
            return super.getTaskProgress();
    }



    @Override
    public String getTaskStatus() {
        if (exp != null)
            return exp.getTaskStatus();
        else
            return super.getTaskStatus();
    }



    @Override
    public void interrupt() {
        if (exp != null) {
            super.interrupt();
            exp.interrupt();
        }
    }



    /**
     * Saves the experiment by asking the user where to save the file.
     * 
     * @throws IOException
     *             if an error occurs while saving
     */
    private void saveExp() throws IOException {
        File file = new File(exp.getDir(), exp.getName() + ".xml");
        new ExpSaver().save(exp, file);
    }



    @Override
    protected void xRun() throws Exception {
        if (!wizard.show())
            return;

        // Preview mode
        if (wizard.getPreviewMode()) {
            createExpPreview(wizard);

            // Turn off the logger
            LoggerUtil.turnOffLogger(Logger.getLogger("ebsd"));

            // Close all maps before run
            new CloseAll().xRun();

            // Run experiment
            exp.run();

            // Preview dialog
            PreviewDialog previewDialog = new PreviewDialog();

            previewDialog.setModal(false);
            int answer = previewDialog.show();

            switch (answer) {
            case 0: // Yes
                break; // Continue to run
            case 1: // No
                xRun();
                return;
            case 2: // Cancel
                return;
            default:
                return;
            }
        }

        // Create experiment for the run
        createExp(wizard);

        // Save
        if (wizard.getSaveMode())
            saveExp();

        // Run
        if (wizard.getRunMode()) {
            // Turn off the logger
            LoggerUtil.turnOffLogger(Logger.getLogger("ebsd"));

            // Add multimap to GUI
            add(exp.mmap);

            // Run experiment
            exp.run();

            // Saves multimap
            File file = new File(exp.getDir(), exp.getName() + ".zip");
            new ExpMMapSaver().save(exp.mmap, file);
        }
    }
}
