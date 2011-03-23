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
import java.util.Map;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlshared.util.Arrays;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhaseMapTest extends TestCase {

    private PhaseMap phaseMap;

    private Crystal phase1;

    private Crystal phase2;

    private byte[] pixArray;



    @Before
    public void setUp() throws Exception {
        phase1 = CrystalFactory.silicon();
        phase2 = CrystalFactory.ferrite();

        HashMap<Integer, Crystal> items = new HashMap<Integer, Crystal>();
        items.put(1, phase1);
        items.put(3, phase2);

        pixArray = new byte[] { 0, 1, 3, 1 };

        phaseMap = new PhaseMap(2, 2, pixArray, items);
    }



    @Test
    public void testCreateMapIntInt() {
        PhaseMap other = phaseMap.createMap(3, 3);

        assertEquals(3, other.width);
        assertEquals(3, other.height);
        assertEquals(1, other.getItems().size());
        assertEquals(PhaseMap.NO_PHASE, other.getItems().get(0));

        for (int i = 0; i < 9; i++)
            assertEquals(0, other.getPixValue(i));
    }



    @Test
    public void testDuplicate() {
        PhaseMap other = phaseMap.duplicate();

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, Crystal> items = other.getItems();
        assertEquals(3, items.size());
        assertEquals(PhaseMap.NO_PHASE, items.get(0));
        assertEquals(phase1, items.get(1));
        assertEquals(phase2, items.get(3));

        assertEquals(0, other.getPixValue(0));
        assertEquals(1, other.getPixValue(1));
        assertEquals(3, other.getPixValue(2));
        assertEquals(1, other.getPixValue(3));
    }



    @Test
    public void testGetPhases() {
        Crystal[] phases = phaseMap.getPhases();

        assertEquals(2, phases.length);
        assertTrue(Arrays.contains(phases, phase1));
        assertTrue(Arrays.contains(phases, phase2));
    }



    @Test
    public void testPhaseMapIntInt() {
        PhaseMap other = new PhaseMap(2, 2);

        assertEquals(2, other.width);
        assertEquals(2, other.height);
        assertEquals(1, other.getItems().size());
        assertEquals(PhaseMap.NO_PHASE, other.getItems().get(0));
    }



    @Test
    public void testPhaseMapIntIntByteArrayHashMap() {
        assertEquals(2, phaseMap.width);
        assertEquals(2, phaseMap.height);

        Map<Integer, Crystal> items = phaseMap.getItems();
        assertEquals(3, items.size());
        assertEquals(PhaseMap.NO_PHASE, items.get(0));
        assertEquals(phase1, items.get(1));
        assertEquals(phase2, items.get(3));

        assertEquals(0, phaseMap.getPixValue(0));
        assertEquals(1, phaseMap.getPixValue(1));
        assertEquals(3, phaseMap.getPixValue(2));
        assertEquals(1, phaseMap.getPixValue(3));
    }



    @Test
    public void testPhaseMapIntIntHashMap() {
        HashMap<Integer, Crystal> items = new HashMap<Integer, Crystal>();
        items.put(1, phase1);
        PhaseMap other = new PhaseMap(2, 2, items);

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        Map<Integer, Crystal> otherItems = other.getItems();
        assertEquals(2, otherItems.size());
        assertEquals(PhaseMap.NO_PHASE, otherItems.get(0));
        assertEquals(phase1, otherItems.get(1));

        for (int i = 0; i < 4; i++)
            assertEquals(0, other.getPixValue(i));
    }
}
