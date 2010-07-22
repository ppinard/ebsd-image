package org.ebsdimage.core.exp.ops.detection.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.*;
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

        OpResult average =
                new OpResult("MinMax Average", Stats.average(diameters),
                        RealMap.class);
        OpResult stddev =
                new OpResult("MinMax Standard Deviation",
                        Stats.standardDeviation(diameters), RealMap.class);
        OpResult min =
                new OpResult("MinMax Min", (byte) Stats.min(diameters),
                        ByteMap.class);
        OpResult max =
                new OpResult("MinMax Max", (byte) Stats.max(diameters),
                        ByteMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Diameter";
    }

}
