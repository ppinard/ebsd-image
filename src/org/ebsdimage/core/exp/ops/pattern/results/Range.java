package org.ebsdimage.core.exp.ops.pattern.results;

import org.ebsdimage.core.MapStats;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.ByteMap;

/**
 * Result operation that calculates the intensity range in the diffraction
 * pattern.
 * 
 * @author ppinard
 */
public class Range extends PatternResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, ByteMap srcMap) {
        rmlshared.util.Range<Integer> range = MapStats.range(srcMap);
        byte value = (byte) (range.max - range.min);

        OpResult result = new OpResult("Pattern Range", value, ByteMap.class);

        return new OpResult[] { result };
    }



    @Override
    public String toString() {
        return "Range";
    }
}
