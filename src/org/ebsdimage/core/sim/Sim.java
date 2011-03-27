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
package org.ebsdimage.core.sim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.PhaseMap;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;
import org.ebsdimage.core.sim.ops.output.OutputOps;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Order;

import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;
import crystallography.core.Reflectors;
import crystallography.core.ReflectorsFactory;

/**
 * Simulation of a pattern.
 * 
 * @author Philippe T. Pinard
 */
@Order(elements = { "listeners", "metadata", "phases", "rotations",
        "patternSimOp", "outputOps" }, attributes = { "name", "dir" })
public class Sim extends Run implements Monitorable {

    /** Simulation listeners. */
    @ElementList(name = "listeners")
    private ArrayList<SimListener> listeners = new ArrayList<SimListener>();

    /** <code>MultiMap</code> holding the result and metadata for the simulation. */
    public final SimMMap mmap;

    /** Reflectors to simulate. */
    private HashSet<Reflectors> reflectors = new HashSet<Reflectors>();

    /** Rotations to simulate. */
    private HashSet<Rotation> rotations = new HashSet<Rotation>();

    /** Operation for the pattern simulation. */
    private PatternSimOp patternSimOp;

    /** Output operations. */
    private ArrayList<OutputOps> outputOps = new ArrayList<OutputOps>();

    /** Runtime variable to access the reflectors currently used. */
    protected Reflectors currentReflectors;

    /** Runtime variable to access the rotation currently used. */
    protected Rotation currentRotation;



    /**
     * Special constructor to load an experiment from a XML. This constructor is
     * used by the deserialization.
     * 
     * @param metadata
     *            simulation metadata
     * @param phases
     *            array of crystals
     * @param rotations
     *            array of rotations
     * @param patternSimOp
     *            pattern simulation operation
     * @param outputs
     *            array of output operations
     * @param listeners
     *            array of listeners
     */
    @SuppressWarnings("unused")
    private Sim(@Element(name = "metadata") SimMetadata metadata,
            @ElementList(name = "listeners") ArrayList<SimListener> listeners,
            @ElementArray(name = "phases") Crystal[] phases,
            @ElementArray(name = "rotations") Rotation[] rotations,
            @Element(name = "patternSimOp") PatternSimOp patternSimOp,
            @ElementArray(name = "outputOps") OutputOps[] outputs) {
        this(metadata, new Operation[] { patternSimOp }, phases, rotations);

        for (Operation op : outputs)
            addOperation(op);

        // Listeners
        for (SimListener listener : listeners)
            addSimListener(listener);
    }



    /**
     * Creates a new simulation.
     * 
     * @param metadata
     *            simulation metadata
     * @param ops
     *            operations of the simulation
     * @param phases
     *            crystals of the simulation
     * @param rotations
     *            rotations of the simulation
     * @throws NullPointerException
     *             if the <code>SimMMap</code> is null
     * @throws NullPointerException
     *             if the array of <code>Operation</code> is null
     * @throws NullPointerException
     *             if the array of <code>Crystal</code> is null
     * @throws NullPointerException
     *             if an <code>Operation</code> is null
     * @throws IllegalArgumentException
     *             if the array of <code>Crystal</code> does not contain at
     *             least one item
     * @throws IllegalArgumentException
     *             if the array of <code>Rotation</code> does not contain at
     *             least one item
     * @see Camera
     * @see Crystal
     * @see Energy
     * @see Quaternion
     */
    public Sim(SimMetadata metadata, Operation[] ops, Crystal[] phases,
            Rotation[] rotations) {
        if (ops == null)
            throw new NullPointerException("Operations cannot be null.");
        if (phases == null)
            throw new NullPointerException("Crystals cannot be null.");
        if (rotations == null)
            throw new NullPointerException("Rotations cannot be null.");

        if (phases.length < 1)
            throw new IllegalArgumentException(
                    "At least one crystal must be given.");
        if (rotations.length < 1)
            throw new IllegalArgumentException(
                    "At least one rotation must be given.");

        // Multimap
        int width = phases.length * rotations.length;
        mmap = new SimMMap(width, 1);
        mmap.setMetadata(metadata);

        // Operations
        for (Operation op : ops)
            addOperation(op);

        if (patternSimOp == null)
            throw new IllegalArgumentException(
                    "No pattern simulation operation is specified.");

        // Crystals
        for (Crystal crystal : phases)
            addCrystal(crystal);

        // Rotation
        for (Rotation rotation : rotations)
            addRotation(rotation);

        // Initialize runtime variables
        initRuntimeVariables();
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

        // Register phase
        PhaseMap phaseMap = mmap.getPhaseMap();
        phaseMap.register(crystal);

        // Calculate reflectors
        SimMetadata metadata = mmap.getMetadata();
        Reflectors refls =
                ReflectorsFactory.generate(crystal,
                        metadata.getScatteringFactors(), metadata.getMaxIndex());
        reflectors.add(refls);
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
    private void addRotation(Rotation rotation) {
        if (rotation == null)
            throw new NullPointerException("Rotation cannot be null.");

        rotations.add(rotation);
    }



    /**
     * Registers an simulation listener.
     * 
     * @param listener
     *            simulation listener
     * @throws NullPointerException
     *             if the listener is null
     */
    public void addSimListener(SimListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener cannot be null.");

        if (!listeners.contains(listener))
            listeners.add(listener);
    }



    /**
     * Registers an array of simulation listeners.
     * 
     * @param listeners
     *            simulation listeners
     * @throws NullPointerException
     *             if the listeners is null
     */
    public void addSimListeners(SimListener[] listeners) {
        if (listeners == null)
            throw new NullPointerException("Listeners cannot be null.");

        for (SimListener listener : listeners)
            addSimListener(listener);
    }



    /**
     * Clears all registered simulation listeners.
     */
    public void clearSimListners() {
        listeners.clear();
    }



    /**
     * Fires output operation action.
     * 
     * @param op
     *            operation
     */
    private void fireOutputOp(OutputOps op) {
        for (SimListener listener : listeners)
            listener.outputOpPerformed(this, op);
    }



    /**
     * Fires pattern simulation operation action.
     * 
     * @param op
     *            operation
     */
    private void firePatternSimOp(PatternSimOp op) {
        for (SimListener listener : listeners)
            listener.patternSimOpPerformed(this, op);
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
     * Returns the rotation that is currently being used by the simulation. Only
     * valid when the simulation is running.
     * 
     * @return current rotation
     */
    public Rotation getCurrentRotation() {
        if (currentRotation == null)
            throw new RuntimeException(
                    "The simulation is not running, there is no rotation being used.");
        return currentRotation;
    }



    /**
     * Returns the metadata of the <code>SimMMap</code>.
     * 
     * @return metadata
     */
    @Element(name = "metadata")
    public SimMetadata getMetadata() {
        return mmap.getMetadata();
    }



    /**
     * Returns an array of all the defined outputs.
     * 
     * @return an array
     */
    @ElementArray(name = "outputOps")
    public OutputOps[] getOutputOps() {
        return outputOps.toArray(new OutputOps[0]);
    }



    /**
     * Returns the operation for the pattern simulation.
     * 
     * @return pattern simulation operation
     */
    @Element(name = "patternSimOp")
    public PatternSimOp getPatternSimOp() {
        return patternSimOp;
    }



    /**
     * Returns an array of all the defined crystals.
     * 
     * @return an array
     */
    @ElementArray(name = "phases")
    public Crystal[] getPhases() {
        Crystal[] crystals = new Crystal[reflectors.size()];

        int i = 0;
        for (Reflectors reflectorz : reflectors) {
            crystals[i] = reflectorz.crystal;
            i++;
        }

        return crystals;
    }



    /**
     * Returns an array of all the defined reflectors.
     * 
     * @return an array
     */
    public Reflectors[] getReflectors() {
        return reflectors.toArray(new Reflectors[0]);
    }



    /**
     * Returns an array of all the defined rotations.
     * 
     * @return an array
     */
    @ElementArray(name = "rotations")
    public Rotation[] getRotations() {
        return rotations.toArray(new Rotation[0]);
    }



    @Override
    protected void initRuntimeVariables() {
        super.initRuntimeVariables();

        currentReflectors = null;
        currentRotation = null;
    }



    /**
     * Removes a registered listener from the simulation.
     * 
     * @param listener
     *            simulation listener
     * @throws NullPointerException
     *             if the listener is null
     */
    public void removeSimListerner(SimListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener cannot be null.");

        listeners.remove(listener);
    }



    /**
     * Runs the experiment based on the given parameters and operations. If some
     * parameters and operations are missing to the correct execution of the
     * experiment, they will be automatically initialize before the run. The
     * results are saved based on the given parameters. Info through out the
     * execution is given by the logger ebsd.
     */
    @Override
    public void run() {
        setStatus("--- START ---");

        // Create directory for the results
        createDir();

        // Initialize ops
        setStatus("--- Initializing ops ---");
        patternSimOp.setUp(this);
        for (OutputOps op : outputOps)
            op.setUp(this);

        // Loop through all the parameters
        int index = 0;
        int size = reflectors.size() * rotations.size();

        for (Reflectors reflectorz : reflectors) {
            for (Rotation rotation : rotations) {
                // Increment progress
                progress = (double) index / (double) size;

                // Interrupt
                if (isInterrupted())
                    break;

                // Set current parameters
                currentReflectors = reflectorz;
                currentRotation = rotation;
                currentIndex = index;

                // Run
                runOnce(index, reflectorz, rotation);

                index++;
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
     * @param index
     *            index
     * @param reflectorz
     *            reflectors of the crystal
     * @param rotation
     *            rotation of the pattern
     */
    private void runOnce(int index, Reflectors reflectorz, Rotation rotation) {
        // Pattern Op
        setStatus("--- Pattern Operation ---");

        setStatus("Performing " + patternSimOp.getName() + "...");

        patternSimOp.simulate(this, getMetadata().getMicroscope(), reflectorz,
                rotation);

        firePatternSimOp(patternSimOp);

        setStatus("Performing " + patternSimOp.getName() + "... DONE");

        // Output Ops
        setStatus("--- Output Operations ---");
        for (OutputOps op : outputOps) {
            setStatus("Saving to " + op.getName() + "...");

            try {
                op.save(this, patternSimOp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            fireOutputOp(op);

            setStatus("Saving to " + op.getName() + "... DONE");
        }

        // Multimap
        mmap.getQ0Map().pixArray[index] = (float) rotation.getQ0();
        mmap.getQ1Map().pixArray[index] = (float) rotation.getQ1();
        mmap.getQ2Map().pixArray[index] = (float) rotation.getQ2();
        mmap.getQ3Map().pixArray[index] = (float) rotation.getQ3();
        mmap.getPhaseMap().setPixValue(index, reflectorz.crystal);
    }



    @Override
    public void setDir(File dir) {
        super.setDir(dir);

        mmap.setDir(dir);
    }



    @Override
    public void setName(String name) {
        super.setName(name);

        mmap.setName(name);
    }

}
