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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlshared.io.FileUtil;

public class RmpFileTest extends TestCase {

    private RmpFile op;



    @Before
    public void setUp() throws Exception {
        op = new RmpFile();
    }



    @After
    public void tearDown() throws Exception {
        if (SimTester.simPath.exists())
            FileUtil.rmdir(SimTester.simPath);
    }



    @Test
    public void testSave() throws IOException {
        // Create simulation
        Operation[] ops =
                new Operation[] { SimTester.createPatternSimOp(), op };
        Sim sim = SimTester.createSim(ops);

        // Run
        sim.run();

        // Test
        assertTrue(new File(SimTester.simPath, "Sim1_0.rmp").exists());
    }



    @Test
    public void testToString() {
        assertEquals("RmpFile []", op.toString());
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new RmpFile()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new RmpFile(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-1307907920, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        RmpFile other = new XmlLoader().load(RmpFile.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
