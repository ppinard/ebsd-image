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
package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimOperation;
import org.ebsdimage.core.sim.SimTester;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOpMock;
import org.ebsdimage.io.SmpInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmpFileTest extends TestCase {

    private SmpFile op;

    private PatternSimOp patternSimOp;

    private Sim sim;



    @Before
    public void setUp() throws Exception {
        op = new SmpFile();
        patternSimOp = new PatternSimOpMock();
        SimOperation[] ops = new SimOperation[] { op, patternSimOp };

        sim = SimTester.createSim(ops);
    }



    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        // SimTester.removeSimPath();
    }



    @Test
    public void testTearDown() throws IOException {
        assertTrue(true); // Tested in testSetUp())
    }



    @Test
    public void testSetUp() throws IOException {
        SimTester.simPath.mkdirs();

        op.setUp(sim);
        op.tearDown(sim);

        // Test SMP
        assertTrue(new File(SimTester.simPath, "Sim1.smp").exists());
    }



    @Test
    public void testSave() throws IOException {
        sim.run();

        // Test SMP
        SmpInputStream smp =
                new SmpInputStream(new File(SimTester.simPath, "Sim1.smp"));
        assertEquals(1, smp.getMapCount());
        assertEquals(2, smp.getMapWidth());
        assertEquals(2, smp.getMapHeight());
        assertEquals(0, smp.getStartIndex());
        assertEquals(0, smp.getEndIndex());
        smp.close();
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new SmpFile()));
    }



    @Test
    public void testHashCode() {
        assertEquals(-420404239, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        SmpFile other = new XmlLoader().load(SmpFile.class, file);
        assertEquals(op, other);
    }

}
