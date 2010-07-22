package org.ebsdimage.core.exp.ops.detection.results;

import java.util.Arrays;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.module.real.core.RealMap;
import rmlshared.math.Stats;

/**
 * Result operation that evaluate the minimum and maximum value inside each
 * peak.
 * 
 * @author ppinard
 */
public class Difference extends DetectionResultsOps {

    @Override
    public OpResult[] calculate(Exp exp, BinMap peaksMap) {
        return calculate(peaksMap, exp.getSourceHoughMap());
    }



    /**
     * Calculate the difference between the minimum and maximum value of each
     * detected peak.
     * 
     * @param peaksMap
     *            detected peaks
     * @param houghMap
     *            original Hough map
     * @return average, standard deviation, minimum and maximum of the
     *         differences
     */
    protected OpResult[] calculate(BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // ========= Find min and max of each peak ===========

        int nbObjects = identMap.getObjectCount();

        // +1 for object 0 (background)
        int[] minValue = new int[nbObjects + 1];
        int[] maxValue = new int[nbObjects + 1];

        // Initialize the array to 0
        Arrays.fill(minValue, 255);
        Arrays.fill(maxValue, 0);

        short objectNumber;

        int n = 0;
        int width = identMap.width;
        int height = identMap.height;
        short[] pixArray = identMap.pixArray;

        int value;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                value = houghMap.pixArray[n] & 0xff;

                if (value < minValue[objectNumber])
                    minValue[objectNumber] = value;
                if (value > maxValue[objectNumber])
                    maxValue[objectNumber] = value;

                n++;
            }
        }

        // ========= Calculate difference ===========

        int diff[] = new int[nbObjects]; // Ignore background

        for (int i = 1; i <= nbObjects; i++)
            diff[i - 1] = maxValue[i] - minValue[i];

        // ========= Calculate results ===========

        OpResult average =
                new OpResult("Difference Average", Stats.average(diff),
                        RealMap.class);
        OpResult stddev =
                new OpResult("Difference Standard Deviation",
                        Stats.standardDeviation(diff), RealMap.class);
        OpResult min =
                new OpResult("Difference Min", (byte) Stats.min(diff),
                        ByteMap.class);
        OpResult max =
                new OpResult("Difference Max", (byte) Stats.max(diff),
                        ByteMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public String toString() {
        return "Difference";
    }

}
