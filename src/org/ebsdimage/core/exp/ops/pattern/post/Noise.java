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

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.ByteMap;

/**
 * Operation to add Gaussian noise to the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class Noise extends PatternPostOps {

    /** Standard deviation of the noise. */
    public final double stdDev;

    /** Default standard deviation. */
    public static final double DEFAULT_STDDEV = 1.0;

    public final int count;



    /**
     * Creates a new noise operation with the default standard deviation.
     */
    public Noise() {
        this(DEFAULT_STDDEV, 1);
    }



    /**
     * Creates a new noise operation from the specified standard deviation.
     * 
     * @param stdDev
     *            standard deviation
     */
    public Noise(double stdDev, int count) {
        if (stdDev <= 0)
            throw new IllegalArgumentException("Standard deviation (" + stdDev
                    + ") must be > " + 0 + '.');

        this.stdDev = stdDev;
        this.count = count;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Noise other = (Noise) obj;
        if (count != other.count)
            return false;
        if (Double.doubleToLongBits(stdDev) != Double.doubleToLongBits(other.stdDev))
            return false;
        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + count;
        long temp;
        temp = Double.doubleToLongBits(stdDev);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Adds noise to the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input pattern map
     * @return output pattern map
     * @see rmlimage.utility.Noise#gaussian(ByteMap, double)
     */
    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        ByteMap destMap = srcMap.duplicate();

        for (int i = 0; i < count; i++)
            rmlimage.utility.Noise.gaussian(destMap, stdDev);

        // Apply properties of srcMap
        destMap.setProperties(srcMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Noise [std. dev.=" + stdDev + "]";
    }

}
