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
package org.ebsdimage.vendors.hkl.io;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Before
    public void setUp() throws Exception {
    }



    @Test
    public void testGetPatternImagesDir() {
        File ctfFile =
                FileUtil.getFile("org/ebsdimage/vendors/hkl/testdata/Project19.ctf");
        File expected =
                FileUtil.getFile("org/ebsdimage/vendors/hkl/testdata/Project19Images");

        assertEquals(expected, Utils.getPatternImagesDir(ctfFile));
    }

}
