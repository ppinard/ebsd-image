package org.ebsdimage.core.exp.ops.hough.results;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;

/**
 * Result operation that evaluates the entropy of the pixels in the Hough map.
 * 
 * @author ppinard
 */
public class Entropy extends HoughResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, HoughMap srcMap) {
        OpResult result =
                new OpResult(getName(), Analysis.entropy(srcMap), RealMap.class);

        return new OpResult[] { result };
    }
}
