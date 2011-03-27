/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RplLoaderTest extends TestCase {

    private RplLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        loader = new RplLoader();
        file = getFile("org/ebsdimage/testdata/warp-x-map.rpl");
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("org/ebsdimage/testdata/warp-x-map.raw")));
    }



    @Test
    public void testLoadFile() throws IOException {
        RplFile rpl = loader.load(file);
        testRplFile(rpl);
    }



    @Test
    public void testLoadFileObject() throws IOException {
        RplFile rpl = loader.load(file, null);
        testRplFile(rpl);
    }



    private void testRplFile(RplFile rpl) {
        assertEquals(1, rpl.dataLength);
        assertEquals(168, rpl.width);
        assertEquals(128, rpl.height);
        assertTrue(rpl.isBigEndian());
        assertFalse(rpl.isSigned());
    }
}
