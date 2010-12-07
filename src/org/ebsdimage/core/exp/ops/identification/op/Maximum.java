package org.ebsdimage.core.exp.ops.identification.op;

import magnitude.core.Magnitude;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPoint;
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
    public String toString() {
        return "Maximum";
    }



    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // Calculate maximum location
        HoughPoint maximums = Analysis.getMaximumLocation(houghMap, identMap);

        // Create Peak objects with intensity at maximum
        HoughPeak[] peaks = new HoughPeak[identMap.getObjectCount()];

        Magnitude rho, theta;
        double intensity;
        int index;
        for (int i = 0; i < identMap.getObjectCount(); i++) {
            rho = (Magnitude) maximums.getRho(i);
            theta = (Magnitude) maximums.getTheta(i);

            index = houghMap.getIndex(rho, theta);
            intensity = houghMap.pixArray[index] & 0xff;

            peaks[i] = new HoughPeak(rho, theta, intensity);
        }

        return peaks;
    }

}
