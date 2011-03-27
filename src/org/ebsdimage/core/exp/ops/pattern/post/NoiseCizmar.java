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
package org.ebsdimage.core.exp.ops.pattern.post;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.module.real.core.Conversion;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.core.ThreeSigmaRenderer;

/**
 * Operation to add Gaussian and Poisson noise to a diffraction pattern based on
 * Cizmar et al.(2008) equation.
 * 
 * @author Philippe T. Pinard
 * @see rmlimage.utility.Noise.cizmar
 */
public class NoiseCizmar extends PatternPostOps {

    /** Default operation. */
    public static final NoiseCizmar DEFAULT = new NoiseCizmar(25, 8, -1);

    /** Standard deviation of the Gaussian noise. */
    @Attribute(name = "gaussian")
    public final double gaussian;

    /** Amplitude of the Poisson noise. */
    @Attribute(name = "poisson")
    public final double poisson;

    /** Seed for the random number generator. */
    @Attribute(name = "seed")
    public final long seed;



    /**
     * Creates a new <code>NoiseCizmar</code> operation. If the seed is less
     * than zero, the seed is taken as the current system time in milliseconds.
     * 
     * @param sigma
     *            standard deviation of the Gaussian noise
     * @param poisson
     *            amplitude of the Poisson noise
     * @param seed
     *            seed for the random number generator
     */
    public NoiseCizmar(@Attribute(name = "gaussian") double sigma,
            @Attribute(name = "poisson") double poisson,
            @Attribute(name = "seed") long seed) {
        if (sigma <= 0)
            throw new IllegalArgumentException(
                    "Standard deviation of the Gaussian noise must be greater than zero.");
        if (poisson <= 0)
            throw new IllegalArgumentException(
                    "Amplitude of the Poisson noise must be greater than zero.");

        this.gaussian = sigma;
        this.poisson = poisson;
        this.seed = seed;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        NoiseCizmar other = (NoiseCizmar) obj;
        if (Math.abs(gaussian - other.gaussian) > delta)
            return false;
        if (Math.abs(poisson - other.poisson) > delta)
            return false;
        if (Math.abs(seed - other.seed) > delta)
            return false;

        return true;
    }



    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        RealMap realMap = Conversion.toRealMap(srcMap);
        if (seed < 0)
            rmlimage.utility.Noise.cizmar(realMap, gaussian, poisson);
        else
            rmlimage.utility.Noise.cizmar(realMap, gaussian, poisson, seed);
        realMap.setMapRenderer(new ThreeSigmaRenderer());

        ByteMap destMap = Conversion.toByteMap(realMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Noise Cizmar [gaussian=" + gaussian + ", poisson=" + poisson
                + "]";
    }

}
