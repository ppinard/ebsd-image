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
package crystallography.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CrystalLoaderTest extends TestCase {

    private CrystalLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        loader = new CrystalLoader();
        file = getFile("crystallography/testdata/silicon.xml");
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("crystallography/testdata/forsterite.cif")));
    }



    @Test
    public void testLoadFile() throws IOException {
        Crystal crystal = loader.load(file);
        assertEquals("Silicon", crystal.name);
    }



    @Test
    public void testLoadFileObject() throws IOException {
        Crystal crystal = loader.load(file, null);
        assertEquals("Silicon", crystal.name);
    }
}
