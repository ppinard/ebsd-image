package org.ebsdimage.gui.exp.ops.detection.results;

import org.ebsdimage.core.exp.ops.detection.results.Difference;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationCreator;

/**
 * GUI creator for the <code>MinMax</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class DifferenceCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Calculates the difference between the minimum and maximum "
                + "values inside each peak.";
    }



    @Override
    public Operation getOperation() {
        return new Difference();
    }



    @Override
    public String toString() {
        return "Difference";
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
