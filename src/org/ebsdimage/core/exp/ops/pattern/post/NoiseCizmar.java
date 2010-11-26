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
 * @author ppinard
 * @see rmlimage.utility.Noise.cizmar
 */
public class NoiseCizmar extends PatternPostOps {

    /** Standard deviation of the Gaussian noise. */
    @Attribute(name = "gaussian")
    public final double gaussian;

    /** Factor for the intensity of the Poisson noise. */
    @Attribute(name = "poisson")
    public final double poisson;

    /** Seed for the random number generator. */
    @Attribute(name = "seed")
    public final long seed;



    @Override
    public String toString() {
        return "Noise Cizmar [gaussian=" + gaussian + ", poisson=" + poisson
                + "]";
    }



    /**
     * Creates a new <code>NoiseCizmar</code> operation. If the seed is less
     * than zero, the seed is taken as the current system time in milliseconds.
     * 
     * @param sigma
     *            standard deviation of the Gaussian noise
     * @param poisson
     *            factor for the intensity of the Poisson noise
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
                    "Factor for the intensity of the Poisson noise must be greater than zero.");

        this.gaussian = sigma;
        this.poisson = poisson;
        this.seed = seed;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(gaussian);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(poisson);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (int) (seed ^ (seed >>> 32));
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        NoiseCizmar other = (NoiseCizmar) obj;
        if (Double.doubleToLongBits(gaussian) != Double.doubleToLongBits(other.gaussian))
            return false;
        if (Double.doubleToLongBits(poisson) != Double.doubleToLongBits(other.poisson))
            return false;
        if (seed != other.seed)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (!super.equals(obj, precision))
            return false;

        NoiseCizmar other = (NoiseCizmar) obj;
        if (Math.abs(gaussian - other.gaussian) >= precision)
            return false;
        if (Math.abs(poisson - other.poisson) >= precision)
            return false;
        if (Math.abs(seed - other.seed) >= precision)
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

        // Apply properties of srcMap
        destMap.setProperties(srcMap);

        return destMap;
    }

}
