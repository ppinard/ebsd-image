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

import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.Map;

import static org.junit.Assert.assertEquals;

public class MapMathTest {

    private ItemMock item0;

    private ItemMock item1;

    private ItemMock item2;

    private ItemMock item3;

    private IndexedByteMap<ItemMock> src1;

    private BinMap src2;

    private IndexedByteMap<ItemMock> src3;



    @Before
    public void setUp() throws Exception {
        item0 = new ItemMock("No item");
        item1 = new ItemMock("Item1");
        item2 = new ItemMock("Item2");
        item3 = new ItemMock("Item3");

        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(2, item2);
        byte[] pixArray = new byte[] { 0, 1, 2, 1 };
        src1 = new IndexedByteMap<ItemMock>(2, 2, pixArray, item0, items);

        src2 = new BinMap(2, 2, new byte[] { 1, 0, 1, 0 });

        items = new HashMap<Integer, ItemMock>();
        items.put(3, item3);
        pixArray = new byte[] { 3, 0, 0, 0 };
        src3 = new IndexedByteMap<ItemMock>(2, 2, pixArray, item0, items);
    }



    @Test
    public void testAdditionIndexedByteMapIndexedByteMapDoubleDoubleIndexedByteMap() {
        IndexedByteMap<ItemMock> dest = src1.createMap(src1.width, src1.height);

        MapMath.addition(src1, src3, 1.0, 0.0, dest);

        assertEquals(2, dest.width);
        assertEquals(2, dest.height);

        assertEquals(3, dest.pixArray[0]);
        assertEquals(1, dest.pixArray[1]);
        assertEquals(2, dest.pixArray[2]);
        assertEquals(1, dest.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = dest.getItems();
        assertEquals(4, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));
        assertEquals(item3, items.get(3));
    }



    @Test
    public void testAdditionMapMapDoubleDoubleMap() {
        rmlimage.core.MapMath.addHandler(MapMath.class);

        IndexedByteMap<ItemMock> dest = src1.createMap(src1.width, src1.height);

        rmlimage.core.MapMath.addition((Map) src1, (Map) src3, 1.0, 0.0,
                (Map) dest);

        assertEquals(2, dest.width);
        assertEquals(2, dest.height);

        assertEquals(3, dest.pixArray[0]);
        assertEquals(1, dest.pixArray[1]);
        assertEquals(2, dest.pixArray[2]);
        assertEquals(1, dest.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = dest.getItems();
        assertEquals(4, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));
        assertEquals(item3, items.get(3));
    }



    @Test
    public void testAndIndexedByteMapBinMapIndexedByteMap() {
        IndexedByteMap<ItemMock> dest = src1.duplicate();

        MapMath.and(src1, src2, dest);

        assertEquals(2, dest.width);
        assertEquals(2, dest.height);

        assertEquals(0, dest.pixArray[0]);
        assertEquals(0, dest.pixArray[1]);
        assertEquals(2, dest.pixArray[2]);
        assertEquals(0, dest.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = dest.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));
    }



    @Test
    public void testAndMapMapMap() {
        // Add handler
        rmlimage.core.MapMath.addHandler(MapMath.class);

        IndexedByteMap<ItemMock> dest = src1.duplicate();

        rmlimage.core.MapMath.and((Map) src1, (Map) src2, dest);

        assertEquals(2, dest.width);
        assertEquals(2, dest.height);

        assertEquals(0, dest.pixArray[0]);
        assertEquals(0, dest.pixArray[1]);
        assertEquals(2, dest.pixArray[2]);
        assertEquals(0, dest.pixArray[3]);

        java.util.Map<Integer, ItemMock> items = dest.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(2));
    }

}
