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
package org.ebsdimage.vendors.hkl.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import rmlshared.io.Loader;
import rmlshared.io.Saver;

public class IOTest {

    private IO handler;



    @Before
    public void setUp() throws Exception {
        handler = new IO();
    }



    @Test
    public void testGetLoaderHklMMap() throws IOException {
        Loader loader =
                handler.getLoader(FileUtil
                        .getFile("org/ebsdimage/vendors/hkl/testdata/Project19.zip"));
        assertNotNull(loader);
        assertTrue(loader instanceof HklMMapLoader);

    }



    @Test
    // Test call to handler
    public void testGetLoaderHklMMap2() throws IOException {
        rmlimage.io.IO.addHandler(org.ebsdimage.vendors.hkl.io.IO.class);
        Loader loader =
                rmlimage.io.IO.getLoader(FileUtil
                        .getFile("org/ebsdimage/vendors/hkl/testdata/Project19.zip"));
        assertNotNull(loader);
        assertTrue(loader instanceof HklMMapLoader);
    }



    @Test
    public void testGetSaverHklMMap() throws IOException {
        HklMMap mmap =
                new HklMMapLoader().load(FileUtil
                        .getFile("org/ebsdimage/vendors/hkl/testdata/Project19.zip"));

        Saver saver = handler.getSaver(mmap);
        assertNotNull(saver);
        assertTrue(saver instanceof HklMMapSaver);
    }



    @Test
    public void testGetSaverHklMMap2() throws IOException {
        HklMMap mmap =
                new HklMMapLoader().load(FileUtil
                        .getFile("org/ebsdimage/vendors/hkl/testdata/Project19.zip"));

        rmlimage.io.IO.addHandler(org.ebsdimage.vendors.hkl.io.IO.class);
        Saver saver = rmlimage.io.IO.getSaver(mmap);
        assertNotNull(saver);
        assertTrue(saver instanceof HklMMapSaver);
    }

}
