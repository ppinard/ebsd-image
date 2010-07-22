package org.ebsdimage.core.exp.ops.detection.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.*;
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

        OpResult average =
                new OpResult("MinMax Average", Stats.average(areas),
                        RealMap.class);
        OpResult stddev =
                new OpResult("MinMax Standard Deviation",
                        Stats.standardDeviation(areas), RealMap.class);
        OpResult min =
                new OpResult("MinMax Min", (byte) Stats.min(areas),
                        ByteMap.class);
        OpResult max =
                new OpResult("MinMax Max", (byte) Stats.max(areas),
                        ByteMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Area";
    }

}
