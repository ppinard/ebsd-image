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

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ptpshared.core.geom.LinePlane;
import ptpshared.core.math.Vector3D;

public class HoughPeakPairTest {

    private HoughPeak peak0;

    private HoughPeak peak1;

    private Vector3D normal0;

    private Vector3D normal1;

    private HoughPeakPair pair;



    @Before
    public void setUp() throws Exception {
        peak0 = new HoughPeak(2, PI / 4.0);
        peak1 = new HoughPeak(1, PI / 3.0);

        normal0 =
                HoughMath.houghSpaceToLine(peak0).toLine3D(LinePlane.XY).v;
        normal1 =
                HoughMath.houghSpaceToLine(peak1).toLine3D(LinePlane.XY).v;

        pair = new HoughPeakPair(peak0, normal0, peak1, normal1);
    }



    @Test
    public void testHoughPeakPair() {
        assertEquals(peak0, pair.peak0);
        assertEquals(peak1, pair.peak1);
        assertEquals(normal0.normalize(), pair.normal0);
        assertEquals(normal1.normalize(), pair.normal1);
        assertEquals(0.9659, pair.directionCosine, 1e-3);
    }

}
