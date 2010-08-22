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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.vendors.tsl.core.TslMMapTester;
import org.junit.Test;

public class TslMMapLoaderTest extends TslMMapTester {

    private File file;



    public TslMMapLoaderTest() throws Exception {
        file = getFile("org/ebsdimage/vendors/tsl/testdata/scan1.zip");
        mmap = new TslMMapLoader().load(file);
    }



    @Test
    public void testIsTslMMap() {
        assertTrue(TslMMapLoader.isTslMMap(file));
        assertFalse(TslMMapLoader.isTslMMap(getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml")));
    }

}
