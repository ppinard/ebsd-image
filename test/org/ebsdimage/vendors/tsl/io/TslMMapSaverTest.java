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

import java.io.File;

import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.ebsdimage.vendors.tsl.core.TslMMapTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import rmlshared.io.FileUtil;

public class TslMMapSaverTest extends TslMMapTester {

    private static File zipFile = new File(FileUtil.getTempDirFile(),
            "scan1.zip");



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Load mmap from zip
        File file =
                FileUtil.getFile("org/ebsdimage/vendors/tsl/testdata/scan1.zip");
        TslMMap tempMMap = new TslMMapLoader().load(file);

        // Save mmap to temp dir
        new TslMMapSaver().save(tempMMap, zipFile);
    }



    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        mmap = new TslMMapLoader().load(zipFile);
    }



    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (zipFile.exists())
            if (!(zipFile.delete()))
                throw new RuntimeException("File (" + zipFile.getAbsolutePath()
                        + ") cannot be deleted.");
    }

}
