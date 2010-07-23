package org.ebsdimage.core.exp.ops.identification.op;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPoint;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;

/**
 * Operation to find the position of the Hough peaks using the center of mass of
 * the detected peaks in the <code>BinMap</code>.
 * 
 * @author ppinard
 */
public class CenterOfMass extends IdentificationOp {

    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // Center of mass
        HoughPoint peaks = Analysis.getCenterOfMass(houghMap, identMap);
        int size = peaks.getValueCount();

        // Create Hough peaks with intensity at center of mass
        HoughPeak[] destPeaks = new HoughPeak[size];

        double rho, theta, intensity;
        int index;
        for (int n = 0; n < size; n++) {
            rho = peaks.rho[n];
            theta = peaks.theta[n];

            index = houghMap.getIndex(rho, theta);
            intensity = houghMap.pixArray[index] & 0xff;

            destPeaks[n] = new HoughPeak(rho, theta, intensity);
        }

        return destPeaks;
    }



    @Override
    public String toString() {
        return "Center of Mass";
    }
}
