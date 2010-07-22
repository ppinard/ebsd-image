package org.ebsdimage.core.exp.ops.pattern.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.ByteMap;
import rmlimage.core.MapStats;
import rmlimage.module.real.core.RealMap;

/**
 * Operation to calculate the signal to noise ratio of the pattern.
 * 
 * @author ppinard
 */
public class SNR extends PatternResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, ByteMap srcMap) {
        OpResult result =
                new OpResult("Pattern SNR", MapStats.snr(srcMap), RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "SNR";
    }

}
