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
import rmlimage.core.Convolution;
import rmlimage.core.Kernel;

/**
 * Operation to perform a convolution of a constant value kernel to produce a
 * smoothing on the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class Smoothing extends PatternPostOps {

    /** Kernel size for the smoothing kernel. */
    public final int kernelSize;

    /** Default kernel size. */
    public static final int DEFAULT_KERNEL_SIZE = 1;



    /**
     * Creates a new smoothing operation with the default kernel size.
     */
    public Smoothing() {
        kernelSize = DEFAULT_KERNEL_SIZE;
    }



    /**
     * Creates a new smoothing operation with the specified kernel size.
     * 
     * @param kernelSize
     *            kernel size of the smoothing
     */
    public Smoothing(int kernelSize) {
        if (kernelSize <= 0)
            throw new IllegalArgumentException(
                    "Kernel size must be greater than 0.");

        this.kernelSize = kernelSize;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Smoothing other = (Smoothing) obj;
        if (kernelSize != other.kernelSize)
            return false;
        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + kernelSize;
        return result;
    }



    /**
     * Performs a convolution of a smoothing kernel with the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input pattern map
     * @return output pattern map
     * 
     * @see Convolution#convolve(ByteMap, Kernel)
     */
    @Override
    public ByteMap process(Exp exp, ByteMap srcMap) {
        if (kernelSize < 3)
            return srcMap;

        int[][] data = new int[kernelSize][kernelSize];

        for (int i = 0; i < kernelSize; i++)
            for (int j = 0; j < kernelSize; j++)
                data[i][j] = 1;
        Kernel kernel = new Kernel(data, kernelSize * kernelSize);

        ByteMap destMap = new ByteMap(srcMap.width, srcMap.height);
        Convolution.convolve(srcMap, kernel, destMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Smoothing [kernel size=" + kernelSize + "]";
    }

}
