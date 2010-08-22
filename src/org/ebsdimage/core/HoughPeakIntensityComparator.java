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

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator to sort the Hough peaks by intensity.
 * 
 * @author Philippe T. Pinard
 */

public class HoughPeakIntensityComparator implements Comparator<HoughPeak>,
        Serializable {

    @Override
    public int compare(HoughPeak peak0, HoughPeak peak1) {
        double intensity0 = peak0.intensity;
        double intensity1 = peak1.intensity;

        if (intensity0 < intensity1)
            return -1;
        else if (intensity0 > intensity1)
            return 1;
        else
            return 0;
    }
}