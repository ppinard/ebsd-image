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
package org.ebsdimage.core.exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ebsdimage.core.exp.ops.pattern.op.PatternFileLoader;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.ebsdimage.core.run.Operation;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class ExpUtilTest {

    @Before
    public void setUp() throws Exception {
    }



    @Test
    public void testCreateExps() {
        // TODO: Test create Exps in ExpUtil
        assertTrue(true);
    }



    @Test
    public void testCreatePatternOpsFromSmp() throws IOException {
        File smpFile = FileUtil.getFile("org/ebsdimage/testdata/Project19.smp");

        PatternSmpLoader[] ops = ExpUtil.createPatternOpsFromSmp(smpFile);

        assertEquals(4, ops.length);
        for (int i = 0; i < ops.length; i++) {
            assertEquals(i, ops[i].index);
            assertEquals(smpFile.getName(), ops[i].filename);
            assertEquals(smpFile.getParent(), ops[i].filedir);
        }
    }



    @Test
    public void testCreatePatternOpsFromDir() {
        File dir = FileUtil.getFile("org/ebsdimage/testdata/Project19/");

        PatternFileLoader[] ops = ExpUtil.createPatternOpsFromDir(dir);

        assertEquals(4, ops.length);
    }



    @Test
    public void testSplitExp() {
        // Operations
        ArrayList<Operation> ops = ExpTester.createOperations();
        ops.add(new PatternOpMock(2));
        ops.add(new PatternOpMock(3));
        ops.add(new PatternOpMock(4));
        ops.add(new PatternOpMock(5));
        ops.add(new PatternOpMock(6));
        ops.add(new PatternOpMock(7));

        // Experiment
        Exp exp =
                new Exp(4, 2, ExpTester.createMetadata(), ExpTester
                        .createPhases(),
                        ops.toArray(new Operation[ops.size()]),
                        new CurrentMapsFileSaver());
        assertEquals(8, exp.getPatternOps().length);

        // Split = 1
        Exp[] exps = ExpUtil.splitExp(exp, 1);
        assertEquals(1, exps.length);
        assertEquals(8, exps[0].getPatternOps().length);

        // Split = 2
        exps = ExpUtil.splitExp(exp, 2);
        assertEquals(2, exps.length);
        assertEquals(4, exps[0].getPatternOps().length);
        assertEquals(4, exps[1].getPatternOps().length);

        // Split = 3
        exps = ExpUtil.splitExp(exp, 3);
        assertEquals(3, exps.length);
        assertEquals(3, exps[0].getPatternOps().length);
        assertEquals(3, exps[1].getPatternOps().length);
        assertEquals(2, exps[2].getPatternOps().length);

        // Split = 4
        exps = ExpUtil.splitExp(exp, 4);
        assertEquals(4, exps.length);
        assertEquals(2, exps[0].getPatternOps().length);
        assertEquals(2, exps[1].getPatternOps().length);
        assertEquals(2, exps[2].getPatternOps().length);
        assertEquals(2, exps[3].getPatternOps().length);

        // Split = 5
        exps = ExpUtil.splitExp(exp, 5);
        assertEquals(5, exps.length);
        assertEquals(2, exps[0].getPatternOps().length);
        assertEquals(2, exps[1].getPatternOps().length);
        assertEquals(2, exps[2].getPatternOps().length);
        assertEquals(2, exps[3].getPatternOps().length);
        assertEquals(0, exps[4].getPatternOps().length);

        // Split = 6
        exps = ExpUtil.splitExp(exp, 6);
        assertEquals(6, exps.length);
        assertEquals(2, exps[0].getPatternOps().length);
        assertEquals(2, exps[1].getPatternOps().length);
        assertEquals(2, exps[2].getPatternOps().length);
        assertEquals(2, exps[3].getPatternOps().length);
        assertEquals(0, exps[4].getPatternOps().length);
        assertEquals(0, exps[5].getPatternOps().length);

        // Split = 7
        exps = ExpUtil.splitExp(exp, 7);
        assertEquals(7, exps.length);
        assertEquals(2, exps[0].getPatternOps().length);
        assertEquals(2, exps[1].getPatternOps().length);
        assertEquals(2, exps[2].getPatternOps().length);
        assertEquals(2, exps[3].getPatternOps().length);
        assertEquals(0, exps[4].getPatternOps().length);
        assertEquals(0, exps[5].getPatternOps().length);
        assertEquals(0, exps[6].getPatternOps().length);

        // Split = 8
        exps = ExpUtil.splitExp(exp, 8);
        assertEquals(8, exps.length);
        assertEquals(1, exps[0].getPatternOps().length);
        assertEquals(1, exps[1].getPatternOps().length);
        assertEquals(1, exps[2].getPatternOps().length);
        assertEquals(1, exps[3].getPatternOps().length);
        assertEquals(1, exps[4].getPatternOps().length);
        assertEquals(1, exps[5].getPatternOps().length);
        assertEquals(1, exps[6].getPatternOps().length);
        assertEquals(1, exps[7].getPatternOps().length);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSplitExpException() {
        // Operations
        ArrayList<Operation> ops = ExpTester.createOperations();

        // Experiment
        Exp exp =
                new Exp(4, 2, ExpTester.createMetadata(), ExpTester
                        .createPhases(),
                        ops.toArray(new Operation[ops.size()]),
                        new CurrentMapsFileSaver());
        assertEquals(2, exp.getPatternOps().length);

        ExpUtil.splitExp(exp, 3);
    }
}
