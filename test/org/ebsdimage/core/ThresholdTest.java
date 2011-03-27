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

import java.io.IOException;
import java.util.HashMap;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlshared.io.FileUtil;
import rmlshared.util.Properties;
import static org.ebsdimage.core.Threshold.*;

import static org.junit.Assert.assertEquals;

public class ThresholdTest extends TestCase {

    private ItemMock item0;

    private ItemMock item1;

    private ItemMock item2;

    private IndexedByteMap<ItemMock> indexedMap;



    @Before
    public void setUp() throws Exception {
        item0 = new ItemMock("No item");

        item1 = new ItemMock("Item1");
        item2 = new ItemMock("Item3");
        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(3, item2);

        byte[] pixArray = new byte[] { 0, 1, 3, 1 };

        indexedMap = new IndexedByteMap<ItemMock>(2, 2, pixArray, item0, items);
    }



    @Test
    public void testAutomaticStdDev() throws IOException {
        // Do the operation
        HoughMap houghMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));
        BinMap destMap = Threshold.automaticStdDev(houghMap, 1.5);

        // Test the pixArray
        BinMap expected =
                (BinMap) load("org/ebsdimage/testdata/automatic_stddev.bmp");
        destMap.assertEquals(expected);

        // Test the properties
        Properties props = destMap.getProperties();
        assertEquals("false", props.getProperty(KEY_OVERFLOW, ""));
        assertEquals(1.5, props.getProperty(KEY_SIGMAFACTOR, Double.NaN), 1e-3);
        assertEquals(103.4514, props.getProperty(KEY_AVERAGE, Double.NaN), 1e-3);
        assertEquals(5.3786, props.getProperty(KEY_STDDEV, Double.NaN), 1e-3);
        assertEquals(112, props.getProperty(KEY_THRESHOLD, -1));
    }



    @Test
    public void testAutomaticTopHat() throws IOException {
        // Do the operation
        HoughMap houghMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));

        BinMap destMap = Threshold.automaticTopHat(houghMap);

        // Test the pixArray
        BinMap expected =
                (BinMap) load("org/ebsdimage/testdata/automatic_top_hat.bmp");
        destMap.assertEquals(expected);

        // Test the properties
        Properties props = destMap.getProperties();
        assertEquals(11, props.getProperty(KEY_MINERROR, -1));
        assertEquals(26, props.getProperty(KEY_KAPUR, -1));
        assertEquals((11 + 26) / 2, props.getProperty(KEY_THRESHOLD, -1));
    }



    @Test
    public void testItemIndexedByteMapInt() {
        // Test (id = 0)
        BinMap destMap = Threshold.item(indexedMap, 0);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        // Test (id = 1)
        destMap = Threshold.item(indexedMap, 1);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(1, destMap.pixArray[3]);

        // Test (id = 3)
        destMap = Threshold.item(indexedMap, 3);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
    }



    @Test
    public void testItemIndexedByteMapIntBinMap() {
        BinMap destMap = new BinMap(2, 2);

        // Test (id = 0)
        Threshold.item(indexedMap, 0, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        // Test (id = 1)
        Threshold.item(indexedMap, 1, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(1, destMap.pixArray[3]);

        // Test (id = 3)
        Threshold.item(indexedMap, 3, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
    }



    @Test
    public void testItemIndexedByteMapItem() {
        // Test (item = item0)
        BinMap destMap = Threshold.item(indexedMap, item0);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        // Test (item = item1)
        destMap = Threshold.item(indexedMap, item1);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(1, destMap.pixArray[3]);

        // Test (item = item2)
        destMap = Threshold.item(indexedMap, item2);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
    }



    @Test
    public void testItemIndexedByteMapItemBinMap() {
        BinMap destMap = new BinMap(2, 2);

        // Test (item = item0)
        Threshold.item(indexedMap, item0, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        // Test (item = item1)
        Threshold.item(indexedMap, item1, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(1, destMap.pixArray[3]);

        // Test (item = item2)
        Threshold.item(indexedMap, item2, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
    }

}
