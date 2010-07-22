package org.ebsdimage.core.exp.ops.detection.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.Analysis;
import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.core.Stats;

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

        OpResult average;
        OpResult stddev;
        OpResult min;
        OpResult max;

        if (diameters.length > 1) {
            average =
                    new OpResult("Diameter Average", Stats.average(diameters),
                            RealMap.class);
            stddev =
                    new OpResult("Diameter Standard Deviation",
                            Stats.standardDeviation(diameters), RealMap.class);
            min =
                    new OpResult("Diameter Min", Stats.min(diameters),
                            RealMap.class);
            max =
                    new OpResult("Diameter Max", Stats.max(diameters),
                            RealMap.class);
        } else {
            average =
                    new OpResult("Diameter Average", Float.NaN, RealMap.class);
            stddev =
                    new OpResult("Diameter Standard Deviation", Float.NaN,
                            RealMap.class);
            min = new OpResult("Diameter Min", Float.NaN, RealMap.class);
            max = new OpResult("Diameter Max", Float.NaN, RealMap.class);
        }

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Diameter";
    }

}
