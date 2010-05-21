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
package org.ebsdimage.vendors.hkl.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.io.PhasesMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.CrystalLoader;

public abstract class HklMMapTester extends TestCase {

    protected HklMMap mmap;
    protected Crystal copperPhase;



    @Before
    public void setUp() throws Exception {
        copperPhase =
                new CrystalLoader()
                        .load(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml"));
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
    public void testGetErrorNumberMap() {
        ByteMap byteMap = mmap.getErrorMap();

        ByteMap expectedByteMap =
                (ByteMap) load("org/ebsdimage/vendors/hkl/testdata/ErrorNumber.bmp");
        byteMap.assertEquals(expectedByteMap);
    }



    @Test
    public void testGetEuler1() {
        RealMap realMap = mmap.getEuler1Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Euler1.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetEuler2() {
        RealMap realMap = mmap.getEuler2Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Euler2.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetEuler3() {
        RealMap realMap = mmap.getEuler3Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Euler3.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetMeanAngularDeviation() {
        RealMap realMap = mmap.getMeanAngularDeviationMap();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/MeanAngularDeviation.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetMetaData() {
        HklMetadata metadata = mmap.getMetadata();

        assertEquals(Quaternion.IDENTITY, metadata.sampleRotation);
        assertEquals(20e3, metadata.beamEnergy, 1e-3);
        assertEquals(600, metadata.magnification, 1e-3);
        assertEquals(1.2217, metadata.tiltAngle, 1e-3);
        assertEquals(8e-7, metadata.pixelWidth, 1e-9);
        assertEquals(8e-7, metadata.pixelHeight, 1e-9);
        assertEquals("Project19", metadata.projectName);
        assertTrue(new Camera(0.1, 0.2, 0.3).equals(metadata.calibration, 1e-3));
        // Cannot be tested
        // assertEquals(new File("").getAbsoluteFile(), metadata.projectPath);
    }



    @Test
    public void testGetPatternFileIndex() {
        File patternFile;
        File expected;

        patternFile = mmap.getPatternFile(0);
        expected = new File(mmap.projectPath, "Project19Images/Project191.jpg");
        assertEquals(expected, patternFile);

        patternFile = mmap.getPatternFile(mmap.size - 1);
        expected = new File(mmap.projectPath, "Project19Images/Project194.jpg");
        assertEquals(expected, patternFile);

        patternFile = mmap.getPatternFile(1);
        expected = new File(mmap.projectPath, "Project19Images/Project192.jpg");
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
        File expected;

        File[] files = mmap.getPatternFiles();
        assertEquals(mmap.size, files.length);

        expected = new File(mmap.projectPath, "Project19Images/Project191.jpg");
        assertEquals(expected, files[0]);

        expected = new File(mmap.projectPath, "Project19Images/Project194.jpg");
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
        PhasesMap phasesMap = mmap.getPhasesMap();

        PhasesMap expectedPhasesMap =
                new PhasesMapLoader()
                        .load(FileUtil
                                .getFile("org/ebsdimage/vendors/hkl/testdata/Phases.bmp"));

        phasesMap.assertEquals(expectedPhasesMap);

        Crystal[] phases = phasesMap.getPhases();
        assertEquals(1, phases.length);

        assertTrue(copperPhase.equals(phases[0], 1e-3));
    }



    @Test
    public void testGetQ0Map() {
        RealMap realMap = mmap.getQ0Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Q0.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ1Map() {
        RealMap realMap = mmap.getQ1Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Q1.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ2Map() {
        RealMap realMap = mmap.getQ2Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Q2.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testGetQ3Map() {
        RealMap realMap = mmap.getQ3Map();

        RealMap expectedRealMap =
                (RealMap) load("org/ebsdimage/vendors/hkl/testdata/Q3.rmp");
        realMap.assertEquals(expectedRealMap, 1e-3);
    }



    @Test
    public void testHklMMap() {
        assertEquals(2, mmap.width);
        assertEquals(2, mmap.height);
        assertEquals(4, mmap.size);

        // Metadata
        assertEquals(Quaternion.IDENTITY, mmap.sampleRotation);
        assertEquals(20e3, mmap.beamEnergy, 1e-3);
        assertEquals(600, mmap.magnification, 1e-3);
        assertEquals(1.2217, mmap.tiltAngle, 1e-3);
        assertEquals(8e-7, mmap.pixelWidth, 1e-9);
        assertEquals(8e-7, mmap.pixelHeight, 1e-9);
        assertEquals("Project19", mmap.projectName);
        // Cannot be tested
        // assertEquals(new File("").getAbsoluteFile(), mmap.projectPath);

        // Test nb of Maps in MultiMap
        Map[] maps = mmap.getMaps();
        assertEquals(13, maps.length);

        // Test the presence of the Maps
        assertTrue(mmap.contains(HklMMap.Q0));
        assertTrue(mmap.contains(HklMMap.Q1));
        assertTrue(mmap.contains(HklMMap.Q2));
        assertTrue(mmap.contains(HklMMap.Q3));
        assertTrue(mmap.contains(HklMMap.BAND_CONTRAST));
        assertTrue(mmap.contains(HklMMap.BAND_COUNT));
        assertTrue(mmap.contains(HklMMap.BAND_SLOPE));
        assertTrue(mmap.contains(HklMMap.ERROR_NUMBER));
        assertTrue(mmap.contains(HklMMap.EULER1));
        assertTrue(mmap.contains(HklMMap.EULER2));
        assertTrue(mmap.contains(HklMMap.EULER3));
        assertTrue(mmap.contains(HklMMap.MEAN_ANGULAR_DEVIATION));
        assertTrue(mmap.contains(HklMMap.PHASES));
    }

}
