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
import static ptpshared.utility.Arrays.reverse;

import java.util.Arrays;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.SolutionFitComparator;
import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpMetadata;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.ebsdimage.core.run.Operation;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;
import crystallography.core.crystals.ZirconiumAlpha;

public class BestSolutionTest {

    private BestSolution op;

    private Solution[] srcSlns;

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        op = new BestSolution();

        Crystal[] phases =
                new Crystal[] { new Silicon(), new IronBCC(),
                        new ZirconiumAlpha() };
        exp =
                new Exp(1, 1, new ExpMetadata(), phases,
                        new Operation[] { new PatternOpMock() },
                        new CurrentMapsFileSaver());

        srcSlns =
                new Solution[] {
                        new Solution(phases[0], new Quaternion(1, 2, 3, 4), 0.4),
                        new Solution(phases[0], new Quaternion(2, 3, 4, 5), 0.7),
                        new Solution(phases[1], new Quaternion(3, 4, 5, 6), 0.1) };
        Arrays.sort(srcSlns, new SolutionFitComparator());
        reverse(srcSlns);
    }



    @Test
    public void testCalculateExpSolutionArray() {
        OpResult[] results = op.calculate(exp, srcSlns);

        // Q0
        OpResult result = results[0];
        assertEquals(EbsdMMap.Q0, result.alias);
        assertEquals(2.0, result.value.doubleValue(), 1e-6);

        // Q1
        result = results[1];
        assertEquals(EbsdMMap.Q1, result.alias);
        assertEquals(3.0, result.value.doubleValue(), 1e-6);

        // Q2
        result = results[2];
        assertEquals(EbsdMMap.Q2, result.alias);
        assertEquals(4.0, result.value.doubleValue(), 1e-6);

        // Q3
        result = results[3];
        assertEquals(EbsdMMap.Q3, result.alias);
        assertEquals(5.0, result.value.doubleValue(), 1e-6);

        // Phase
        result = results[4];
        assertEquals(EbsdMMap.PHASES, result.alias);
        assertEquals(1, result.value.intValue());

        // Fit
        result = results[5];
        assertEquals("SolutionFit", result.alias);
        assertEquals(0.7, result.value.doubleValue(), 1e-6);
    }



    @Test
    public void testToString() {
        String expected = "Best Solution";
        assertEquals(expected, op.toString());
    }

}
