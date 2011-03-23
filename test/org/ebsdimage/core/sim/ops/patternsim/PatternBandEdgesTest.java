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
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.sim.SimTester;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;
import rmlimage.module.real.core.RealMap;
import crystallography.core.CrystalFactory;
import crystallography.core.Reflectors;
import crystallography.core.ReflectorsFactory;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatternBandEdgesTest extends TestCase {

    private PatternBandEdges op;

    private Microscope microscope;

    private Reflectors reflectors;

    private Rotation rotation;



    @Before
    public void setUp() throws Exception {
        op = new PatternBandEdges(400, 400);

        microscope = SimTester.createMetadata().getMicroscope();
        reflectors =
                ReflectorsFactory.generate(CrystalFactory.silicon(),
                        ScatteringFactorsEnum.XRAY, 4);
        rotation = Rotation.IDENTITY;
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PatternBandEdges(401, 400)));
        assertFalse(op.equals(new PatternBandEdges(400, 401)));
        assertTrue(op.equals(new PatternBandEdges(400, 400)));
    }



    @Test
    public void testGetBands() {
        op.simulate(null, microscope, reflectors, rotation);
        assertEquals(72, op.getBands().length);
    }



    @Test
    public void testGetPatternMap() throws IOException {
        op.simulate(null, microscope, reflectors, rotation);

        ByteMap patternMap = op.getPatternMap();

        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/patternbandedges.bmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testGetPatternRealMap() throws IOException {
        op.simulate(null, microscope, reflectors, rotation);

        RealMap patternMap = op.getPatternRealMap();

        RealMap expectedMap =
                (RealMap) load("org/ebsdimage/testdata/patternbandedges_rmp.rmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testHashCode() {
        assertEquals(-1151570512, op.hashCode());
    }



    @Test
    public void testPatternBandEdgesIntIntIntScatteringFactorsEnum() {
        assertEquals(400, op.width);
        assertEquals(400, op.height);
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PatternBandEdges other =
                new XmlLoader().load(PatternBandEdges.class, file);
        assertEquals(op, other);
    }

}
