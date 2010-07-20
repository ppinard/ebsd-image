package org.ebsdimage.core.exp.ops.identification.results;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.QualityIndex;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;

/**
 * Result operation to calculate Oxford's INCA pattern quality index.
 * 
 * @author ppinard
 */
public class PatternQuality extends IdentificationResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks) {
        OpResult result =
                new OpResult(getName(), QualityIndex.patternQuality(srcPeaks,
                        exp.getSourceHoughMap()), RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Pattern Quality";
    }
}
