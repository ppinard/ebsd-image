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
package crystallography.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.crystals.Silicon;

public class CrystalLoaderTest {

    private File file;



    @Before
    public void setUp() throws Exception {
        file = FileUtil.getFile("crystallography/testdata/silicon.xml");
    }



    @Test
    public void testLoadFile() throws IOException {
        Crystal crystal = new CrystalLoader().load(file);
        Crystal expected = new Silicon();

        assertEquals(expected.name, crystal.name);
        assertTrue(expected.unitCell.equals(crystal.unitCell, 1e-6));
        assertTrue(expected.atoms.equals(crystal.atoms, 1e-6));
    }

}
