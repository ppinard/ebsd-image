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
import org.ebsdimage.core.run.Run;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import crystallography.core.Crystal;
import crystallography.core.Reflectors;
import crystallography.core.ReflectorsFactory;
import crystallography.core.ScatteringFactorsEnum;

/**
 * Operation to perform indexing based on the algorithm described in Krieger
 * Lassen (1994).
 * 
 * @author Philippe T. Pinard
 */
public class KriegerLassen1994 extends IndexingOp {

    /** Default operation. */
    public static final KriegerLassen1994 DEFAULT = new KriegerLassen1994(4,
            ScatteringFactorsEnum.XRAY);

    /** Maximum index of the planes to compute. */
    @Attribute(name = "maxIndex")
    public final int maxIndex;

    /** Type of scattering factor. */
    @Element(name = "scatterType")
    public final ScatteringFactorsEnum scatterType;

    /** Reflectors for all the defined phases. */
    private Reflectors[] reflsArray;

    /** Calibration for the indexing. */
    private Camera camera;



    /**
     * Creates a new indexing operation from the algorithm described in Krieger
     * Lassen (1994).
     * 
     * @param maxIndex
     *            maximum index of the planes to compute
     * @param scatterType
     *            type of scattering factor
     */
    public KriegerLassen1994(@Attribute(name = "maxIndex") int maxIndex,
            @Element(name = "scatterType") ScatteringFactorsEnum scatterType) {
        if (maxIndex < 1)
            throw new IllegalArgumentException(
                    "The maximum index has to greater or equal to 1.");

        this.maxIndex = maxIndex;
        this.scatterType = scatterType;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + maxIndex;
        result =
                prime * result
                        + ((scatterType == null) ? 0 : scatterType.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        KriegerLassen1994 other = (KriegerLassen1994) obj;
        if (maxIndex != other.maxIndex)
            return false;
        if (scatterType != other.scatterType)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        KriegerLassen1994 other = (KriegerLassen1994) obj;
        if (Math.abs(maxIndex - other.maxIndex) > delta)
            return false;
        if (scatterType != other.scatterType)
            return false;

        return true;
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
            return Indexing.index(reflsArray, srcPeaks, camera);
    }



    @Override
    public void setUp(Run run) throws IOException {
        super.setUp(run);

        Exp exp = (Exp) run;

        // Camera
        camera = exp.getMetadata().camera;

        // Reflectors
        Crystal[] phases = exp.mmap.getPhases();
        reflsArray = new Reflectors[phases.length];

        for (int i = 0; i < reflsArray.length; i++)
            reflsArray[i] =
                    ReflectorsFactory.generate(phases[i], scatterType, maxIndex);
    }



    @Override
    public String toString() {
        return "Krieger Lassen (1994) [maxIndex=" + maxIndex + ", scatterType="
                + scatterType + "]";
    }

}
