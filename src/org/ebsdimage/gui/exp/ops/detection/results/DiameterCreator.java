package org.ebsdimage.gui.exp.ops.detection.results;

import org.ebsdimage.core.exp.ops.detection.results.Diameter;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>Diameter</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class DiameterCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Calculates the the diameter of the greatest inscribed circle "
                + "inside the detected peaks.";
    }



    @Override
    public Operation getOperation() {
        return new Diameter();
    }



    @Override
    public String toString() {
        return "Diameter";
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
