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
package org.ebsdimage.gui.exp;

import static rmlshared.io.FileUtil.getURL;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.ebsdimage.core.exp.ExpMetadata;
import org.ebsdimage.core.exp.ExpUtil;
import org.ebsdimage.core.exp.ops.pattern.op.PatternFileLoader;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.PhasesWizardPage;

import ptpshared.gui.Wizard;
import ptpshared.gui.WizardPage;
import rmlshared.cui.ErrorDialog;
import crystallography.core.Crystal;

/**
 * Wizard to setup an experiment.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class ExpWizard extends Wizard {

    /**
     * Creates a new wizard for the experiment.
     * 
     * @throws IOException
     *             if an error occurs
     */
    public ExpWizard() throws IOException {
        super(new WizardPage[] { new StartWizardPage(), new InfoWizardPage(),
                new AcqMetadataWizardPage(), new PhasesWizardPage(),
                new PatternsWizardPage(), new PatternWizardPage(),
                new HoughWizardPage(), new DetectionWizardPage(),
                new IdentificationWizardPage(), new IndexingWizardPage(),
                new OutputWizardPage() });

        setTitle("Experiment");

        BufferedImage image =
                ImageIO.read(getURL("org/ebsdimage/gui/exp/sidepanel.png"));
        setSidePanelBackground(image);

        setPreferredWidth(800);
    }



    /**
     * Adds the operations stored in the <code>results</code> to the overall
     * operations array list.
     * 
     * @param ops
     *            overall operations array list
     * @param key
     *            key in the <code>results</code> for the operations to add
     */
    private void addOps(ArrayList<Operation> ops, String key) {
        Operation[] tmpOps = (Operation[]) results.get(key);
        if (tmpOps != null)
            ops.addAll(Arrays.asList(tmpOps));
    }



    /**
     * Returns the working directory of the experiment.
     * 
     * @return working directory
     */
    public File getDir() {
        File dir = (File) results.get(InfoWizardPage.KEY_DIR);

        if (dir == null)
            throw new NullPointerException(
                    "Could not get the working directory from wizard.");

        return dir;
    }



    /**
     * Returns the height of the experiment's maps.
     * 
     * @return height of the maps
     */
    public int getHeight() {
        Integer height = (Integer) results.get(PatternsWizardPage.KEY_HEIGHT);

        if (height == null)
            throw new NullPointerException(
                    "Could not get the maps' height from wizard.");

        return height;
    }



    /**
     * Returns the metadata of the experiment.
     * 
     * @return metadata
     */
    public ExpMetadata getMetadata() {
        ExpMetadata metadata =
                (ExpMetadata) results.get(AcqMetadataWizardPage.KEY_METADATA);

        if (metadata == null)
            throw new NullPointerException(
                    "Could not get the metadata from wizard.");

        return metadata;
    }



    /**
     * Returns the name of the experiment.
     * 
     * @return name
     */
    public String getName() {
        String name = (String) results.get(InfoWizardPage.KEY_NAME);

        if (name == null)
            throw new NullPointerException(
                    "Could not get the name from wizard.");

        return name;
    }



    /**
     * Returns all the operations from the experiment.
     * 
     * @return operations
     */
    public Operation[] getOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        // Pattern
        ops.addAll(Arrays.asList(getPatternOperations()));

        // Other operations
        ops.addAll(Arrays.asList(getOperationsExceptPatternOps()));

        return ops.toArray(new Operation[ops.size()]);
    }



    /**
     * Returns all the operations from the experiment except the pattern
     * operations.
     * 
     * @return operations
     */
    private Operation[] getOperationsExceptPatternOps() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        // Pattern
        addOps(ops, PatternWizardPage.KEY_PATTERN_POST);
        addOps(ops, PatternWizardPage.KEY_PATTERN_RESULTS);

        // Hough
        addOps(ops, HoughWizardPage.KEY_HOUGH_PRE);
        addOps(ops, HoughWizardPage.KEY_HOUGH_OP);
        addOps(ops, HoughWizardPage.KEY_HOUGH_POST);
        addOps(ops, HoughWizardPage.KEY_HOUGH_RESULTS);

        // Detection
        addOps(ops, DetectionWizardPage.KEY_DETECTION_PRE);
        addOps(ops, DetectionWizardPage.KEY_DETECTION_OP);
        addOps(ops, DetectionWizardPage.KEY_DETECTION_POST);
        addOps(ops, DetectionWizardPage.KEY_DETECTION_RESULTS);

        // Identification
        addOps(ops, IdentificationWizardPage.KEY_IDENTIFICATION_PRE);
        addOps(ops, IdentificationWizardPage.KEY_IDENTIFICATION_OP);
        addOps(ops, IdentificationWizardPage.KEY_IDENTIFICATION_POST);
        addOps(ops, IdentificationWizardPage.KEY_IDENTIFICATION_RESULTS);

        // Indexing
        addOps(ops, IndexingWizardPage.KEY_INDEXING_PRE);
        addOps(ops, IndexingWizardPage.KEY_INDEXING_OP);
        addOps(ops, IndexingWizardPage.KEY_INDEXING_POST);
        addOps(ops, IndexingWizardPage.KEY_INDEXING_RESULTS);

        return ops.toArray(new Operation[ops.size()]);
    }



    /**
     * Returns the pattern operations for the experiment. The operations can be
     * coming from a smp file, a directory or a single file.
     * 
     * @return pattern operations
     */
    private PatternOp[] getPatternOperations() {
        File smpFile = (File) results.get(PatternsWizardPage.KEY_SMP_FILE);
        File dir = (File) results.get(PatternsWizardPage.KEY_DIR);
        File patternFile =
                (File) results.get(PatternsWizardPage.KEY_PATTERN_FILE);

        PatternOp[] ops;

        if (smpFile != null) {
            try {
                ops = ExpUtil.createPatternOpsFromSmp(smpFile);
            } catch (IOException e) {
                ops = new PatternOp[0];
                ErrorDialog.show("Cannot load images in smp file.");
            }
        } else if (dir != null) {
            ops = ExpUtil.createPatternOpsFromDir(dir);
        } else if (patternFile != null)
            ops = new PatternOp[] { new PatternFileLoader(0, patternFile) };
        else
            ops = new PatternOp[0];

        return ops;
    }



    /**
     * Returns the phases of the experiment.
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
     * Returns whether or not to launch the preview mode of the experiment.
     * 
     * @return <code>true</code> if the experiment launched in the preview mode,
     *         <code>false</code> otherwise
     */
    public boolean getPreviewMode() {
        Boolean preview = (Boolean) results.get(OutputWizardPage.KEY_PREVIEW);

        if (preview == null)
            throw new NullPointerException(
                    "Could not get the preview mode value from wizard.");

        return preview;
    }



    /**
     * Returns all the operations from the preview mode of the experiment.
     * 
     * @return operations
     */
    public Operation[] getPreviewOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        // Pattern
        ops.addAll(Arrays.asList(getPreviewPatternOperation()));

        // Other operations
        ops.addAll(Arrays.asList(getOperationsExceptPatternOps()));

        return ops.toArray(new Operation[ops.size()]);
    }



    /**
     * Returns the pattern operation for the preview mode.
     * 
     * @return pattern operation
     */
    private PatternOp getPreviewPatternOperation() {
        Integer index =
                (Integer) results.get(OutputWizardPage.KEY_PREVIEW_INDEX);

        if (index == null)
            throw new NullPointerException(
                    "Could not get the pattern index for the preview mode from wizard.");

        File smpFile = (File) results.get(PatternsWizardPage.KEY_SMP_FILE);
        File dir = (File) results.get(PatternsWizardPage.KEY_DIR);
        File patternFile =
                (File) results.get(PatternsWizardPage.KEY_PATTERN_FILE);

        PatternOp op;

        if (smpFile != null) {
            op = new PatternSmpLoader(index, smpFile);
        } else if (dir != null) {
            op = ExpUtil.createPatternOpsFromDir(dir)[index];
        } else if (patternFile != null)
            op = new PatternFileLoader(0, patternFile);
        else
            throw new IllegalArgumentException("Invalid pattern operation.");

        return op;
    }



    /**
     * Returns whether or not to run the experiment.
     * 
     * @return <code>true</code> if the experiment must be run,
     *         <code>false</code> otherwise
     */
    public boolean getRunMode() {
        Boolean run = (Boolean) results.get(OutputWizardPage.KEY_RUN);

        if (run == null)
            throw new NullPointerException(
                    "Could not get the run mode value from wizard.");

        return run;
    }



    /**
     * Returns whether or not to save the experiment.
     * 
     * @return <code>true</code> if the experiment must be saved,
     *         <code>false</code> otherwise
     */
    public boolean getSaveMode() {
        Boolean saved = (Boolean) results.get(OutputWizardPage.KEY_SAVE);

        if (saved == null)
            throw new NullPointerException(
                    "Could not get the save mode value from wizard.");

        return saved;
    }



    /**
     * Returns the width of the experiment's maps.
     * 
     * @return width of the maps
     */
    public int getWidth() {
        Integer width = (Integer) results.get(PatternsWizardPage.KEY_WIDTH);

        if (width == null)
            throw new NullPointerException(
                    "Could not get the maps' width from wizard.");

        return width;
    }
}
