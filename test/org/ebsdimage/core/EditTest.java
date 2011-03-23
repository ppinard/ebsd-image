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

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.ROI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.toRadians;

import static junittools.test.Assert.assertEquals;

public class EditTest extends TestCase {

    private ItemMock item0;

    private ItemMock item1;

    private ItemMock item2;

    private IndexedByteMap<ItemMock> indexedMap;



    @Before
    public void setUp() throws Exception {
        item0 = new ItemMock("No item");
        item1 = new ItemMock("Item1");
        item2 = new ItemMock("Item2");

        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(2, item2);

        byte[] pixArray = new byte[] { 0, 1, 2, 1, 0, 0, 0, 0, 2 };

        indexedMap = new IndexedByteMap<ItemMock>(3, 3, pixArray, item0, items);
    }



    @Test
    public void testCopyIndexedByteMapROIIndexedByteMapIntInt() {
        IndexedByteMap<ItemMock> destMap =
                new IndexedByteMap<ItemMock>(2, 2, item0);

        // Copy
        Edit.copy(indexedMap, new ROI(0, 0, 1, 1), destMap, 0, 0);

        // Test
        assertEquals(2, destMap.height);
        assertEquals(2, destMap.width);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = destMap.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));

        assertTrue(destMap.isCorrect());
    }



    @Test
    public void testCopyMapROIMapIntInt() {
        // Add handler
        rmlimage.core.Edit.addHandler(Edit.class);

        IndexedByteMap<ItemMock> destMap =
                new IndexedByteMap<ItemMock>(2, 2, item0);

        // Copy
        rmlimage.core.Edit.copy((Map) indexedMap, new ROI(0, 0, 1, 1),
                (Map) destMap, 0, 0);

        // Test
        assertEquals(2, destMap.height);
        assertEquals(2, destMap.width);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = destMap.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));

        assertTrue(destMap.isCorrect());
    }



    @Test
    public void testCropHoughMapDouble() throws IOException {
        // Create a HoughMap
        HoughMap houghMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap.bmp"));

        HoughMap croppedMap = Edit.crop(houghMap, 50.0);
        assertEquals(houghMap.width, croppedMap.width);
        assertEquals(55, croppedMap.height);
        assertEquals(50.80506, croppedMap.rhoMax, 0.001);
        assertTrue(houghMap.getDeltaRho().equals(croppedMap.getDeltaRho(),
                0.001));

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/houghmap_cropped.bmp");
        Conversion.toByteMap(croppedMap).assertEquals(expected);
    }



    @Test
    // Bug #108 (Rounding error in height calculation)
    public void testCropHoughMapDouble2() {
        ByteMap pattern =
                (ByteMap) load("org/ebsdimage/testdata/pattern_masked.bmp");
        HoughMap houghMap = Transform.hough(pattern, toRadians(0.5));

        for (int i = 1; i <= (int) houghMap.getRhoMax(); i++) {
            Logger.getLogger("ebsd").info("Cropping radius: " + i);
            HoughMap croppedMap = Edit.crop(houghMap, i);

            assertEquals(houghMap.getDeltaRho(), croppedMap.getDeltaRho(), 1e-6);
            assertEquals(houghMap.getDeltaTheta(), croppedMap.getDeltaTheta(),
                    1e-6);
        }
    }



    @Test
    public void testCropIndexedByteMapROI() {
        IndexedByteMap<ItemMock> cropMap =
                Edit.crop(indexedMap, new ROI(0, 0, 1, 1));

        assertEquals(2, cropMap.height);
        assertEquals(2, cropMap.width);
        assertEquals(0, cropMap.pixArray[0]);
        assertEquals(1, cropMap.pixArray[1]);
        assertEquals(1, cropMap.pixArray[2]);
        assertEquals(0, cropMap.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = cropMap.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));
    }



    @Test
    public void testCropMapROIErrorMap() {
        // Add handler
        rmlimage.core.Edit.addHandler(Edit.class);

        IndexedByteMap<?> cropMap =
                (IndexedByteMap<?>) rmlimage.core.Edit.crop((Map) indexedMap,
                        new ROI(0, 0, 1, 1));

        assertEquals(2, cropMap.height);
        assertEquals(2, cropMap.width);
        assertEquals(0, cropMap.pixArray[0]);
        assertEquals(1, cropMap.pixArray[1]);
        assertEquals(1, cropMap.pixArray[2]);
        assertEquals(0, cropMap.pixArray[3]);

        java.util.Map<Integer, ?> items = cropMap.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));
    }

}
