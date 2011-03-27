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
package org.ebsdimage.core.exp.ops.detection.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.*;
import rmlimage.module.real.core.RealMap;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.average;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.max;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.min;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.standardDeviation;

/**
 * Result operation that evaluate the diameter of the greatest inscribed circle
 * inside the detected peaks.
 * 
 * @author Philippe T. Pinard
 */
public class Diameter extends DetectionResultsOps {

    /** Default operation. */
    public static final Diameter DEFAULT = new Diameter();



    @Override
    public OpResult[] calculate(Exp exp, BinMap peaksMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // Cannot keep peaksMap calibration (rad - px)
        identMap.setCalibration(Calibration.NONE);

        // ========= Calculate diameter ===========

        rmlimage.core.Feret result = Analysis.getFeret(identMap);
        double[] diameters = result.max;
        String units = result.units[rmlimage.core.Feret.MAX];

        // ========= Calculate results ===========

        OpResult average =
                new OpResult("Diameter Average", average(diameters), units,
                        RealMap.class);
        OpResult stddev =
                new OpResult("Diameter Standard Deviation",
                        standardDeviation(diameters), units, RealMap.class);
        OpResult min =
                new OpResult("Diameter Min", min(diameters), units,
                        RealMap.class);
        OpResult max =
                new OpResult("Diameter Max", max(diameters), units,
                        RealMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Diameter";
    }

}
