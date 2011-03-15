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
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;

/**
 * Operation to add Gaussian noise to the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class Noise extends PatternPostOps {

    /** Default operation. */
    public static final Noise DEFAULT = new Noise(1.0);

    /** Standard deviation of the noise. */
    @Attribute(name = "stdDev")
    public final double stdDev;



    /**
     * Creates a new noise operation from the specified standard deviation.
     * 
     * @param stdDev
     *            standard deviation
     */
    public Noise(@Attribute(name = "stdDev") double stdDev) {
        if (stdDev <= 0)
            throw new IllegalArgumentException("Standard deviation (" + stdDev
                    + ") must be > " + 0 + '.');

        this.stdDev = stdDev;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        Noise other = (Noise) obj;
        if (Math.abs(stdDev - other.stdDev) > delta)
            return false;

        return true;
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

        rmlimage.utility.Noise.gaussian(destMap, stdDev);

        return destMap;
    }



    @Override
    public String toString() {
        return "Noise [std. dev.=" + stdDev + "]";
    }

}
