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
package org.ebsdimage.core.exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class CurrentMapsSaverTest {

    private CurrentMapsFileSaver saveMaps;

    private File path;

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        saveMaps = new CurrentMapsFileSaver(true, true, true, true, true, true);
        path = new File(FileUtil.getTempDirFile(), "saveMaps");

        exp = ExpTester.createExp(saveMaps);
        exp.setDir(path);
    }



    @After
    public void tearDown() throws Exception {
        if (path.exists())
            FileUtil.rmdir(path);
    }



    @Test
    public void testEquals() {
        CurrentMapsFileSaver other =
                new CurrentMapsFileSaver(true, true, true, true, true, true);
        assertFalse(saveMaps == other);
        assertEquals(other, saveMaps);
    }



    @Test
    public void testSaveHoughMap() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSaveMapOperationBinMap() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSaveMapOperationByteMap() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSaveMapOperationHoughMap() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSaveMapHoughPeaksOverlay() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSaveMapSolutionOverlay() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSaveMaps() {
        CurrentMapsFileSaver tmpSaveMaps = new CurrentMapsFileSaver();
        assertEquals(CurrentMapsFileSaver.DEFAULT_SAVEMAPS_ALL,
                tmpSaveMaps.saveAllMaps);
        assertEquals(CurrentMapsFileSaver.DEFAULT_SAVE_PATTERNMAP,
                tmpSaveMaps.savePatternMap);
        assertEquals(CurrentMapsFileSaver.DEFAULT_SAVE_HOUGHMAP,
                tmpSaveMaps.saveHoughMap);
        assertEquals(CurrentMapsFileSaver.DEFAULT_SAVE_PEAKSMAP,
                tmpSaveMaps.savePeaksMap);
        assertEquals(CurrentMapsFileSaver.DEFAULT_SAVE_SOLUTIONOVERLAY,
                tmpSaveMaps.saveSolutionOverlay);
    }



    @Test
    public void testSaveMapsStringBooleanBooleanBoolean() {
        CurrentMapsFileSaver tmpSaveMaps;

        tmpSaveMaps =
                new CurrentMapsFileSaver(false, true, false, false, true, true);
        assertFalse(tmpSaveMaps.saveAllMaps);
        assertTrue(tmpSaveMaps.savePatternMap);
        assertFalse(tmpSaveMaps.saveHoughMap);
        assertFalse(tmpSaveMaps.savePeaksMap);
        assertTrue(tmpSaveMaps.saveHoughPeaksOverlay);
        assertTrue(tmpSaveMaps.saveSolutionOverlay);

        tmpSaveMaps =
                new CurrentMapsFileSaver(true, true, false, false, false, false);
        assertTrue(tmpSaveMaps.saveAllMaps);
        assertTrue(tmpSaveMaps.savePatternMap);
        assertTrue(tmpSaveMaps.saveHoughMap);
        assertTrue(tmpSaveMaps.savePeaksMap);
        assertTrue(tmpSaveMaps.saveHoughPeaksOverlay);
        assertTrue(tmpSaveMaps.saveSolutionOverlay);
    }



    @Test
    public void testSavePatternMap() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testSavePeaksMap() {
        // Cannot be tested since experiment needs to be running.
        assertTrue(true);
    }



    @Test
    public void testToString() {
        CurrentMapsFileSaver tmpSaveMaps =
                new CurrentMapsFileSaver(false, false, false, false, false,
                        false);
        String expected =
                "CurrentMapsFileSaver [saveAllMaps=false, savePatternMap=false, saveHoughMap=false, savePeaksMap=false, saveHoughPeaksOverlay=false, saveSolutionOverlay=false]";
        assertEquals(expected, tmpSaveMaps.toString());
    }
}
