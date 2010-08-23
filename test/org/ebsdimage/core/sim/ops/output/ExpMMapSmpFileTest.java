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

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOpMock;
import org.ebsdimage.io.SmpInputStream;
import org.ebsdimage.io.exp.ExpMMapLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class ExpMMapSmpFileTest {

    private ExpMMapSmpFile op;

    private PatternSimOp patternSimOp;

    private Sim sim;

    private File tmpPath;

    private Quaternion rotation;

    private Camera camera;



    @Before
    public void setUp() throws Exception {
        tmpPath = new File(FileUtil.getTempDirFile(), "expmmapsmp");
        if (!tmpPath.mkdirs())
            throw new IOException("Directory (" + tmpPath
                    + ") could not be created.");

        op = new ExpMMapSmpFile();

        patternSimOp = new PatternSimOpMock();
        camera = new Camera(0.1, 0.2, 0.3);
        rotation = new Quaternion(new Eulers(0.1, 0.2, 0.3));

        Crystal[] crystals = new Crystal[] { new Silicon(), new IronBCC() };
        Camera[] cameras = new Camera[] { camera };
        Energy[] energies = new Energy[] { new Energy(25e3) };
        Quaternion[] rotations = new Quaternion[] { rotation };
        Operation[] ops = new Operation[] { op, patternSimOp };

        sim = new Sim(ops, cameras, crystals, energies, rotations);
        sim.setName("Sim1");
        sim.setDir(tmpPath);
    }



    @After
    public void tearDown() throws Exception {
        FileUtil.rmdir(tmpPath);
    }



    @Test
    public void testTearDown() throws IOException {
        assertTrue(true); // Tested in testSetUp())
    }



    @Test
    public void testSetUp() throws IOException {
        op.setUp(sim);
        op.tearDown(sim);

        // Load ExpMMap
        ExpMMap mmap = new ExpMMapLoader().load(new File(tmpPath, "Sim1.zip"));

        // Test ExpMMap
        testExpMMap(mmap);

        // Test SMP
        assertTrue(new File(tmpPath, "Sim1.smp").exists());
    }



    private void testExpMMap(ExpMMap mmap) {
        assertEquals(2, mmap.width);
        assertEquals(1, mmap.height);
        assertEquals(9, mmap.getMaps().length);

        assertEquals(25e3, mmap.beamEnergy, 1e-6);
        assertEquals(1, mmap.magnification, 1e-6);
        assertEquals(0, mmap.tiltAngle, 1e-6);
        assertEquals(Double.NaN, mmap.workingDistance, 1e-6);
        assertEquals(1, mmap.pixelWidth * 1e6, 1e-6);
        assertEquals(1, mmap.pixelHeight * 1e6, 1e-6);
        assertTrue(Quaternion.IDENTITY.equals(mmap.sampleRotation, 1e-6));
        assertTrue(new Camera(0.1, 0.2, 0.3).equals(mmap.calibration, 1e-6));

        assertEquals(2, mmap.getPhases().length);

        Map map = mmap.getMap(ExpMMapSmpFile.ENERGY);
        assertEquals(2, map.width);
        assertEquals(1, map.height);

        map = mmap.getMap(ExpMMapSmpFile.PCH);
        assertEquals(2, map.width);
        assertEquals(1, map.height);

        map = mmap.getMap(ExpMMapSmpFile.PCV);
        assertEquals(2, map.width);
        assertEquals(1, map.height);

        map = mmap.getMap(ExpMMapSmpFile.DD);
        assertEquals(2, map.width);
        assertEquals(1, map.height);
    }



    @Test
    public void testSave() throws IOException {
        sim.run();

        // Test ExpMMap
        ExpMMap mmap = new ExpMMapLoader().load(new File(tmpPath, "Sim1.zip"));

        testExpMMap(mmap);

        assertEquals(1, mmap.getPhaseId(0));
        assertEquals(2, mmap.getPhaseId(1));

        assertEquals(rotation.getQ0(), mmap.getQ0Map().pixArray[0], 1e-6);
        assertEquals(rotation.getQ1(), mmap.getQ1Map().pixArray[0], 1e-6);
        assertEquals(rotation.getQ2(), mmap.getQ2Map().pixArray[0], 1e-6);
        assertEquals(rotation.getQ3(), mmap.getQ3Map().pixArray[0], 1e-6);

        assertEquals(rotation.getQ0(), mmap.getQ0Map().pixArray[1], 1e-6);
        assertEquals(rotation.getQ1(), mmap.getQ1Map().pixArray[1], 1e-6);
        assertEquals(rotation.getQ2(), mmap.getQ2Map().pixArray[1], 1e-6);
        assertEquals(rotation.getQ3(), mmap.getQ3Map().pixArray[1], 1e-6);

        RealMap map = (RealMap) mmap.getMap(ExpMMapSmpFile.ENERGY);
        assertEquals(25e3, map.pixArray[0], 1e-6);
        assertEquals(25e3, map.pixArray[1], 1e-6);

        map = (RealMap) mmap.getMap(ExpMMapSmpFile.PCH);
        assertEquals(camera.patternCenterH, map.pixArray[0], 1e-6);
        assertEquals(camera.patternCenterH, map.pixArray[1], 1e-6);

        map = (RealMap) mmap.getMap(ExpMMapSmpFile.PCV);
        assertEquals(camera.patternCenterV, map.pixArray[0], 1e-6);
        assertEquals(camera.patternCenterV, map.pixArray[1], 1e-6);

        map = (RealMap) mmap.getMap(ExpMMapSmpFile.DD);
        assertEquals(camera.detectorDistance, map.pixArray[0], 1e-6);
        assertEquals(camera.detectorDistance, map.pixArray[1], 1e-6);

        // Test SMP
        SmpInputStream smp = new SmpInputStream(new File(tmpPath, "Sim1.smp"));
        assertEquals(2, smp.getMapCount());
        assertEquals(2, smp.getMapWidth());
        assertEquals(2, smp.getMapHeight());
        assertEquals(0, smp.getStartIndex());
        assertEquals(1, smp.getEndIndex());
        smp.close();
    }

}
