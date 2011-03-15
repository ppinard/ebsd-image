package org.ebsdimage.core.exp.ops.identification.op;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.Centroid;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;

/**
 * Operation to find the position of the Hough peaks using the position of the
 * maximum from each detected peak in the <code>BinMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Maximum extends IdentificationOp {

    /** Default operation. */
    public static final Maximum DEFAULT = new Maximum();



    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        Centroid maximums = Analysis.getMaximumLocation(identMap, houghMap);

        return maximums.toHoughPeakArray();
    }



    @Override
    public String toString() {
        return "Maximum";
    }

}
