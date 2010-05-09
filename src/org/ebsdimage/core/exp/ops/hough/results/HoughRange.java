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
package org.ebsdimage.core.exp.ops.hough.results;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.QualityIndex;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;

/**
 * Operation to calculate the Hough range quality index.
 * 
 * @author Philippe T. Pinard
 */
public class HoughRange extends HoughResultsOps {

    @Override
    public String toString() {
        return "Hough Range";
    }



    /**
     * Calculates the Hough range quality index of the source map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            source Hough map
     * @return one entry with the Hough range quality index
     * 
     * @see QualityIndex#houghRange(HoughMap)
     */
    @Override
    public OpResult[] calculate(Exp exp, HoughMap srcMap) {
        OpResult result =
                new OpResult(getName(), QualityIndex.houghRange(srcMap),
                        RealMap.class);

        return new OpResult[] { result };
    }

}
