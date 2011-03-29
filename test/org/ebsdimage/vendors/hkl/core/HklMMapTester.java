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
package org.ebsdimage.vendors.hkl.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.ErrorMap;
import org.ebsdimage.core.PhaseMap;
import org.ebsdimage.io.ErrorMapLoader;
import org.ebsdimage.io.PhaseMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.io.RmpLoader;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public abstract class HklMMapTester extends TestCase {

    protected HklMMap mmap;

    protected Crystal copperPhase;



    @Before
    public void setUp() throws Exception {
        copperPhase =
                new CrystalLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml"));
    }



    @Test
    public void testCreateMapIntInt() {
        HklMMap newMMap = mmap.createMap(2, 2);

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

        ByteMap bandContrastMap = newMMap.getBandContrastMap();
        assertEquals(2, bandContrastMap.width);
        assertEquals(2, bandContrastMap.height);

        ByteMap bandSlopeMap = newMMap.getBandSlopeMap();
        assertEquals(2, bandSlopeMap.width);
        assertEquals(2, bandSlopeMap.height);

        ByteMap bandCountMap = newMMap.getBandCountMap();
        assertEquals(2, bandCountMap.width);
        assertEquals(2, bandCountMap.height);

        RealMap madMap = newMMap.getMeanAngularDeviationMap();
        assertEquals(2, madMap.width);
        assertEquals(2, madMap.height);
    }



    @Test
    public void testDuplicate() {
        HklMMap newMMap = mmap.duplicate();

        assertEquals(mmap.width, newMMap.width);
        assertEquals(mmap.height, newMMap.height);

        mmap.getQ0Map().assertEquals(newMMap.getQ0Map(), 1e-6);
        mmap.getQ1Map().assertEquals(newMMap.getQ1Map(), 1e-6);
        mmap.getQ2Map().assertEquals(newMMap.getQ2Map(), 1e-6);
        mmap.getQ3Map().assertEquals(newMMap.getQ3Map(), 1e-6);
        mmap.getPhaseMap().assertEquals(newMMap.getPhaseMap(), 1e-6);

        mmap.getBandContrastMap().assertEquals(newMMap.getBandContrastMap());
        mmap.getBandSlopeMap().assertEquals(newMMap.getBandSlopeMap());
        mmap.getBandCountMap().assertEquals(newMMap.getBandCountMap());
        mmap.getMeanAngularDeviationMap().assertEquals(
                newMMap.getMeanAngularDeviationMap(), 1e-6);
    }



    @Test
    public void testGetBandContrast() {
        ByteMap byteMap = mmap.getBandContrastMap();

        ByteMap expectedByteMap =
                (ByteMap) load("org/ebsdimage/vendors/hkl/testdata/BandContrast.bmp");
        byteMap.assertEquals(expectedByteMap);
    }



    @Test
    public void testGetBandCount() {
        ByteMap byteMap = mmap.getBandCountMap();

        ByteMap expectedByteMap =
                (ByteMap) load("org/ebsdimage/vendors/hkl/testdata/BandCount.bmp");
        byteMap.assertEquals(expectedByteMap);
    }



    @Test
    public void testGetBandSlope() {
        ByteMap byteMap = mmap.getBandSlopeMap();

        ByteMap expectedByteMap =
                (ByteMap) load("org/ebsdimage/vendors/hkl/testdata/BandSlope.bmp");
        byteMap.assertEquals(expectedByteMap);
    }



    @Test
    public void testGetCalibration() {
        Calibration cal = mmap.getCalibration();

        assertEquals(0.8, cal.getDX().getValue("um"), 1e-6);
        assertEquals(0.8, cal.getDY().getValue("um"), 1e-6);
    }



    @Test
    public void testGetErrorMap() throws IOException {
        ByteMap byteMap = mmap.getErrorMap();

        ErrorMap expectedMap =
                new ErrorMapLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Errors.bmp"));
        byteMap.assertEquals(expectedMap);
    }



    @Test
    public void testGetMeanAngularDeviation() throws IOException {
        RealMap realMap = mmap.getMeanAngularDeviationMap();

        RealMap expectedRealMap =
                new RmpLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/MeanAngularDeviation.rmp"));
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetMetaData() {
        HklMetadata metadata = mmap.getMetadata();

        assertEquals("Project19", metadata.projectName);
        // Cannot be tested
        // assertEquals(new File("").getAbsoluteFile(), metadata.projectPath);
    }



    @Test
    public void testGetMicroscope() {
        AcquisitionConfig acqConfig = mmap.getAcquisitionConfig();

        assertEquals(Rotation.IDENTITY, acqConfig.sampleRotation, 1e-6);
        assertEquals(20e3, acqConfig.beamEnergy, 1e-6);
        assertEquals(70, Math.toDegrees(acqConfig.tiltAngle), 1e-6);
        assertEquals(600, acqConfig.magnification, 1e-6);
    }



    @Test
    public void testGetPatternFileIndex() {
        File projectDir = mmap.getMetadata().projectDir;

        File patternFile = mmap.getPatternFile(0);
        File expected = new File(projectDir, "Project19Images/Project191.jpg");
        assertEquals(expected, patternFile);

        patternFile = mmap.getPatternFile(mmap.size - 1);
        expected = new File(projectDir, "Project19Images/Project194.jpg");
        assertEquals(expected, patternFile);

        patternFile = mmap.getPatternFile(1);
        expected = new File(projectDir, "Project19Images/Project192.jpg");
        assertEquals(expected, patternFile);
    }



    @Test
    public void testGetPatternFileIndexFile() {
        File patternFile;

        patternFile = mmap.getPatternFile(0, new File("blah"));
        assertEquals(new File("blah/Project191.jpg"), patternFile);

        patternFile = mmap.getPatternFile(mmap.size - 1, new File("blah"));
        assertEquals(new File("blah/Project194.jpg"), patternFile);

        patternFile = mmap.getPatternFile(1, new File("blah"));
        assertEquals(new File("blah/Project192.jpg"), patternFile);
    }



    @Test
    public void testGetPatternFiles() {
        File projectDir = mmap.getMetadata().projectDir;
        File[] files = mmap.getPatternFiles();
        assertEquals(mmap.size, files.length);

        File expected = new File(projectDir, "Project19Images/Project191.jpg");
        assertEquals(expected, files[0]);

        expected = new File(projectDir, "Project19Images/Project194.jpg");
        assertEquals(expected, files[files.length - 1]);
    }



    @Test
    public void testGetPatternFilesFile() {
        File[] files = mmap.getPatternFiles(new File("blah"));
        assertEquals(mmap.size, files.length);
        assertEquals(new File("blah/Project191.jpg"), files[0]);
        assertEquals(new File("blah/Project194.jpg"), files[files.length - 1]);
    }



    @Test
    public void testGetPhasesMap() throws IOException {
        PhaseMap phasesMap = mmap.getPhaseMap();

        PhaseMap expectedPhasesMap =
                new PhaseMapLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Phases.bmp"));

        phasesMap.assertEquals(expectedPhasesMap, 1e-2);

        Crystal[] phases = phasesMap.getPhases();
        assertEquals(1, phases.length);

        assertTrue(copperPhase.equals(phases[0], 1e-3));
    }



    @Test
    public void testGetQ0Map() throws IOException {
        RealMap realMap = mmap.getQ0Map();

        RealMap expectedRealMap =
                new RmpLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Q0.rmp"));
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ1Map() throws IOException {
        RealMap realMap = mmap.getQ1Map();

        RealMap expectedRealMap =
                new RmpLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Q1.rmp"));
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ2Map() throws IOException {
        RealMap realMap = mmap.getQ2Map();

        RealMap expectedRealMap =
                new RmpLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Q2.rmp"));
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ3Map() throws IOException {
        RealMap realMap = mmap.getQ3Map();

        RealMap expectedRealMap =
                new RmpLoader().load(getFile("org/ebsdimage/vendors/hkl/testdata/Q3.rmp"));
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testHklMMap() {
        assertEquals(2, mmap.width);
        assertEquals(2, mmap.height);
        assertEquals(4, mmap.size);

        // Test nb of Maps in MultiMap
        Map[] maps = mmap.getMaps();
        assertEquals(10, maps.length);

        // Test the presence of the Maps
        assertTrue(mmap.contains(EbsdMMap.Q0));
        assertTrue(mmap.contains(EbsdMMap.Q1));
        assertTrue(mmap.contains(EbsdMMap.Q2));
        assertTrue(mmap.contains(EbsdMMap.Q3));
        assertTrue(mmap.contains(EbsdMMap.PHASES));
        assertTrue(mmap.contains(EbsdMMap.ERRORS));

        assertTrue(mmap.contains(HklMMap.BAND_CONTRAST));
        assertTrue(mmap.contains(HklMMap.BAND_COUNT));
        assertTrue(mmap.contains(HklMMap.BAND_SLOPE));
        assertTrue(mmap.contains(HklMMap.MEAN_ANGULAR_DEVIATION));
    }

}
