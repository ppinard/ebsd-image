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

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.sim.SimTester;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;
import crystallography.core.CrystalFactory;
import crystallography.core.Reflectors;
import crystallography.core.ReflectorsFactory;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;

public class PatternSimOpMockTest extends TestCase {

    private PatternSimOp op;

    private Reflectors reflectors;

    private Rotation rotation;

    private Microscope microscope;



    @Before
    public void setUp() throws Exception {
        op = new PatternSimOpMock();

        reflectors =
                ReflectorsFactory.generate(CrystalFactory.silicon(),
                        ScatteringFactorsEnum.XRAY, 1);
        rotation = Rotation.IDENTITY;
        microscope = SimTester.createMetadata().microscope;
    }



    @Test
    public void testGetPattern() {
        op.simulate(null, microscope, reflectors, rotation);

        ByteMap pattern = op.getPatternMap();
        assertEquals(2, pattern.width);
        assertEquals(2, pattern.height);
        assertEquals(4, op.getBands().length);
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PatternSimOpMock other =
                new XmlLoader().load(PatternSimOpMock.class, file);
        assertEquals(op, other);
    }

}
