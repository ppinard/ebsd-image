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

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMMapTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

public class CtfSaverTest extends HklMMapTester {

    private static File file =
            new File(FileUtil.getTempDirFile(), "Project19.ctf");



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        HklMMap old =
                new HklMMapLoader()
                        .load(FileUtil
                                .getFile("org/ebsdimage/vendors/hkl/testdata/Project19.zip"));

        new CtfSaver().save(old, file);
    }



    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (file.exists())
            if (!file.delete())
                throw new RuntimeException("Could not delete file: "
                        + file.getAbsolutePath());
    }



    public CtfSaverTest() throws Exception {
        Crystal copperPhase =
                new CrystalLoader()
                        .load(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml"));

        mmap =
                new CtfLoader().load(file,
                        EbsdMetadata.DEFAULT_WORKING_DISTANCE, new Camera(0.1,
                                0.2, 0.3), new Crystal[] { copperPhase });
    }



    @Test
    public void testSaveObjectFile() {
    }



    @Test
    public void testSaveHklMMapFile() {
        assertTrue(file.exists());
    }

}
