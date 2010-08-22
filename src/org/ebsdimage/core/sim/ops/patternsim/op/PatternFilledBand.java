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
package org.ebsdimage.core.sim.ops.patternsim.op;

import static java.lang.Math.ceil;

import java.awt.geom.Line2D;

import org.ebsdimage.core.sim.Band;

import rmlimage.module.real.core.Drawing;
import rmlimage.module.real.core.MapMath;
import rmlimage.module.real.core.RealMap;
import crystallography.core.ScatteringFactorsEnum;

/**
 * Operation to draw a filled band pattern using x-rays scattering factors.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilledBand extends PatternSimOp {

    /**
     * Creates a new <code>PatternFilledBand</code>.
     */
    public PatternFilledBand() {
        super();
    }



    /**
     * Creates a new <code>PatternFilledBand</code>.
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
    public PatternFilledBand(int width, int height, int maxIndex,
            ScatteringFactorsEnum scatterType) {
        super(width, height, maxIndex, scatterType);
    }



    @Override
    protected RealMap createPatternMap() {
        RealMap map = new RealMap(width, height);
        return map;
    }



    /**
     * Draws a band on top of the current <code>patternMap</code>. The colour of
     * the band is determined by {@link #getBandColorArray(Band)}.
     * 
     * @param canvas
     *            <code>RealMap</code> of the pattern
     * @param band
     *            band to draw
     */
    @Override
    protected void drawBand(RealMap canvas, Band band) {
        // Get coordinates
        Line2D.Double coords = band.line.toLine2D(width, height);
        if (coords == null)
            return; // Band outside image

        int x1 = (int) ceil(coords.x1);
        int y1 = (int) ceil(coords.y1);
        int x2 = (int) ceil(coords.x2);
        int y2 = (int) ceil(coords.y2);

        // Get color array
        float[] colorArray = getBandColorArray(band);

        // Create temporary map to store the band
        RealMap tmpMap = new RealMap(width, height);
        Drawing.line(tmpMap, x1, y1, x2, y2, colorArray);

        // Add temporary map to the canvas
        MapMath.addition(canvas, tmpMap, canvas);
    }



    /**
     * Creates an array that represents the band thickness (length) and the
     * colour across the band. The colour is kept constant across the thickness.
     * The normalised intensity is used.
     * 
     * @param band
     *            a band
     * @return array of float
     */
    private float[] getBandColorArray(Band band) {
        int thickness = (int) (band.width * width + 1);
        double intensity = band.normalizedIntensity;

        float[] colorArray = new float[thickness];

        for (int i = 0; i < thickness; i++)
            colorArray[i] = (float) intensity;

        return colorArray;
    }

}
