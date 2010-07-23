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
package org.ebsdimage.core;

import rmlimage.core.*;
import rmlimage.core.MapMath;

/**
 * Quality control methods.
 * 
 * @author Philippe T. Pinard
 */
public class QC {

    /**
     * Overlays the solution of the peaks found in the peaks map over the
     * pattern map.
     * 
     * @param patternMap
     *            original pattern map
     * @param peaksMap
     *            peaks map
     * @param rgb
     *            color of the lines
     */
    public void overlay(ByteMap patternMap, BinMap peaksMap, RGB rgb) {
        HoughPoint centroids =
                Analysis.getCentroid(Identification.identify(peaksMap));

        // Set color 255 to specified RGB
        MapMath.subtraction(patternMap, 1, patternMap);
        patternMap.lut.shiftDown(1);
        patternMap.setLUT(255, rgb.red, rgb.green, rgb.blue);

        int nbPeaks = centroids.getValueCount();
        for (int n = 0; n < nbPeaks; n++)
            Drawing.line(patternMap, centroids.rho[n], centroids.theta[n], 255);

    }

}
