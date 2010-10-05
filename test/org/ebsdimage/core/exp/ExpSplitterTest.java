package org.ebsdimage.core.exp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.ebsdimage.core.run.Operation;
import org.junit.Before;
import org.junit.Test;

public class ExpSplitterTest {

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        ArrayList<Operation> ops = ExpTester.createOperations();
        ops.add(new PatternOpMock(8));

        exp =
                new Exp(4, 2, ExpTester.createMetadata(),
                        ExpTester.createPhases(),
                        ops.toArray(new Operation[ops.size()]),
                        new CurrentMapsFileSaver());
        assertEquals(8, exp.getPatternOp().size);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testExpSplitterException() {
        // Operations
        ArrayList<Operation> ops = ExpTester.createOperations();

        // Experiment
        Exp exp =
                new Exp(4, 2, ExpTester.createMetadata(),
                        ExpTester.createPhases(),
                        ops.toArray(new Operation[ops.size()]),
                        new CurrentMapsFileSaver());
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