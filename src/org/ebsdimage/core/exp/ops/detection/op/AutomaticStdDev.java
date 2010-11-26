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
package org.ebsdimage.core.exp.ops.detection.op;

import static java.lang.Math.abs;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Threshold;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.BinMap;

/**
 * Operation to perform the automatic standard deviation detection algorithm.
 * 
 * @author Philippe T. Pinard
 * @see Threshold#automaticStdDev
 */
public class AutomaticStdDev extends DetectionOp {

    /** Standard deviation scaling factor. */
    @Attribute(name = "sigma")
    public final double sigmaFactor;

    /** Default operation. */
    public static final AutomaticStdDev DEFAULT = new AutomaticStdDev(2);



    /**
     * Creates a new <code>AutomaticStdDev</code> operation with the specified
     * sigma factor.
     * 
     * @param sigmaFactor
     *            standard deviation scaling factor
     * @throws IllegalArgumentException
     *             if the sigma factor is less than 0
     */
    public AutomaticStdDev(@Attribute(name = "sigma") double sigmaFactor) {
        if (sigmaFactor < 0)
            throw new IllegalArgumentException("Sigma factor (" + sigmaFactor
                    + ") cannot be less than 0.");

        this.sigmaFactor = sigmaFactor;
    }



    /**
     * Detects peaks in the Hough map using the automatic standard deviation
     * peak detection algorithm.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            Hough map
     * @return peaks map
     * @see Threshold#automaticStdDev(HoughMap, double)
     */
    @Override
    public BinMap detect(Exp exp, HoughMap srcMap) {
        BinMap peaksMap = Threshold.automaticStdDev(srcMap, sigmaFactor);

        return peaksMap;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        AutomaticStdDev other = (AutomaticStdDev) obj;
        if (Double.doubleToLongBits(sigmaFactor) != Double.doubleToLongBits(other.sigmaFactor))
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (!super.equals(obj, precision))
            return false;

        AutomaticStdDev other = (AutomaticStdDev) obj;
        if (abs(sigmaFactor - other.sigmaFactor) >= precision)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(sigmaFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public String toString() {
        return "Automatic Std Dev [sigmaFactor=" + sigmaFactor + "]";
    }

}
