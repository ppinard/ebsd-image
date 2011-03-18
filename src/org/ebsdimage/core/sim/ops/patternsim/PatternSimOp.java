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

import java.util.logging.Logger;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.BandsCalculator;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimOperation;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.module.real.core.Conversion;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.core.ThreeSigmaRenderer;
import crystallography.core.Reflectors;

/**
 * Superclass of operation to simulate a pattern.
 * 
 * @author Philippe T. Pinard
 */
public abstract class PatternSimOp extends SimOperation {

    /** Width of the pattern. */
    @Attribute(name = "width")
    public final int width;

    /** Height of the pattern. */
    @Attribute(name = "height")
    public final int height;

    /** Pattern's <code>ByteMap</code>. */
    private ByteMap patternMap;

    /** Pattern's <code>RealMap</code>. */
    private RealMap patternRealMap;

    /** Pattern's bands. */
    private Band[] bands;



    /**
     * Creates a new pattern simulation operation.
     * 
     * @param width
     *            width of the pattern to simulate
     * @param height
     *            height of the pattern to simulate
     * @throws IllegalArgumentException
     *             if the width is less than zero
     * @throws IllegalArgumentException
     *             if the height is less than zero
     */
    public PatternSimOp(int width, int height) {
        if (width < 0)
            throw new IllegalArgumentException("Width (" + width
                    + ") must be greater than zero.");
        if (height < 0)
            throw new IllegalArgumentException("Height (" + height
                    + ") must be greater than zero.");

        this.width = width;
        this.height = height;
    }



    /**
     * Creates a empty <code>RealMap</code> to store the pattern.
     * 
     * @return pattern map
     */
    protected abstract RealMap createPatternMap();



    /**
     * Returns the <code>BandsCalculator</code> to use to calculate the bands of
     * this simulated diffraction pattern.
     * 
     * @return <code>BandsCalculator</code>
     */
    protected abstract BandsCalculator getBandsCalculator();



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
     * @param microscope
     *            microscope parameters
     * @param rotation
     *            rotation of the pattern
     * @return simulated pattern
     */
    private RealMap drawRealMap(Microscope microscope, Reflectors reflectors,
            Rotation rotation) {
        calculateBands(microscope, reflectors, rotation);

        RealMap canvas = createPatternMap();

        Logger logger = Logger.getLogger("ebsd");
        logger.info("Number of bands in pattern: " + bands.length);

        for (Band band : bands)
            drawBand(canvas, band);

        return canvas;
    }



    /**
     * Calculates the bands using the <code>BandsCalculator</code>.
     * 
     * @param reflectors
     *            reflectors of the crystal
     * @param microscope
     *            microscope parameters
     * @param rotation
     *            rotation of the pattern
     */
    private void calculateBands(Microscope microscope, Reflectors reflectors,
            Rotation rotation) {
        bands =
                getBandsCalculator().calculate(width, height, microscope,
                        reflectors, rotation);
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PatternSimOp other = (PatternSimOp) obj;
        if (height != other.height)
            return false;
        if (width != other.width)
            return false;

        return true;
    }



    /**
     * Returns the simulated pattern <code>ByteMap</code>.
     * 
     * @return simulated pattern
     */
    public ByteMap getPatternMap() {
        if (patternMap == null)
            throw new RuntimeException("Pattern is not yet simulated.");
        return patternMap;
    }



    /**
     * Returns the simulated pattern <code>RealMap</code>.
     * 
     * @return simulated pattern
     */
    public RealMap getPatternRealMap() {
        if (patternRealMap == null)
            throw new RuntimeException("Pattern is not yet simulated.");
        return patternRealMap;
    }



    /**
     * Returns the calculated bands used in the simulated pattern.
     * 
     * @return calculated bands
     */
    public Band[] getBands() {
        if (bands == null)
            throw new RuntimeException("Pattern is not yet simulated.");
        return bands;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }



    /**
     * Simulates a new pattern with the specified variables.
     * 
     * @param sim
     *            simulation executing this method
     * @param reflectors
     *            reflectors of the crystal
     * @param microscope
     *            microscope parameters
     * @param rotation
     *            rotation of the pattern
     */
    public void simulate(Sim sim, Microscope microscope, Reflectors reflectors,
            Rotation rotation) {
        patternRealMap = drawRealMap(microscope, reflectors, rotation);

        // Set calibration
        patternRealMap.setCalibration(microscope.getCamera().getCalibration(
                width, height));

        // Use 3-sigma rendered
        patternRealMap.setMapRenderer(new ThreeSigmaRenderer());
        patternMap = Conversion.toByteMap(patternRealMap);
    }

}
