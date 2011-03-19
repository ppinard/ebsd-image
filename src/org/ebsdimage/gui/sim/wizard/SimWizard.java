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
package org.ebsdimage.gui.sim.wizard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.run.Operation;

import ptpshared.gui.Wizard;
import ptpshared.gui.WizardPage;
import rmlshared.util.ArrayList;
import crystallography.core.Crystal;
import crystallography.core.ScatteringFactorsEnum;
import static rmlshared.io.FileUtil.getURL;

/**
 * Wizard to setup an experiment.
 * 
 * @author Philippe T. Pinard
 */
public class SimWizard extends Wizard {

    /**
     * Creates a new wizard for the experiment.
     */
    public SimWizard() {
        super("Simulation", new WizardPage[] { new StartWizardPage(),
                new InfoWizardPage(), new MicroscopeWizardPage(),
                new PhasesWizardPage(), new ParamsWizardPage() });

        try {
            BufferedImage image =
                    ImageIO.read(getURL("org/ebsdimage/gui/sidepanel.png"));
            setSidePanelBackground(image);
        } catch (IOException e) {
        }

        setPreferredWidth(800);
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
     * Returns the operations of the simulation (pattern simulation operation
     * and output ops).
     * 
     * @return operations
     */
    public Operation[] getOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        ops.add(getPatternSimOp());
        ops.addAll(getOutputOps());

        return ops.toArray(new Operation[0]);
    }



    /**
     * Returns the output operations.
     * 
     * @return output operations
     */
    private Operation[] getOutputOps() {
        Operation[] ops =
                (Operation[]) results.get(ParamsWizardPage.KEY_OUTPUT_OPS);

        if (ops == null)
            throw new NullPointerException(
                    "Could not get the output operations from wizard.");

        return ops;
    }



    /**
     * Returns the pattern simulation operation of the simulation.
     * 
     * @return pattern simulation operation
     */
    private Operation getPatternSimOp() {
        Operation op =
                (Operation) results.get(ParamsWizardPage.KEY_PATTERNSIMOP);

        if (op == null)
            throw new NullPointerException(
                    "Could not get the pattern simulation operation from wizard.");

        return op;
    }



    /**
     * Returns the phases of the simulation.
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



    /**
     * Returns the microscope of the simulation.
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



    /**
     * Returns the scattering factors.
     * 
     * @return scattering factors
     */
    public ScatteringFactorsEnum getScattringFactors() {
        ScatteringFactorsEnum scatteringFactors =
                (ScatteringFactorsEnum) results.get(ParamsWizardPage.KEY_SCATTERINGFACTORS);

        if (scatteringFactors == null)
            throw new NullPointerException(
                    "Could not get the scattering factors from wizard.");

        return scatteringFactors;
    }



    /**
     * Returns the maximum index of the planes to simulate.
     * 
     * @return maximum index
     */
    public int getMaxIndex() {
        Integer maxIndex = (Integer) results.get(ParamsWizardPage.KEY_MAXINDEX);

        if (maxIndex == null)
            throw new NullPointerException(
                    "Could not get the maximum index from wizard.");

        return maxIndex;
    }



    /**
     * Returns the rotations of the simulation.
     * 
     * @return rotations
     */
    public Rotation[] getRotations() {
        Rotation[] rotations =
                (Rotation[]) results.get(ParamsWizardPage.KEY_ROTATIONS);

        if (rotations == null)
            throw new NullPointerException(
                    "Could not get the rotations from wizard.");

        return rotations;
    }

}
