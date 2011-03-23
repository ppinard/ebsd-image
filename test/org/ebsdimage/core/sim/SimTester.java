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
package org.ebsdimage.core.sim;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.PhaseMap;
import org.ebsdimage.core.sim.ops.output.OutputOps2Mock;
import org.ebsdimage.core.sim.ops.output.OutputOpsMock;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOpMock;
import org.junit.After;
import org.junit.Test;

import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public abstract class SimTester extends TestCase {

    public static SimOperation[] createOperations() {
        return new SimOperation[] { PatternSimOpMock.DEFAULT,
                new OutputOpsMock(), new OutputOps2Mock() };
    }



    public static void removeSimPath() throws IOException {
        if (simPath.exists())
            FileUtil.rmdir(simPath);
    }

    protected Sim sim;

    public static final File simPath = new File(FileUtil.getTempDirFile(),
            "sim1");



    public static SimMetadata createMetadata() {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.04);
        Vector3D tiltAxis = new Vector3D(0, 1, 0);

        Microscope microscope = new Microscope(camera, tiltAxis);
        microscope.setBeamEnergy(20e3);
        microscope.setPatternCenterX(0.5);
        microscope.setPatternCenterY(0.5);
        microscope.setWorkingDistance(0.015);
        microscope.setCameraDistance(0.02);
        microscope.setTiltAngle(0.0);

        ScatteringFactorsEnum scatteringFactors = ScatteringFactorsEnum.XRAY;
        int maxIndex = 2;

        return new SimMetadata(microscope, scatteringFactors, maxIndex);
    }



    public static Sim createSim() {
        return createSim(createOperations());
    }



    public static Sim createSim(SimOperation[] ops) {
        Crystal[] phases = new Crystal[] { CrystalFactory.silicon() };
        Rotation[] rotations =
                new Rotation[] { new Rotation(RotationOrder.ZXZ, 0.1, 0.2, 0.3) };

        SimMetadata metadata = createMetadata();

        Sim sim = new Sim(metadata, ops, phases, rotations);
        sim.setName("Sim1");
        sim.setDir(simPath);

        return sim;
    }



    @Override
    @After
    public void tearDown() throws Exception {
        removeSimPath();
    }



    @Test
    public void testGetOutputOps() {
        assertEquals(2, sim.getOutputOps().length);
    }



    @Test
    public void testGetPatternSimOp() {
        assertEquals(2, sim.getPatternSimOp().width);
        assertEquals(2, sim.getPatternSimOp().height);
    }



    @Test
    public void testRun() {
        // Make sure it is in the temporary folder
        sim.setDir(simPath);

        sim.run();

        // Test
        assertEquals(1, sim.mmap.width);
        assertEquals(1, sim.mmap.height);

        // Rotation
        Rotation rotation = new Rotation(RotationOrder.ZXZ, 0.1, 0.2, 0.3);

        RealMap realMap = sim.mmap.getQ0Map();
        assertEquals(rotation.getQ0(), realMap.pixArray[0], 1e-6);
        realMap = sim.mmap.getQ1Map();
        assertEquals(rotation.getQ1(), realMap.pixArray[0], 1e-6);
        realMap = sim.mmap.getQ2Map();
        assertEquals(rotation.getQ2(), realMap.pixArray[0], 1e-6);
        realMap = sim.mmap.getQ3Map();
        assertEquals(rotation.getQ3(), realMap.pixArray[0], 1e-6);

        // Phases
        PhaseMap phaseMap = sim.mmap.getPhaseMap();
        assertEquals(1, sim.mmap.getPhases().length);
        assertEquals(1, phaseMap.pixArray[0]);
        assertEquals(CrystalFactory.silicon(), phaseMap.getItem(1), 1e-6);
    }
}
