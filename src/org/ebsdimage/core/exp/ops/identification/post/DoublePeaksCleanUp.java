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

import rmlshared.util.ArrayList;

/**
 * Clean-up peaks that appears twice in the list of Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public class DoublePeaksCleanUp extends IdentificationPostOps {

    /** Minimum number of pixels between two peaks in rho. */
    public final int spacingRho;

    /** Default value of the minimum number of pixels between two peaks in rho. */
    public static final int DEFAULT_SPACING_RHO = 2;

    /**
     * Default value of the minimum number of pixels between two peaks in theta.
     */
    public static final int DEFAULT_SPACING_THETA = 2;

    /** Minimum number of pixels between two peaks in theta. */
    public final int spacingTheta;



    /**
     * Creates a new <code>DoublePeaksCleanUp</code> with the default
     * parameters.
     */
    public DoublePeaksCleanUp() {
        this(DEFAULT_SPACING_RHO, DEFAULT_SPACING_THETA);
    }



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
    public DoublePeaksCleanUp(int spacingRho, int spacingTheta) {
        if (spacingRho < 1)
            throw new IllegalArgumentException(
                    "Spacing in rho cannot be less than 1.");
        if (spacingTheta < 1)
            throw new IllegalArgumentException(
                    "Spacing in theta cannot be less than 1.");

        this.spacingRho = spacingRho;
        this.spacingTheta = spacingTheta;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        DoublePeaksCleanUp other = (DoublePeaksCleanUp) obj;
        if (spacingRho != other.spacingRho)
            return false;
        if (spacingTheta != other.spacingTheta)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + spacingRho;
        result = prime * result + spacingTheta;
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
        return abs(peak1.rho - peak0.rho) < deltaR * spacingRho
                && abs(peak1.theta - peak0.theta) < deltaTheta * spacingTheta;
    }



    @Override
    public String toString() {
        return "DoublePeaksCleanUp [spacingRho=" + spacingRho
                + ", spacingTheta=" + spacingTheta + "]";
    }
}
