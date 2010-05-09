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
package org.ebsdimage.vendors.hkl.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.vendors.hkl.core.HklMMapTester;
import org.ebsdimage.vendors.hkl.io.CtfLoader;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

public class CtfLoaderTest extends HklMMapTester {

    private File file;



    public CtfLoaderTest() throws Exception {
        file = getFile("org/ebsdimage/vendors/hkl/testdata/Project19.ctf");

        Crystal copperPhase =
                new CrystalLoader()
                        .load(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml"));

        mmap =
                new CtfLoader().load(file,
                        EbsdMetadata.DEFAULT_WORKING_DISTANCE,
                        EbsdMetadata.DEFAULT_CALIBRATION,
                        new Crystal[] { copperPhase });
    }



    @Test
    public void testGetPhaseNames() throws IOException {
        String[] phaseNames = CtfLoader.getPhaseNames(file);

        assertEquals(1, phaseNames.length);
        assertEquals("Copper", phaseNames[0]);
    }

}
