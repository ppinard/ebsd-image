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
package org.ebsdimage.core.exp.ops.identification.pre;

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.MathMorph;

/**
 * Operation to perform a dilation on the peaks map to increase the size of the
 * Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public class Dilation extends IdentificationPreOps {

    /**
     * Applies a dilation on the peaks map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input peaks map
     * @return output peaks map
     * @see MathMorph#dilation
     */
    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        BinMap destMap = srcMap.duplicate();

        MathMorph.dilation(destMap, 1, 8, 1);

        return destMap;
    }



    @Override
    public String toString() {
        return "Dilation";
    }

}
