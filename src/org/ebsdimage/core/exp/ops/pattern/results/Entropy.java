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
package org.ebsdimage.core.exp.ops.pattern.results;

import org.ebsdimage.core.MapStats;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.ByteMap;
import rmlimage.module.real.core.RealMap;

/**
 * Operation to calculate the entropy quality index.
 * 
 * @author Philippe T. Pinard
 */
public class Entropy extends PatternResultsOps {

    /** Default operation. */
    public static final Entropy DEFAULT = new Entropy();



    /**
     * Calculates the entropy quality index of the source map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            source map
     * @return one entry with the entropy quality index
     */
    @Override
    public OpResult[] calculate(Exp exp, ByteMap srcMap) {
        OpResult result =
                new OpResult("Pattern Entropy", MapStats.entropy(srcMap),
                        RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Entropy";
    }

}
