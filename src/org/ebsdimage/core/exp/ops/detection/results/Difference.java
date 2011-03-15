/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.average;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.max;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.min;
import static org.ebsdimage.core.exp.ops.detection.results.ResultsHelper.standardDeviation;

/**
 * Result operation that evaluate the minimum and maximum value inside each
 * peak.
 * 
 * @author Philippe T. Pinard
 */
public class Difference extends DetectionResultsOps {

    /** Default operation. */
    public static final Difference DEFAULT = new Difference();



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
        // Apply houghMap properties on binMap
        peaksMap.setProperties(houghMap);

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

        double theta;
        double rho;
        int value;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                objectNumber = pixArray[n];

                theta = HoughMap.getTheta(peaksMap, n);
                rho = HoughMap.getRho(peaksMap, n);
                value = houghMap.pixArray[houghMap.getIndex(theta, rho)] & 0xff;

                if (value < minValue[objectNumber])
                    minValue[objectNumber] = value;
                if (value > maxValue[objectNumber])
                    maxValue[objectNumber] = value;

                n++;
            }
        }

        // ========= Calculate difference ===========

        double diff[] = new double[nbObjects]; // Ignore background

        for (int i = 1; i <= nbObjects; i++)
            diff[i - 1] = maxValue[i] - minValue[i];

        // ========= Calculate results ===========

        OpResult average =
                new OpResult("Difference Average", average(diff), RealMap.class);
        OpResult stddev =
                new OpResult("Difference Standard Deviation",
                        standardDeviation(diff), RealMap.class);
        OpResult min =
                new OpResult("Difference Min", (byte) min(diff), ByteMap.class);
        OpResult max =
                new OpResult("Difference Max", (byte) max(diff), ByteMap.class);

        return new OpResult[] { average, stddev, min, max };
    }



    @Override
    public OpResult[] calculate(Exp exp, BinMap peaksMap) {
        return calculate(peaksMap, exp.getSourceHoughMap());
    }



    @Override
    public String toString() {
        return "Difference";
    }

}
