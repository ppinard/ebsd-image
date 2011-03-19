package org.ebsdimage.core.exp.ops.indexing.op;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;

/**
 * Temporary null indexing operation.
 * 
 * @author ppinard
 */
public class IndexingOpNull extends IndexingOp {

    @Override
    public Solution[] index(Exp exp, HoughPeak[] srcPeaks) {
        return new Solution[0];
    }

}
