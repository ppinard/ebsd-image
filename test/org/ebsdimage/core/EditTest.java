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

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Filter;
import rmlimage.core.ROI;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class EditTest extends TestCase {

    private PhasesMap createPhasesMap() {
        Crystal[] phases = new Crystal[] { new Silicon(), new IronBCC() };
        byte[] pixArray = new byte[] { 0, 1, 2, 1, 0, 0, 0, 0, 2 };
        return new PhasesMap(3, 3, pixArray, phases);
    }



    @Test
    public void testCopyPhasesMapROIPhasesMapIntInt() {
        // Source phases map
        PhasesMap srcMap = createPhasesMap();

        // Destination phases map
        PhasesMap destMap = new PhasesMap(2, 2);

        // Copy
        Edit.copy(srcMap, new ROI(0, 0, 1, 1), destMap, 0, 0);

        // Test
        assertEquals(2, destMap.height);
        assertEquals(2, destMap.width);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
        assertEquals(0, destMap.getPhases().length);
    }



    @Test
    public void testCropHoughMapDouble() {
        // Create a HoughMap
        ByteMap pattern =
                (ByteMap) load("org/ebsdimage/testdata/pattern_masked.bmp");
        HoughMap houghMap = Transform.hough(pattern, toRadians(1.0));

        // Apply median on hough
        Filter.median(houghMap);

        HoughMap croppedMap = Edit.crop(houghMap, 50.0);
        assertEquals(houghMap.width, croppedMap.width);
        assertEquals(141, croppedMap.height);
        assertEquals(49.7777, croppedMap.rMax, 0.001);
        assertEquals(houghMap.deltaR, croppedMap.deltaR, 0.001);

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

        for (int i = 1; i <= (int) houghMap.rMax; i++) {
            Logger.getLogger("ebsd").info("Cropping radius: " + i);
            HoughMap croppedMap = Edit.crop(houghMap, i);
            assertEquals(houghMap.deltaR, croppedMap.deltaR, 1e-6);
            assertEquals(houghMap.deltaTheta, croppedMap.deltaTheta, 1e-6);
        }
    }



    @Test
    public void testCropMapROIPhasesMap() {
        // Add handler
        rmlimage.core.Edit.addHandler(Edit.class);

        // Original phases map
        PhasesMap map = createPhasesMap();

        // Crop
        PhasesMap cropMap =
                (PhasesMap) rmlimage.core.Edit.crop(map, new ROI(0, 0, 1, 1));

        // Test
        testCropPhasesMap(cropMap);
    }



    private void testCropPhasesMap(PhasesMap cropMap) {
        assertEquals(2, cropMap.height);
        assertEquals(2, cropMap.width);
        assertEquals(0, cropMap.pixArray[0]);
        assertEquals(1, cropMap.pixArray[1]);
        assertEquals(1, cropMap.pixArray[2]);
        assertEquals(0, cropMap.pixArray[3]);
        assertEquals(2, cropMap.getPhases().length);
    }



    @Test
    public void testCropPhasesMapROI() {
        // Original phases map
        PhasesMap map = createPhasesMap();

        // Crop
        PhasesMap cropMap = Edit.crop(map, new ROI(0, 0, 1, 1));

        // Test
        testCropPhasesMap(cropMap);
    }
}
