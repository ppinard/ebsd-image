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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import rmlimage.core.ByteMap;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Reflectors;
import crystallography.core.XrayScatteringFactors;
import crystallography.core.crystals.Silicon;

public class PatternBandEdgesTest extends TestCase {

    private PatternBandEdges pattern;

    private Reflectors reflectors;
    private Camera camera;
    private Energy energy;
    private Eulers rotation;



    @Before
    public void setUp() throws Exception {
        reflectors =
                new Reflectors(new Silicon(), new XrayScatteringFactors(), 4);
        camera = new Camera(0.0, 0.0, 0.3);
        energy = new Energy(20e3);
        rotation = new Eulers(0.0, 0.0, 0.0);

        pattern =
                new PatternBandEdges(336, 256, reflectors, -1, camera, energy,
                        rotation);
    }



    @Test
    public void testDraw() throws IOException {
        pattern.draw();

        ByteMap patternMap = pattern.getPatternMap();

        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "patternbandedges.bmp"));
        // rmlimage.io.IO.save(patternMap);

        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/patternbandedges.bmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testDrawRealMap() throws IOException {
        pattern.draw();

        RealMap patternMap = pattern.getPatternRealMap();

        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "patternbandedges.rmp"));
        // rmlimage.module.real.io.IO.save(patternMap);

        RealMap expectedMap =
                (RealMap) load("org/ebsdimage/testdata/patternbandedges.rmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testGetBands() {
        pattern.draw();

        assertEquals(92, pattern.getBands().length);
    }



    @Test
    public void testPatternBandEdgesIntIntReflectorsIntCameraEnergyEulers() {
        assertEquals(336, pattern.width);
        assertEquals(256, pattern.height);
        assertEquals(reflectors.size(), pattern.numberReflectors);
        assertTrue(energy.equals(pattern.energy, 1e-6));
        assertTrue(camera.equals(pattern.camera, 1e-6));
        assertTrue(new Quaternion(rotation).equals(pattern.rotation, 1e-6));
    }



    @Test
    public void testPatternBandEdgesIntIntReflectorsIntCameraEnergyQuaternion() {
        Quaternion quaternion = new Quaternion(rotation);
        PatternBandEdges other =
                new PatternBandEdges(336, 256, reflectors, 5, camera, energy,
                        quaternion);

        assertEquals(336, other.width);
        assertEquals(256, other.height);
        assertEquals(5, other.numberReflectors);
        assertTrue(energy.equals(other.energy, 1e-6));
        assertTrue(camera.equals(other.camera, 1e-6));
        assertTrue(quaternion.equals(other.rotation, 1e-6));
    }

}
