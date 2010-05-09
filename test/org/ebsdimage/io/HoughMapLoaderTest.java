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

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

public class HoughMapLoaderTest {

    private HoughMapLoader loader;
    private File file;



    @Before
    public void setUp() throws Exception {
        file = FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp");
        loader = new HoughMapLoader();
    }



    @Test
    public void testLoad() throws IOException {
        HoughMap map = loader.load(file);

        testHoughMap(map);
    }



    private void testHoughMap(HoughMap map) throws IOException {
        ByteMap original =
                (ByteMap) rmlimage.io.IO.load(FileUtil
                        .getFile("org/ebsdimage/testdata/pattern.bmp"));
        HoughMap other = Transform.hough(original, toRadians(1.0));

        assertEquals(other.width, map.width);
        assertEquals(other.height, map.height);
        assertEquals(other.size, map.size);

        assertEquals(other.deltaR, map.deltaR, 1e-6);
        assertEquals(other.deltaTheta, map.deltaTheta, 1e-6);
        assertEquals(other.rMax, map.rMax, 1e-6);
        assertEquals(other.rMin, map.rMin, 1e-6);
        assertEquals(other.thetaMax, map.thetaMax, 1e-6);
        assertEquals(other.thetaMin, map.thetaMin, 1e-6);

        other.assertEquals(map);
    }

}
