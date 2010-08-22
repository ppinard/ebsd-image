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
package org.ebsdimage.core.sim.ops.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class BmpFileTest {

    private BmpFile bmpFile;



    @Before
    public void setUp() throws Exception {
        bmpFile = new BmpFile();
    }



    @After
    public void tearDown() throws Exception {
        if (SimTester.simPath.exists())
            FileUtil.rmdir(SimTester.simPath);
    }



    @Test
    public void testSave() throws IOException {
        // Create simulation
        Operation[] ops =
                new Operation[] { SimTester.createPatternSimOp(), bmpFile };
        Sim sim = SimTester.createSim(ops);

        // Run
        sim.run();

        // Test
        assertTrue(new File(SimTester.simPath, "Sim1_0.bmp").exists());
    }



    @Test
    public void testToString() {
        assertEquals("BmpFile []", bmpFile.toString());
    }

}
