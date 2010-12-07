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
package org.ebsdimage.core.exp.ops.pattern.post;

import java.util.Random;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

/**
 * Operation to add radial noise to the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class RadialNoise extends PatternPostOps {

    /** Default operation. */
    public static final RadialNoise DEFAULT = new RadialNoise(0, 0, -1.0, -1.0,
            1.0, 15.0);

    /** Location of the centre in x. */
    @Attribute(name = "x")
    public final int x;

    /** Location of the centre in y. */
    @Attribute(name = "y")
    public final int y;

    /** Standard deviation in x for the 2D gaussian distribution. */
    @Attribute(name = "stdDevX")
    public final double stdDevX;

    /** Standard deviation in y for the 2D gaussian distribution. */
    @Attribute(name = "stdDevY")
    public final double stdDevY;

    /** Initial noise level at (x0, y0). */
    @Attribute(name = "initialNoiseStdDev")
    public final double initialNoiseStdDev;

    /** Final noise level. */
    @Attribute(name = "finalNoiseStdDev")
    public final double finalNoiseStdDev;



    /**
     * Creates a new radial noise operation from the specified parameters.
     * 
     * @param x
     *            location of the center in x (x0 = 0 is the center of the map)
     * @param y
     *            location of the center in y (y0 = 0 is the center of the map)
     * @param stdDevX
     *            standard deviation in x for the 2D gaussian distribution
     * @param stdDevY
     *            standard deviation in y for the 2D gaussian distribution
     * @param initialNoiseStdDev
     *            initial noise level at (x0, y0)
     * @param finalNoiseStdDev
     *            final noise level
     * @see org.ebsdimage.core.Noise#radialNoise(ByteMap, int, int, double,
     *      double, double, double)
     */
    public RadialNoise(@Attribute(name = "x") int x,
            @Attribute(name = "y") int y,
            @Attribute(name = "stdDevX") double stdDevX,
            @Attribute(name = "stdDevY") double stdDevY,
            @Attribute(name = "initialNoiseStdDev") double initialNoiseStdDev,
            @Attribute(name = "finalNoiseStdDev") double finalNoiseStdDev) {
        if (initialNoiseStdDev <= 0)
            throw new IllegalArgumentException("The initial std.dev. ("
                    + initialNoiseStdDev + ") must be greater than 0.");
        if (finalNoiseStdDev <= 0)
            throw new IllegalArgumentException("The final std.dev. in y ("
                    + finalNoiseStdDev + ") must be greater than 0.");

        this.x = x;
        this.y = y;
        this.stdDevX = stdDevX;
        this.stdDevY = stdDevY;
        this.initialNoiseStdDev = initialNoiseStdDev;
        this.finalNoiseStdDev = finalNoiseStdDev;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        RadialNoise other = (RadialNoise) obj;
        if (Double.doubleToLongBits(finalNoiseStdDev) != Double.doubleToLongBits(other.finalNoiseStdDev))
            return false;
        if (Double.doubleToLongBits(initialNoiseStdDev) != Double.doubleToLongBits(other.initialNoiseStdDev))
            return false;
        if (Double.doubleToLongBits(stdDevX) != Double.doubleToLongBits(other.stdDevX))
            return false;
        if (Double.doubleToLongBits(stdDevY) != Double.doubleToLongBits(other.stdDevY))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        RadialNoise other = (RadialNoise) obj;
        if (Math.abs(finalNoiseStdDev - other.finalNoiseStdDev) > delta)
            return false;
        if (Math.abs(initialNoiseStdDev - other.initialNoiseStdDev) > delta)
            return false;
        if (Math.abs(stdDevX - other.stdDevX) > delta)
            return false;
        if (Math.abs(stdDevY - other.stdDevY) > delta)
            return false;
        if (Math.abs(x - other.x) > delta)
            return false;
        if (Math.abs(y - other.y) > delta)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(finalNoiseStdDev);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(initialNoiseStdDev);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stdDevX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(stdDevY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }



    /**
     * Adds radial noise to the source map.
     * <ul>
     * <li>If <code>stdDevX</code> is negative, it is recalculated to be half of
     * the source map's width.</li>
     * <li>If the <code>stdDevY</code> is negative, it is recalculated to be
     * half of the source map's height.</li>
     * </ul>
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input pattern map
     * @return output pattern map
     */
    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        // Set default if needed
        double stdDevX = this.stdDevX;
        double stdDevY = this.stdDevY;

        if (stdDevX < 0)
            stdDevX = srcMap.width / 2.0;
        if (stdDevY < 0)
            stdDevY = srcMap.height / 2.0;

        ByteMap destMap = srcMap.duplicate();

        radialNoise(destMap, x, y, stdDevX, stdDevY, initialNoiseStdDev,
                finalNoiseStdDev, System.currentTimeMillis());

        // Apply properties of srcMap
        destMap.setProperties(srcMap);

        return destMap;
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
     *            location of the centre in x (x0 = 0 is the centre of the map)
     * @param y0
     *            location of the centre in y (y0 = 0 is the centre of the map)
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
     * @throws IllegalArgumentException
     *             if stdDevX <= 0
     * @throws IllegalArgumentException
     *             if stdDevY <= 0
     */
    public void radialNoise(ByteMap map, int x0, int y0, double stdDevX,
            double stdDevY, double initialNoiseStdDev, double finalNoiseStdDev,
            long seed) {

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

        map.setChanged(Map.MAP_CHANGED);
    }



    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("RadialNoise [");
        str.append("x0=" + x + " px, ");
        str.append("y0=" + y + " px, ");
        str.append("std. dev. x=" + stdDevX + ", ");
        str.append("std. dev. y=" + stdDevY + ", ");
        str.append("initial noise std. dev.=" + initialNoiseStdDev + ", ");
        str.append("final noise std. dev.=" + finalNoiseStdDev + "]");

        return str.toString();
    }

}
