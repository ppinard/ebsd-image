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
package org.ebsdimage.core;

import static java.lang.Math.pow;
import static java.util.Arrays.sort;
import static ptpshared.utility.Arrays.reverse;
import rmlimage.core.ByteMap;
import rmlimage.module.complex.core.ComplexMap;
import rmlimage.module.complex.core.Edit;
import rmlimage.module.complex.core.Extract;
import rmlimage.module.complex.core.FFT;
import rmlimage.module.real.core.MapMath;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.core.Stats;

/**
 * Calculations of quality indexes related to the diffraction pattern and Hough
 * transform.
 * 
 * @author Philippe T. Pinard
 */
public class QualityIndex {

    /**
     * Returns the Fourier transform quality index of the pattern.
     * 
     * @param pattern
     *            diffraction pattern
     * @return Fourier transform quality index
     */
    public static double fourier(ByteMap pattern) {
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
     * Returns the image quality (as calculated by TSL OIM software).
     * 
     * @param peaks
     *            <code>HoughPeaks</code>
     * @return image quality index
     */
    public static double imageQuality(HoughPeak[] peaks) {
        int numberPeaks = peaks.length;

        if (numberPeaks == 0)
            return 0.0;

        double value = 0.0;
        for (HoughPeak peak : peaks)
            value += peak.intensity;

        value /= numberPeaks;

        return value;
    }



    /**
     * Returns the image quality (as calculated by Oxford INCA Crystal).
     * 
     * @param peaks
     *            <code>HoughPeaks</code>
     * @return image quality index
     */
    public static double imageQualityInca(HoughPeak[] peaks) {
        if (peaks.length < 2)
            return 0.0;

        sort(peaks, new HoughPeakIntensityComparator());
        reverse(peaks);

        double minimum;
        if (peaks.length >= 7)
            minimum = peaks[6].intensity;
        else
            minimum = peaks[peaks.length - 1].intensity;

        double maximum = peaks[0].intensity;

        return maximum - minimum;
    }



    /**
     * Calculate Oxford's INCA pattern quality index. It consists of the sum of
     * the three most intense peaks divided by 3 times the standard deviation of
     * the Hough map.
     * 
     * @param peaks
     *            list of peaks
     * @param houghMap
     *            hough map
     * @return pattern quality index (PQI)
     */
    public static double patternQuality(HoughPeak[] peaks, HoughMap houghMap) {
        if (peaks.length < 3)
            return 0.0;

        double houghStdDev = Analysis.standardDeviation(houghMap);

        sort(peaks, new HoughPeakIntensityComparator());
        reverse(peaks);

        double quality = 0.0;
        for (int i = 0; i < 3; i++)
            quality += peaks[i].intensity;

        return quality / (3.0 * houghStdDev);
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
    private static RealMap radiusMap(int width, int height) {
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
    private static double sumMap(RealMap map) {
        double average = Stats.average(map.pixArray);
        int count = map.size;

        return count * average;
    }

}
