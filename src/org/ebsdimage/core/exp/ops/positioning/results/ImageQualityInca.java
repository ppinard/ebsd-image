/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package org.ebsdimage.core.exp.ops.positioning.results;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;
import static ptpshared.util.Arrays.reverse;
import static java.util.Arrays.sort;

/**
 * Operation to calculate the image quality index (INCA).
 * 
 * @author Philippe T. Pinard
 */
public class ImageQualityInca extends PositioningResultsOps {

    /** Default operation. */
    public static final ImageQualityInca DEFAULT = new ImageQualityInca();



    /**
     * Calculates the image quality index (INCA) of the Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaks
     *            Hough peaks
     * @return one entry with the image quality quality (INCA) index
     * @see QualityIndex#imageQualityInca(HoughPeak[])
     */
    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] peaks) {
        OpResult result =
                new OpResult(getName(), calculate(peaks), RealMap.class);

        return new OpResult[] { result };
    }



    /**
     * Returns the image quality (as calculated by Oxford INCA Crystal).
     * 
     * @param peaks
     *            <code>HoughPeaks</code>
     * @return image quality index
     */
    public double calculate(HoughPeak[] peaks) {
        if (peaks.length < 2)
            return 0.0;

        sort(peaks, new HoughPeakIntensityComparator());
        reverse(peaks);

        double minimum;
        if (peaks.length >= 7)
            minimum = peaks[6].intensity;
        else
            minimum = peaks[peaks.length - 1].intensity;

        double maximum = peaks[0].intensity;

        return maximum - minimum;
    }



    @Override
    public String toString() {
        return "Image Quality (INCA)";
    }
}
