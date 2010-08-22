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

import static java.lang.Math.exp;
import static java.lang.Math.pow;

import java.util.Random;

import rmlimage.core.ByteMap;

/**
 * Noise generator.
 * 
 * @author Philippe T. Pinard
 */
public class Noise {

    /**
     * Adds noise to a ByteMap radially. The seed for the random number
     * generator is taken as the current time in milliseconds.
     * <p/>
     * At every pixel, a random gaussian noise is calculated. The standard
     * deviation for this random noise follows a 2D gaussian distribution
     * varying between the <code>initialNoiseStdDev</code> at (<code>x0</code>,
     * <code>y0</code>) to <code>finalNoiseStdDev</code>. The 2D gaussian
     * distribution is defined by <code>stdDevX</code> and <code>stdDevY</code>.
     * 
     * @param map
     *            source map
     * @param x0
     *            location of the center in x (x0 = 0 is the center of the map)
     * @param y0
     *            location of the center in y (y0 = 0 is the center of the map)
     * @param stdDevX
     *            standard deviation in x for the 2D gaussian distribution
     * @param stdDevY
     *            standard deviation in y for the 2D gaussian distribution
     * @param initialNoiseStdDev
     *            initial noise level at (x0, y0)
     * @param finalNoiseStdDev
     *            final noise level
     * @return map with noise
     * @throws IllegalArgumentException
     *             if stdDevX <= 0
     * @throws IllegalArgumentException
     *             if stdDevY <= 0
     */
    public static ByteMap radialNoise(ByteMap map, int x0, int y0,
            double stdDevX, double stdDevY, double initialNoiseStdDev,
            double finalNoiseStdDev) {
        return radialNoise(map, x0, y0, stdDevX, stdDevY, initialNoiseStdDev,
                finalNoiseStdDev, System.currentTimeMillis());
    }



    /**
     * Adds noise to a ByteMap radially.
     * <p/>
     * At every pixel, a random gaussian noise is calculated. The standard
     * deviation for this random noise follows a 2D gaussian distribution
     * varying between the <code>initialNoiseStdDev</code> at (<code>x0</code>,
     * <code>y0</code>) to <code>finalNoiseStdDev</code>. The 2D gaussian
     * distribution is defined by <code>stdDevX</code> and <code>stdDevY</code>.
     * 
     * @param map
     *            source map
     * @param x0
     *            location of the center in x (x0 = 0 is the center of the map)
     * @param y0
     *            location of the center in y (y0 = 0 is the center of the map)
     * @param stdDevX
     *            standard deviation in x for the 2D gaussian distribution
     * @param stdDevY
     *            standard deviation in y for the 2D gaussian distribution
     * @param initialNoiseStdDev
     *            initial noise level at (x0, y0)
     * @param finalNoiseStdDev
     *            final noise level
     * @param seed
     *            seed to use to initialize the internal pseudo-random number
     *            generator.
     * @return map with noise
     * @throws IllegalArgumentException
     *             if stdDevX <= 0
     * @throws IllegalArgumentException
     *             if stdDevY <= 0
     */
    public static ByteMap radialNoise(ByteMap map, int x0, int y0,
            double stdDevX, double stdDevY, double initialNoiseStdDev,
            double finalNoiseStdDev, long seed) {

        if (stdDevX <= 0)
            throw new IllegalArgumentException("The std.dev. in x (" + stdDevX
                    + ") must be greater than 0.");
        if (stdDevY <= 0)
            throw new IllegalArgumentException("The std.dev. in y (" + stdDevY
                    + ") must be greater than 0.");

        if (initialNoiseStdDev <= 0)
            throw new IllegalArgumentException("The initial std.dev. ("
                    + initialNoiseStdDev + ") must be greater than 0.");
        if (finalNoiseStdDev <= 0)
            throw new IllegalArgumentException("The final std.dev. in y ("
                    + finalNoiseStdDev + ") must be greater than 0.");

        int pixValue;
        byte[] pixArray = map.pixArray;

        Random rnd = new Random(seed);

        for (int i = 0; i < map.width; i++)
            for (int j = 0; j < map.height; j++) {
                int index = map.getPixArrayIndex(i, j);
                int x = i - map.width / 2;
                int y = j - map.height / 2;

                double a = finalNoiseStdDev - initialNoiseStdDev;
                double exponent =
                        pow(x - x0, 2) / pow(stdDevX, 2) + pow(y - y0, 2)
                                / pow(stdDevY, 2);
                double amplitude =
                        a * (1 - exp(-exponent / 2.0)) + initialNoiseStdDev;

                pixValue = pixArray[index] & 0xff;
                pixValue += (int) (amplitude * rnd.nextGaussian());

                if (pixValue < 0)
                    pixValue = 0;
                if (pixValue > 255)
                    pixValue = 255;
                pixArray[index] = (byte) pixValue;
            }

        return map;
    }
}
