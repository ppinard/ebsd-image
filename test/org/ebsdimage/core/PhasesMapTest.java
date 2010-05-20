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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;
import crystallography.core.crystals.ZirconiumAlpha;

public class PhasesMapTest {

    private PhasesMap map;
    private Crystal[] phases;
    private byte[] pixArray;



    @Before
    public void setUp() throws Exception {
        phases = new Crystal[] { new Silicon(), new IronBCC() };
        pixArray = new byte[] { 0, 1, 2, 1 };

        map = new PhasesMap(2, 2, pixArray, phases);
    }



    @Test
    public void testAssertEquals() {
        PhasesMap other = new PhasesMap(2, 2, pixArray, phases);
        map.assertEquals(other);
    }



    @Test(expected = AssertionError.class)
    public void testAssertEqualsException1() {
        PhasesMap other =
                new PhasesMap(2, 2, pixArray, new Crystal[] { new Silicon(),
                        new IronBCC(), new ZirconiumAlpha() });
        map.assertEquals(other);
    }



    @Test(expected = AssertionError.class)
    public void testAssertEqualsException2() {
        PhasesMap other =
                new PhasesMap(2, 2, pixArray, new Crystal[] { new Silicon(),
                        new ZirconiumAlpha() });
        map.assertEquals(other);
    }



    @Test
    public void testCreateMapIntInt() {
        PhasesMap other = map.createMap(3, 3);

        assertEquals(3, other.width);
        assertEquals(3, other.height);
        assertEquals(9, other.size);

        for (int i = 0; i < 9; i++)
            assertEquals(0, other.getPixValue(i));
    }



    @Test
    public void testDuplicate() {
        PhasesMap other = map.duplicate();

        assertEquals(2, other.width);
        assertEquals(2, other.height);
        assertEquals(4, other.size);

        assertEquals(0, other.getPixValue(0));
        assertEquals(1, other.getPixValue(1));
        assertEquals(2, other.getPixValue(2));
        assertEquals(1, other.getPixValue(3));
    }



    @Test
    public void testGetPhases() {
        assertEquals(2, map.getPhases().length);
        assertEquals(new Silicon(), map.getPhases()[0]);
        assertEquals(new IronBCC(), map.getPhases()[1]);
    }



    @Test
    public void testGetPixelInt() {
        assertEquals("0: Not indexed", map.getPixel(0).getValueLabel());
        assertEquals("1: Silicon", map.getPixel(1).getValueLabel());
        assertEquals("2: Iron BCC", map.getPixel(2).getValueLabel());
        assertEquals("1: Silicon", map.getPixel(3).getValueLabel());
    }



    @Test
    public void testGetValidFileFormats() {
        assertEquals(1, map.getValidFileFormats().length);
        assertEquals("bmp", map.getValidFileFormats()[0]);
    }



    @Test
    public void testIsCorrect() {
        assertTrue(map.isCorrect());

        map.pixArray[2] = 4; // Incorrect phase
        assertFalse(map.isCorrect());
    }



    @Test
    public void testPhasesMapIntInt() {
        PhasesMap other = new PhasesMap(3, 3);

        assertEquals(3, other.width);
        assertEquals(3, other.height);
        assertEquals(9, other.size);

        for (int i = 0; i < 9; i++)
            assertEquals(0, other.getPixValue(i));
    }



    @Test
    public void testPhasesMapIntIntByteArrayCrystalArray() {
        assertEquals(2, map.width);
        assertEquals(2, map.height);
        assertEquals(4, map.size);

        assertEquals(0, map.getPixValue(0));
        assertEquals(1, map.getPixValue(1));
        assertEquals(2, map.getPixValue(2));
        assertEquals(1, map.getPixValue(3));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPhasesMapIntIntByteArrayCrystalArrayExceptionNumberPhases1() {
        new PhasesMap(2, 2, new byte[] { 0, 1, 2, 3 }, phases);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPhasesMapIntIntByteArrayCrystalArrayExceptionSize() {
        new PhasesMap(1, 1, pixArray, phases);
    }



    @Test
    public void testPhasesMapPhasesMap() {
        PhasesMap other = new PhasesMap(map);

        assertEquals(2, other.width);
        assertEquals(2, other.height);
        assertEquals(4, other.size);

        assertEquals(0, other.getPixValue(0));
        assertEquals(1, other.getPixValue(1));
        assertEquals(2, other.getPixValue(2));
        assertEquals(1, other.getPixValue(3));
    }



    @Test
    public void testSetPixValueIntInt() {
        assertEquals(1, map.getPixValue(1));
        map.setPixValue(1, 0);
        assertEquals(0, map.getPixValue(1));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPixValueIntIntException1() {
        map.setPixValue(1, -1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPixValueIntIntException2() {
        map.setPixValue(1, 3);
    }



    @Test
    public void testSetPhases() {
        Crystal[] newPhases = new Crystal[] { new IronBCC(), new Silicon() };
        map.setPhases(newPhases);

        assertEquals(2, map.getPhases().length);
        assertEquals(new IronBCC(), map.getPhases()[0]);
        assertEquals(new Silicon(), map.getPhases()[1]);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPhasesException() {
        Crystal[] newPhases = new Crystal[0];
        map.setPhases(newPhases);
    }



    @Test
    public void testSetPixValueIntIntInt() {
        assertEquals(1, map.getPixValue(1, 0));
        map.setPixValue(1, 0, 0);
        assertEquals(0, map.getPixValue(1, 0));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testValidate() {
        map.pixArray[2] = 4; // Incorrect phase
        map.validate();
    }

}
