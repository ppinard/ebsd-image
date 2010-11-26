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
import static org.junit.Assert.assertSame;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.module.integer.core.IntMap;

public class RawLoaderTest extends TestCase {

    private RawLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        loader = new RawLoader();
        file = getFile("org/ebsdimage/testdata/warp-y-map.raw");
    }



    @Test
    public void getDimension() throws IOException {
        Dimension dimension = loader.getDimension(file);
        assertEquals(168, dimension.width);
        assertEquals(128, dimension.height);
    }



    @Test
    public void load() throws IOException {
        // Load the IntMap and convert it to a ByteMap for easier comparison
        IntMap intMap = loader.load(file);
        ByteMap byteMap =
                rmlimage.module.integer.core.Conversion.toByteMap(intMap);
        byteMap.clearProperties();

        // Load the expected map
        ByteMap xpctMap =
                (ByteMap) load("org/ebsdimage/testdata/warp-y-map.bmp");
        xpctMap.clearProperties();

        byteMap.assertEquals(xpctMap);
    }



    @Test
    public void load2() throws IOException {
        // Load the IntMap and convert it to a ByteMap for easier comparison
        IntMap intMap = new IntMap(168, 128);
        IntMap map = loader.load(file, intMap);
        ByteMap byteMap =
                rmlimage.module.integer.core.Conversion.toByteMap(intMap);
        byteMap.clearProperties();

        // Check the IntMap returned is the same as the one sent as an argument
        assertSame(intMap, map);

        // Load the expected map
        ByteMap xpctMap =
                (ByteMap) load("org/ebsdimage/testdata/warp-y-map.bmp");
        xpctMap.clearProperties();

        byteMap.assertEquals(xpctMap);
    }

}
