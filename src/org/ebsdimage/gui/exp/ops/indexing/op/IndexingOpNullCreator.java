package org.ebsdimage.gui.exp.ops.indexing.op;

import org.ebsdimage.core.exp.ops.indexing.op.IndexingOpNull;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * Temporary null indexing operation.
 * 
 * @author ppinard
 */
public class IndexingOpNullCreator implements OperationCreator {

    @Override
    public String toString() {
        return "Null Indexing";
    }



    @Override
    public String getDescription() {
        return "Temporary indexing operation";
    }



    @Override
    public Operation getOperation() {
        return new IndexingOpNull();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
