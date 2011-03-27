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
package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimMetadata;
import org.ebsdimage.core.sim.SimOperation;
import org.ebsdimage.core.sim.SimTester;
import org.ebsdimage.core.sim.ops.patternsim.PatternBandCenter;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.*;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimHoughMapTest extends TestCase {

    private SimHoughMap op;

    private PatternSimOp patternSimOp;



    @Before
    public void setUp() throws Exception {
        op = new SimHoughMap(Math.toRadians(1.0), 1.0, 6);
        patternSimOp = new PatternBandCenter(400, 400);
    }



    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        SimTester.removeSimPath();
    }



    @Test
    public void testSave() throws IOException {
        // Create simulation
        SimOperation[] ops =
                new SimOperation[] { patternSimOp, op, new BmpFile() };
        Sim sim = SimTester.createSim(ops);

        SimMetadata metadata =
                new SimMetadata(sim.getMetadata().getMicroscope(),
                        ScatteringFactorsEnum.XRAY, 4);
        sim.mmap.setMetadata(metadata);

        // Run
        sim.run();

        // Test
        File file = new File(SimTester.simPath, "Sim1_houghmap_0.bmp");
        assertTrue(file.exists());

        BinMap peaksMap =
                Threshold.densitySlice((ByteMap) load(file), 254, 255);
        peaksMap.setCalibration(Calibration.NONE);
        Centroid centroid =
                Analysis.getCentroid(Identification.identify(peaksMap));

        assertEquals(6, centroid.x.length);

        // TODO: Test position of the peaks
        // assertEquals(125.0, centroid.x[0], 1e-6);
        // assertEquals(5.0, centroid.y[0], 1e-6);
        //
        // assertEquals(33.0, centroid.x[1], 1e-6);
        // assertEquals(103.0, centroid.y[1], 1e-6);
        //
        // assertEquals(89.0, centroid.x[2], 1e-6);
        // assertEquals(127.0, centroid.y[2], 1e-6);
        //
        // assertEquals(139.0, centroid.x[3], 1e-6);
        // assertEquals(221.0, centroid.y[3], 1e-6);
        //
        // assertEquals(4.0, centroid.x[4], 1e-6);
        // assertEquals(224.0, centroid.y[4], 1e-6);
        //
        // assertEquals(66.0, centroid.x[5], 1e-6);
        // assertEquals(227.0, centroid.y[5], 1e-6);
    }



    @Test
    public void testSimHoughMap() {
        assertEquals(Math.toRadians(1.0), op.deltaTheta, 1e-6);
        assertEquals(1.0, op.deltaRho, 1e-6);
        assertEquals(6, op.count);
    }

}
