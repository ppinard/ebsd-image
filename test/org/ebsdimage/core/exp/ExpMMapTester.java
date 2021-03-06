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
package org.ebsdimage.core.exp;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.*;
import org.ebsdimage.io.ErrorMapLoader;
import org.ebsdimage.io.PhaseMapLoader;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;
import static java.lang.Math.toRadians;

import static junittools.test.Assert.assertEquals;

public abstract class ExpMMapTester extends TestCase {

    public static ExpMMap createExpMMap() {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Microscope microscope =
                new Microscope("Unnamed", camera, new Vector3D(0, 1, 0));
        AcquisitionConfig acqConfig =
                new AcquisitionConfig(microscope, Math.toRadians(70), 0.015,
                        20e3, 100, Rotation.IDENTITY, 0.0, 0.0, 0.0);

        EbsdMetadata metadata = new EbsdMetadata(acqConfig);

        HashMap<String, Map> mapList = new HashMap<String, Map>();

        RealMap q0Map = new RealMap(2, 2);
        q0Map.pixArray[0] = 1.0f;
        mapList.put(EbsdMMap.Q0, q0Map);

        RealMap q1Map = new RealMap(2, 2);
        q1Map.pixArray[1] = 1.0f;
        mapList.put(EbsdMMap.Q1, q1Map);

        RealMap q2Map = new RealMap(2, 2);
        q2Map.pixArray[2] = 1.0f;
        mapList.put(EbsdMMap.Q2, q2Map);

        RealMap q3Map = new RealMap(2, 2);
        q3Map.pixArray[3] = 1.0f;
        mapList.put(EbsdMMap.Q3, q3Map);

        HashMap<Integer, Crystal> phases = new HashMap<Integer, Crystal>();
        phases.put(1, CrystalFactory.silicon());
        phases.put(2, CrystalFactory.ferrite());
        PhaseMap phasesMap =
                new PhaseMap(2, 2, new byte[] { 0, 1, 2, 1 }, phases);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        HashMap<Integer, ErrorCode> errorCodes =
                new HashMap<Integer, ErrorCode>();
        errorCodes.put(1, new ErrorCode("Error1", "First test error"));
        ErrorMap errorMap = new ErrorMap(2, 2, errorCodes);
        mapList.put(EbsdMMap.ERRORS, errorMap);

        ExpMMap mmap = new ExpMMap(2, 2, mapList);
        mmap.setMetadata(metadata);

        mmap.setCalibration(new Calibration(1e-6, 1e-6, "m"));

        return mmap;
    }

    protected ExpMMap mmap;



    @Test
    public void testCreateMapIntInt() {
        ExpMMap other = mmap.createMap(2, 2);

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        assertEquals(EbsdMetadata.DEFAULT, other.getMetadata(), 1e-6);

        RealMap q0Map = other.getQ0Map();
        assertEquals(2, q0Map.width);
        assertEquals(2, q0Map.height);

        RealMap q1Map = other.getQ1Map();
        assertEquals(2, q1Map.width);
        assertEquals(2, q1Map.height);

        RealMap q2Map = other.getQ2Map();
        assertEquals(2, q2Map.width);
        assertEquals(2, q2Map.height);

        RealMap q3Map = other.getQ3Map();
        assertEquals(2, q3Map.width);
        assertEquals(2, q3Map.height);

        PhaseMap phasesMap = other.getPhaseMap();
        assertEquals(2, phasesMap.width);
        assertEquals(2, phasesMap.height);
        assertEquals(1, phasesMap.getItems().size());

        ErrorMap errorMap = other.getErrorMap();
        assertEquals(2, errorMap.width);
        assertEquals(2, errorMap.height);
        assertEquals(1, errorMap.getItems().size());
    }



    @Test
    public void testDuplicate() {
        ExpMMap other = mmap.duplicate();

        assertEquals(mmap.width, other.width);
        assertEquals(mmap.height, other.height);
        assertEquals(mmap.size, other.size);

        assertEquals(mmap.getMetadata(), other.getMetadata(), 1e-6);

        mmap.getQ0Map().assertEquals(other.getQ0Map());
        mmap.getQ1Map().assertEquals(other.getQ1Map());
        mmap.getQ2Map().assertEquals(other.getQ2Map());
        mmap.getQ3Map().assertEquals(other.getQ3Map());
        mmap.getPhaseMap().assertEquals(other.getPhaseMap());
    }



    @Test
    public void testExpMMap() {
        assertEquals(2, mmap.width);
        assertEquals(2, mmap.height);
        assertEquals(4, mmap.size);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionErrorIncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.ERRORS, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionPhasesIncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.PHASES, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ0IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ1IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q1, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ2IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q2, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ3IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q3, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList);
    }



    @Test
    public void testExpMMapIntIntExpMetadata() {
        ExpMMap other = new ExpMMap(2, 2);
        other.setMetadata(mmap.getMetadata());

        assertEquals(2, other.width);
        assertEquals(2, other.height);
        assertEquals(4, other.size);

        // Metadata
        assertEquals(mmap.getMetadata(), other.getMetadata(), 1e-6);

        // Test nb of Maps in MultiMap
        Map[] maps = other.getMaps();
        assertEquals(6, maps.length);

        // Test the presence of the Maps
        assertTrue(other.contains(EbsdMMap.Q0));
        assertTrue(other.contains(EbsdMMap.Q1));
        assertTrue(other.contains(EbsdMMap.Q2));
        assertTrue(other.contains(EbsdMMap.Q3));
        assertTrue(other.contains(EbsdMMap.PHASES));
        assertTrue(other.contains(EbsdMMap.ERRORS));
    }



    @Test
    public void testGetErrorMap() throws IOException {
        ErrorMap errorMap = mmap.getErrorMap();

        ErrorMap expectedErrorMap =
                new ErrorMapLoader().load(getFile("org/ebsdimage/testdata/Errors.bmp"));

        errorMap.assertEquals(expectedErrorMap);

        java.util.Map<Integer, ErrorCode> errorCodes = errorMap.getItems();
        assertEquals(2, errorCodes.size());

        assertEquals("Error1", errorCodes.get(1).name);
        assertEquals("First test error", errorCodes.get(1).description);
    }



    @Test
    public void testGetMetadata() {
        AcquisitionConfig acqConfig = mmap.getAcquisitionConfig();

        assertEquals(20e3, acqConfig.beamEnergy, 1e-3);
        assertEquals(100.0, acqConfig.magnification, 1e-3);
        assertEquals(toRadians(70), acqConfig.tiltAngle, 1e-3);
        assertEquals(15, acqConfig.workingDistance * 1000, 1e-3);
    }



    @Test
    public void testGetPhase() {
        assertEquals(PhaseMap.NO_PHASE, mmap.getPhase(0), 1e-4);
        assertEquals(CrystalFactory.silicon(), mmap.getPhase(1), 1e-4);
        assertEquals(CrystalFactory.ferrite(), mmap.getPhase(2), 1e-4);
        assertEquals(CrystalFactory.silicon(), mmap.getPhase(3), 1e-4);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetPhaseException1() {
        mmap.getPhase(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetPhaseException2() {
        mmap.getPhase(4);
    }



    @Test
    public void testGetPhaseId() {
        assertEquals(0, mmap.getPhaseId(0));
        assertEquals(1, mmap.getPhaseId(1));
        assertEquals(2, mmap.getPhaseId(2));
        assertEquals(1, mmap.getPhaseId(3));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetPhaseIdException1() {
        mmap.getPhaseId(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetPhaseIdException2() {
        mmap.getPhaseId(4);
    }



    @Test
    public void testGetPhases() {
        Crystal[] phases = mmap.getPhases();

        assertEquals(2, phases.length);

        assertEquals(CrystalFactory.silicon(), phases[0], 1e-3);
        assertEquals(CrystalFactory.ferrite(), phases[1], 1e-3);
    }



    @Test
    public void testGetPhasesMap() throws IOException {
        PhaseMap phasesMap = mmap.getPhaseMap();

        PhaseMap expectedPhasesMap =
                new PhaseMapLoader().load(getFile("org/ebsdimage/testdata/Phases.bmp"));

        assertArrayEquals(expectedPhasesMap.pixArray, phasesMap.pixArray);

        java.util.Map<Integer, Crystal> items = phasesMap.getItems();
        assertEquals(3, items.size());
        assertEquals(PhaseMap.NO_PHASE, items.get(0), 1e-6);
        assertEquals(CrystalFactory.silicon(), items.get(1), 1e-6);
        assertEquals(CrystalFactory.ferrite(), items.get(2), 1e-6);
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
        assertEquals(new Rotation(1, 0, 0, 0, false), mmap.getRotation(0), 1e-6);
        assertEquals(new Rotation(0, 1, 0, 0, false), mmap.getRotation(1), 1e-6);
        assertEquals(new Rotation(0, 0, 1, 0, false), mmap.getRotation(2), 1e-6);
        assertEquals(new Rotation(0, 0, 0, 1, false), mmap.getRotation(3), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRotationException1() {
        mmap.getRotation(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRotationException2() {
        mmap.getRotation(4);
    }



    @Test
    public void testRemoveMap() {
        ByteMap map = new ByteMap(2, 2);
        map.setCalibration(new Calibration(1e-6, 1e-6, "m"));

        mmap.add("MAP", map);
        assertTrue(mmap.contains("MAP"));

        mmap.remove(map);
        assertFalse(mmap.contains("MAP"));
    }



    @Test
    public void testRemoveString() {
        ByteMap map = new ByteMap(2, 2);
        map.setCalibration(new Calibration(1e-6, 1e-6, "m"));

        mmap.add("MAP", map);
        assertTrue(mmap.contains("MAP"));

        mmap.remove("MAP");
        assertFalse(mmap.contains("MAP"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRemoveStringException() {
        mmap.remove(EbsdMMap.Q0);
    }

}
