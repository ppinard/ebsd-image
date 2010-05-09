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
package org.ebsdimage.vendors.tsl.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.io.PhasesMapLoader;
import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.ebsdimage.vendors.tsl.core.TslMetadata;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

public abstract class TslMMapTester extends TestCase {

    protected TslMMap mmap;
    protected Crystal wcPhase;
    protected Crystal nickelPhase;



    @Before
    public void setUp() throws Exception {
        wcPhase =
                new CrystalLoader().load(FileUtil
                        .getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml"));
        nickelPhase =
                new CrystalLoader().load(FileUtil
                        .getFile("org/ebsdimage/vendors/tsl/testdata/Nickel.xml"));
    }



    @Test
    public void testCreateMapIntInt() {
        TslMMap newMMap = mmap.createMap(2, 2);

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

        RealMap euler1Map = newMMap.getEuler1Map();
        assertEquals(2, euler1Map.width);
        assertEquals(2, euler1Map.height);

        RealMap euler2Map = newMMap.getEuler2Map();
        assertEquals(2, euler2Map.width);
        assertEquals(2, euler2Map.height);

        RealMap euler3Map = newMMap.getEuler3Map();
        assertEquals(2, euler3Map.width);
        assertEquals(2, euler3Map.height);

        RealMap iqMap = newMMap.getImageQualityMap();
        assertEquals(2, iqMap.width);
        assertEquals(2, iqMap.height);

        RealMap ciMap = newMMap.getConfidenceIndexMap();
        assertEquals(2, ciMap.width);
        assertEquals(2, ciMap.height);
    }



    @Test
    public void testDuplicate() {
        TslMMap newMMap = mmap.duplicate();

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

        mmap.getEuler1Map().assertEquals(newMMap.getEuler1Map(), 1e-6);
        mmap.getEuler2Map().assertEquals(newMMap.getEuler2Map(), 1e-6);
        mmap.getEuler3Map().assertEquals(newMMap.getEuler3Map(), 1e-6);

        mmap.getImageQualityMap().assertEquals(newMMap.getImageQualityMap(),
                1e-6);
        mmap.getConfidenceIndexMap().assertEquals(
                newMMap.getConfidenceIndexMap(), 1e-6);
    }



    @Test
    public void testGetConfidenceIndexMap() {
        RealMap realMap = mmap.getConfidenceIndexMap();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/confidenceIndex.rmp");

        realMap.assertEquals(expectedRealMap);
    }



    @Test
    public void testGetEuler1() {
        RealMap realMap = mmap.getEuler1Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Euler1.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetEuler2() {
        RealMap realMap = mmap.getEuler2Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Euler2.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetEuler3() {
        RealMap realMap = mmap.getEuler3Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Euler3.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetImageQualityMap() {
        RealMap realMap = mmap.getImageQualityMap();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/ImageQuality.rmp");

        realMap.assertEquals(expectedRealMap);
    }



    @Test
    public void testGetMetadata() {
        TslMetadata metadata = mmap.getMetadata();

        assertEquals(Double.NaN, metadata.beamEnergy, 1e-3);
        assertEquals(Double.NaN, metadata.magnification, 1e-3);
        assertEquals(Double.NaN, metadata.tiltAngle, 1e-3);
        assertEquals(15, metadata.workingDistance * 1000, 1e-3);
        assertEquals(0.07, metadata.pixelWidth * 1e6, 1e-3);
        assertEquals(0.07, metadata.pixelHeight * 1e6, 1e-3);
        assertTrue(Quaternion.IDENTITY.equals(metadata.sampleRotation, 1e-3));
        assertTrue(new Camera(0.0235, 0.3049, 0.7706).equals(
                metadata.calibration, 1e-3));
    }



    @Test
    public void testGetPhase() {
        assertNull(mmap.getPhase(0));
        assertTrue(wcPhase.equals(mmap.getPhase(1), 1e-4));
        assertTrue(nickelPhase.equals(mmap.getPhase(5), 1e-4));
    }



    @Test
    public void testGetPhaseId() {
        assertEquals(0, mmap.getPhaseId(0));
        assertEquals(1, mmap.getPhaseId(1));
        assertEquals(2, mmap.getPhaseId(5));
    }



    @Test
    public void testGetPhases() throws IOException {
        Crystal[] phases = mmap.getPhases();

        assertEquals(2, phases.length);

        assertTrue(wcPhase.equals(phases[0], 1e-3));
        assertTrue(nickelPhase.equals(phases[1], 1e-3));
    }



    @Test
    public void testGetPhasesMap() throws IOException {
        PhasesMap phasesMap = mmap.getPhasesMap();

        PhasesMap expectedPhasesMap =
                new PhasesMapLoader().load(FileUtil
                        .getFile("org/ebsdimage/vendors/tsl/testdata/Phases.bmp"));

        phasesMap.assertEquals(expectedPhasesMap);

        Crystal[] phases = phasesMap.getPhases();
        assertEquals(2, phases.length);

        assertTrue(wcPhase.equals(phases[0], 1e-3));
        assertTrue(nickelPhase.equals(phases[1], 1e-3));
    }



    @Test
    public void testGetQ0Map() {
        RealMap realMap = mmap.getQ0Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Q0.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ1Map() {
        RealMap realMap = mmap.getQ1Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Q1.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ2Map() {
        RealMap realMap = mmap.getQ2Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Q2.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ3Map() {
        RealMap realMap = mmap.getQ3Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/Q3.rmp");

        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetRotation() {
        double[] rotation = mmap.getRotation(1).toEuler().positive();
        assertEquals(5.92334, rotation[0], 1e-4);
        assertEquals(1.33599, rotation[1], 1e-4);
        assertEquals(2.82812, rotation[2], 1e-4);
    }



    @Test
    public void testTslMMap() {
        assertEquals(5, mmap.width);
        assertEquals(5, mmap.height);
        assertEquals(25, mmap.size);

        // Metadata
        assertEquals(Double.NaN, mmap.beamEnergy, 1e-3);
        assertEquals(Double.NaN, mmap.magnification, 1e-3);
        assertEquals(Double.NaN, mmap.tiltAngle, 1e-3);
        assertEquals(15, mmap.workingDistance * 1000, 1e-3);
        assertEquals(0.07, mmap.pixelWidth * 1e6, 1e-3);
        assertEquals(0.07, mmap.pixelHeight * 1e6, 1e-3);
        assertTrue(Quaternion.IDENTITY.equals(mmap.sampleRotation, 1e-3));
        assertTrue(new Camera(0.0235, 0.3049, 0.7706).equals(mmap.calibration,
                1e-3));

        // Test nb of Maps in MultiMap
        Map[] maps = mmap.getMaps();
        assertEquals(10, maps.length);

        // Test the presence of the Maps
        assertTrue(mmap.contains(TslMMap.Q0));
        assertTrue(mmap.contains(TslMMap.Q1));
        assertTrue(mmap.contains(TslMMap.Q2));
        assertTrue(mmap.contains(TslMMap.Q3));
        assertTrue(mmap.contains(TslMMap.PHASES));
        assertTrue(mmap.contains(TslMMap.EULER1));
        assertTrue(mmap.contains(TslMMap.EULER2));
        assertTrue(mmap.contains(TslMMap.EULER3));
        assertTrue(mmap.contains(TslMMap.IMAGE_QUALITY));
        assertTrue(mmap.contains(TslMMap.CONFIDENCE_INDEX));
    }



    @Test
    public void testTslMMapIntIntTslMetadata() {
        TslMMap other = new TslMMap(2, 2, mmap.getMetadata());

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
        assertEquals(10, maps.length);

        // Test the presence of the Maps
        assertTrue(other.contains(TslMMap.Q0));
        assertTrue(other.contains(TslMMap.Q1));
        assertTrue(other.contains(TslMMap.Q2));
        assertTrue(other.contains(TslMMap.Q3));
        assertTrue(other.contains(TslMMap.PHASES));
        assertTrue(other.contains(TslMMap.EULER1));
        assertTrue(other.contains(TslMMap.EULER2));
        assertTrue(other.contains(TslMMap.EULER3));
        assertTrue(other.contains(TslMMap.IMAGE_QUALITY));
        assertTrue(other.contains(TslMMap.CONFIDENCE_INDEX));
    }

}
