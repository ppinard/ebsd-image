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
import rmlimage.module.real.core.RealMap;
import crystallography.core.ScatteringFactorsEnum;

/**
 * Generates a pattern with only the center of the bands.
 * 
 * @author Philippe T. Pinard
 */
public class PatternBandCenter extends PatternSimOp {

    /**
     * Creates a new <code>PatternBandCenter</code>.
     */
    public PatternBandCenter() {
        super();
    }



    /**
     * Creates a new <code>PatternBandCenter</code>.
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
    public PatternBandCenter(int width, int height, int maxIndex,
            ScatteringFactorsEnum scatterType) {
        super(width, height, maxIndex, scatterType);
    }



    @Override
    protected RealMap createPatternMap() {
        RealMap map = new RealMap(width, height);
        return map;
    }



    /**
     * Draws the centre line of a band.
     * 
     * @param canvas
     *            simulated pattern
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

        // Draw line on top of patternMap
        Drawing.line(canvas, x1, y1, x2, y2, 1.0f);
    }

}
