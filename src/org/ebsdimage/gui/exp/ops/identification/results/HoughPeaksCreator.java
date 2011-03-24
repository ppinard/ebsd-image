package org.ebsdimage.gui.exp.ops.identification.results;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.identification.results.HoughPeaks;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>HoughPeaks</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeaksCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Save the identified peaks in a XML file.";
    }



    @Override
    public ExpOperation getOperation() {
        return new HoughPeaks();
    }



    @Override
    public String toString() {
        return "Hough Peaks";
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
