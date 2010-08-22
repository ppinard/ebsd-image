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

import org.junit.Test;

import rmlimage.core.BinMap;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class MapMathTest {

    private PhasesMap createSrc1PhasesMap() {
        Crystal[] phases = new Crystal[] { new Silicon(), new IronBCC() };
        byte[] pixArray = new byte[] { 0, 1, 2, 1 };

        return new PhasesMap(2, 2, pixArray, phases);
    }



    private BinMap createSrc2BinMap() {
        return new BinMap(2, 2, new byte[] { 1, 0, 1, 0 });
    }



    private PhasesMap createSrc2PhasesMap() {
        Crystal[] phases = new Crystal[] { new Silicon(), new IronBCC() };
        byte[] pixArray = new byte[] { 2, 0, 0, 0 };

        return new PhasesMap(2, 2, pixArray, phases);
    }



    @Test
    public void testAdditionMapMapDoubleDoubleMap() {
        rmlimage.core.MapMath.addHandler(MapMath.class);

        PhasesMap src1 = createSrc1PhasesMap();
        PhasesMap src2 = createSrc2PhasesMap();
        PhasesMap dest = src1.createMap(src1.width, src1.height);

        rmlimage.core.MapMath.addition(src1, src2, 1.0, 0.0, dest);

        testAdditionPhasesMap(dest);
    }



    private void testAdditionPhasesMap(PhasesMap dest) {
        assertEquals(2, dest.width);
        assertEquals(2, dest.height);

        assertEquals(2, dest.pixArray[0]);
        assertEquals(1, dest.pixArray[1]);
        assertEquals(2, dest.pixArray[2]);
        assertEquals(1, dest.pixArray[3]);

        assertEquals(2, dest.getPhases().length);
    }



    @Test
    public void testAdditionPhasesMapPhasesMapPhasesMap() {
        PhasesMap src1 = createSrc1PhasesMap();
        PhasesMap src2 = createSrc2PhasesMap();
        PhasesMap dest = src1.createMap(src1.width, src1.height);

        MapMath.addition(src1, src2, dest);

        testAdditionPhasesMap(dest);
    }



    @Test
    public void testAndMapMapMap() {
        // Add handler
        rmlimage.core.MapMath.addHandler(MapMath.class);

        PhasesMap src1 = createSrc1PhasesMap();
        BinMap src2 = createSrc2BinMap();
        PhasesMap dest = src1.duplicate();

        rmlimage.core.MapMath.and(src1, src2, dest);

        testAndPhasesMap(dest);
    }



    private void testAndPhasesMap(PhasesMap dest) {
        assertEquals(2, dest.width);
        assertEquals(2, dest.height);

        assertEquals(0, dest.pixArray[0]);
        assertEquals(0, dest.pixArray[1]);
        assertEquals(2, dest.pixArray[2]);
        assertEquals(0, dest.pixArray[3]);

        assertEquals(2, dest.getPhases().length);
    }



    @Test
    public void testAndPhasesMapBinMapPhasesMap() {
        PhasesMap src1 = createSrc1PhasesMap();
        BinMap src2 = createSrc2BinMap();
        PhasesMap dest = src1.duplicate();

        MapMath.and(src1, src2, dest);

        testAndPhasesMap(dest);
    }

}
