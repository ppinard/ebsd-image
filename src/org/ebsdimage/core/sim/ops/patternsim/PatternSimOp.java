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
package org.ebsdimage.core.sim.ops.patternsim;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.BandException;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Sim;

import ptpshared.core.math.Quaternion;
import rmlimage.core.ByteMap;
import rmlimage.module.real.core.Contrast;
import rmlimage.module.real.core.Conversion;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.core.ThreeSigmaRenderer;
import rmlshared.thread.Reflection;
import crystallography.core.*;

/**
 * Superclass of operation to simulate a pattern.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PatternSimOp extends Operation {

    /** Width of the pattern. */
    public final int width;

    /** Default width of the pattern. */
    public static final int DEFAULT_WIDTH = 1344;

    /** Height of the pattern. */
    public final int height;

    /** Default height of the pattern. */
    public static final int DEFAULT_HEIGHT = 1024;

    /** Maximum reflector index. */
    public final int maxIndex;

    /** Default maximum reflector index. */
    public static final int DEFAULT_MAX_INDEX = 6;

    /** Type of scattering factors. */
    public final ScatteringFactorsEnum scatterType;

    /** Default type of scattering factors. */
    public static final ScatteringFactorsEnum DEFAULT_SCATTER_TYPE =
            ScatteringFactorsEnum.XRAY;

    /** List of bands. */
    protected ArrayList<Band> bands;

    /** Pattern's <code>ByteMap</code>. */
    protected ByteMap patternMap;

    /** Pattern's <code>RealMap</code>. */
    protected RealMap patternRealMap;



    /**
     * Creates a new pattern simulation operation with the default parameters.
     */
    public PatternSimOp() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_MAX_INDEX,
                DEFAULT_SCATTER_TYPE);
    }



    /**
     * Creates a new pattern simulation operation.
     * 
     * @param width
     *            width of the pattern to simulate
     * @param height
     *            height of the pattern to simulate
     * @param maxIndex
     *            maximum index of the reflectors to use in the pattern simulate
     * @param scatterType
     *            type of scattering factors
     * @throws IllegalArgumentException
     *             if the width is less than zero
     * @throws IllegalArgumentException
     *             if the height is less than zero
     * @throws IllegalArgumentException
     *             if the maximum index is less than zero
     */
    public PatternSimOp(int width, int height, int maxIndex,
            ScatteringFactorsEnum scatterType) {
        if (width < 0)
            throw new IllegalArgumentException("Width (" + width
                    + ") must be greater than zero.");
        if (height < 0)
            throw new IllegalArgumentException("Height (" + height
                    + ") must be greater than zero.");
        if (maxIndex < 0)
            throw new IllegalArgumentException("Max index (" + maxIndex
                    + ") must be greater than zero.");

        this.width = width;
        this.height = height;
        this.maxIndex = maxIndex;
        this.scatterType = scatterType;
    }



    /**
     * Calculates the bands with the given parameters.
     * 
     * @param reflectors
     *            reflectors of the crystal
     * @param camera
     *            camera parameters
     * @param energy
     *            energy object for the beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     */
    protected void calculateBands(Camera camera, Reflectors reflectors,
            Energy energy, Quaternion rotation) {
        // Clear bands previously created.
        bands = new ArrayList<Band>();

        // Sort reflectors by decreasing order of intensity.
        reflectors.sortByIntensity(true);

        // Create bands
        for (Reflector reflector : reflectors) {
            // Create band and calculate half-widths, edge intercepts and full
            // width
            Band band;
            try {
                band = new Band(reflector, rotation, camera, energy.value);
            } catch (BandException e) {
                continue;
            }

            // Add band to Array
            bands.add(band);
        }
    }



    /**
     * Calculates the reflectors for the crystal.
     * 
     * @param crystal
     *            a crystal
     * @return reflectors
     */
    public Reflectors calculateReflectors(Crystal crystal) {
        ScatteringFactors scatter =
                (ScatteringFactors) Reflection.newInstance(scatterType.getScatteringFactors());
        return new Reflectors(crystal, scatter, maxIndex);
    }



    /**
     * Creates a empty <code>RealMap</code> to store the pattern.
     * 
     * @return pattern map
     */
    protected abstract RealMap createPatternMap();



    /**
     * Draws a band in the canvas.
     * 
     * @param canvas
     *            simulated pattern
     * @param band
     *            band to draw
     */
    protected abstract void drawBand(RealMap canvas, Band band);



    /**
     * Draws all the bands of the pattern in a <code>RealMap</code>.
     * 
     * @param reflectors
     *            reflectors of the crystal
     * @param camera
     *            camera parameters
     * @param energy
     *            energy object for the beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     * @return simulated pattern
     */
    private RealMap drawRealMap(Camera camera, Reflectors reflectors,
            Energy energy, Quaternion rotation) {
        calculateBands(camera, reflectors, energy, rotation);

        RealMap canvas = createPatternMap();

        Logger logger = Logger.getLogger("ebsd");
        logger.info("Number of bands in pattern: " + bands.size());

        for (Band band : bands)
            drawBand(canvas, band);

        return canvas;
    }



    /**
     * Returns the calculated bands. If they are not previously calculated, they
     * are automatically calculated.
     * 
     * @return calculated bands
     */
    public Band[] getBands() {
        if (bands == null)
            throw new RuntimeException("Pattern is not yet simulated.");
        return bands.toArray(new Band[bands.size()]);
    }



    /**
     * Returns the simulated pattern <code>ByteMap</code>. If the pattern was
     * not generated, it is automatically generated.
     * 
     * @return simulated pattern
     */
    public ByteMap getPatternMap() {
        if (patternMap == null)
            throw new RuntimeException("Pattern is not yet simulated.");
        return patternMap;
    }



    /**
     * Returns the simulated pattern <code>RealMap</code>. If the pattern was
     * not generated, it is automatically generated.
     * 
     * @return simulated pattern
     */
    public RealMap getPatternRealMap() {
        if (patternRealMap == null)
            throw new RuntimeException("Pattern is not yet simulated.");
        return patternRealMap;
    }



    /**
     * Simulates a new pattern with the specified variables.
     * 
     * @param sim
     *            simulation executing this method
     * @param reflectors
     *            reflectors of the crystal
     * @param camera
     *            camera parameters
     * @param energy
     *            energy object for the beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     */
    public void simulate(Sim sim, Camera camera, Reflectors reflectors,
            Energy energy, Quaternion rotation) {
        patternRealMap = drawRealMap(camera, reflectors, energy, rotation);

        // Use 3-sigma rendered
        patternRealMap.setMapRenderer(new ThreeSigmaRenderer());
        patternMap = Conversion.toByteMap(patternRealMap);
    }



    /**
     * Simulates a new pattern with the specified variables.
     * 
     * @param reflectors
     *            reflectors of the crystal
     * @param camera
     *            camera parameters
     * @param energy
     *            energy object for the beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     */
    public void simulate(Camera camera, Reflectors reflectors, Energy energy,
            Quaternion rotation) {
        patternRealMap = drawRealMap(camera, reflectors, energy, rotation);
        patternMap = Contrast.expansion(patternRealMap);
    }

}
