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
package org.ebsdimage.vendors.tsl.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.Loader;
import rmlshared.io.Saver;

public class IOTest extends TestCase {

    private IO handler;



    @Before
    public void setUp() throws Exception {
        handler = new IO();
    }



    @Test
    public void testGetSaverTslMMap() throws IOException {
        TslMMap mmap =
                new TslMMapLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/scan1.zip"));

        Saver saver = handler.getSaver(mmap);
        assertNotNull(saver);
        assertTrue(saver instanceof TslMMapSaver);
    }



    @Test
    public void testGetSaverTslMMap2() throws IOException {
        TslMMap mmap =
                new TslMMapLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/scan1.zip"));

        rmlimage.io.IO.addHandler(org.ebsdimage.vendors.tsl.io.IO.class);
        Saver saver = rmlimage.io.IO.getSaver(mmap);
        assertNotNull(saver);
        assertTrue(saver instanceof TslMMapSaver);
    }



    @Test
    public void testGetLoaderTslMMap() throws IOException {
        Loader loader =
                handler.getLoader(getFile("org/ebsdimage/vendors/tsl/testdata/scan1.zip"));
        assertNotNull(loader);
        assertTrue(loader instanceof TslMMapLoader);

    }



    @Test
    // Test call to handler
    public void testGetLoaderTslMMa2() throws IOException {
        rmlimage.io.IO.addHandler(org.ebsdimage.vendors.tsl.io.IO.class);
        Loader loader =
                rmlimage.io.IO.getLoader(getFile("org/ebsdimage/vendors/tsl/testdata/scan1.zip"));
        assertNotNull(loader);
        assertTrue(loader instanceof TslMMapLoader);
    }
}
