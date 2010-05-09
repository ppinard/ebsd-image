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
package org.ebsdimage.core;

import static java.lang.Math.PI;
import static java.util.Arrays.sort;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.PatternBandCenter;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.AxisAngle;
import ptpshared.core.math.Quaternion;
import ptpshared.utility.LoggerUtil;
import crystallography.core.*;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;
import crystallography.core.crystals.ZirconiumAlpha;

public class IndexingTest {

    private Camera camera;

    private Reflectors reflsFCC;
    private Reflectors reflsBCC;
    private Reflectors reflsHCP;

    private Quaternion rotation;

    private HoughPeak[] peaks1;
    private HoughPeak[] peaks2;



    // private HoughPeak[] peaks3;

    @Before
    public void setUp() throws Exception {
        LoggerUtil.turnOffLogger(Logger.getLogger("ebsd"));

        camera = new Camera(0.0, 0.0, 0.4);
        rotation = new Quaternion(new AxisAngle(PI / 3, 1, -1, 1));

        ScatteringFactors scatter = new XrayScatteringFactors();
        reflsFCC = new Reflectors(new Silicon(), scatter, 3);
        reflsBCC = new Reflectors(new IronBCC(), scatter, 3);
        reflsHCP = new Reflectors(new ZirconiumAlpha(), scatter, 2);

        PatternBandCenter pattern =
                new PatternBandCenter(356, 256, reflsFCC, 25, camera,
                        new Energy(20e3), rotation);
        peaks1 =
                Arrays.copyOf(HoughMath.bandsToHoughPeaks(pattern.getBands()),
                        3);

        pattern =
                new PatternBandCenter(356, 256, reflsBCC, 25, camera,
                        new Energy(20e3), rotation);
        peaks2 =
                Arrays.copyOf(HoughMath.bandsToHoughPeaks(pattern.getBands()),
                        3);

        // pattern =
        // new PatternBandCenter(356, 256, reflsHCP, 25, camera,
        // new Energy(20e3), rotation);
        // peaks3 =
        // Arrays.copyOf(HoughMath.bandsToHoughPeaks(pattern.getBands()),
        // 5);

        // ByteMap patternMap = pattern.getPatternMap();
        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "indexing.bmp"));
        // IO.save(patternMap);
    }



    @Test
    public void testIndex1() throws IOException {
        // Find all solutions
        Solution[] slns =
                Indexing.index(
                        new Reflectors[] { reflsFCC, reflsBCC, reflsHCP },
                        peaks1, camera);

        // Find most best solution
        sort(slns, new SolutionFitComparator());
        Solution sln = slns[0];

        Quaternion expectedRotation =
                Calculations.reduce(rotation, PointGroup.PG432);
        assertTrue(expectedRotation.equals(sln.rotation, 1e-4));

        Crystal expectedPhase = new Silicon();
        assertTrue(expectedPhase.equals(sln.phase, 1e-4));
    }



    @Test
    public void testIndex2() throws IOException {
        // Find all solutions
        Solution[] slns =
                Indexing.index(
                        new Reflectors[] { reflsFCC, reflsBCC, reflsHCP },
                        peaks2, camera);

        // Find most best solution
        sort(slns, new SolutionFitComparator());
        Solution sln = slns[0];

        Quaternion expectedRotation =
                Calculations.reduce(rotation, PointGroup.PG432);
        assertTrue(expectedRotation.equals(sln.rotation, 1e-4));

        Crystal expectedPhase = new IronBCC();
        assertTrue(expectedPhase.equals(sln.phase, 1e-4));
    }

    // @Test
    // public void testIndex3() throws IOException {
    // // Find all solutions
    // Solution[] slns =
    // Indexing.index(new Reflectors[] { reflsHCP }, peaks3, camera);
    //
    // // Find most best solution
    // sort(slns, new SolutionFitComparator());
    // reverse(slns);
    //
    // for (Solution sln : slns)
    // System.out.println(sln);
    //
    // Solution sln = slns[0];
    //
    // PatternBandCenter pattern =
    // new PatternBandCenter(356, 256, reflsBCC, 25, camera,
    // new Energy(20e3), slns[slns.length - 1].rotation);
    // ByteMap slnMap = pattern.getPatternMap();
    // slnMap.setFile(new File(FileUtil.getTempDir(), "solution.bmp"));
    // IO.save(slnMap);
    //
    // Quaternion expectedRotation =
    // Calculations.reduce(rotation, PointGroup.pg622());
    //
    // System.out.println(expectedRotation.toAxisAngle().toString());
    // System.out.println(sln.rotation.toAxisAngle().toString());
    // assertTrue(expectedRotation.equals(sln.rotation, 1e-4));
    //
    // Crystal expectedPhase = new ZirconiumAlpha();
    // assertTrue(expectedPhase.equals(sln.phase, 1e-4));
    // }

}
