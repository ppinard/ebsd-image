/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import java.io.File;
import java.util.Arrays;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.SolutionFitComparator;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;

import static ptpshared.math.Constants.S2_2;
import static ptpshared.util.Arrays.reverse;

import static junittools.test.Assert.assertEquals;

public class BestSolutionTest extends TestCase {

    private BestSolution op;

    private Solution[] srcSlns;

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        op = new BestSolution();

        Crystal phase1 = CrystalFactory.silicon();
        Crystal phase2 = CrystalFactory.ferrite();
        Crystal phase3 = CrystalFactory.zirconium();

        ExpMMap mmap = new ExpMMap(1, 1);
        mmap.getPhaseMap().register(1, phase1);
        mmap.getPhaseMap().register(2, phase2);
        mmap.getPhaseMap().register(3, phase3);

        exp = new Exp(mmap, new ExpOperation[] { new PatternOpMock(1) });

        Rotation rotation1 = new Rotation(S2_2, 0, 0, S2_2, false);
        Rotation rotation2 = new Rotation(0, S2_2, S2_2, 0, false);
        Rotation rotation3 = new Rotation(S2_2, S2_2, 0, 0, false);

        srcSlns =
                new Solution[] { new Solution(phase1, rotation1, 0.4),
                        new Solution(phase1, rotation2, 0.7),
                        new Solution(phase2, rotation3, 0.1) };
        Arrays.sort(srcSlns, new SolutionFitComparator());
        reverse(srcSlns);
    }



    @Test
    public void testCalculateExpSolutionArray() {
        OpResult[] results = op.calculate(exp, srcSlns);

        // Q0
        OpResult result = results[0];
        assertEquals(EbsdMMap.Q0, result.alias);
        assertEquals(0.0, result.value.doubleValue(), 1e-6);

        // Q1
        result = results[1];
        assertEquals(EbsdMMap.Q1, result.alias);
        assertEquals(S2_2, result.value.doubleValue(), 1e-6);

        // Q2
        result = results[2];
        assertEquals(EbsdMMap.Q2, result.alias);
        assertEquals(S2_2, result.value.doubleValue(), 1e-6);

        // Q3
        result = results[3];
        assertEquals(EbsdMMap.Q3, result.alias);
        assertEquals(0.0, result.value.doubleValue(), 1e-6);

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



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        BestSolution other = new XmlLoader().load(BestSolution.class, file);
        assertEquals(op, other, 1e-6);
    }

}
