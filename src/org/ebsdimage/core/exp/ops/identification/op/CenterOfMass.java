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
package org.ebsdimage.core.exp.ops.identification.op;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPoint;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;

/**
 * Operation to find the position of the Hough peaks using the center of mass of
 * the detected peaks in the <code>BinMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CenterOfMass extends IdentificationOp {

    /** Default operation. */
    public static final CenterOfMass DEFAULT = new CenterOfMass();



    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        // Center of mass
        HoughPoint peaks = Analysis.getCenterOfMass(houghMap, identMap);
        int size = peaks.getValueCount();

        // Create Hough peaks with intensity at center of mass
        HoughPeak[] destPeaks = new HoughPeak[size];

        double rho, theta, intensity;
        int index;
        for (int n = 0; n < size; n++) {
            rho = peaks.rho[n];
            theta = peaks.theta[n];

            index = houghMap.getIndex(rho, theta);
            intensity = houghMap.pixArray[index] & 0xff;

            destPeaks[n] = new HoughPeak(rho, theta, intensity);
        }

        return destPeaks;
    }



    @Override
    public String toString() {
        return "Center of Mass";
    }
}
