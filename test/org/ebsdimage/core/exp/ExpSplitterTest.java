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
package org.ebsdimage.core.exp;

import java.util.ArrayList;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpSplitterTest {

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        ArrayList<ExpOperation> ops = ExpTester.createOperations();
        ops.add(new PatternOpMock(8));

        ExpMMap mmap = new ExpMMap(4, 2);
        exp = new Exp(mmap, ops.toArray(new ExpOperation[ops.size()]));

        assertEquals(8, exp.getPatternOp().size);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpSplitterException() {
        // Operations
        ArrayList<ExpOperation> ops = ExpTester.createOperations();

        // Experiment
        ExpMMap mmap = new ExpMMap(4, 2);
        Exp exp = new Exp(mmap, ops.toArray(new ExpOperation[ops.size()]));

        assertEquals(2, exp.getPatternOp().size);

        new ExpSplitter(exp, 3);
    }



    @Test
    public void testNext1() {
        ExpSplitter splitter = new ExpSplitter(exp, 1);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(1, exps.size());
        assertEquals(8, exps.get(0).getPatternOp().size);
    }



    @Test
    public void testNext2() {
        ExpSplitter splitter = new ExpSplitter(exp, 2);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(2, exps.size());
        assertEquals(4, exps.get(0).getPatternOp().size);
        assertEquals(4, exps.get(1).getPatternOp().size);
    }



    @Test
    public void testNext3() {
        ExpSplitter splitter = new ExpSplitter(exp, 3);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(3, exps.size());
        assertEquals(3, exps.get(0).getPatternOp().size);
        assertEquals(3, exps.get(1).getPatternOp().size);
        assertEquals(2, exps.get(2).getPatternOp().size);
    }



    @Test
    public void testNext4() {
        ExpSplitter splitter = new ExpSplitter(exp, 4);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(4, exps.size());
        assertEquals(2, exps.get(0).getPatternOp().size);
        assertEquals(2, exps.get(1).getPatternOp().size);
        assertEquals(2, exps.get(2).getPatternOp().size);
        assertEquals(2, exps.get(3).getPatternOp().size);
    }



    @Test
    public void testNext5() {
        ExpSplitter splitter = new ExpSplitter(exp, 5);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(4, exps.size());
        assertEquals(2, exps.get(0).getPatternOp().size);
        assertEquals(2, exps.get(1).getPatternOp().size);
        assertEquals(2, exps.get(2).getPatternOp().size);
        assertEquals(2, exps.get(3).getPatternOp().size);
    }



    @Test
    public void testNext6() {
        ExpSplitter splitter = new ExpSplitter(exp, 6);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(4, exps.size());
        assertEquals(2, exps.get(0).getPatternOp().size);
        assertEquals(2, exps.get(1).getPatternOp().size);
        assertEquals(2, exps.get(2).getPatternOp().size);
        assertEquals(2, exps.get(3).getPatternOp().size);
    }



    @Test
    public void testNext7() {
        ExpSplitter splitter = new ExpSplitter(exp, 7);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(4, exps.size());
        assertEquals(2, exps.get(0).getPatternOp().size);
        assertEquals(2, exps.get(1).getPatternOp().size);
        assertEquals(2, exps.get(2).getPatternOp().size);
        assertEquals(2, exps.get(3).getPatternOp().size);
    }



    @Test
    public void testNext8() {
        ExpSplitter splitter = new ExpSplitter(exp, 8);
        ArrayList<Exp> exps = new ArrayList<Exp>();

        while (splitter.hasNext())
            exps.add(splitter.next());

        assertEquals(8, exps.size());
        assertEquals(1, exps.get(0).getPatternOp().size);
        assertEquals(1, exps.get(1).getPatternOp().size);
        assertEquals(1, exps.get(2).getPatternOp().size);
        assertEquals(1, exps.get(3).getPatternOp().size);
        assertEquals(1, exps.get(4).getPatternOp().size);
        assertEquals(1, exps.get(5).getPatternOp().size);
        assertEquals(1, exps.get(6).getPatternOp().size);
        assertEquals(1, exps.get(7).getPatternOp().size);
    }

}
