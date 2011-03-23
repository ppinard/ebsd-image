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

import java.io.IOException;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.ErrorMap;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.PhaseMap;
import org.ebsdimage.io.ErrorMapLoader;
import org.ebsdimage.io.PhaseMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.geom.RotationUtils;
import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public abstract class TslMMapTester extends TestCase {

    protected TslMMap mmap;

    protected Crystal wcPhase;

    protected Crystal nickelPhase;



    @Before
    public void setUp() throws Exception {
        wcPhase =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/WC.xml"));
        nickelPhase =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/Nickel.xml"));
    }



    @Test
    public void testCreateMapIntInt() {
        TslMMap newMMap = mmap.createMap(2, 2);

        assertEquals(2, newMMap.width);
        assertEquals(2, newMMap.height);

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

        PhaseMap phasesMap = newMMap.getPhaseMap();
        assertEquals(2, phasesMap.width);
        assertEquals(2, phasesMap.height);
        assertEquals(0, phasesMap.getPhases().length);

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

        mmap.getQ0Map().assertEquals(newMMap.getQ0Map(), 1e-6);
        mmap.getQ1Map().assertEquals(newMMap.getQ1Map(), 1e-6);
        mmap.getQ2Map().assertEquals(newMMap.getQ2Map(), 1e-6);
        mmap.getQ3Map().assertEquals(newMMap.getQ3Map(), 1e-6);
        mmap.getPhaseMap().assertEquals(newMMap.getPhaseMap(), 1e-6);

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
    public void testGetImageQualityMap() {
        RealMap realMap = mmap.getImageQualityMap();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/tsl/testdata/ImageQuality.rmp");

        realMap.assertEquals(expectedRealMap);
    }



    @Test
    public void testGetMicroscope() {
        Microscope microscope = mmap.getMicroscope();

        assertEquals(0.523500, microscope.getPatternCenterX(), 1e-6);
        assertEquals(1 - 0.804900, microscope.getPatternCenterY(), 1e-6);
        assertEquals(0.0, microscope.getCameraDistance(), 1e-6);
        assertEquals(15, microscope.getWorkingDistance() * 1000, 1e-3);
        assertEquals(Rotation.IDENTITY, microscope.getSampleRotation(), 1e-6);
    }



    @Test
    public void testCalibration() {
        Calibration cal = mmap.getCalibration();

        assertEquals(0.07, cal.getDX().getValue("um"), 1e-6);
        assertEquals(0.07, cal.getDY().getValue("um"), 1e-6);
    }



    @Test
    public void testGetPhasesMap() throws IOException {
        PhaseMap phasesMap = mmap.getPhaseMap();

        PhaseMap expectedPhasesMap =
                new PhaseMapLoader().load(FileUtil.getFile("org/ebsdimage/vendors/tsl/testdata/Phases.bmp"));

        phasesMap.assertEquals(expectedPhasesMap, 1e-6);

        Crystal[] phases = phasesMap.getPhases();
        assertEquals(2, phases.length);

        assertTrue(wcPhase.equals(phases[0], 1e-3));
        assertTrue(nickelPhase.equals(phases[1], 1e-3));
    }



    @Test
    public void testGetErrorMap() throws IOException {
        ByteMap byteMap = mmap.getErrorMap();

        ErrorMap expectedMap =
                new ErrorMapLoader().load(getFile("org/ebsdimage/vendors/tsl/testdata/Errors.bmp"));
        byteMap.assertEquals(expectedMap);
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
    public void testGetRotation() throws CardanEulerSingularityException {
        double[] rotation =
                RotationUtils.getBungeEulerAngles(mmap.getRotation(1));
        assertEquals(5.92334, rotation[0], 1e-4);
        assertEquals(1.33599, rotation[1], 1e-4);
        assertEquals(2.82812, rotation[2], 1e-4);
    }



    @Test
    public void testTslMMap() {
        assertEquals(5, mmap.width);
        assertEquals(5, mmap.height);
        assertEquals(25, mmap.size);

        // Test nb of Maps in MultiMap
        Map[] maps = mmap.getMaps();
        assertEquals(8, maps.length);

        // Test the presence of the Maps
        assertTrue(mmap.contains(EbsdMMap.Q0));
        assertTrue(mmap.contains(EbsdMMap.Q1));
        assertTrue(mmap.contains(EbsdMMap.Q2));
        assertTrue(mmap.contains(EbsdMMap.Q3));
        assertTrue(mmap.contains(EbsdMMap.PHASES));
        assertTrue(mmap.contains(EbsdMMap.ERRORS));
        assertTrue(mmap.contains(TslMMap.IMAGE_QUALITY));
        assertTrue(mmap.contains(TslMMap.CONFIDENCE_INDEX));
    }

}
