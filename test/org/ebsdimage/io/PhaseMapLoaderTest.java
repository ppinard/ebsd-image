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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.PhaseMap;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class PhaseMapLoaderTest extends TestCase {

    private PhaseMapLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        file = getFile("org/ebsdimage/testdata/phasemap.bmp");
        loader = new PhaseMapLoader();
    }



    @Test
    public void testGetTaskProgress() {
        assertEquals(0, loader.getTaskProgress(), 1e-6);
    }



    @Test
    public void testLoadFile() throws IOException {
        PhaseMap map = loader.load(file);
        testPhasesMap(map);
    }



    @Test
    public void testLoadFileMap() throws IOException {
        PhaseMap map = loader.load(file, null);
        testPhasesMap(map);
    }



    private void testPhasesMap(PhaseMap map) {
        assertEquals(2, map.width);
        assertEquals(2, map.height);
        assertEquals(4, map.size);

        assertEquals(0, map.pixArray[0]);
        assertEquals(1, map.pixArray[1]);
        assertEquals(3, map.pixArray[2]);
        assertEquals(1, map.pixArray[3]);

        Map<Integer, Crystal> items = map.getItems();
        assertEquals(3, items.size());
        assertEquals(PhaseMap.NO_PHASE, items.get(0), 1e-6);
        assertEquals(CrystalFactory.silicon(), items.get(1), 1e-6);
        assertEquals(CrystalFactory.ferrite(), items.get(3), 1e-6);
    }

}
