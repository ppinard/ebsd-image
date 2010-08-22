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
package org.ebsdimage.core.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;
import org.ebsdimage.core.sim.ops.output.OutputOps;
import org.ebsdimage.core.sim.ops.patternsim.op.PatternFilledBand;
import org.ebsdimage.core.sim.ops.patternsim.op.PatternSimOp;

import ptpshared.core.math.Quaternion;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;
import crystallography.core.Reflectors;

/**
 * Simulation of a pattern.
 * 
 * @author Philippe T. Pinard
 */
public class Sim extends Run implements Monitorable {

    /** Cameras to simulate. */
    private HashSet<Camera> cameras = new HashSet<Camera>();

    /** Reflectors to simulate. */
    private HashSet<Reflectors> reflectors = new HashSet<Reflectors>();

    /** Energies to simulate. */
    private HashSet<Energy> energies = new HashSet<Energy>();

    /** Rotations to simulate. */
    private HashSet<Quaternion> rotations = new HashSet<Quaternion>();

    /** Operation for the pattern simulation. */
    private PatternSimOp patternSimOp = new PatternFilledBand();

    /** Output operations. */
    private ArrayList<OutputOps> outputOps = new ArrayList<OutputOps>();

    /** Runtime variable to access the camera currently used. */
    protected Camera currentCamera;

    /** Runtime variable to access the reflectors currently used. */
    protected Reflectors currentReflectors;

    /** Runtime variable to access the energy currently used. */
    protected Energy currentEnergy;

    /** Runtime variable to access the rotation currently used. */
    protected Quaternion currentRotation;



    /**
     * Creates a new simulation. The simulation is constructed from arrays of
     * <code>Operation</code>, <code>Camera</code>, <code>Crystal</code>,
     * <code>Energy</code> and <code>Quaternion</code> (rotation).
     * <p/>
     * Except the array of <code>Operation</code>, the other arrays must at
     * least have one item.
     * 
     * @param ops
     *            operations of the simulation
     * @param cameras
     *            cameras of the simulation
     * @param crystals
     *            crystals of the simulation
     * @param energies
     *            energies of the simulation
     * @param rotations
     *            rotations of the simulation
     * @throws NullPointerException
     *             if the array of <code>Operation</code> is null
     * @throws NullPointerException
     *             if the array of <code>Camera</code> is null
     * @throws NullPointerException
     *             if the array of <code>Crystal</code> is null
     * @throws NullPointerException
     *             if the array of <code>Energy</code> is null
     * @throws NullPointerException
     *             if the array of <code>Quaternion</code> (rotations) is null
     * @throws NullPointerException
     *             if an <code>Operation</code> is null
     * @throws IllegalArgumentException
     *             if the array of <code>Camera</code> does not contain at least
     *             one item
     * @throws IllegalArgumentException
     *             if the array of <code>Crystal</code> does not contain at
     *             least one item
     * @throws IllegalArgumentException
     *             if the array of <code>Energy</code> does not contain at least
     *             one item
     * @throws IllegalArgumentException
     *             if the array of <code>Quaternion</code> (rotations) does not
     *             contain at least one item
     * @see Camera
     * @see Crystal
     * @see Energy
     * @see Quaternion
     */
    public Sim(Operation[] ops, Camera[] cameras, Crystal[] crystals,
            Energy[] energies, Quaternion[] rotations) {
        if (ops == null)
            throw new NullPointerException("Operations cannot be null.");
        if (cameras == null)
            throw new NullPointerException("Cameras cannot be null.");
        if (crystals == null)
            throw new NullPointerException("Crystals cannot be null.");
        if (energies == null)
            throw new NullPointerException("Energies cannot be null.");
        if (rotations == null)
            throw new NullPointerException("Rotations cannot be null.");

        if (cameras.length < 1)
            throw new IllegalArgumentException(
                    "At least one camera must be given.");
        if (crystals.length < 1)
            throw new IllegalArgumentException(
                    "At least one crystal must be given.");
        if (energies.length < 1)
            throw new IllegalArgumentException(
                    "At least one energy must be given.");
        if (rotations.length < 1)
            throw new IllegalArgumentException(
                    "At least one rotation must be given.");

        // Operations
        for (Operation op : ops)
            addOperation(op);

        // Cameras
        for (Camera camera : cameras)
            addCamera(camera);

        // Crystals
        for (Crystal crystal : crystals)
            addCrystal(crystal);

        // Energies
        for (Energy energy : energies)
            addEnergy(energy);

        // Rotation
        for (Quaternion rotation : rotations)
            addRotation(rotation);

        // Initialise runtime variables
        initRuntimeVariables();
    }



    @Override
    protected void initRuntimeVariables() {
        super.initRuntimeVariables();

        currentCamera = null;
        currentReflectors = null;
        currentEnergy = null;
        currentRotation = null;
    }



    /**
     * Adds a <code>Camera</code> to the simulation.
     * 
     * @param camera
     *            a camera
     */
    private void addCamera(Camera camera) {
        if (camera == null)
            throw new NullPointerException("Camera cannot be null.");

        cameras.add(camera);
    }



    /**
     * Adds a <code>Crystal</code> to the simulation. The reflectors of that
     * crystal are calculated based on the selected pattern simulation
     * operation.
     * 
     * @param crystal
     *            a crystal
     */
    private void addCrystal(Crystal crystal) {
        if (crystal == null)
            throw new NullPointerException("Crystal cannot be null.");

        reflectors.add(patternSimOp.calculateReflectors(crystal));
    }



    /**
     * Adds an <code>Energy</code> to the simulation.
     * 
     * @param energy
     *            a energy
     */
    private void addEnergy(Energy energy) {
        if (energy == null)
            throw new NullPointerException("Energy cannot be null.");

        energies.add(energy);
    }



    /**
     * Adds an <code>Operation</code> to the experiment. The type of
     * <code>Operation</code> is automatically deduced from its super class.
     * 
     * @param op
     *            operation to be added.
     * @throws IllegalArgumentException
     *             if the operation doesn't have a known super class
     * @throws NullPointerException
     *             if the operation is null
     */
    @Override
    protected void addOperation(Operation op) {
        if (op == null)
            throw new NullPointerException("Operation cannot be null.");

        String superClassName = op.getClass().getSuperclass().getSimpleName();

        if (superClassName.equals(PatternSimOp.class.getSimpleName()))
            patternSimOp = (PatternSimOp) op;
        else if (superClassName.equals(OutputOps.class.getSimpleName()))
            outputOps.add((OutputOps) op);
        else
            throw new IllegalArgumentException("The operation (" + op.getName()
                    + ") is unknown and cannot be loaded.");
    }



    /**
     * Adds a rotation to the simulation.
     * 
     * @param rotation
     *            a rotation
     */
    private void addRotation(Quaternion rotation) {
        if (rotation == null)
            throw new NullPointerException("Rotation cannot be null.");

        rotations.add(rotation);
    }



    /**
     * Returns an array of all the defined cameras.
     * 
     * @return an array
     */
    public Camera[] getCameras() {
        return cameras.toArray(new Camera[cameras.size()]);
    }



    /**
     * Returns an array of all the defined reflectors.
     * 
     * @return an array
     */
    public Reflectors[] getReflectors() {
        return reflectors.toArray(new Reflectors[reflectors.size()]);
    }



    /**
     * Returns an array of all the defined crystals.
     * 
     * @return an array
     */
    public Crystal[] getCrystals() {
        Crystal[] crystals = new Crystal[reflectors.size()];

        int i = 0;
        for (Reflectors reflectorz : reflectors) {
            crystals[i] = reflectorz.crystal;
            i++;
        }

        return crystals;
    }



    /**
     * Returns the camera that is currently being used by the simulation. Only
     * valid when the simulation is running.
     * 
     * @return current camera
     */
    public Camera getCurrentCamera() {
        if (currentCamera == null)
            throw new RuntimeException(
                    "The simulation is not running, there is no camera being used.");
        return currentCamera;
    }



    /**
     * Returns the reflectors that is currently being used by the simulation.
     * Only valid when the simulation is running.
     * 
     * @return current reflectors
     */
    public Reflectors getCurrentReflectors() {
        if (currentReflectors == null)
            throw new RuntimeException(
                    "The simulation is not running, there is no crystal being used.");
        return currentReflectors;
    }



    /**
     * Returns the crystal that is currently being used by the simulation. Only
     * valid when the simulation is running.
     * 
     * @return current crystal
     */
    public Crystal getCurrentCrystal() {
        if (currentReflectors == null)
            throw new RuntimeException(
                    "The simulation is not running, there is no crystal being used.");
        return currentReflectors.crystal;
    }



    /**
     * Returns the energy that is currently being used by the simulation. Only
     * valid when the simulation is running.
     * 
     * @return current energy
     */
    public Energy getCurrentEnergy() {
        if (currentEnergy == null)
            throw new RuntimeException(
                    "The simulation is not running, there is no energy being used.");
        return currentEnergy;
    }



    /**
     * Returns the rotation that is currently being used by the simulation. Only
     * valid when the simulation is running.
     * 
     * @return current rotation
     */
    public Quaternion getCurrentRotation() {
        if (currentRotation == null)
            throw new RuntimeException(
                    "The simulation is not running, there is no rotation being used.");
        return currentRotation;
    }



    /**
     * Returns an array of all the defined energies.
     * 
     * @return an array
     */
    public Energy[] getEnergies() {
        return energies.toArray(new Energy[energies.size()]);
    }



    /**
     * Returns an array of all the defined outputs.
     * 
     * @return an array
     */
    public OutputOps[] getOutputOps() {
        return outputOps.toArray(new OutputOps[outputOps.size()]);
    }



    /**
     * Returns the operation for the pattern simulation.
     * 
     * @return pattern simulation operation
     */
    public PatternSimOp getPatternSimOp() {
        return patternSimOp;
    }



    /**
     * Returns an array of all the defined rotations.
     * 
     * @return an array
     */
    public Quaternion[] getRotations() {
        return rotations.toArray(new Quaternion[rotations.size()]);
    }



    /**
     * Runs the experiment based on the given parameters and operations. If some
     * parameters and operations are missing to the correct execution of the
     * experiment, they will be automatically initialize before the run. The
     * results are saved based on the given parameters. Info through out the
     * execution is given by the logger ebsd.
     * 
     * @throws IOException
     *             if an error occurs during the run
     */
    @Override
    public void run() throws IOException {
        setStatus("--- START ---");

        // Create directory for the results
        createDir();

        // Initialise ops
        setStatus("--- Initializing ops ---");
        patternSimOp.setUp(this);
        for (OutputOps op : outputOps)
            op.setUp(this);

        // Loop through all the parameters
        int index = 0;
        int size =
                cameras.size() * reflectors.size() * energies.size()
                        * rotations.size();

        for (Camera camera : cameras) {
            for (Reflectors reflectorz : reflectors) {
                for (Energy energy : energies) {
                    for (Quaternion rotation : rotations) {
                        // Increment progress
                        progress = index / size;

                        // Interrupt
                        if (isInterrupted())
                            break;

                        // Set current parameters
                        currentCamera = camera;
                        currentReflectors = reflectorz;
                        currentEnergy = energy;
                        currentRotation = rotation;
                        currentIndex = index;

                        // Run
                        runOnce(camera, reflectorz, energy, rotation);

                        index++;
                    }
                }
            }
        }

        // Flush output ops
        setStatus("--- Flushing ops ---");
        patternSimOp.tearDown(this);
        for (OutputOps op : outputOps)
            op.tearDown(this);

        initRuntimeVariables();

        setStatus("--- END ---");
    }



    /**
     * Runs the simulation with one set of parameters.
     * 
     * @param reflectorz
     *            reflectors of the crystal
     * @param camera
     *            camera parameters
     * @param energy
     *            energy object for the beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     * @throws IOException
     *             if an error occurs during the run
     */
    private void runOnce(Camera camera, Reflectors reflectorz, Energy energy,
            Quaternion rotation) throws IOException {
        // Pattern Op
        setStatus("--- Pattern Operation ---");

        setStatus("Performing " + patternSimOp.getName() + "...");

        patternSimOp.simulate(this, camera, reflectorz, energy, rotation);

        setStatus("Performing " + patternSimOp.getName() + "... DONE");

        // Output Ops
        setStatus("--- Output Operations ---");
        for (OutputOps op : outputOps) {
            setStatus("Saving to " + op.getName() + "...");

            op.save(this, patternSimOp);

            setStatus("Saving to " + op.getName() + "... DONE");
        }
    }

}
