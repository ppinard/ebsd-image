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
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorMapTest {

    private ErrorMap errorMap;

    private ErrorCode errorCode1;

    private ErrorCode errorCode2;

    private byte[] pixArray;



    @Before
    public void setUp() throws Exception {
        errorCode1 = new ErrorCode("Error1");
        errorCode2 = new ErrorCode("Error3", "Desc3");

        HashMap<Integer, ErrorCode> items = new HashMap<Integer, ErrorCode>();
        items.put(1, errorCode1);
        items.put(3, errorCode2);

        pixArray = new byte[] { 0, 1, 3, 1 };

        errorMap = new ErrorMap(2, 2, pixArray, items);
    }



    @Test
    public void testCreateMapIntInt() {
        ErrorMap other = errorMap.createMap(3, 3);

        assertEquals(3, other.width);
        assertEquals(3, other.height);
        assertEquals(1, other.getItems().size());
        assertEquals(ErrorMap.NO_ERROR, other.getItems().get(0));

        for (int i = 0; i < 9; i++)
            assertEquals(0, other.getPixValue(i));
    }



    @Test
    public void testDuplicate() {
        ErrorMap other = errorMap.duplicate();

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, ErrorCode> items = other.getItems();
        assertEquals(3, items.size());
        assertEquals(ErrorMap.NO_ERROR, items.get(0));
        assertEquals(errorCode1, items.get(1));
        assertEquals(errorCode2, items.get(3));

        assertEquals(0, other.getPixValue(0));
        assertEquals(1, other.getPixValue(1));
        assertEquals(3, other.getPixValue(2));
        assertEquals(1, other.getPixValue(3));
    }



    @Test
    public void testErrorMapIntInt() {
        ErrorMap other = new ErrorMap(2, 2);

        assertEquals(2, other.width);
        assertEquals(2, other.height);
        assertEquals(1, other.getItems().size());
        assertEquals(ErrorMap.NO_ERROR, other.getItems().get(0));
    }



    @Test
    public void testErrorMapIntIntByteArrayHashMap() {
        assertEquals(2, errorMap.width);
        assertEquals(2, errorMap.height);

        Map<Integer, ErrorCode> items = errorMap.getItems();
        assertEquals(3, items.size());
        assertEquals(ErrorMap.NO_ERROR, items.get(0));
        assertEquals(errorCode1, items.get(1));
        assertEquals(errorCode2, items.get(3));

        assertEquals(0, errorMap.getPixValue(0));
        assertEquals(1, errorMap.getPixValue(1));
        assertEquals(3, errorMap.getPixValue(2));
        assertEquals(1, errorMap.getPixValue(3));
    }



    @Test
    public void testErrorMapIntIntHashMap() {
        HashMap<Integer, ErrorCode> items = new HashMap<Integer, ErrorCode>();
        items.put(1, errorCode1);
        ErrorMap other = new ErrorMap(2, 2, items);

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, ErrorCode> otherItems = other.getItems();
        assertEquals(2, otherItems.size());
        assertEquals(ErrorMap.NO_ERROR, otherItems.get(0));
        assertEquals(errorCode1, otherItems.get(1));

        for (int i = 0; i < 4; i++)
            assertEquals(0, other.getPixValue(i));
    }



    @Test
    public void testThrowError() {
        errorMap.throwError(0, errorCode2);
        assertEquals(3, errorMap.getPixValue(0));

        errorMap.throwError(0, new ErrorCode("Error"));
        assertEquals(4, errorMap.getItems().size());
        assertEquals(2, errorMap.getPixValue(0));
    }

}
