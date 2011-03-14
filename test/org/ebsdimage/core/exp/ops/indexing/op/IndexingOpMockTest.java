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
package org.ebsdimage.core.exp.ops.indexing.op;

import java.io.File;

import magnitude.core.Magnitude;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import ptpshared.math.old.Quaternion;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class IndexingOpMockTest extends TestCase {

    private IndexingOpMock op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IndexingOpMock();

        Magnitude theta = new Magnitude(0.0, "rad");
        Magnitude rho = new Magnitude(15.0, "px");
        HoughPeak peak1 = new HoughPeak(theta, rho);

        theta = new Magnitude(1.0, "rad");
        rho = new Magnitude(23.0, "px");
        HoughPeak peak2 = new HoughPeak(theta, rho);

        theta = new Magnitude(0.0, "rad");
        rho = new Magnitude(31.0, "px");
        HoughPeak peak3 = new HoughPeak(theta, rho);

        srcPeaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testIndex() {
        Solution[] slns = op.index(null, srcPeaks);

        assertEquals(3, slns.length);

        Crystal expectedPhase = CrystalFactory.silicon();
        Quaternion expectedRotation = Quaternion.IDENTITY;
        for (Solution sln : slns) {
            assertTrue(expectedPhase.equals(sln.phase, 1e-6));
            assertTrue(expectedRotation.equals(sln.rotation, 1e-6));
        }

        assertEquals(0.0, slns[0].fit, 1e-6);
        assertEquals(0.333333, slns[1].fit, 1e-6);
        assertEquals(0.666666, slns[2].fit, 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "IndexingOpMock []");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new IndexingOpMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new IndexingOpMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1865016634, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IndexingOpMock other = new XmlLoader().load(IndexingOpMock.class, file);
        assertEquals(op, other, 1e-6);
    }
}
