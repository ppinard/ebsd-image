package org.ebsdimage.gui.exp.ops.detection.results;

import org.ebsdimage.core.exp.ops.detection.results.Area;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>Area</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class AreaCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Calculates the area of detected peaks.";
    }



    @Override
    public Operation getOperation() {
        return new Area();
    }



    @Override
    public String toString() {
        return "Area";
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
