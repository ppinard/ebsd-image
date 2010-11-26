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
package org.ebsdimage.core.exp.ops.identification.post;

import static java.lang.Math.abs;
import static java.util.Arrays.sort;
import static ptpshared.utility.Arrays.reverse;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlshared.util.ArrayList;

/**
 * Clean-up peaks that appears twice in the list of Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public class DoublePeaksCleanUp extends IdentificationPostOps {

    /** Default operation. */
    public static final DoublePeaksCleanUp DEFAULT = new DoublePeaksCleanUp(2,
            2);

    /** Minimum number of pixels between two peaks in rho. */
    @Attribute(name = "deltaRho")
    public final int deltaRho;

    /** Minimum number of pixels between two peaks in theta. */
    @Attribute(name = "deltaTheta")
    public final int deltaTheta;



    /**
     * Creates a new <code>DoublePeaksCleanUp</code> with the specified
     * parameters.
     * 
     * @param spacingRho
     *            minimum number of pixels between two peaks in rho
     * @param spacingTheta
     *            minimum number of pixels between two peaks in theta
     * @throws IllegalArgumentException
     *             if the spacing in rho and theta cannot be less than 1
     */
    public DoublePeaksCleanUp(@Attribute(name = "deltaRho") int spacingRho,
            @Attribute(name = "deltaTheta") int spacingTheta) {
        if (spacingRho < 1)
            throw new IllegalArgumentException(
                    "Spacing in rho cannot be less than 1.");
        if (spacingTheta < 1)
            throw new IllegalArgumentException(
                    "Spacing in theta cannot be less than 1.");

        this.deltaRho = spacingRho;
        this.deltaTheta = spacingTheta;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        DoublePeaksCleanUp other = (DoublePeaksCleanUp) obj;
        if (deltaRho != other.deltaRho)
            return false;
        if (deltaTheta != other.deltaTheta)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (!super.equals(obj, precision))
            return false;

        DoublePeaksCleanUp other = (DoublePeaksCleanUp) obj;
        if (Math.abs(deltaRho - other.deltaRho) >= precision)
            return false;
        if (Math.abs(deltaTheta - other.deltaTheta) >= precision)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + deltaRho;
        result = prime * result + deltaTheta;
        return result;
    }



    /**
     * Returns a list of <code>HoughPeak</code> without equivalent peaks based
     * on the specified spacings. The resolutions in rho and theta of the
     * <code>HoughMap</code> in the experiment is used to calculate the pixel
     * size.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcPeaks
     *            source <code>HoughPeak</code>
     * @return list of <code>HoughPeak</code> without duplicates
     */
    @Override
    public HoughPeak[] process(Exp exp, HoughPeak[] srcPeaks) {
        double deltaR = exp.getCurrentHoughMap().deltaR;
        double deltaTheta = exp.getCurrentHoughMap().deltaTheta;

        return process(srcPeaks, deltaR, deltaTheta);
    }



    /**
     * Returns a list of <code>HoughPeak</code> without equivalent peaks based
     * on the specified spacings and resolutions in rho and theta.
     * 
     * @param srcPeaks
     *            source <code>HoughPeak</code>
     * @param deltaR
     *            resolution in rho
     * @param deltaTheta
     *            resolution in theta
     * @return list of <code>HoughPeak</code> without duplicates
     */
    protected HoughPeak[] process(HoughPeak[] srcPeaks, double deltaR,
            double deltaTheta) {
        ArrayList<HoughPeak> destPeaks = new ArrayList<HoughPeak>();

        HoughPeak peak0;
        HoughPeak peak1;
        boolean same;

        sort(srcPeaks, new HoughPeakIntensityComparator());
        reverse(srcPeaks);

        for (int i = 0; i < srcPeaks.length; i++) {
            peak0 = srcPeaks[i];
            same = false;

            for (int j = i + 1; j < srcPeaks.length; j++) {
                peak1 = srcPeaks[j];

                if (equivalent(peak0, peak1, deltaR, deltaTheta))
                    same = true;
            }

            if (!same)
                destPeaks.add(peak0);
        }

        return destPeaks.toArray(new HoughPeak[0]);
    }



    /**
     * Returns if two peaks are equivalent based on the specified spacing and
     * the resolutions in rho and theta.
     * 
     * @param peak0
     *            first peak
     * @param peak1
     *            second peak
     * @param deltaR
     *            resolution in rho
     * @param deltaTheta
     *            resolution in theta
     * @return <code>true</code> if the two peaks are equivalent,
     *         <code>false</code> otherwise
     */
    private boolean equivalent(HoughPeak peak0, HoughPeak peak1, double deltaR,
            double deltaTheta) {
        return abs(peak1.rho - peak0.rho) < deltaR * deltaRho
                && abs(peak1.theta - peak0.theta) < deltaTheta * deltaTheta;
    }



    @Override
    public String toString() {
        return "DoublePeaksCleanUp [deltaRho=" + deltaRho + ", deltaTheta="
                + deltaTheta + "]";
    }
}
