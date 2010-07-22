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
 * Result operation that evaluate the area over peaks.
 * 
 * @author ppinard
 */
public class Area extends DetectionResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, BinMap peaksMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // ========= Calculate area ===========

        float[] areas = Analysis.getArea(identMap).val;

        // ========= Calculate results ===========

        OpResult average;
        OpResult stddev;
        OpResult min;
        OpResult max;

        if (areas.length > 1) {
            average =
                    new OpResult("Area Average", Stats.average(areas),
                            RealMap.class);
            stddev =
                    new OpResult("Area Standard Deviation",
                            Stats.standardDeviation(areas), RealMap.class);
            min = new OpResult("Area Min", Stats.min(areas), RealMap.class);
            max = new OpResult("Area Max", Stats.max(areas), RealMap.class);
        } else {
            average = new OpResult("Area Average", Float.NaN, RealMap.class);
            stddev =
                    new OpResult("Area Standard Deviation", Float.NaN,
                            RealMap.class);
            min = new OpResult("Area Min", Float.NaN, RealMap.class);
            max = new OpResult("Area Max", Float.NaN, RealMap.class);
        }

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Area";
    }

}
