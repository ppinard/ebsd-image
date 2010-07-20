package org.ebsdimage.core.exp.ops.hough.results;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;

/**
 * Result operation that evaluates the average of the pixels in the Hough map.
 * 
 * @author ppinard
 */
public class Average extends HoughResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, HoughMap srcMap) {
        OpResult result =
                new OpResult("Hough Average", Analysis.average(srcMap),
                        RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Average";
    }
}
