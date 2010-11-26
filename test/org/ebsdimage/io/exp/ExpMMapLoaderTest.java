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
package org.ebsdimage.io.exp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.core.exp.ExpMMapTester;
import org.junit.Test;

public class ExpMMapLoaderTest extends ExpMMapTester {

    private ExpMMapLoader loader;

    private File file;



    public ExpMMapLoaderTest() throws Exception {
        file = getFile("org/ebsdimage/testdata/expmmap.zip");
        loader = new ExpMMapLoader();
        mmap = loader.load(file);
    }



    @Test
    public void testIsExpMMap() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("org/ebsdimage/testdata/houghmap.bmp")));
    }

}
