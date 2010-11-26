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
package org.ebsdimage.core.exp.ops.detection.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.BinMap;
import rmlimage.core.Identification;
import rmlimage.module.real.core.RealMap;

/**
 * Result operation that calculates the number of detected peaks.
 * 
 * @author Philippe T. Pinard
 */
public class Count extends DetectionResultsOps {

    /** Default operation. */
    public static final Count DEFAULT = new Count();



    @Override
    public OpResult[] calculate(Exp exp, BinMap peaksMap) {
        int value = Identification.identify(peaksMap).getObjectCount();

        OpResult result =
                new OpResult("Detected Peaks Count", value, RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Count";
    }

}
