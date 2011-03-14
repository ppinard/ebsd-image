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
package org.ebsdimage.core.sim.ops.patternsim;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Energy;
import org.junit.Before;
import org.junit.Test;

import ptpshared.math.old.Quaternion;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;
import rmlimage.module.real.core.RealMap;
import crystallography.core.CrystalFactory;
import crystallography.core.Reflectors;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class PatternFilledBandTest extends TestCase {

    private PatternFilledBand op;

    private Reflectors reflectors;

    private Camera camera;

    private Energy energy;

    private Quaternion rotation;



    @Before
    public void setUp() throws Exception {
        op = new PatternFilledBand(336, 256, 4, ScatteringFactorsEnum.XRAY);

        reflectors = op.calculateReflectors(CrystalFactory.silicon());
        camera = new Camera(0.0, 0.0, 0.3);
        energy = new Energy(20e3);
        rotation = Quaternion.IDENTITY;
    }



    @Test
    public void testPatternFilledBandIntIntIntScatteringFactorsEnum() {
        assertEquals(336, op.width);
        assertEquals(256, op.height);
        assertEquals(4, op.maxIndex);
        assertEquals(ScatteringFactorsEnum.XRAY, op.scatterType);
    }



    @Test
    public void testCalculateReflectors() {
        assertEquals(94, reflectors.size());
    }



    @Test
    public void testGetBands() {
        op.simulate(camera, reflectors, energy, rotation);
        assertEquals(92, op.getBands().length);
    }



    @Test
    public void testGetPatternMap() {
        op.simulate(camera, reflectors, energy, rotation);

        ByteMap patternMap = op.getPatternMap();

        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "patternfilledband.bmp"));
        // rmlimage.io.IO.save(patternMap);

        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/patternfilledband.bmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testGetPatternRealMap() {
        op.simulate(camera, reflectors, energy, rotation);

        RealMap patternMap = op.getPatternRealMap();

        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "patternfilledband.rmp"));
        // rmlimage.module.real.io.IO.save(patternMap);

        RealMap expectedMap =
                (RealMap) load("org/ebsdimage/testdata/patternfilledband.rmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PatternFilledBand(337, 256, 4,
                ScatteringFactorsEnum.XRAY)));
        assertFalse(op.equals(new PatternFilledBand(336, 257, 4,
                ScatteringFactorsEnum.XRAY)));
        assertFalse(op.equals(new PatternFilledBand(336, 256, 5,
                ScatteringFactorsEnum.XRAY)));
        assertFalse(op.equals(new PatternFilledBand(336, 256, 4,
                ScatteringFactorsEnum.ELECTRON)));
        assertTrue(op.equals(new PatternFilledBand(336, 256, 4,
                ScatteringFactorsEnum.XRAY)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new PatternFilledBand(338, 256, 4,
                ScatteringFactorsEnum.XRAY), 2));
        assertFalse(op.equals(new PatternFilledBand(336, 258, 4,
                ScatteringFactorsEnum.XRAY), 2));
        assertFalse(op.equals(new PatternFilledBand(336, 256, 6,
                ScatteringFactorsEnum.XRAY), 2));
        assertFalse(op.equals(new PatternFilledBand(336, 256, 4,
                ScatteringFactorsEnum.ELECTRON), 2));
        assertTrue(op.equals(new PatternFilledBand(337, 257, 5,
                ScatteringFactorsEnum.XRAY), 2));
    }



    @Test
    public void testHashCode() {
        // Impossible to test the hash code because of the enum
        assertTrue(true);
        // assertEquals(1052419856, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PatternFilledBand other =
                new XmlLoader().load(PatternFilledBand.class, file);
        assertEquals(op, other, 1e-6);
    }

}
