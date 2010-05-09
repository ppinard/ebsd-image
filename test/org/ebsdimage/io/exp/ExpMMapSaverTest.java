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

import java.io.File;

import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.exp.ExpMMapTester;
import org.ebsdimage.io.exp.ExpMMapLoader;
import org.ebsdimage.io.exp.ExpMMapSaver;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import rmlshared.io.FileUtil;

public class ExpMMapSaverTest extends ExpMMapTester {

    private static File zipFile =
            new File(FileUtil.getTempDirFile(), "expmmap.zip");



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Load mmap from zip
        File file = getFile("org/ebsdimage/testdata/expmmap.zip");
        ExpMMap tempMMap = new ExpMMapLoader().load(file);

        // Save mmap to temp dir
        new ExpMMapSaver().save(tempMMap, zipFile);
    }



    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        mmap = new ExpMMapLoader().load(zipFile);
    }



    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (zipFile.exists())
            if (!(zipFile.delete()))
                throw new RuntimeException("File (" + zipFile.getAbsolutePath()
                        + ") cannot be deleted.");
    }

}
