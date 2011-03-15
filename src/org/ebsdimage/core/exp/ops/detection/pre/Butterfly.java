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
package org.ebsdimage.core.exp.ops.detection.pre;

import org.ebsdimage.core.Conversion;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.Kernel;
import rmlimage.module.real.core.Contrast;
import rmlimage.module.real.core.Convolution;
import rmlimage.module.real.core.Edit;
import rmlimage.module.real.core.RealMap;
import static java.lang.Math.abs;

/**
 * Operation to apply a butterfly filter on the Hough map.
 * 
 * @author Philippe T. Pinard
 */
public class Butterfly extends DetectionPreOps {

    /** Size of the butterfly kernel. */
    @Attribute(name = "kernelSize")
    public final int kernelSize;

    /** Lower limit of the flattening operation. */
    @Attribute(name = "flattenLowerLimit")
    public final float flattenLowerLimit;

    /** Upper limit of the flattening operation. */
    @Attribute(name = "flattenUpperLimit")
    public final float flattenUpperLimit;

    /** Default operation. */
    public static final Butterfly DEFAULT = new Butterfly(9, -800f, 800f);



    /**
     * Creates a new butterfly operation from the specified parameters.
     * 
     * @param kernelSize
     *            butterfly filter kernel size
     * @param flattenLowerLimit
     *            lower limit of the flattening operation
     * @param flattenUpperLimit
     *            upper limit of the flattening operation
     */
    public Butterfly(@Attribute(name = "kernelSize") int kernelSize,
            @Attribute(name = "flattenLowerLimit") float flattenLowerLimit,
            @Attribute(name = "flattenUpperLimit") float flattenUpperLimit) {
        if (kernelSize != 9 && kernelSize != 3)
            throw new IllegalArgumentException(
                    "Only a kernel size of 3 or 9 is implemented.");
        if (flattenLowerLimit >= flattenUpperLimit)
            throw new IllegalArgumentException("Flatten lower limit ("
                    + flattenLowerLimit + ") must be < than the upper limit ("
                    + flattenUpperLimit + ").");

        this.kernelSize = kernelSize;
        this.flattenLowerLimit = flattenLowerLimit;
        this.flattenUpperLimit = flattenUpperLimit;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        Butterfly other = (Butterfly) obj;
        if (abs(kernelSize - other.kernelSize) > delta)
            return false;
        if (abs(flattenLowerLimit - other.flattenLowerLimit) > delta)
            return false;
        if (abs(flattenUpperLimit - other.flattenUpperLimit) > delta)
            return false;

        return true;
    }



    /**
     * Applies a butterfly filter on the source map. First the butterfly filter
     * is convoluted with the source map. Second the convoluted map is flatten.
     * Third the convoluted and flatten map is converted back to a
     * <code>ByteMap</code> then a <code>HoughMap</code>.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input Hough map
     * @return output Hough map
     * @see Convolution#convolve(ByteMap, Kernel)
     * @see Edit#flatten(RealMap, float, float, float)
     * @see Contrast#expansion(RealMap)
     */
    @Override
    public HoughMap process(Exp exp, HoughMap srcMap) {
        // TODO: Implements different kernel size

        int[][] data;
        switch (kernelSize) {
        case 3:
            data = new int[][] { { 0, -2, 0 }, { 1, 3, 1 }, { 0, -2, 0 } };
            break;

        case 9:
            data =
                    new int[][] {
                            { -10, -15, -22, -22, -22, -22, -22, -15, -10 },
                            { -1, -6, -13, -22, -22, -22, -13, -6, -1 },
                            { 3, 6, 4, -3, -22, -3, 4, 6, 3 },
                            { 3, 11, 19, 28, 42, 28, 19, 11, 3 },
                            { 3, 11, 27, 42, 42, 42, 27, 11, 3 },
                            { 3, 11, 19, 28, 42, 28, 19, 11, 3 },
                            { 3, 6, 4, -3, -22, -3, 4, 6, 3 },
                            { -1, -6, -13, -22, -22, -22, -13, -6, -1 },
                            { -10, -15, -22, -22, -22, -22, -22, -15, -10 } };
            break;

        default:
            throw new IllegalArgumentException("Invalid kernel size");
        }

        Kernel kernel = new Kernel(data, 1);

        RealMap houghMapConvol = Convolution.convolve(srcMap, kernel);

        // Flatten convoluted real map
        Edit.flatten(houghMapConvol, flattenLowerLimit, flattenUpperLimit, 0);

        // Convert back to byteMap
        ByteMap houghMapFlatten = Contrast.expansion(houghMapConvol);

        HoughMap destMap = srcMap.duplicate();
        Conversion.toHoughMap(houghMapFlatten, destMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Butterfly [flatten lower limit=" + flattenLowerLimit
                + ", flatten upper limit=" + flattenUpperLimit
                + ", kernel size=" + kernelSize + "]";
    }

}
