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

import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Threshold;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.core.MapMath;
import rmlshared.io.FileUtil;

public class CenterOfMassTest {

    private CenterOfMass centerOfMass;

    private HoughMap houghMap;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        centerOfMass = new CenterOfMass();

        houghMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));

        // Create peaks map with three peaks;
        IdentMap identMap =
                Identification.identify(Threshold.automaticTopHat(houghMap));
        peaksMap = new BinMap(identMap.width, identMap.height);

        MapMath.or(peaksMap, Identification.extractObject(identMap, 1),
                peaksMap);
        MapMath.or(peaksMap, Identification.extractObject(identMap, 2),
                peaksMap);
        MapMath.or(peaksMap, Identification.extractObject(identMap, 3),
                peaksMap);
    }



    @Test
    public void testIdentify() throws Exception {
        HoughPeak[] destPeaks = centerOfMass.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);

        // Peak 1
        assertEquals(88.09204877, destPeaks[0].rho, 1e-6);
        assertEquals(2.525296303, destPeaks[0].theta, 1e-6);
        assertEquals(123.0, destPeaks[0].intensity, 1e-6);

        // Peak 2
        assertEquals(87.47055816, destPeaks[1].rho, 1e-6);
        assertEquals(2.927335805, destPeaks[1].theta, 1e-6);
        assertEquals(125.0, destPeaks[1].intensity, 1e-6);

        // Peak 3
        assertEquals(87.46666666, destPeaks[2].rho, 1e-6);
        assertEquals(1.274090353, destPeaks[2].theta, 1e-6);
        assertEquals(123.0, destPeaks[2].intensity, 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "Center of Mass";
        assertEquals(expected, centerOfMass.toString());

    }

}
