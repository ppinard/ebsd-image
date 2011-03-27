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
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IndexedByteMapTest {

    private ItemMock item0;

    private ItemMock item1;

    private ItemMock item2;

    private IndexedByteMap<ItemMock> indexedMap;

    private byte[] pixArray;



    @Before
    public void setUp() throws Exception {
        item0 = new ItemMock("No item");

        item1 = new ItemMock("Item1");
        item2 = new ItemMock("Item3");
        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(3, item2);

        pixArray = new byte[] { 0, 1, 3, 1 };

        indexedMap = new IndexedByteMap<ItemMock>(2, 2, pixArray, item0, items);
    }



    @Test
    public void testAssertEquals() {
        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(3, item2);
        IndexedByteMap<ItemMock> other =
                new IndexedByteMap<ItemMock>(2, 2, pixArray, item0, items);

        indexedMap.assertEquals(other);
    }



    @Test(expected = AssertionError.class)
    public void testAssertEqualsException() {
        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(3, new ItemMock("DIFF"));
        IndexedByteMap<ItemMock> other =
                new IndexedByteMap<ItemMock>(2, 2, pixArray, item0, items);

        indexedMap.assertEquals(other);
    }



    @Test
    public void testCreateMapIntInt() {
        IndexedByteMap<ItemMock> other = indexedMap.createMap(3, 3);

        assertEquals(3, other.width);
        assertEquals(3, other.height);
        assertEquals(1, other.getItems().size());
        assertEquals(item0, other.getItems().get(0));

        for (int i = 0; i < 9; i++)
            assertEquals(0, other.getPixValue(i));
    }



    @Test
    public void testDuplicate() {
        IndexedByteMap<ItemMock> other = indexedMap.duplicate();

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, ItemMock> items = other.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(3));

        assertEquals(0, other.getPixValue(0));
        assertEquals(1, other.getPixValue(1));
        assertEquals(3, other.getPixValue(2));
        assertEquals(1, other.getPixValue(3));
    }



    @Test
    public void testFindFirstNullItem() {
        assertEquals(2, indexedMap.findFirstNullItem());
    }



    @Test
    public void testGetItem() {
        assertEquals(item0, indexedMap.getItem(0));
        assertEquals(item1, indexedMap.getItem(1));
        assertEquals(null, indexedMap.getItem(2));
        assertEquals(item2, indexedMap.getItem(3));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetItemException1() {
        indexedMap.getItem(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetItemException2() {
        indexedMap.getItem(256);
    }



    @Test
    public void testGetItemId() {
        assertEquals(0, indexedMap.getItemId(item0));
        assertEquals(1, indexedMap.getItemId(item1));
        assertEquals(3, indexedMap.getItemId(item2));
        assertEquals(-1, indexedMap.getItemId(new ItemMock("OTHER")));
    }



    @Test
    public void testGetItems() {
        Map<Integer, ItemMock> items = indexedMap.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(3));
    }



    @Test
    public void testGetPixelInfoLabelInt() throws IOException {
        String expected = "Value=0 (0;0;0)  0 - No item";
        assertEquals(expected, indexedMap.getPixelInfoLabel(0));

        expected = "Value=1 (187;25;104)  1 - Item1";
        assertEquals(expected, indexedMap.getPixelInfoLabel(1));
    }



    @Test
    public void testGetValidFileFormats() {
        assertEquals("bmp", indexedMap.getValidFileFormats()[0]);
    }



    @Test
    public void testIndexedByteMapIntIntByteArrayItemHashMap() {
        assertEquals(2, indexedMap.width);
        assertEquals(2, indexedMap.height);

        Map<Integer, ItemMock> items = indexedMap.getItems();
        assertEquals(3, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item2, items.get(3));

        assertEquals(0, indexedMap.getPixValue(0));
        assertEquals(1, indexedMap.getPixValue(1));
        assertEquals(3, indexedMap.getPixValue(2));
        assertEquals(1, indexedMap.getPixValue(3));
    }



    @Test
    public void testIndexedByteMapIntIntItem() {
        IndexedByteMap<ItemMock> other =
                new IndexedByteMap<ItemMock>(2, 2, item0);

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, ItemMock> otherItems = other.getItems();
        assertEquals(1, otherItems.size());
        assertEquals(item0, otherItems.get(0));

        assertEquals(0, other.getPixValue(0));
        assertEquals(0, other.getPixValue(1));
        assertEquals(0, other.getPixValue(2));
        assertEquals(0, other.getPixValue(3));
    }



    @Test
    public void testIndexedByteMapIntIntItemHashMap() {
        HashMap<Integer, ItemMock> items = new HashMap<Integer, ItemMock>();
        items.put(1, item1);
        items.put(3, item2);
        IndexedByteMap<ItemMock> other =
                new IndexedByteMap<ItemMock>(2, 2, item0, items);

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, ItemMock> otherItems = other.getItems();
        assertEquals(3, otherItems.size());
        assertEquals(item0, otherItems.get(0));
        assertEquals(item1, otherItems.get(1));
        assertEquals(item2, otherItems.get(3));

        assertEquals(0, other.getPixValue(0));
        assertEquals(0, other.getPixValue(1));
        assertEquals(0, other.getPixValue(2));
        assertEquals(0, other.getPixValue(3));
    }



    @Test
    public void testIsCorrect() {
        assertTrue(indexedMap.isCorrect());

        indexedMap.pixArray[2] = 2;
        assertFalse(indexedMap.isCorrect());
    }



    @Test
    public void testIsRegisteredInt() {
        assertTrue(indexedMap.isRegistered(0));
        assertTrue(indexedMap.isRegistered(1));
        assertFalse(indexedMap.isRegistered(2));
        assertTrue(indexedMap.isRegistered(3));
        assertFalse(indexedMap.isRegistered(4));
    }



    @Test
    public void testIsRegisteredItem() {
        assertTrue(indexedMap.isRegistered(item0));
        assertTrue(indexedMap.isRegistered(item1));
        assertFalse(indexedMap.isRegistered(null));
        assertTrue(indexedMap.isRegistered(item2));
        assertFalse(indexedMap.isRegistered(new ItemMock("OTHER")));
    }



    @Test
    public void testRegisterIntItem() {
        ItemMock item = new ItemMock("NEW");
        indexedMap.register(2, item);

        assertTrue(indexedMap.isRegistered(2));
        assertTrue(indexedMap.isRegistered(item));
        assertEquals(2, indexedMap.getItemId(item));
        assertEquals(item, indexedMap.getItem(2));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRegisterIntItemException1() {
        // already register id
        indexedMap.register(1, new ItemMock("NEW"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRegisterIntItemException2() {
        // id < 1
        indexedMap.register(0, new ItemMock("NEW"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRegisterIntItemException3() {
        // id > 255
        indexedMap.register(256, new ItemMock("NEW"));
    }



    @Test
    public void testRegisterItem() {
        ItemMock item = new ItemMock("NEW");
        int id = indexedMap.register(item);

        assertEquals(2, id);
        assertTrue(indexedMap.isRegistered(id));
        assertTrue(indexedMap.isRegistered(item));
        assertEquals(id, indexedMap.getItemId(item));
        assertEquals(item, indexedMap.getItem(id));
    }



    @Test
    public void testSetPixValueIntInt() {
        indexedMap.setPixValue(0, 3);
        assertEquals(3, indexedMap.getPixValue(0));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPixValueIntIntException() {
        // unregistered id
        indexedMap.setPixValue(0, 2);
    }



    @Test
    public void testSetPixValueIntIntInt() {
        indexedMap.setPixValue(0, 0, 3);
        assertEquals(3, indexedMap.getPixValue(0));
    }



    @Test
    public void testSetPixValueIntIntItem() {
        indexedMap.setPixValue(0, 0, item2);
        assertEquals(3, indexedMap.getPixValue(0));
    }



    @Test
    public void testSetPixValueIntItem() {
        indexedMap.setPixValue(0, item2);
        assertEquals(3, indexedMap.getPixValue(0));

        // New item
        indexedMap.setPixValue(0, new ItemMock("NEW"));
        assertEquals(2, indexedMap.getPixValue(0));
    }



    @Test
    public void testUnregisterInt() {
        indexedMap.setPixValue(2, 0);
        indexedMap.unregister(3);

        Map<Integer, ItemMock> otherItems = indexedMap.getItems();
        assertEquals(2, otherItems.size());
        assertEquals(item0, otherItems.get(0));
        assertEquals(item1, otherItems.get(1));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testUnregisterIntException1() {
        // id == 0
        indexedMap.unregister(0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testUnregisterIntException2() {
        // unregistered id
        indexedMap.unregister(2);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testUnregisterIntException3() {
        // invalid map after unregister
        indexedMap.unregister(3);
    }



    @Test
    public void testUnregisterItem() {
        indexedMap.setPixValue(2, 0);
        indexedMap.unregister(item2);

        Map<Integer, ItemMock> otherItems = indexedMap.getItems();
        assertEquals(2, otherItems.size());
        assertEquals(item0, otherItems.get(0));
        assertEquals(item1, otherItems.get(1));
    }



    @Test
    public void testValidate() {
        indexedMap.validate();
    }



    @Test(expected = IllegalArgumentException.class)
    public void testValidateException() {
        indexedMap.pixArray[2] = 2;
        indexedMap.validate();
    }

}
