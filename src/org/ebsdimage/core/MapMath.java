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

import rmlimage.core.BinMap;
import rmlimage.core.Map;
import rmlimage.core.handler.MapMathHandler;

/**
 * Mathematical operations EBSD specific maps (e.g. Phases map).
 * 
 * @author Philippe T. Pinard
 */
public class MapMath implements MapMathHandler {

    /**
     * Does an addition operation on two <code>PhasesMap</code> such that:
     * <code>(src1 + src2) = dest</code>.
     * <p/>
     * The destination <code>PhasesMap</code> must already contains the array of
     * defined phases.
     * <p/>
     * The destination <code>PhasesMap</code> is validated to assess that the
     * number of phases is valid.
     * 
     * @param src1
     *            source <code>PhasesMap</code>
     * @param src2
     *            source <code>PhasesMap</code>
     * @param dest
     *            <code>PhasesMap</code> to put the result into
     * @throws NullPointerException
     *             if a source map or destination is null
     * @throws IllegalArgumentException
     *             if the size of the three <code>Map</code>s are not the same
     */
    public static void addition(PhasesMap src1, PhasesMap src2, PhasesMap dest) {
        if (src1 == null)
            throw new NullPointerException(
                    "Source phases map 1 cannot be null.");
        if (src2 == null)
            throw new NullPointerException(
                    "Source phases map 2 cannot be null.");
        if (dest == null)
            throw new NullPointerException(
                    "Destination phases map cannot be null.");

        if (!src1.isSameSize(src2))
            throw new IllegalArgumentException("src1 size (" + src1.getName()
                    + ")(" + src1.getDimensionLabel()
                    + ") is different than src2 size (" + src2.getName() + ")("
                    + src2.getDimensionLabel() + ")");
        if (!src1.isSameSize(dest))
            throw new IllegalArgumentException("src1 size (" + src1.getName()
                    + ")(" + src1.getDimensionLabel()
                    + ") is different than dest size (" + dest.getName() + ")("
                    + dest.getDimensionLabel() + ")");

        byte[] src1PixArray = src1.pixArray;
        byte[] src2PixArray = src2.pixArray;
        byte[] destPixArray = dest.pixArray;
        int pixValue;
        int size = src1.size;
        for (int n = 0; n < size; n++) {
            pixValue = (src1PixArray[n] & 0xff) + (src2PixArray[n] & 0xff);

            // Truncate
            if (pixValue < 0)
                pixValue = 0;
            else if (pixValue > 255)
                pixValue = 255;

            destPixArray[n] = (byte) pixValue;
        }

        dest.validate();

        dest.setChanged(Map.MAP_CHANGED);
    }



    /**
     * Does an <code>AND</code> operation such that:
     * <code>src1 AND src2 = dest</code>.
     * 
     * @param src1
     *            source <code>PhasesMap</code>
     * @param src2
     *            source <code>BinMap</code> (i.e. mask)
     * @param dest
     *            <code>PhasesMap</code> to put the result into
     * @throws NullPointerException
     *             if a source map or destination is null
     * @throws IllegalArgumentException
     *             if the size of the three <code>Map</code>s are not the same
     */
    public static void and(PhasesMap src1, BinMap src2, PhasesMap dest) {
        if (src1 == null)
            throw new NullPointerException("Source phases map cannot be null.");
        if (src2 == null)
            throw new NullPointerException("Source BinMap cannot be null.");
        if (dest == null)
            throw new NullPointerException(
                    "Destination phases map cannot be null.");

        if (!src1.isSameSize(src2))
            throw new IllegalArgumentException("src1 size (" + src1.getName()
                    + ")(" + src1.getDimensionLabel()
                    + ") is different than src2 size (" + src2.getName() + ")("
                    + src2.getDimensionLabel() + ")");
        if (!src1.isSameSize(dest))
            throw new IllegalArgumentException("src1 size (" + src1.getName()
                    + ")(" + src1.getDimensionLabel()
                    + ") is different than dest size (" + dest.getName() + ")("
                    + dest.getDimensionLabel() + ")");

        byte[] src1PixArray = src1.pixArray;
        byte[] src2PixArray = src2.pixArray;
        byte[] destPixArray = dest.pixArray;
        int size = src1.size;
        for (int n = 0; n < size; n++)
            destPixArray[n] =
                    (src2PixArray[n] == 1) ? src1PixArray[n] : (byte) 0;

        // Copy the props and phases
        if (dest != src1) {
            dest.clearProperties();
            dest.setProperties(src1);
            dest.setPhases(src1.getPhases());
        }

        dest.validate();

        dest.setChanged(Map.MAP_CHANGED);
    }



    @Override
    public boolean addition(Map src1, Map src2, double multValue,
            double addValue, Map dest) {
        if ((src1 instanceof PhasesMap) && (src2 instanceof PhasesMap)
                && (dest instanceof PhasesMap)) {
            addition((PhasesMap) src1, (PhasesMap) src2, (PhasesMap) dest);
            return true;
        } else {
            return false;
        }
    }



    @Override
    public boolean and(Map src1, Map src2, Map dest) {
        if ((src1 instanceof PhasesMap) && (src2 instanceof BinMap)
                && (dest instanceof PhasesMap)) {
            and((PhasesMap) src1, (BinMap) src2, (PhasesMap) dest);
            return true;
        } else {
            return false;
        }
    }



    @Override
    public boolean arithmetic(Map source, double multValue, double addValue,
            Map dest) {
        return false;
    }



    @Override
    public boolean division(Map src1, Map src2, double multValue,
            double addValue, Map dest) {
        return false;
    }



    @Override
    public boolean multiplication(Map src1, Map src2, double multValue,
            double addValue, Map dest) {
        return false;
    }



    @Override
    public boolean not(Map map) {
        return false;
    }



    @Override
    public boolean or(Map src1, Map src2, Map dest) {
        return false;
    }



    @Override
    public boolean subtraction(Map src1, Map src2, double multValue,
            double addValue, Map dest) {
        return false;
    }



    @Override
    public boolean xor(Map src1, Map src2, Map dest) {
        return false;
    }

}
