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
package org.ebsdimage.core;

import java.util.HashMap;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.ExpMMap;
import org.junit.Before;
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

    private IndexedByteMap<ItemMock> indexedMap;



    @Before
    public void setUp() throws Exception {
        ItemMock item0 = new ItemMock("No item");

        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, new ItemMock("Item1"));
        items.put(2, new ItemMock("Item2"));

        byte[] pixArray = new byte[] { 0, 1, 2, 1, 0, 0, 0, 0, 2 };

        indexedMap = new IndexedByteMap<ItemMock>(3, 3, pixArray, item0, items);
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
    public void testToByteMapIndexedByteMap() {
        ByteMap byteMap = Conversion.toByteMap(indexedMap);

        assertEquals(indexedMap.width, byteMap.width);
        assertEquals(indexedMap.height, byteMap.height);

        assertEquals(indexedMap.pixArray[0], byteMap.pixArray[0]);
        assertEquals(indexedMap.pixArray[1], byteMap.pixArray[1]);
        assertEquals(indexedMap.pixArray[2], byteMap.pixArray[2]);
        assertEquals(indexedMap.pixArray[3], byteMap.pixArray[3]);

        indexedMap.getLUT().assertEquals(byteMap.getLUT());
    }



    @Test
    // Test call to handler
    public void testToByteMapMapHoughMap() {
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
    public void testToByteMapMapIndexedByteMap() {
        rmlimage.core.Conversion.addHandler(org.ebsdimage.core.Conversion.class);

        ByteMap byteMap = rmlimage.core.Conversion.toByteMap(indexedMap);

        assertEquals(indexedMap.width, byteMap.width);
        assertEquals(indexedMap.height, byteMap.height);

        assertEquals(indexedMap.pixArray[0], byteMap.pixArray[0]);
        assertEquals(indexedMap.pixArray[1], byteMap.pixArray[1]);
        assertEquals(indexedMap.pixArray[2], byteMap.pixArray[2]);
        assertEquals(indexedMap.pixArray[3], byteMap.pixArray[3]);

        indexedMap.getLUT().assertEquals(byteMap.getLUT());
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

        assertEquals(212.628585835, destMap.getRhoMax(), 0.001);
        assertEquals(-212.628585835, destMap.getRhoMin(), 0.001);
        assertEquals(1.8816689, destMap.getDeltaRho().getValue("px"), 0.001);

        assertEquals(0.0, destMap.getThetaMin(), 0.001);
        assertEquals(3.12414, destMap.getThetaMax(), 0.001);
        assertEquals(1.0, destMap.getDeltaTheta().getValue("deg"), 0.001);

        assertArrayEquals(destMap.pixArray, byteMap.pixArray);
    }



    @Test
    public void testToRGBMap() {
        float S2_2 = (float) (Math.sqrt(2) / 2.0);

        // Create ExpMMap
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        float[] pixArray = new float[] { 0.0f, 0.0f, 0.0f, S2_2 };
        RealMap q0Map = new RealMap(2, 2, pixArray);
        mapList.put(EbsdMMap.Q0, q0Map);

        pixArray = new float[] { S2_2, S2_2, 0.0f, 0.0f };
        RealMap q1Map = new RealMap(2, 2, pixArray);
        mapList.put(EbsdMMap.Q1, q1Map);

        pixArray = new float[] { S2_2, 0.0f, S2_2, 0.0f };
        RealMap q2Map = new RealMap(2, 2, pixArray);
        mapList.put(EbsdMMap.Q2, q2Map);

        pixArray = new float[] { 0.0f, S2_2, S2_2, S2_2 };
        RealMap q3Map = new RealMap(2, 2, pixArray);
        mapList.put(EbsdMMap.Q3, q3Map);

        HashMap<Integer, Crystal> items = new HashMap<Integer, Crystal>();
        items.put(1, CrystalFactory.silicon());
        PhaseMap phasesMap =
                new PhaseMap(2, 2, new byte[] { 1, 1, 0, 1 }, items);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        ExpMMap mmap = new ExpMMap(2, 2, mapList);

        // Convert
        RGBMap rgbMap = Conversion.toRGBMap(mmap);

        // Test
        assertEquals(0, rgbMap.pixArray[0]); // singularity
        assertEquals(12550335, rgbMap.pixArray[1]);
        assertEquals(0, rgbMap.pixArray[2]); // no phase
        assertEquals(16711808, rgbMap.pixArray[3]);
    }
}
