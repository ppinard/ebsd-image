package org.ebsdimage.core.exp.ops.hough.results;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.MapStats;
import rmlimage.module.real.core.RealMap;

/**
 * Result operation which calculates the total intensity of the Hough map.
 * 
 * @author ppinard
 */
public class Sum extends HoughResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, HoughMap srcMap) {
        OpResult result =
                new OpResult("Hough Sum", MapStats.sum(srcMap), RealMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Sum";
    }
}
