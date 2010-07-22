package org.ebsdimage.core.exp.ops.identification.op;

import java.util.Arrays;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
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
        // Apply houghMap properties on binMap
        peaksMap.setProperties(houghMap);

        IdentMap identMap = Identification.identify(peaksMap);

        int nbObjects = identMap.getObjectCount();

        // +1 for object 0 (background)
        double[] massRho = new double[nbObjects + 1];
        double[] massTheta = new double[nbObjects + 1];
        double[] area = new double[nbObjects + 1];

        // Initialize the array to 0
        Arrays.fill(massRho, 0);
        Arrays.fill(massTheta, 0);
        Arrays.fill(area, 0);

        short objectNumber;

        int n = 0;
        int width = identMap.width;
        int height = identMap.height;
        double rho;
        double theta;
        double value;

        short[] pixArray = identMap.pixArray;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                rho = Analysis.getR(peaksMap, n);
                theta = Analysis.getTheta(peaksMap, n);

                value = houghMap.pixArray[houghMap.getIndex(rho, theta)] & 0xff;

                massRho[objectNumber] += rho * value;
                massTheta[objectNumber] += theta * value;
                area[objectNumber] += value;

                n++;
            }
        }

        // Create Hough peaks
        HoughPeak[] destPeaks = new HoughPeak[nbObjects];

        double intensity;
        int index;
        for (n = 0; n < nbObjects; n++) {
            rho = massRho[n + 1] / area[n + 1];
            theta = massTheta[n + 1] / area[n + 1];

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
