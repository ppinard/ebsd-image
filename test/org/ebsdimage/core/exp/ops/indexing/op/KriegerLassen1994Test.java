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

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.ScatteringFactorsEnum;

public class KriegerLassen1994Test {

    private KriegerLassen1994 op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new KriegerLassen1994(1, ScatteringFactorsEnum.XRAY);
        srcPeaks = new HoughPeak[] { new HoughPeak(7, 0.1, 1) };
    }



    @Test
    public void testToString() {
        String expected =
                "Krieger Lassen (1994) [maxIndex=1, scatterType=XRAY]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testIndex() {
        // For further testing see the org.ebsdimage.core.Indexing class

        Solution[] slns = op.index(null, srcPeaks);
        assertEquals(0, slns.length);
    }



    @Test
    public void testKriegerLassen1994() {
        KriegerLassen1994 other = new KriegerLassen1994();

        assertEquals(KriegerLassen1994.DEFAULT_MAX_INDEX, other.maxIndex);
        assertEquals(KriegerLassen1994.DEFAULT_SCATTER_TYPE, other.scatterType);
    }



    @Test
    public void testKriegerLassen1994IntScatteringFactorsEnum() {
        assertEquals(1, op.maxIndex);
        assertEquals(ScatteringFactorsEnum.XRAY, op.scatterType);
    }

}
