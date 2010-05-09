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

import static java.lang.Math.ceil;

import java.awt.geom.Line2D;
import java.util.logging.Logger;

import org.ebsdimage.core.Camera;

import ptpshared.core.geom.Line;
import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import rmlimage.module.real.core.Contrast;
import rmlimage.module.real.core.Drawing;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Reflectors;

/**
 * Generates a pattern with only the edges of the bands.
 * 
 * @author Philippe T. Pinard
 */
public class PatternBandEdges extends Pattern {

    /**
     * Creates a new pattern. If the number of reflectors is negative, all the
     * reflectors are taken.
     * 
     * @param width
     *            width of the pattern
     * @param height
     *            height of the pattern
     * @param reflectors
     *            of the pattern
     * @param numberReflectors
     *            number of reflectors to consider
     * @param camera
     *            parameters of the camera
     * @param energy
     *            beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     * 
     * @throws NullPointerException
     *             if the reflectors are null
     * @throws NullPointerException
     *             if the camera are null
     * @throws NullPointerException
     *             if the energy are null
     * @throws NullPointerException
     *             if the rotation are null
     */
    public PatternBandEdges(int width, int height, Reflectors reflectors,
            int numberReflectors, Camera camera, Energy energy, Eulers rotation) {
        super(width, height, reflectors, numberReflectors, camera, energy,
                rotation);
    }



    /**
     * Creates a new pattern. If the number of reflectors is negative, all the
     * reflectors are taken.
     * 
     * @param width
     *            width of the pattern
     * @param height
     *            height of the pattern
     * @param reflectors
     *            of the pattern
     * @param numberReflectors
     *            number of reflectors to consider
     * @param camera
     *            parameters of the camera
     * @param energy
     *            beam energy (in eV)
     * @param rotation
     *            rotation of the pattern
     * 
     * @throws NullPointerException
     *             if the reflectors are null
     * @throws NullPointerException
     *             if the camera are null
     * @throws NullPointerException
     *             if the energy are null
     * @throws NullPointerException
     *             if the rotation are null
     */
    public PatternBandEdges(int width, int height, Reflectors reflectors,
            int numberReflectors, Camera camera, Energy energy,
            Quaternion rotation) {
        super(width, height, reflectors, numberReflectors, camera, energy,
                rotation);
    }



    /**
     * Creates a empty <code>RealMap</code> to store the pattern.
     * 
     * @return pattern map
     */
    private RealMap createPatternMap() {
        RealMap map = new RealMap(width, height);
        return map;
    }



    @Override
    public void draw() {
        patternRealMap = drawRealMap();
        patternMap = Contrast.expansion(patternRealMap);
    }



    /**
     * Draws the two edges of a band.
     * 
     * @param patternMap
     *            simulated pattern
     * @param band
     *            band to draw
     */
    private void drawBand(RealMap patternMap, Band band) {
        drawEdge(patternMap, band.edgeIntercepts[0]);
        drawEdge(patternMap, band.edgeIntercepts[1]);
    }



    /**
     * Draws one edge of a band from its <code>Line</code>.
     * 
     * @param patternMap
     *            simulated pattern
     * @param line
     *            line corresponding to the band's edge
     */
    private void drawEdge(RealMap patternMap, Line line) {
        // Get coordinates
        Line2D.Double coords = line.toLine2D(width, height);
        if (coords == null)
            return; // Band outside image

        int x1 = (int) ceil(coords.x1);
        int y1 = (int) ceil(coords.y1);
        int x2 = (int) ceil(coords.x2);
        int y2 = (int) ceil(coords.y2);

        // Draw top edge line on top of patternMap
        Drawing.line(patternMap, x1, y1, x2, y2, 1.0f);
    }



    /**
     * Draws all the bands of the pattern in a <code>RealMap</code>.
     * 
     * @return simulated pattern
     */
    private RealMap drawRealMap() {
        calculateBands();

        RealMap canvas = createPatternMap();

        Logger logger = Logger.getLogger("ebsd");
        logger.info("Number of bands in pattern: " + bands.size());

        for (Band band : bands)
            drawBand(canvas, band);

        return canvas;
    }

}
