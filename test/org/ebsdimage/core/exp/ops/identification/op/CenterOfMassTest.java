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

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Threshold;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.core.MapMath;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class CenterOfMassTest extends TestCase {

    private CenterOfMass op;

    private HoughMap houghMap;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        op = new CenterOfMass();

        houghMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));

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
        HoughPeak[] destPeaks = op.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);

        // Peak 1
        assertEquals(48.95774459838867, destPeaks[0].rho, 1e-4);
        assertEquals(2.67545223236084, destPeaks[0].theta, 1e-4);
        assertEquals(153.0, destPeaks[0].intensity, 1e-4);

        // Peak 2
        assertEquals(37.75833511352539, destPeaks[1].rho, 1e-4);
        assertEquals(2.202118158340454, destPeaks[1].theta, 1e-4);
        assertEquals(128.0, destPeaks[1].intensity, 1e-4);

        // Peak 3
        assertEquals(29.368501663208008, destPeaks[2].rho, 1e-4);
        assertEquals(0.5339681506156921, destPeaks[2].theta, 1e-4);
        assertEquals(121.0, destPeaks[2].intensity, 1e-4);
    }



    @Test
    public void testToString() {
        String expected = "Center of Mass";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        CenterOfMass other = new XmlLoader().load(CenterOfMass.class, file);
        assertEquals(op, other, 1e-6);
    }

}
