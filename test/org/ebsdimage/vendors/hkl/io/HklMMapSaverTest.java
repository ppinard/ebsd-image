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

import java.io.File;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMMapTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

public class HklMMapSaverTest extends HklMMapTester {

    private static File zipFile = new File(FileUtil.getTempDirFile(),
            "Project19.zip");



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Create mmap from ctf
        File file = getFile("org/ebsdimage/vendors/hkl/testdata/Project19.ctf");

        Crystal copperPhase =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml"));

        HklMMap tempMMap =
                new CtfLoader().load(file,
                        EbsdMetadata.DEFAULT_WORKING_DISTANCE, new Camera(0.1,
                                0.2, 0.3), new Crystal[] { copperPhase });

        // Save mmap to temp dir
        new HklMMapSaver().save(tempMMap, zipFile);
    }



    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        mmap = new HklMMapLoader().load(zipFile);
    }



    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (zipFile.exists())
            if (!(zipFile.delete()))
                throw new RuntimeException("File (" + zipFile.getAbsolutePath()
                        + ") cannot be deleted.");
    }

}
