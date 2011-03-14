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
package org.ebsdimage.core.old;

import java.util.ArrayList;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;

import ptpshared.math.old.Vector3D;
import static ptpshared.util.Arrays.reverse;
import static java.util.Arrays.sort;

/**
 * Store many <code>HoughPeakPairs</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeakPairs extends Pairs<HoughPeakPair> {

    /**
     * Creates a new <code>HoughPeakPairs</code> from an array of Hough peaks
     * and the camera's calibration. The normal of the Hough peaks is
     * automatically calculated based on the coordinates frame of this module.
     * 
     * @param peaks
     *            Hough peaks
     * @param calibration
     *            calibration
     * @throws NullPointerException
     *             if the Hough peaks are null
     * @throws NullPointerException
     *             if the camera is null
     */
    public HoughPeakPairs(HoughPeak[] peaks, Camera calibration) {
        if (peaks == null)
            throw new NullPointerException("Hough peaks cannot be null.");
        if (calibration == null)
            throw new NullPointerException("Camera cannot be null.");

        ArrayList<HoughPeakPair> tmpPairs = new ArrayList<HoughPeakPair>();

        sort(peaks, new HoughPeakIntensityComparator());
        reverse(peaks);

        Vector3D cameraPos =
                new Vector3D(calibration.patternCenterH,
                        -calibration.detectorDistance,
                        calibration.patternCenterV);

        // TODO: Fix when calibration is fully implemented
        // for (int i = 0; i < peaks.length; i++) {
        // for (int j = i + 1; j < peaks.length; j++) {
        // HoughPeak peak0 = peaks[i];
        // Vector3D normal0 =
        // HoughMath.houghSpaceToVectorNormal(peak0, cameraPos);
        //
        // HoughPeak peak1 = peaks[j];
        // Vector3D normal1 =
        // HoughMath.houghSpaceToVectorNormal(peak1, cameraPos);
        //
        // tmpPairs.add(new HoughPeakPair(peak0, normal0, peak1, normal1));
        // }
        // }

        // Initialize pairs array
        pairs = new HoughPeakPair[tmpPairs.size()];
        System.arraycopy(tmpPairs.toArray(), 0, pairs, 0, tmpPairs.size());
    }



    @Override
    public HoughPeakPair[] findClosestMatches(double directionCosine,
            double precision) {
        ArrayList<HoughPeakPair> matches =
                findClosestMatchesArrayList(directionCosine, precision);
        return matches.toArray(new HoughPeakPair[matches.size()]);
    }

}
