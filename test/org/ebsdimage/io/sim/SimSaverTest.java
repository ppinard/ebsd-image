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
package org.ebsdimage.io.sim;

import java.io.File;

import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import rmlshared.io.FileUtil;

public class SimSaverTest extends SimTester {

    private static File file = new File(FileUtil.getTempDir(), "sim.xml");



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Sim other = createSim();
        new SimSaver().save(other, file);
    }



    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (file.exists())
            if (!file.delete())
                throw new RuntimeException("Could not delete "
                        + file.getAbsolutePath());
    }



    @Before
    public void setUp() throws Exception {
        sim = new SimLoader().load(file);
    }

}
