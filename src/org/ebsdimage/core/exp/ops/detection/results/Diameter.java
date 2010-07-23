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
 * @author ppinard
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
