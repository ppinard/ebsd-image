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
package org.ebsdimage.core.exp.ops.positioning.op;

import org.ebsdimage.core.Analysis;
import org.ebsdimage.core.Centroid;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;

/**
 * Operation to identify the Hough peaks by finding the centroid of each peaks
 * in the peaks map. The intensity is taken as the maximum intensity of the
 * peaks.
 * 
 * @author Philippe T. Pinard
 */
public class LocalCentroid extends PositioningOp {

    /** Default operation. */
    public static final LocalCentroid DEFAULT = new LocalCentroid();



    /**
     * Identifies the Hough peaks with the centroid of each peaks in the peaks
     * map. The intensity is taken as the maximum intensity of each peak in the
     * Hough map.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaksMap
     *            peaks map
     * @param houghMap
     *            Hough map
     * @return Hough peaks
     */
    @Override
    public HoughPeak[] identify(Exp exp, BinMap peaksMap, HoughMap houghMap) {
        IdentMap identMap = Identification.identify(peaksMap);

        Centroid centroids = Analysis.getCentroid(identMap, houghMap);

        return centroids.toHoughPeakArray();
    }



    @Override
    public String toString() {
        return "Local Centroid";
    }
}
