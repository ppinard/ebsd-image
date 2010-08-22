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

import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.average;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.max;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.min;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.standardDeviation;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.Analysis;
import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.module.real.core.RealMap;

/**
 * Result operation that evaluate the diameter of the greatest inscribed circle
 * inside the detected peaks.
 * 
 * @author Philippe T. Pinard
 */
public class Diameter extends DetectionResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, BinMap peaksMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // ========= Calculate diameter ===========

        float[] diameters = Analysis.getFeret(identMap).max;

        // ========= Calculate results ===========

        OpResult average =
                new OpResult("Diameter Average", average(diameters),
                        RealMap.class);
        OpResult stddev =
                new OpResult("Diameter Standard Deviation",
                        standardDeviation(diameters), RealMap.class);
        OpResult min =
                new OpResult("Diameter Min", min(diameters), RealMap.class);
        OpResult max =
                new OpResult("Diameter Max", max(diameters), RealMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Diameter";
    }

}
