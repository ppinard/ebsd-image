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
package org.ebsdimage.plugin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.RawLoader;
import org.ebsdimage.plugin.UnWarp;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.module.integer.core.IntMap;
import rmlshared.io.FileUtil;

public class UnWarpTest {

    @Test
    public void getGenericName() {
        File srcDir = new File(FileUtil.getTempDirFile(), "Project2Images");
        String genericName = new UnWarp().getGenericName(srcDir);
        assertEquals("Project2", genericName);
    }



    @Test
    public void unwarp() throws IOException {
        // Get the src file
        File srcFile = TestCase.getFile("org/ebsdimage/plugin/UnWarpSrc.jpg");
        ByteMap srcMap = new UnWarp().load(srcFile, null);

        // Get the warp files
        File xWarpFile = TestCase.getFile("org/ebsdimage/io/warp-x-map.raw");
        IntMap xWarpMap = (IntMap) new RawLoader().load(xWarpFile);
        File yWarpFile = TestCase.getFile("org/ebsdimage/io/warp-y-map.raw");
        IntMap yWarpMap = (IntMap) new RawLoader().load(yWarpFile);

        // Get the dest map
        ByteMap destMap = new ByteMap(srcMap.width, srcMap.height);

        // Unwarp
        new UnWarp().unwarp(srcMap, xWarpMap, yWarpMap, destMap);

        // Load the expected map
        ByteMap xpctMap = (ByteMap) TestCase.load("org/ebsdimage/plugin/UnWarpDest.bmp");

        destMap.assertEquals(xpctMap);
    }

}
