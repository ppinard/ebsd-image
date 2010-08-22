package org.ebsdimage.gui.exp.ops.detection.results;

import org.ebsdimage.core.exp.ops.detection.results.Count;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>Count</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class CountCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Calculates the number of detected peaks";
    }



    @Override
    public Operation getOperation() {
        return new Count();
    }



    @Override
    public String toString() {
        return "Count";
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
