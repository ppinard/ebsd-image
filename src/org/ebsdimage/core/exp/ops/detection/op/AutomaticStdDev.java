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

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Threshold;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;

/**
 * Operation to perform the automatic standard deviation detection algorithm.
 * 
 * @author Philippe T. Pinard
 * 
 * @see Threshold#automaticStdDev
 */
public class AutomaticStdDev extends DetectionOp {

    @Override
    public String toString() {
        return "Automatic Std Dev";
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
     * @see Threshold#automaticStdDev(HoughMap)
     */
    @Override
    public BinMap detect(Exp exp, HoughMap srcMap) {
        BinMap peaksMap = Threshold.automaticStdDev(srcMap);

        return peaksMap;
    }

}