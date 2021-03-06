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
package org.ebsdimage.core.sim.ops.patternsim;

import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.BandsCalculator;
import org.ebsdimage.core.sim.LinearBandsCalculator;
import org.simpleframework.xml.Attribute;

import rmlimage.module.real.core.Drawing;
import rmlimage.module.real.core.RealMap;

/**
 * Generates a pattern with only the center of the bands.
 * 
 * @author Philippe T. Pinard
 */
public class PatternBandCenter extends PatternSimOp {

    /**
     * Creates a new <code>PatternBandCenter</code>.
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
    public PatternBandCenter(@Attribute(name = "width") int width,
            @Attribute(name = "height") int height) {
        super(width, height);
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
        Drawing.shape(canvas, band.middle, 1.0f);
    }



    @Override
    protected BandsCalculator getBandsCalculator() {
        return new LinearBandsCalculator();
    }



    @Override
    public String toString() {
        return "PatternBandCenter [width=" + width + ", height=" + height + "]";
    }

}
