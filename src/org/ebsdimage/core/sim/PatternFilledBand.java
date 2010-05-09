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

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import rmlimage.module.real.core.Contrast;
import rmlimage.module.real.core.Drawing;
import rmlimage.module.real.core.MapMath;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Reflectors;

/**
 * Generates a pattern with uniform intensity bands. The bands are superimposed
 * on top of each other starting from the least intense.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilledBand extends Pattern {

    /**
     * Creates a new pattern with uniform intensity band. If the number of
     * reflectors is negative, all the reflectors are taken.
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
    public PatternFilledBand(int width, int height, Reflectors reflectors,
            int numberReflectors, Camera camera, Energy energy, Eulers rotation) {
        this(width, height, reflectors, numberReflectors, camera, energy,
                new Quaternion(rotation));
    }



    /**
     * Creates a new pattern with uniform intensity band. If the number of
     * reflectors is negative, all the reflectors are taken.
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
    public PatternFilledBand(int width, int height, Reflectors reflectors,
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
     * Draws a band on top of the current <code>patternMap</code>. The color of
     * the band is determined by {@link #getBandColorArray(Band)}.
     * 
     * @param patternMap
     *            <code>RealMap</code> of the pattern
     * @param band
     *            band to draw
     */
    private void drawBand(RealMap patternMap, Band band) {
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
        MapMath.addition(patternMap, tmpMap, patternMap);
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



    /**
     * Creates an array that represents the band thickness (length) and the
     * color across the band. The color is kept constant across the thickness.
     * The normalized intensity is used.
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
