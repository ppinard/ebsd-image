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
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class HoughMapSaverTest extends TestCase {

    private File file;

    private HoughMapSaver saver;



    @Before
    public void setUp() throws Exception {
        file = new File(createTempDir(), "houghmap.bmp");
        saver = new HoughMapSaver();
    }



    private void testHoughMap(HoughMap map) throws IOException {
        HoughMap other = new HoughMapLoader().load(file);
        other.assertEquals(map);
    }



    @Test
    public void testSaveHoughMapFile() throws IOException {
        ByteMap original = (ByteMap) load("org/ebsdimage/testdata/Lena.bmp");
        HoughMap map = Transform.hough(original, Math.toRadians(1.5));

        saver.save(map, file);

        testHoughMap(map);
    }

}
