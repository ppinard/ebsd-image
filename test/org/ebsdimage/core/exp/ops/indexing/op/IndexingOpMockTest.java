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

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class IndexingOpMockTest extends TestCase {

    private IndexingOpMock op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IndexingOpMock();

        HoughPeak peak1 = new HoughPeak(0.0, 15.0, 1.0);
        HoughPeak peak2 = new HoughPeak(1.0, 23.0, 2.0);
        HoughPeak peak3 = new HoughPeak(0.0, 31.0, 3.0);

        srcPeaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testIndex() {
        Solution[] slns = op.index(null, srcPeaks);

        assertEquals(3, slns.length);

        Crystal expectedPhase = CrystalFactory.silicon();
        for (Solution sln : slns) {
            assertEquals(expectedPhase, sln.phase, 1e-6);
            assertEquals(Rotation.IDENTITY, sln.rotation, 1e-6);
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
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IndexingOpMock other = new XmlLoader().load(IndexingOpMock.class, file);
        assertEquals(op, other, 1e-6);
    }
}
