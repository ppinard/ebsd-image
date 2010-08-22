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
package org.ebsdimage.core.exp;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.io.PhasesMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public abstract class ExpMMapTester extends TestCase {

    protected ExpMMap mmap;

    protected Crystal siliconPhase;

    protected Crystal ironPhase;



    @Before
    public void setUp() throws Exception {
        siliconPhase = new Silicon();
        ironPhase = new IronBCC();
    }



    @Test
    public void testCreateMapIntInt() {
        ExpMMap newMMap = mmap.createMap(2, 2);

        assertEquals(2, newMMap.width);
        assertEquals(2, newMMap.height);

        assertEquals(mmap.beamEnergy, newMMap.beamEnergy, 1e-6);
        assertEquals(mmap.magnification, newMMap.magnification, 1e-6);
        assertEquals(mmap.tiltAngle, newMMap.tiltAngle, 1e-6);
        assertEquals(mmap.workingDistance, newMMap.workingDistance, 1e-6);
        assertEquals(mmap.pixelWidth, newMMap.pixelWidth, 1e-6);
        assertEquals(mmap.pixelHeight, newMMap.pixelHeight, 1e-6);
        assertTrue(mmap.sampleRotation.equals(newMMap.sampleRotation, 1e-6));
        assertTrue(mmap.calibration.equals(newMMap.calibration, 1e-6));

        RealMap q0Map = newMMap.getQ0Map();
        assertEquals(2, q0Map.width);
        assertEquals(2, q0Map.height);

        RealMap q1Map = newMMap.getQ1Map();
        assertEquals(2, q1Map.width);
        assertEquals(2, q1Map.height);

        RealMap q2Map = newMMap.getQ2Map();
        assertEquals(2, q2Map.width);
        assertEquals(2, q2Map.height);

        RealMap q3Map = newMMap.getQ3Map();
        assertEquals(2, q3Map.width);
        assertEquals(2, q3Map.height);

        PhasesMap phasesMap = newMMap.getPhasesMap();
        assertEquals(2, phasesMap.width);
        assertEquals(2, phasesMap.height);
        assertEquals(0, phasesMap.getPhases().length);
    }



    @Test
    public void testDuplicate() {
        ExpMMap newMMap = mmap.duplicate();

        assertEquals(mmap.width, newMMap.width);
        assertEquals(mmap.height, newMMap.height);

        assertEquals(mmap.beamEnergy, newMMap.beamEnergy, 1e-6);
        assertEquals(mmap.magnification, newMMap.magnification, 1e-6);
        assertEquals(mmap.tiltAngle, newMMap.tiltAngle, 1e-6);
        assertEquals(mmap.workingDistance, newMMap.workingDistance, 1e-6);
        assertEquals(mmap.pixelWidth, newMMap.pixelWidth, 1e-6);
        assertEquals(mmap.pixelHeight, newMMap.pixelHeight, 1e-6);
        assertTrue(mmap.sampleRotation.equals(newMMap.sampleRotation, 1e-6));
        assertTrue(mmap.calibration.equals(newMMap.calibration, 1e-6));

        mmap.getQ0Map().assertEquals(newMMap.getQ0Map(), 1e-6);
        mmap.getQ1Map().assertEquals(newMMap.getQ1Map(), 1e-6);
        mmap.getQ2Map().assertEquals(newMMap.getQ2Map(), 1e-6);
        mmap.getQ3Map().assertEquals(newMMap.getQ3Map(), 1e-6);
        mmap.getPhasesMap().assertEquals(newMMap.getPhasesMap(), 1e-6);
    }



    @Test
    public void testExpMMapIntIntExpMetadata() {
        ExpMMap other = new ExpMMap(2, 2, mmap.getMetadata());

        assertEquals(2, other.width);
        assertEquals(2, other.height);
        assertEquals(4, other.size);

        // Metadata
        assertEquals(mmap.beamEnergy, other.beamEnergy, 1e-3);
        assertEquals(mmap.magnification, other.magnification, 1e-3);
        assertEquals(mmap.tiltAngle, other.tiltAngle, 1e-3);
        assertEquals(mmap.workingDistance * 1000, other.workingDistance * 1000,
                1e-3);
        assertEquals(mmap.pixelWidth * 1e6, other.pixelWidth * 1e6, 1e-3);
        assertEquals(mmap.pixelHeight * 1e6, other.pixelHeight * 1e6, 1e-3);
        assertTrue(mmap.sampleRotation.equals(other.sampleRotation, 1e-3));
        assertTrue(mmap.calibration.equals(other.calibration, 1e-3));

        // Test nb of Maps in MultiMap
        Map[] maps = other.getMaps();
        assertEquals(5, maps.length);

        // Test the presence of the Maps
        assertTrue(other.contains(EbsdMMap.Q0));
        assertTrue(other.contains(EbsdMMap.Q1));
        assertTrue(other.contains(EbsdMMap.Q2));
        assertTrue(other.contains(EbsdMMap.Q3));
        assertTrue(other.contains(EbsdMMap.PHASES));
    }



    @Test
    public void testGetMetadata() {
        ExpMetadata metadata = mmap.getMetadata();

        assertEquals(20e3, metadata.beamEnergy, 1e-3);
        assertEquals(100.0, metadata.magnification, 1e-3);
        assertEquals(toRadians(70), metadata.tiltAngle, 1e-3);
        assertEquals(15, metadata.workingDistance * 1000, 1e-3);
        assertEquals(1.0, metadata.pixelWidth * 1e6, 1e-3);
        assertEquals(1.0, metadata.pixelHeight * 1e6, 1e-3);
        assertTrue(Quaternion.IDENTITY.equals(metadata.sampleRotation, 1e-3));
        assertTrue(new Camera(0.1, 0.2, 0.3).equals(metadata.calibration, 1e-3));
    }



    @Test
    public void testGetPhase() {
        assertNull(mmap.getPhase(0));
        assertTrue(siliconPhase.equals(mmap.getPhase(1), 1e-4));
        assertTrue(ironPhase.equals(mmap.getPhase(2), 1e-4));
        assertTrue(siliconPhase.equals(mmap.getPhase(3), 1e-4));
    }



    @Test
    public void testGetPhaseId() {
        assertEquals(0, mmap.getPhaseId(0));
        assertEquals(1, mmap.getPhaseId(1));
        assertEquals(2, mmap.getPhaseId(2));
        assertEquals(1, mmap.getPhaseId(3));
    }



    @Test
    public void testGetPhases() {
        Crystal[] phases = mmap.getPhases();

        assertEquals(2, phases.length);

        assertTrue(siliconPhase.equals(phases[0], 1e-3));
        assertTrue(ironPhase.equals(phases[1], 1e-3));
    }



    @Test
    public void testGetPhasesMap() throws IOException {
        PhasesMap phasesMap = mmap.getPhasesMap();

        PhasesMap expectedPhasesMap =
                new PhasesMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/Phases.bmp"));

        phasesMap.assertEquals(expectedPhasesMap);

        Crystal[] phases = phasesMap.getPhases();
        assertEquals(2, phases.length);

        assertTrue(siliconPhase.equals(phases[0], 1e-3));
        assertTrue(ironPhase.equals(phases[1], 1e-3));
    }



    @Test
    public void testGetQ0Map() {
        RealMap realMap = mmap.getQ0Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/testdata/Q0.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ1Map() {
        RealMap realMap = mmap.getQ1Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/testdata/Q1.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ2Map() {
        RealMap realMap = mmap.getQ2Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/testdata/Q2.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ3Map() {
        RealMap realMap = mmap.getQ3Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/testdata/Q3.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetRotation() {
        assertEquals(1.0, mmap.getRotation(0).getQ0(), 1e-6);
        assertEquals(0.0, mmap.getRotation(0).getQ1(), 1e-6);
        assertEquals(0.0, mmap.getRotation(0).getQ2(), 1e-6);
        assertEquals(0.0, mmap.getRotation(0).getQ3(), 1e-6);

        assertEquals(0.0, mmap.getRotation(1).getQ0(), 1e-6);
        assertEquals(1.0, mmap.getRotation(1).getQ1(), 1e-6);
        assertEquals(0.0, mmap.getRotation(1).getQ2(), 1e-6);
        assertEquals(0.0, mmap.getRotation(1).getQ3(), 1e-6);

        assertEquals(0.0, mmap.getRotation(2).getQ0(), 1e-6);
        assertEquals(0.0, mmap.getRotation(2).getQ1(), 1e-6);
        assertEquals(1.0, mmap.getRotation(2).getQ2(), 1e-6);
        assertEquals(0.0, mmap.getRotation(2).getQ3(), 1e-6);

        assertEquals(0.0, mmap.getRotation(3).getQ0(), 1e-6);
        assertEquals(0.0, mmap.getRotation(3).getQ1(), 1e-6);
        assertEquals(0.0, mmap.getRotation(3).getQ2(), 1e-6);
        assertEquals(1.0, mmap.getRotation(3).getQ3(), 1e-6);
    }

}
