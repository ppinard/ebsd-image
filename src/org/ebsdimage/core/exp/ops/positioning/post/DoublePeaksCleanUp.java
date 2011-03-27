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
package org.ebsdimage.core.exp.ops.positioning.post;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.Calibration;
import rmlshared.util.ArrayList;
import static java.util.Arrays.sort;

/**
 * Clean-up peaks that appears twice in the list of Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public class DoublePeaksCleanUp extends PositioningPostOps {

    /** Default operation. */
    public static final DoublePeaksCleanUp DEFAULT = new DoublePeaksCleanUp(2,
            2);

    /** Minimum number of pixels between two peaks in rho. */
    @Attribute(name = "spacingRho")
    public final int spacingRho;

    /** Minimum number of pixels between two peaks in theta. */
    @Attribute(name = "spacingTheta")
    public final int spacingTheta;



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
    public DoublePeaksCleanUp(@Attribute(name = "spacingRho") int spacingRho,
            @Attribute(name = "spacingTheta") int spacingTheta) {
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
        if (!super.equals(obj))
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
        Calibration cal = exp.getCurrentHoughMap().getCalibration();
        return process(srcPeaks, cal.dx, cal.dy);
    }



    /**
     * Returns a list of <code>HoughPeak</code> without equivalent peaks based
     * on the specified spacings and resolutions in rho and theta.
     * 
     * @param srcPeaks
     *            source <code>HoughPeak</code>
     * @param deltaRho
     *            resolution in rho
     * @param deltaTheta
     *            resolution in theta
     * @return list of <code>HoughPeak</code> without duplicates
     */
    protected HoughPeak[] process(HoughPeak[] srcPeaks, double deltaTheta,
            double deltaRho) {
        ArrayList<HoughPeak> destPeaks = new ArrayList<HoughPeak>();

        sort(srcPeaks, new HoughPeakIntensityComparator());

        double precisionTheta = deltaTheta * spacingTheta;
        double precisionRho = deltaRho * spacingRho;

        HoughPeak peak0;
        HoughPeak peak1;
        boolean same;
        for (int i = 0; i < srcPeaks.length; i++) {
            peak0 = srcPeaks[i];
            same = false;

            for (int j = i + 1; j < srcPeaks.length; j++) {
                peak1 = srcPeaks[j];

                if (peak0.equivalent(peak1, precisionTheta, precisionRho))
                    same = true;
            }

            if (!same)
                destPeaks.add(peak0);
        }

        return destPeaks.toArray(new HoughPeak[0]);
    }



    @Override
    public String toString() {
        return "DoublePeaksCleanUp [spacingRho=" + spacingRho
                + ", spacingTheta=" + spacingTheta + "]";
    }
}
