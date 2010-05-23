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
package org.ebsdimage.core.exp.ops.indexing.results;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.crystals.Silicon;

public class IndexingResultsOpsMockTest {

    private IndexingResultsOps op;
    private Solution[] srcSlns;



    @Before
    public void setUp() throws Exception {
        op = new IndexingResultsOpsMock();

        Crystal phase = new Silicon();
        Quaternion rotation = Quaternion.IDENTITY;
        srcSlns =
                new Solution[] { new Solution(phase, rotation, 0.0),
                        new Solution(phase, rotation, 1.0 / 6.0),
                        new Solution(phase, rotation, 1.0 / 3.0) };
    }



    @Test
    public void testCalculate() {
        double expected = 0.5;
        OpResult result = op.calculate(null, srcSlns)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-6);
    }

}
