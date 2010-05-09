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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.crystals.Silicon;

public class CrystalSaverTest {

    private Crystal crystal;
    private File file;



    @Before
    public void setUp() throws Exception {
        crystal = new Silicon();
        file = new File(FileUtil.getTempDirFile(), "crystal.xml");
    }



    @After
    public void tearDown() throws Exception {
        if (file.exists())
            if (!file.delete())
                throw new RuntimeException("Could not delete (" + file + ").");
    }



    @Test
    public void testSaveObjectXmlFile() throws IOException {
        new CrystalSaver().save(crystal, file);

        // Test
        Crystal other = new CrystalLoader().load(file);
        assertEquals(crystal.name, other.name);
        assertTrue(crystal.unitCell.equals(other.unitCell, 1e-6));
        assertTrue(crystal.atoms.equals(other.atoms, 1e-6));
    }

}
