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

        OpResult average =
                new OpResult("Area Average", average(areas), RealMap.class);
        OpResult stddev =
                new OpResult("Area Standard Deviation",
                        standardDeviation(areas), RealMap.class);
        OpResult min = new OpResult("Area Min", min(areas), RealMap.class);
        OpResult max = new OpResult("Area Max", max(areas), RealMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Area";
    }

}
