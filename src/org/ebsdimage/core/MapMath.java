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

import java.util.Map.Entry;

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
     * Does an addition operation on two <code>IndexedByteMap</code> such that:
     * <code>(src1 + src2) = dest</code>.
     * <p/>
     * The destination <code>IndexedByteMap</code> must already contains the
     * array of defined phases.
     * <p/>
     * The destination <code>IndexedByteMap</code> is validated to assess that
     * the number of phases is valid.
     * 
     * @param <Item>
     *            type of item for the <code>IndexedByteMap</code>
     * @param src1
     *            source <code>IndexedByteMap</code>
     * @param src2
     *            source <code>IndexedByteMap</code>
     * @param multValue
     *            scaling
     * @param addValue
     *            offset
     * @param dest
     *            <code>IndexedByteMap</code> to put the result into
     * @throws NullPointerException
     *             if a source map or destination is null
     * @throws IllegalArgumentException
     *             if the size of the three <code>Map</code>s are not the same
     */
    public static <Item> void addition(IndexedByteMap<Item> src1,
            IndexedByteMap<Item> src2, double multValue, double addValue,
            IndexedByteMap<Item> dest) {
        rmlimage.core.MapMath.addition(src1, src2, multValue, addValue, dest);

        // Update items
        for (Entry<Integer, Item> entry : src1.getItems().entrySet())
            if (!dest.isRegistered(entry.getKey()))
                dest.register(entry.getKey(), entry.getValue());

        for (Entry<Integer, Item> entry : src2.getItems().entrySet())
            if (!dest.isRegistered(entry.getKey()))
                dest.register(entry.getKey(), entry.getValue());

        dest.validate();
    }



    /**
     * Does an <code>AND</code> operation such that:
     * <code>src1 AND src2 = dest</code>.
     * 
     * @param <Item>
     *            type of item for the <code>IndexedByteMap</code>
     * @param src1
     *            source <code>IndexedByteMap</code>
     * @param src2
     *            source <code>BinMap</code> (i.e. mask)
     * @param dest
     *            <code>IndexedByteMap</code> to put the result into
     * @throws NullPointerException
     *             if a source map or destination is null
     * @throws IllegalArgumentException
     *             if the size of the three <code>Map</code>s are not the same
     */
    public static <Item> void and(IndexedByteMap<Item> src1, BinMap src2,
            IndexedByteMap<Item> dest) {
        rmlimage.core.MapMath.and(src1, src2, dest);

        // Update items
        for (Entry<Integer, Item> entry : src1.getItems().entrySet())
            if (!dest.isRegistered(entry.getKey()))
                dest.register(entry.getKey(), entry.getValue());

        dest.validate();
    }



    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean addition(Map src1, Map src2, double multValue,
            double addValue, Map dest) {
        if ((src1 instanceof IndexedByteMap)
                && (src2 instanceof IndexedByteMap)
                && (dest instanceof IndexedByteMap)) {
            addition((IndexedByteMap) src1, (IndexedByteMap) src2, multValue,
                    addValue, (IndexedByteMap) dest);
            return true;
        } else {
            return false;
        }
    }



    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean and(Map src1, Map src2, Map dest) {
        if ((src1 instanceof IndexedByteMap) && (src2 instanceof BinMap)
                && (dest instanceof IndexedByteMap)) {
            and((IndexedByteMap) src1, (BinMap) src2, (IndexedByteMap) dest);
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
