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
package org.ebsdimage.core.exp.ops.pattern.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.ByteMap;
import rmlimage.module.complex.core.ComplexMap;
import rmlimage.module.complex.core.Edit;
import rmlimage.module.complex.core.Extract;
import rmlimage.module.complex.core.FFT;
import rmlimage.module.real.core.MapMath;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.core.Stats;
import static java.lang.Math.pow;

/**
 * Operation to calculate the Fourier transform quality index.
 * 
 * @author Philippe T. Pinard
 */
public class Fourier extends PatternResultsOps {

    /** Default operation. */
    public static final Fourier DEFAULT = new Fourier();



    /**
     * Returns the Fourier transform quality index of the pattern.
     * 
     * @param pattern
     *            diffraction pattern
     * @return Fourier transform quality index
     */
    public double calculate(ByteMap pattern) {
        // Crop pattern to nearest power of two
        ByteMap patternCrop =
                Edit.cropToNearestPowerOfTwo(pattern, Edit.Position.CENTER);

        // FFT
        ComplexMap fftMap = new FFT().forward(patternCrop);
        Edit.flip(fftMap);
        RealMap normMap = Extract.norm(fftMap);

        // Radius map
        RealMap radiusMap = radiusMap(normMap.width, normMap.height);
        RealMap radiusMapSquared =
                new RealMap(radiusMap.width, radiusMap.height);
        MapMath.multiplication(radiusMap, radiusMap, radiusMapSquared);

        // Numerator
        RealMap numeratorMap = new RealMap(normMap.width, normMap.height);
        MapMath.multiplication(normMap, radiusMapSquared, numeratorMap);
        double numerator = sumMap(numeratorMap);

        // Denominator
        double denominator = sumMap(normMap);

        // Intensity
        double intensity = numerator / denominator;

        // Max intensity
        double intensityMax =
                sumMap(radiusMapSquared)
                        / (radiusMapSquared.width * radiusMapSquared.height);

        // Quality
        double quality = 1.0 - intensity / intensityMax;

        return quality;
    }



    /**
     * Calculates the Fourier transform quality index of the source map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            source map
     * @return one entry with the Fourier quality index
     * @see QualityIndex#fourier(ByteMap)
     */
    @Override
    public OpResult[] calculate(Exp exp, ByteMap srcMap) {
        OpResult result =
                new OpResult("Pattern Fourier", calculate(srcMap),
                        RealMap.class);

        return new OpResult[] { result };
    }



    /**
     * Generates a radius map according to the specified width and height for
     * the Fourier transform quality index.
     * 
     * @param width
     *            width of the image
     * @param height
     *            height of the image
     * @return radius map
     */
    private RealMap radiusMap(int width, int height) {
        RealMap map = new RealMap(width, height);

        int centerX = width / 2;
        int centerY = height / 2;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                double rSquare = pow(x - centerX, 2) + pow(y - centerY, 2);
                map.setPixValue(x, y, rSquare);
            }

        return map;
    }



    /**
     * Calculate the sum of the specified map for the Fourier transform quality
     * index.
     * 
     * @param map
     *            map to sum
     * @return sum
     */
    private double sumMap(RealMap map) {
        double average = Stats.average(map.pixArray);
        int count = map.size;

        return count * average;
    }



    @Override
    public String toString() {
        return "Fourier";
    }

}
