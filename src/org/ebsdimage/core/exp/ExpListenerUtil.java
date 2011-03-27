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
package org.ebsdimage.core.exp;

import java.util.Arrays;

import org.ebsdimage.core.*;

import rmlimage.core.ByteMap;
import rmlimage.core.MapMath;

/**
 * Utilities used by the experiment listeners.
 * 
 * @author Philippe T. Pinard
 */
public class ExpListenerUtil {

    /**
     * Draw a cross at the position of the Hough peak.
     * 
     * @param map
     *            map to draw in
     * @param index
     *            index of the position of the Hough peak
     * @param value
     *            value of the cross
     */
    private static void drawHoughPeakPosition(HoughMap map, int index,
            byte value) {
        if ((index - 1) >= 0)
            map.pixArray[index - 1] = value;

        map.pixArray[index] = value;

        if ((index + 1) < map.size)
            map.pixArray[index + 1] = value;

        if ((index - map.width) >= 0)
            map.pixArray[index - map.width] = value;

        if ((index + map.width) < map.size)
            map.pixArray[index + map.width] = value;
    }



    /**
     * Draws the position of the Hough peaks overlaid on top of the Hough map.
     * 
     * @param houghMap
     *            hough map
     * @param peaks
     *            identified Hough peaks
     * @return <code>HoughMap</code> with overlay
     */
    public static HoughMap drawHoughPeaksOverlay(HoughMap houghMap,
            HoughPeak[] peaks) {
        MapMath.subtraction(houghMap, -3, houghMap);

        houghMap.getLUT().setLUT(255, 255, 0, 0);
        houghMap.getLUT().setLUT(254, 0, 255, 0);
        houghMap.getLUT().setLUT(253, 0, 0, 255);

        Arrays.sort(peaks, new HoughPeakIntensityComparator());

        int index;
        for (int i = 0; i < peaks.length / 3; i++) {
            index = houghMap.getIndex(peaks[i].rho, peaks[i].theta);
            drawHoughPeakPosition(houghMap, index, (byte) 253);
        }
        for (int i = peaks.length / 3; i < 2 * peaks.length / 3; i++) {
            index = houghMap.getIndex(peaks[i].rho, peaks[i].theta);
            drawHoughPeakPosition(houghMap, index, (byte) 254);
        }

        for (int i = 2 * peaks.length / 3; i < peaks.length; i++) {
            index = houghMap.getIndex(peaks[i].rho, peaks[i].theta);
            drawHoughPeakPosition(houghMap, index, (byte) 255);
        }

        return houghMap;
    }



    /**
     * Draws a solution on top of the pattern map.
     * 
     * @param patternMap
     *            pattern map on which the overlay is applied
     * @param camera
     *            camera calibration used to get the solution
     * @param beamEnergy
     *            beam energy in eV
     * @param sln
     *            solution
     */
    public static void drawSolutionOverlay(ByteMap patternMap, Camera camera,
            double beamEnergy, Solution sln) {
        // FIXME: Change with new simulation pattern operation
        // // Get source pattern map
        // int width = patternMap.width;
        // int height = patternMap.height;
        //
        // // Draw simulation pattern
        // PatternBandCenter pattern =
        // new PatternBandCenter(width, height, 2,
        // ScatteringFactorsEnum.XRAY);
        // Reflectors refls = pattern.calculateReflectors(sln.phase);
        //
        // pattern.simulate(camera, refls, new Energy(beamEnergy),
        // sln.rotation);
        //
        // ByteMap simPatternMap = pattern.getPatternMap();
        // Contrast.expansion(simPatternMap);
        //
        // // Combine simulation pattern with source pattern map
        // MapMath.addition(patternMap, simPatternMap, 1.0, 0.0, patternMap);
        //
        // // Draw pattern centre
        // int pcH = (int) ((camera.patternCenterH + 0.5) * width);
        // int pcV = height - (int) ((camera.patternCenterV + 0.5) * height);
        // for (int x = pcH - width / 50; x <= pcH + width / 50; x++)
        // patternMap.setPixValue(x, pcV, 254);
        // for (int y = pcV - height / 50; y <= pcV + height / 50; y++)
        // patternMap.setPixValue(pcH, y, 254);
        //
        // // Change colour for solution and pattern centre
        // patternMap.lut.setLUT(255, 255, 0, 0);
        // patternMap.lut.setLUT(254, 0, 255, 0);
    }
}
