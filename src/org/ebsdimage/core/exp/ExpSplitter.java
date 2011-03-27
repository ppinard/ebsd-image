package org.ebsdimage.core.exp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;

/**
 * Splits a specified experiment into smaller experiments.
 * 
 * @author Philippe T. Pinard
 */
public class ExpSplitter implements Iterator<Exp> {

    /** Number of smaller experiments. */
    private final int count;

    /** Index of the current split. */
    private int currentIndex;

    /** Experiment to split. */
    private final Exp exp;

    /** Number of patterns in original experiment. */
    private final int expSize;

    /** All the operations except PatternOp. */
    private final ArrayList<ExpOperation> ops;

    /** Number of patterns in each split. */
    private final int size;



    /**
     * Creates a new <code>ExpSplitter</code>.
     * 
     * @param exp
     *            experiment to be split
     * @param count
     *            number of smaller experiments wanted
     * @throws NullPointerException
     *             if the experiment is null
     * @throws IllegalArgumentException
     *             if the split count is less or equal to zero
     * @throws IllegalArgumentException
     *             if the split count exceeds the number of pattern operations
     *             in the experiment
     */
    public ExpSplitter(Exp exp, int count) {
        if (exp == null)
            throw new NullPointerException("Experiment cannot be null.");
        if (count < 1)
            throw new IllegalArgumentException("The split count (" + count
                    + ") must be greater than 0.");
        if (count > exp.getPatternOp().size)
            throw new IllegalArgumentException("The split count (" + count
                    + ") cannot be greater than the "
                    + "number of pattern operations in the experiment ("
                    + exp.getPatternOp().size + ").");

        this.exp = exp;
        this.count = count;

        // List all the operations except PatternOp
        ops = new ArrayList<ExpOperation>();
        for (ExpOperation op : exp.getAllOperations())
            if (!(op instanceof PatternOp))
                ops.add(op);

        // Calculate the number of pattern operation in each split
        expSize = exp.getPatternOp().size;
        size = (int) Math.ceil((double) expSize / count);

        // Reset current index
        currentIndex = 0;
    }



    @Override
    public boolean hasNext() {
        // Check current index with total number of split
        if (currentIndex >= count)
            return false;

        // Check start and end index of the patterns
        int startIndex = Math.min(currentIndex * size, expSize);
        int endIndex = Math.min(currentIndex * size + size, expSize) - 1;

        if (startIndex > endIndex)
            return false;

        return true;
    }



    @Override
    public Exp next() {
        if (!hasNext())
            throw new NoSuchElementException("Iterator has no more elements");

        // Lower and upper bound of the copy range
        int startIndex = Math.min(currentIndex * size, expSize);
        int endIndex = Math.min(currentIndex * size + size, expSize) - 1;
        assert startIndex <= endIndex;

        // Pattern operation for this split
        PatternOp patternOp = exp.getPatternOp().extract(startIndex, endIndex);

        // Combine pattern operations and other operations
        ArrayList<ExpOperation> tmpOps = new ArrayList<ExpOperation>();
        tmpOps.addAll(ops);
        tmpOps.add(patternOp);

        // Increment index
        currentIndex++;

        // Create and add new experiment to the array
        Exp splitExp =
                new Exp(exp.mmap.duplicate(),
                        tmpOps.toArray(new ExpOperation[0]));

        splitExp.addExpListeners(exp.getExpListeners());

        splitExp.setName(exp.getName() + "_" + currentIndex);

        return splitExp;
    }



    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
