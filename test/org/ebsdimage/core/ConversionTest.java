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

import java.util.HashMap;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.ExpMMap;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.RGBMap;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import static java.lang.Math.toRadians;

public class ConversionTest extends TestCase {

    private PhasesMap createPhasesMap() {
        Crystal[] phases =
                new Crystal[] { CrystalFactory.silicon(),
                        CrystalFactory.ferrite() };
        byte[] pixArray = new byte[] { 0, 1, 2, 1 };

        return new PhasesMap(2, 2, pixArray, phases);
    }



    @Test
    // Test call to handler
    public void testToByteMaMapHoughMap() {
        rmlimage.core.Conversion.addHandler(org.ebsdimage.core.Conversion.class);

        // Create a HoughMap
        ByteMap pattern = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");
        HoughMap houghMap = Transform.hough(pattern, toRadians(1.0));

        ByteMap byteMap = rmlimage.core.Conversion.toByteMap(houghMap);

        assertEquals(houghMap.width, byteMap.width);
        assertEquals(houghMap.height, byteMap.height);

        // If the pixArray are different
        byte[] bytePixArray = byteMap.pixArray;
        byte[] houghPixArray = houghMap.pixArray;
        int size = byteMap.size;
        for (int n = 0; n < size; n++)
            if (bytePixArray[n] != houghPixArray[n])
                throw new AssertionError("Pixel different at ("
                        + (n % byteMap.width) + ';' + (n / byteMap.width)
                        + ") between the ByteMap (" + (bytePixArray[n] & 0xff)
                        + ") and the HoughMap (" + (houghPixArray[n] & 0xff)
                        + ')');

        // Calibration
        byteMap.getCalibration().assertEquals(houghMap.getCalibration());
    }



    @Test
    public void testToByteMapHoughMap() {
        // Create a HoughMap
        ByteMap pattern = (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");
        HoughMap houghMap = Transform.hough(pattern, toRadians(1.0));

        ByteMap byteMap = Conversion.toByteMap(houghMap);

        assertEquals(houghMap.width, byteMap.width);
        assertEquals(houghMap.height, byteMap.height);

        // If the pixArray are different
        byte[] bytePixArray = byteMap.pixArray;
        byte[] houghPixArray = houghMap.pixArray;
        int size = byteMap.size;
        for (int n = 0; n < size; n++)
            if (bytePixArray[n] != houghPixArray[n])
                throw new AssertionError("Pixel different at ("
                        + (n % byteMap.width) + ';' + (n / byteMap.width)
                        + ") between the ByteMap (" + (bytePixArray[n] & 0xff)
                        + ") and the HoughMap (" + (houghPixArray[n] & 0xff)
                        + ')');

        // Calibration
        byteMap.getCalibration().assertEquals(houghMap.getCalibration());
    }



    @Test
    public void testToByteMapMapPhasesMap() {
        rmlimage.core.Conversion.addHandler(org.ebsdimage.core.Conversion.class);

        PhasesMap map = createPhasesMap();

        ByteMap byteMap = rmlimage.core.Conversion.toByteMap(map);

        assertEquals(map.width, byteMap.width);
        assertEquals(map.height, byteMap.height);

        assertEquals(map.pixArray[0], byteMap.pixArray[0]);
        assertEquals(map.pixArray[1], byteMap.pixArray[1]);
        assertEquals(map.pixArray[2], byteMap.pixArray[2]);
        assertEquals(map.pixArray[3], byteMap.pixArray[3]);

        map.getLUT().assertEquals(byteMap.getLUT());
    }



    @Test
    public void testToByteMapPhasesMap() {
        PhasesMap map = createPhasesMap();

        ByteMap byteMap = Conversion.toByteMap(map);

        assertEquals(map.width, byteMap.width);
        assertEquals(map.height, byteMap.height);

        assertEquals(map.pixArray[0], byteMap.pixArray[0]);
        assertEquals(map.pixArray[1], byteMap.pixArray[1]);
        assertEquals(map.pixArray[2], byteMap.pixArray[2]);
        assertEquals(map.pixArray[3], byteMap.pixArray[3]);

        map.getLUT().assertEquals(byteMap.getLUT());
    }



    @Test
    public void testToHoughMap() {
        // Load a HoughMap derived ByteMap
        ByteMap byteMap = (ByteMap) load("org/ebsdimage/testdata/houghmap.bmp");

        // Create an empty HoughMap with the good calibration
        HoughMap destMap =
                new HoughMap(byteMap.width, byteMap.height, toRadians(1.0),
                        1.8816689);

        Conversion.toHoughMap(byteMap, destMap);

        assertEquals(180, destMap.width);
        assertEquals(227, destMap.height);

        assertEquals(212.628585835, destMap.rhoMax.getValue("px"), 0.001);
        assertEquals(-212.628585835, destMap.rhoMin.getValue("px"), 0.001);
        assertEquals(1.8816689, destMap.getDeltaRho().getValue("px"), 0.001);

        assertEquals(0.0, destMap.thetaMin.getValue("rad"), 0.001);
        assertEquals(3.12414, destMap.thetaMax.getValue("rad"), 0.001);
        assertEquals(1.0, destMap.getDeltaTheta().getValue("deg"), 0.001);

        assertArrayEquals(destMap.pixArray, byteMap.pixArray);
    }



    @Test
    public void testToRGBMap() {
        // Create ExpMMap

        HashMap<String, Map> mapList = new HashMap<String, Map>();

        RealMap q0Map = new RealMap(2, 2);
        q0Map.pixArray[0] = 1.0f;
        mapList.put(EbsdMMap.Q0, q0Map);

        RealMap q1Map = new RealMap(2, 2);
        q1Map.pixArray[1] = 1.0f;
        mapList.put(EbsdMMap.Q1, q1Map);

        RealMap q2Map = new RealMap(2, 2);
        q2Map.pixArray[2] = 1.0f;
        mapList.put(EbsdMMap.Q2, q2Map);

        RealMap q3Map = new RealMap(2, 2);
        q3Map.pixArray[3] = 1.0f;
        mapList.put(EbsdMMap.Q3, q3Map);

        PhasesMap phasesMap =
                new PhasesMap(2, 2, new byte[] { 0, 1, 2, 1 }, new Crystal[] {
                        CrystalFactory.silicon(), CrystalFactory.ferrite() });
        mapList.put(EbsdMMap.PHASES, phasesMap);

        ExpMMap mmap = new ExpMMap(2, 2, mapList);

        // Convert
        RGBMap rgbMap = Conversion.toRGBMap(mmap);

        // Test
        assertEquals(0, rgbMap.pixArray[0]); // No phase
        assertEquals(8454016, rgbMap.pixArray[1]);
        assertEquals(16777088, rgbMap.pixArray[2]);
        assertEquals(16711808, rgbMap.pixArray[3]);
    }
}
