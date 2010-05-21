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
package org.ebsdimage.core.exp.ops.indexing.op;

import java.io.IOException;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Indexing;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;

import crystallography.core.*;

/**
 * Operation to perform indexing based on the algorithm described in Krieger
 * Lassen (1994).
 * 
 * @author Philippe T. Pinard
 * 
 */
public class KriegerLassen1994 extends IndexingOp {

    /** Maximum index of the planes to compute. */
    public final int maxIndex;

    /** Default maximum index. */
    public static final int DEFAULT_MAX_INDEX = 6;

    /** Type of scattering factor. */
    public final ScatteringFactorsEnum scatterType;

    /** Default type of scattering factor. */
    public static final ScatteringFactorsEnum DEFAULT_SCATTER_TYPE =
            ScatteringFactorsEnum.XRAY;

    /** Reflectors for all the defined phases. */
    private Reflectors[] reflsArray;

    /** Calibration for the indexing. */
    private Camera calibration;



    /**
     * Creates a new indexing operation from the algorithm described in Krieger
     * Lassen (1994) with the default maximum index and scattering factor type.
     */
    public KriegerLassen1994() {
        this.maxIndex = DEFAULT_MAX_INDEX;
        this.scatterType = DEFAULT_SCATTER_TYPE;
    }



    /**
     * Creates a new indexing operation from the algorithm described in Krieger
     * Lassen (1994).
     * 
     * @param maxIndex
     *            maximum index of the planes to compute
     * @param scatterType
     *            type of scattering factor
     */
    public KriegerLassen1994(int maxIndex, ScatteringFactorsEnum scatterType) {
        if (maxIndex < 1)
            throw new IllegalArgumentException(
                    "The maximum index has to greater or equal to 1.");

        this.maxIndex = maxIndex;
        this.scatterType = scatterType;
    }



    /**
     * Performs indexing of the Hough peaks using the algorithm described in
     * Krieger Lassen(1994). At least 3 Hough peaks are required for the
     * indexing. If this condition is not met, an empty array of solution is
     * returned.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcPeaks
     *            Hough peaks
     * @return solutions
     */
    @Override
    public Solution[] index(Exp exp, HoughPeak[] srcPeaks) {
        if (srcPeaks.length < 3)
            return new Solution[0];
        else
            return Indexing.index(reflsArray, srcPeaks, calibration);
    }



    @Override
    public void setUp(Exp exp) throws IOException {
        super.setUp(exp);

        // Camera
        calibration = exp.mmap.calibration;

        // Scattering factors
        ScatteringFactors scatter = null;
        switch (scatterType) {
        case ELECTRON:
            scatter = new ElectronScatteringFactors();
            break;
        case XRAY:
            scatter = new XrayScatteringFactors();
            break;
        }

        if (scatter == null)
            throw new IOException("The scattering factor type is unknown.");

        // Reflectors
        Crystal[] phases = exp.mmap.getPhases();
        reflsArray = new Reflectors[phases.length];

        for (int i = 0; i < reflsArray.length; i++)
            reflsArray[i] = new Reflectors(phases[i], scatter, maxIndex);
    }



    @Override
    public String toString() {
        return "Krieger Lassen (1994) [maxIndex=" + maxIndex + ", scatterType="
                + scatterType + "]";
    }

}
