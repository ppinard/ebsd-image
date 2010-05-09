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
package org.ebsdimage.core.exp.ops.indexing.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOps;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.crystals.Silicon;

public class IndexingPostOpsMockTest {

    private IndexingPostOps op;
    private Solution[] srcSlns;



    @Before
    public void setUp() throws Exception {
        op = new IndexingPostOpsMock();

        Crystal phase = new Silicon();
        Quaternion rotation = Quaternion.IDENTITY;
        srcSlns = new Solution[] { new Solution(phase, rotation, 15.0),
                new Solution(phase, rotation, 23.0),
                new Solution(phase, rotation, 31.0) };
    }



    @Test
    public void testProcess() {
        Solution[] destSlns = op.process(null, srcSlns);

        assertEquals(3, destSlns.length);

        Crystal expectedPhase = new Silicon();
        Quaternion expectedRotation = Quaternion.IDENTITY;
        for (Solution sln : destSlns) {
            assertTrue(expectedPhase.equals(sln.phase, 1e-6));
            assertTrue(expectedRotation.equals(sln.rotation, 1e-6));
        }

        assertEquals(30.0, destSlns[0].fit, 1e-6);
        assertEquals(46.0, destSlns[1].fit, 1e-6);
        assertEquals(62.0, destSlns[2].fit, 1e-6);
    }

}
