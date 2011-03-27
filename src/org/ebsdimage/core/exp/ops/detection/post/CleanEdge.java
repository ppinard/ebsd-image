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
package org.ebsdimage.core.exp.ops.detection.post;

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.*;
import rmlimage.core.Identification.Edge;

/**
 * Operation to remove peaks touching the right edge of the peaks map. This
 * operation is to remove double peaks that appear on the left and right edges.
 * 
 * @author Philippe T. Pinard
 */
public class CleanEdge extends DetectionPostOps {

    /** Default operation. */
    public static final CleanEdge DEFAULT = new CleanEdge();



    /**
     * Removes the peaks touching the right edge of the peaks map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input peaks map
     * @return output peaks map
     * @see Identification#removeObjectsTouching
     */
    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        IdentMap identMap = Identification.identify(srcMap);

        // Remove peaks touching the edges
        Identification.removeObjectsTouching(identMap, Edge.RIGHT);

        // Centroid
        BinMap destMap = Conversion.toBinMap(identMap);
        destMap.setProperties(srcMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Clean Edge";
    }
}
