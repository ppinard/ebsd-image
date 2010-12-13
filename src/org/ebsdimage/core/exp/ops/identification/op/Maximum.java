package org.ebsdimage.core.exp.ops.identification.op;

import magnitude.core.Magnitude;

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
    public String toString() {
        return "Maximum";
    }



    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // Calculate maximum location
        Centroid maximums = Analysis.getMaximumLocation(identMap, houghMap);

        // Create Peak objects with intensity at maximum
        HoughPeak[] peaks = new HoughPeak[maximums.getValueCount()];

        double intensity;
        Magnitude theta = new Magnitude(0, maximums.units[Centroid.X]);
        Magnitude rho = new Magnitude(0, maximums.units[Centroid.Y]);
        for (int i = 0; i < maximums.getValueCount(); i++) {
            theta = new Magnitude(maximums.x[i], theta);
            rho = new Magnitude(maximums.y[i], rho);
            intensity = maximums.intensity[i];

            peaks[i] = new HoughPeak(theta, rho, intensity);
        }

        return peaks;
    }

}
