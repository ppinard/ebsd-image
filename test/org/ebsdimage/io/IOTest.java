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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.io.exp.ExpMMapLoader;
import org.ebsdimage.io.exp.ExpMMapSaver;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import rmlshared.io.Loader;
import rmlshared.io.Saver;

public class IOTest extends TestCase {

    private IO handler;



    @Before
    public void setUp() throws Exception {
        handler = new IO();
    }



    @Test
    public void testGetLoaderHoughMap() throws IOException {
        Loader loader =
                handler.getLoader(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        assertNotNull(loader);
        assertTrue(loader instanceof HoughMapLoader);

    }



    @Test
    // Test call to handler
    public void testGetLoaderHoughMap2() throws IOException {
        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
        Loader loader =
                rmlimage.io.IO.getLoader(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        assertNotNull(loader);
        assertTrue(loader instanceof HoughMapLoader);
    }



    @Test
    public void testGetLoaderPhasesMap() throws IOException {
        Loader loader =
                handler.getLoader(FileUtil.getFile("org/ebsdimage/io/phasesmap.bmp"));
        assertNotNull(loader);
        assertTrue(loader instanceof PhasesMapLoader);

    }



    @Test
    // Test call to handler
    public void testGetLoaderPhasesMap2() throws IOException {
        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
        Loader loader =
                rmlimage.io.IO.getLoader(FileUtil.getFile("org/ebsdimage/io/phasesmap.bmp"));
        assertNotNull(loader);
        assertTrue(loader instanceof PhasesMapLoader);

    }



    @Test
    public void testGetLoaderExpMMap() throws IOException {
        Loader loader =
                handler.getLoader(FileUtil.getFile("org/ebsdimage/testdata/expmmap.zip"));
        assertNotNull(loader);
        assertTrue(loader instanceof ExpMMapLoader);

    }



    @Test
    // Test call to handler
    public void testGetLoaderExpMMap2() throws IOException {
        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
        Loader loader =
                rmlimage.io.IO.getLoader(FileUtil.getFile("org/ebsdimage/testdata/expmmap.zip"));
        assertNotNull(loader);
        assertTrue(loader instanceof ExpMMapLoader);

    }



    @Test
    public void testGetSaverHoughMap() throws IOException {
        HoughMap houghMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));

        Saver saver = handler.getSaver(houghMap);
        assertNotNull(saver);
        assertTrue(saver instanceof HoughMapSaver);
    }



    @Test
    // Test call to handler
    public void testGetSaverHoughMap2() throws IOException {
        HoughMap houghMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));

        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
        Saver saver = rmlimage.io.IO.getSaver(houghMap);
        assertNotNull(saver);
        assertTrue(saver instanceof HoughMapSaver);
    }



    @Test
    public void testGetSaverPhasesMap() throws IOException {
        PhasesMap map = new PhasesMap(2, 2);

        Saver saver = handler.getSaver(map);
        assertNotNull(saver);
        assertTrue(saver instanceof PhasesMapSaver);
    }



    @Test
    public void testGetSaverPhasesMap2() throws IOException {
        PhasesMap map = new PhasesMap(2, 2);

        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
        Saver saver = rmlimage.io.IO.getSaver(map);
        assertNotNull(saver);
        assertTrue(saver instanceof PhasesMapSaver);
    }



    @Test
    public void testGetSaverExpMMap() throws IOException {
        ExpMMap mmap =
                new ExpMMapLoader().load(getFile("org/ebsdimage/testdata/expmmap.zip"));

        Saver saver = handler.getSaver(mmap);
        assertNotNull(saver);
        assertTrue(saver instanceof ExpMMapSaver);
    }



    @Test
    public void testGetSaverExpMMap2() throws IOException {
        ExpMMap mmap =
                new ExpMMapLoader().load(getFile("org/ebsdimage/testdata/expmmap.zip"));

        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
        Saver saver = rmlimage.io.IO.getSaver(mmap);
        assertNotNull(saver);
        assertTrue(saver instanceof ExpMMapSaver);
    }

}
