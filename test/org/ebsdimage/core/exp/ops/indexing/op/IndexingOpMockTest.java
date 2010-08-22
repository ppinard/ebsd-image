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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.crystals.Silicon;

public class IndexingOpMockTest {

    private IndexingOp op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IndexingOpMock();

        srcPeaks =
                new HoughPeak[] { new HoughPeak(15.0, 0.0),
                        new HoughPeak(23.0, 1.0), new HoughPeak(31.0, 0.0) };
    }



    @Test
    public void testIndex() {
        Solution[] slns = op.index(null, srcPeaks);

        assertEquals(3, slns.length);

        Crystal expectedPhase = new Silicon();
        Quaternion expectedRotation = Quaternion.IDENTITY;
        for (Solution sln : slns) {
            assertTrue(expectedPhase.equals(sln.phase, 1e-6));
            assertTrue(expectedRotation.equals(sln.rotation, 1e-6));
        }

        assertEquals(0.0, slns[0].fit, 1e-6);
        assertEquals(0.333333, slns[1].fit, 1e-6);
        assertEquals(0.666666, slns[2].fit, 1e-6);
    }
}
