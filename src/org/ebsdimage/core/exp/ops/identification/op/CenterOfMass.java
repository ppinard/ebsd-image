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

import magnitude.core.Magnitude;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.Centroid;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
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
        System.out.println("identMap:" + identMap.getCalibration());

        // Centre of mass
        Centroid centers = Analysis.getCenterOfMass(identMap, houghMap);

        // Create Hough peaks with intensity at centre of mass
        HoughPeak[] peaks = new HoughPeak[centers.getValueCount()];

        double intensity;
        Magnitude theta = new Magnitude(0, centers.units[Centroid.X]);
        Magnitude rho = new Magnitude(0, centers.units[Centroid.Y]);
        for (int i = 0; i < centers.getValueCount(); i++) {
            theta = new Magnitude(centers.x[i], theta);
            rho = new Magnitude(centers.y[i], rho);
            intensity = centers.intensity[i];

            peaks[i] = new HoughPeak(theta, rho, intensity);
        }

        return peaks;
    }



    @Override
    public String toString() {
        return "Center of Mass";
    }
}
