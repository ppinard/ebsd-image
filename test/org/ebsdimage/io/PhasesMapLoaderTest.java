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
package org.ebsdimage.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.io.PhasesMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class PhasesMapLoaderTest {

    private PhasesMapLoader loader;
    private File file;



    @Before
    public void setUp() throws Exception {
        file = FileUtil.getFile("org/ebsdimage/io/phasesmap.bmp");
        loader = new PhasesMapLoader();
    }



    @Test
    public void testGetTaskProgress() {
        assertEquals(0, loader.getTaskProgress(), 1e-6);
    }



    @Test
    public void testLoadFile() throws IOException {
        PhasesMap map = loader.load(file);
        testPhasesMap(map);
    }



    @Test
    public void testLoadFileMap() throws IOException {
        PhasesMap map = loader.load(file, null);
        testPhasesMap(map);
    }



    private void testPhasesMap(PhasesMap map) {
        assertEquals(2, map.width);
        assertEquals(2, map.height);
        assertEquals(4, map.size);

        assertEquals(0, map.pixArray[0]);
        assertEquals(1, map.pixArray[1]);
        assertEquals(2, map.pixArray[2]);
        assertEquals(1, map.pixArray[3]);

        assertEquals(2, map.getPhases().length);
        assertTrue(new Silicon().equals(map.getPhases()[0], 1e-6));
        assertTrue(new IronBCC().equals(map.getPhases()[1], 1e-6));
    }

}
