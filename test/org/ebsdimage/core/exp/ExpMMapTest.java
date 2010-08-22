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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.PhasesMap;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class ExpMMapTest {

    private ExpMMap mmap;

    private ExpMetadata metadata;

    private RealMap q0Map;

    private RealMap q1Map;

    private RealMap q2Map;

    private RealMap q3Map;

    private PhasesMap phasesMap;



    @Before
    public void setUp() throws Exception {
        metadata =
                new ExpMetadata(20e3, 100, toRadians(70), 15e-3, 1e-6, 1e-6,
                        Quaternion.IDENTITY, new Camera(0.1, 0.2, 0.3));

        HashMap<String, Map> mapList = new HashMap<String, Map>();

        q0Map = new RealMap(2, 2);
        q0Map.pixArray[0] = 1.0f;
        mapList.put(EbsdMMap.Q0, q0Map);

        q1Map = new RealMap(2, 2);
        q1Map.pixArray[1] = 1.0f;
        mapList.put(EbsdMMap.Q1, q1Map);

        q2Map = new RealMap(2, 2);
        q2Map.pixArray[2] = 1.0f;
        mapList.put(EbsdMMap.Q2, q2Map);

        q3Map = new RealMap(2, 2);
        q3Map.pixArray[3] = 1.0f;
        mapList.put(EbsdMMap.Q3, q3Map);

        phasesMap =
                new PhasesMap(2, 2, new byte[] { 0, 1, 2, 1 }, new Crystal[] {
                        new Silicon(), new IronBCC() });
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test
    public void testDuplicate() {
        ExpMMap other = mmap.duplicate();

        assertEquals(mmap.width, other.width);
        assertEquals(mmap.height, other.height);
        assertEquals(mmap.size, other.size);

        assertEquals(mmap.beamEnergy, other.beamEnergy, 1e-6);
        assertEquals(mmap.magnification, other.magnification, 1e-6);
        assertEquals(mmap.tiltAngle, other.tiltAngle, 1e-6);
        assertEquals(mmap.pixelWidth, other.pixelWidth, 1e-6);
        assertEquals(mmap.pixelHeight, other.pixelHeight, 1e-6);
        assertTrue(mmap.sampleRotation.equals(other.sampleRotation, 1e-6));

        mmap.getQ0Map().assertEquals(other.getQ0Map());
        mmap.getQ1Map().assertEquals(other.getQ1Map());
        mmap.getQ2Map().assertEquals(other.getQ2Map());
        mmap.getQ3Map().assertEquals(other.getQ3Map());
        mmap.getPhasesMap().assertEquals(other.getPhasesMap());
    }



    @Test
    public void testExpMMap() {
        assertEquals(2, mmap.width);
        assertEquals(2, mmap.height);
        assertEquals(4, mmap.size);

        assertEquals(metadata.beamEnergy, mmap.beamEnergy, 1e-6);
        assertEquals(metadata.magnification, mmap.magnification, 1e-6);
        assertEquals(metadata.tiltAngle, mmap.tiltAngle, 1e-6);
        assertEquals(metadata.workingDistance, mmap.workingDistance, 1e-6);
        assertEquals(metadata.pixelWidth, mmap.pixelWidth, 1e-6);
        assertEquals(metadata.pixelHeight, mmap.pixelHeight, 1e-6);
        assertTrue(metadata.sampleRotation.equals(mmap.sampleRotation, 1e-6));
        assertTrue(metadata.calibration.equals(mmap.calibration, 1e-6));

        q0Map.assertEquals(mmap.getQ0Map());
        q1Map.assertEquals(mmap.getQ1Map());
        q2Map.assertEquals(mmap.getQ2Map());
        q3Map.assertEquals(mmap.getQ3Map());
        phasesMap.assertEquals(mmap.getPhasesMap());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionPhasesIncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, new ByteMap(2, 2));

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionPhasesMissing() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ0IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, new ByteMap(2, 2));
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ0Missing() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ1IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, new ByteMap(2, 2));
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ1Missing() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ2IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, new ByteMap(2, 2));
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ2Missing() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ3IncorrectType() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, new ByteMap(2, 2));
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpMMapExceptionQ3Missing() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        mmap = new ExpMMap(2, 2, mapList, metadata);
    }



    @Test
    public void testGetMetadata() {
        EbsdMetadata other = mmap.getMetadata();

        assertEquals(metadata.beamEnergy, other.beamEnergy, 1e-6);
        assertEquals(metadata.magnification, other.magnification, 1e-6);
        assertEquals(metadata.tiltAngle, other.tiltAngle, 1e-6);
        assertEquals(metadata.workingDistance, other.workingDistance, 1e-6);
        assertEquals(metadata.pixelWidth, other.pixelWidth, 1e-6);
        assertEquals(metadata.pixelHeight, other.pixelHeight, 1e-6);
        assertTrue(metadata.sampleRotation.equals(other.sampleRotation, 1e-6));
        assertTrue(metadata.calibration.equals(other.calibration, 1e-6));
    }



    @Test
    public void testGetPhase() {
        assertNull(mmap.getPhase(0));
        assertTrue(new Silicon().equals(mmap.getPhase(1), 1e-6));
        assertTrue(new IronBCC().equals(mmap.getPhase(2), 1e-6));
        assertTrue(new Silicon().equals(mmap.getPhase(1), 1e-6));
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
    public void testGetPhasesMap() {
        phasesMap.assertEquals(mmap.getPhasesMap());
    }



    @Test
    public void testGetQ0Map() {
        q0Map.assertEquals(mmap.getQ0Map());
    }



    @Test
    public void testGetQ1Map() {
        q1Map.assertEquals(mmap.getQ1Map());
    }



    @Test
    public void testGetQ2Map() {
        q2Map.assertEquals(mmap.getQ2Map());
    }



    @Test
    public void testGetQ3Map() {
        q3Map.assertEquals(mmap.getQ3Map());
    }



    @Test
    public void testGetRotation() {
        assertTrue(new Quaternion(1, 0, 0, 0).equals(mmap.getRotation(0), 1e-6));
        assertTrue(new Quaternion(0, 1, 0, 0).equals(mmap.getRotation(1), 1e-6));
        assertTrue(new Quaternion(0, 0, 1, 0).equals(mmap.getRotation(2), 1e-6));
        assertTrue(new Quaternion(0, 0, 0, 1).equals(mmap.getRotation(3), 1e-6));
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
        mmap.add("MAP", map);
        assertTrue(mmap.contains("MAP"));

        mmap.remove(map);
        assertFalse(mmap.contains("MAP"));
    }



    @Test
    public void testRemoveString() {
        ByteMap map = new ByteMap(2, 2);
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
