package org.ebsdimage.core.exp.ops.detection.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.BinMap;
import rmlimage.core.Identification;
import rmlimage.module.real.core.RealMap;

/**
 * Result operation that calculates the number of detected peaks.
 * 
 * @author ppinard
 */
public class Count extends DetectionResultsOps {

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
