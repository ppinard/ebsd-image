package org.ebsdimage.core.sim.ops.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimTester;
import org.ebsdimage.core.sim.ops.patternsim.PatternBandCenter;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.*;
import rmlshared.io.FileUtil;
import crystallography.core.ScatteringFactorsEnum;

public class SimHoughMapTest extends TestCase {

    private SimHoughMap op;

    private PatternSimOp patternSimOp;



    @Before
    public void setUp() throws Exception {
        op = new SimHoughMap(356, 256, Math.toRadians(1.0), 1.0, 6);
        patternSimOp =
                new PatternBandCenter(356, 256, 4, ScatteringFactorsEnum.XRAY);
    }



    @After
    public void tearDown() throws Exception {
        if (SimTester.simPath.exists())
            FileUtil.rmdir(SimTester.simPath);
    }



    @Test
    public void testSave() throws IOException {
        // Create simulation
        Operation[] ops = new Operation[] { patternSimOp, op };
        Sim sim = SimTester.createSim(ops);

        // Run
        sim.run();

        // Test
        File file = new File(SimTester.simPath, "Sim1_houghmap_0.bmp");
        assertTrue(file.exists());

        BinMap peaksMap =
                Threshold.densitySlice((ByteMap) load(file), 254, 255);
        Centroid centroid =
                Analysis.getCentroid(Identification.identify(peaksMap));

        assertEquals(6, centroid.x.length);

        assertEquals(125.0, centroid.x[0], 1e-6);
        assertEquals(5.0, centroid.y[0], 1e-6);

        assertEquals(33.0, centroid.x[1], 1e-6);
        assertEquals(103.0, centroid.y[1], 1e-6);

        assertEquals(89.0, centroid.x[2], 1e-6);
        assertEquals(127.0, centroid.y[2], 1e-6);

        assertEquals(139.0, centroid.x[3], 1e-6);
        assertEquals(221.0, centroid.y[3], 1e-6);

        assertEquals(4.0, centroid.x[4], 1e-6);
        assertEquals(224.0, centroid.y[4], 1e-6);

        assertEquals(66.0, centroid.x[5], 1e-6);
        assertEquals(227.0, centroid.y[5], 1e-6);
    }



    @Test
    public void testSimHoughMap() {
        assertEquals(356, op.width);
        assertEquals(256, op.height);
        assertEquals(Math.toRadians(1.0), op.deltaTheta, 1e-6);
        assertEquals(1.0, op.deltaRho, 1e-6);
        assertEquals(6, op.count);
    }

}
