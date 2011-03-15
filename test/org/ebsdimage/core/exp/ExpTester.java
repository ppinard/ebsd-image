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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.*;
import org.ebsdimage.core.exp.ops.detection.op.DetectionOpMock;
import org.ebsdimage.core.exp.ops.detection.post.DetectionPostOpsMock;
import org.ebsdimage.core.exp.ops.detection.pre.DetectionPreOpsMock;
import org.ebsdimage.core.exp.ops.detection.results.DetectionResultsOpsMock;
import org.ebsdimage.core.exp.ops.hough.op.HoughOpMock;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOpsMock;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOpsMock;
import org.ebsdimage.core.exp.ops.hough.results.HoughResultsOpsMock;
import org.ebsdimage.core.exp.ops.identification.op.IdentificationOpMock;
import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOpsMock;
import org.ebsdimage.core.exp.ops.identification.pre.IdentificationPreOpsMock;
import org.ebsdimage.core.exp.ops.identification.results.IdentificationResultsOpsMock;
import org.ebsdimage.core.exp.ops.indexing.op.IndexingOpMock;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOpsMock;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOpsMock;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOpsMock;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOpsMock;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOpsMock;
import org.ebsdimage.core.run.Operation;
import org.junit.After;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.toRadians;

public abstract class ExpTester extends TestCase {

    // static {
    // rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
    // rmlimage.io.IO.addHandler(rmlimage.module.real.io.IO.class);
    // }

    protected Exp exp;

    public static final File expPath = new File(FileUtil.getTempDirFile(),
            "exp1/");



    public static Exp createExp() {
        int width = 2;
        int height = 1;

        ArrayList<Operation> ops = createOperations();

        Exp exp =
                new Exp(createExpMMap(width, height),
                        ops.toArray(new Operation[ops.size()]));
        exp.setName("ExpTester");
        exp.setDir(expPath);

        exp.addExpListener(new MapsSaverListener());

        return exp;
    }



    public static ExpMMap createExpMMap(int width, int height) {
        ExpMMap mmap = new ExpMMap(width, height);

        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Microscope microscope = new Microscope(camera, new Vector3D(0, 1, 0));
        microscope.setBeamEnergy(20e3);
        microscope.setMagnification(100);
        microscope.setTiltAngle(Math.toRadians(70));
        microscope.setWorkingDistance(0.015);

        EbsdMetadata metadata = new EbsdMetadata(microscope);
        mmap.setMetadata(metadata);

        for (Crystal phase : createPhases())
            mmap.getPhaseMap().register(phase);

        return mmap;
    }



    public static ArrayList<Operation> createOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        ops.add(new PatternOpMock(2));
        ops.add(new PatternPostOpsMock());
        ops.add(new PatternResultsOpsMock());

        ops.add(new HoughPreOpsMock());
        ops.add(new HoughOpMock());
        ops.add(new HoughPostOpsMock());
        ops.add(new HoughResultsOpsMock());

        ops.add(new DetectionPreOpsMock());
        ops.add(new DetectionOpMock());
        ops.add(new DetectionPostOpsMock());
        ops.add(new DetectionResultsOpsMock());

        ops.add(new IdentificationPreOpsMock());
        ops.add(new IdentificationOpMock());
        ops.add(new IdentificationPostOpsMock());
        ops.add(new IdentificationResultsOpsMock());

        ops.add(new IndexingPreOpsMock());
        ops.add(new IndexingOpMock());
        ops.add(new IndexingPostOpsMock());
        ops.add(new IndexingResultsOpsMock());

        return ops;
    }



    public static Crystal[] createPhases() {
        return new Crystal[] { CrystalFactory.silicon() };
    }



    @Override
    @After
    public void tearDown() throws Exception {
        if (expPath.exists())
            FileUtil.rmdir(expPath);
    }



    @Test
    public void testExpMMap() {
        // Dimensions
        assertEquals(2, exp.mmap.width);
        assertEquals(1, exp.mmap.height);

        // Metadata
        Microscope microscope = exp.getMetadata().microscope;
        assertEquals(20e3, microscope.getBeamEnergy(), 1e-6);
        assertEquals(100, microscope.getMagnification(), 1e-6);
        assertEquals(toRadians(70), microscope.getTiltAngle(), 1e-6);
        assertEquals(15e-3, microscope.getWorkingDistance(), 1e-6);

        // Phases
        assertEquals(1, exp.mmap.getPhases().length);
        assertTrue(CrystalFactory.silicon().equals(exp.mmap.getPhases()[0],
                1e-6));
    }



    @Test
    public void testGetDetectionOp() {
        assertEquals(new DetectionOpMock(), exp.getDetectionOp());
    }



    @Test
    public void testGetDetectionPostOps() {
        assertEquals(1, exp.getDetectionPostOps().length);
        assertEquals(new DetectionPostOpsMock(), exp.getDetectionPostOps()[0]);
    }



    @Test
    public void testGetDetectionPreOps() {
        assertEquals(1, exp.getDetectionPreOps().length);
        assertEquals(new DetectionPreOpsMock(), exp.getDetectionPreOps()[0]);
    }



    @Test
    public void testGetDetectionResultsOps() {
        assertEquals(1, exp.getDetectionResultsOps().length);
        assertEquals(new DetectionResultsOpsMock(),
                exp.getDetectionResultsOps()[0]);
    }



    @Test
    public void testGetHoughOp() {
        assertEquals(new HoughOpMock(), exp.getHoughOp());
    }



    @Test
    public void testGetHoughPostOps() {
        assertEquals(1, exp.getHoughPostOps().length);
        assertEquals(new HoughPostOpsMock(), exp.getHoughPostOps()[0]);
    }



    @Test
    public void testGetHoughPreOps() {
        assertEquals(1, exp.getHoughPreOps().length);
        assertEquals(new HoughPreOpsMock(), exp.getHoughPreOps()[0]);
    }



    @Test
    public void testGetHoughResultsOps() {
        assertEquals(1, exp.getHoughResultsOps().length);
        assertEquals(new HoughResultsOpsMock(), exp.getHoughResultsOps()[0]);
    }



    @Test
    public void testGetIdentificationOp() {
        assertEquals(new IdentificationOpMock(), exp.getIdentificationOp());
    }



    @Test
    public void testGetIdentificationPostOps() {
        assertEquals(1, exp.getIdentificationPostOps().length);
        assertEquals(new IdentificationPostOpsMock(),
                exp.getIdentificationPostOps()[0]);
    }



    @Test
    public void testGetIdentificationPreOps() {
        assertEquals(1, exp.getIdentificationPreOps().length);
        assertEquals(new IdentificationPreOpsMock(),
                exp.getIdentificationPreOps()[0]);
    }



    @Test
    public void testGetIdentificationResultsOps() {
        assertEquals(1, exp.getIdentificationResultsOps().length);
        assertEquals(new IdentificationResultsOpsMock(),
                exp.getIdentificationResultsOps()[0]);
    }



    @Test
    public void testGetIndexingOp() {
        assertEquals(new IndexingOpMock(), exp.getIndexingOp());
    }



    @Test
    public void testGetIndexingPostOps() {
        assertEquals(1, exp.getIndexingPostOps().length);
        assertEquals(new IndexingPostOpsMock(), exp.getIndexingPostOps()[0]);
    }



    @Test
    public void testGetIndexingPreOps() {
        assertEquals(1, exp.getIndexingPreOps().length);
        assertEquals(new IndexingPreOpsMock(), exp.getIndexingPreOps()[0]);
    }



    @Test
    public void testGetIndexingResultsOps() {
        assertEquals(1, exp.getIndexingResultsOps().length);
        assertEquals(new IndexingResultsOpsMock(),
                exp.getIndexingResultsOps()[0]);
    }



    @Test
    public void testGetName() {
        assertEquals("ExpTester", exp.getName());
    }



    @Test
    public void testGetPatternOp() {
        System.out.println(exp.getPatternOp().size);
        System.out.println(exp.getPatternOp().startIndex);
        assertEquals(new PatternOpMock(2), exp.getPatternOp());
    }



    @Test
    public void testGetPatternPostOps() {
        assertEquals(1, exp.getPatternPostOps().length);
        assertEquals(new PatternPostOpsMock(), exp.getPatternPostOps()[0]);
    }



    @Test
    public void testGetPatternResultsOps() {
        assertEquals(1, exp.getPatternResultsOps().length);
        assertEquals(new PatternResultsOpsMock(), exp.getPatternResultsOps()[0]);
    }



    @Test
    public void testRun() throws IOException {
        // Make sure it is in the temporary folder
        exp.setDir(expPath);

        exp.run();

        // Test save maps
        assertEquals(21 * 2, FileUtil.listFiles(expPath).length);

        // Test maps
        assertEquals(11, exp.mmap.getAliases().length);

        // Q0
        Map map = exp.mmap.getMap(EbsdMMap.Q0);
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        RealMap realMap = (RealMap) map;
        assertEquals(Float.NaN, realMap.pixArray[0], 1e-6);
        assertEquals(Float.NaN, realMap.pixArray[1], 1e-6);

        // Q1
        map = exp.mmap.getMap(EbsdMMap.Q1);
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(Float.NaN, realMap.pixArray[0], 1e-6);
        assertEquals(Float.NaN, realMap.pixArray[1], 1e-6);

        // Q2
        map = exp.mmap.getMap(EbsdMMap.Q2);
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(Float.NaN, realMap.pixArray[0], 1e-6);
        assertEquals(Float.NaN, realMap.pixArray[1], 1e-6);

        // Q3
        map = exp.mmap.getMap(EbsdMMap.Q3);
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(Float.NaN, realMap.pixArray[0], 1e-6);
        assertEquals(Float.NaN, realMap.pixArray[1], 1e-6);

        // Phases
        map = exp.mmap.getMap(EbsdMMap.PHASES);
        assertNotNull(map);
        assertEquals(PhaseMap.class, map.getClass());

        // PatternResultsOpsMock
        map = exp.mmap.getMap(PatternResultsOpsMock.class.getSimpleName());
        assertNotNull(map);
        assertEquals(ByteMap.class, map.getClass());

        ByteMap byteMap = (ByteMap) map;
        assertEquals(18, byteMap.pixArray[0]);
        assertEquals(20, byteMap.pixArray[1]);

        // HoughResultsOpsMock
        map = exp.mmap.getMap(HoughResultsOpsMock.class.getSimpleName());
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(132, realMap.pixArray[0], 1e-6);
        assertEquals(144, realMap.pixArray[1], 1e-6);

        // DetectionResultsOpsMock
        map = exp.mmap.getMap(DetectionResultsOpsMock.class.getSimpleName());
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(12, realMap.pixArray[0], 1e-6);
        assertEquals(12, realMap.pixArray[1], 1e-6);

        // IdentificationResultsOpsMock
        map =
                exp.mmap.getMap(IdentificationResultsOpsMock.class.getSimpleName());
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(0.26, realMap.pixArray[0], 1e-6);
        assertEquals(0.30, realMap.pixArray[1], 1e-6);

        // IndexingResultsOpsMock
        map = exp.mmap.getMap(IndexingResultsOpsMock.class.getSimpleName());
        assertNotNull(map);
        assertEquals(RealMap.class, map.getClass());

        realMap = (RealMap) map;
        assertEquals(0.5, realMap.pixArray[0], 1e-6);
        assertEquals(0.5, realMap.pixArray[1], 1e-6);
    }

}
