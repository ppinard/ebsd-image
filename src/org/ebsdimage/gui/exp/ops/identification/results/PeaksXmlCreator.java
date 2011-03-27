package org.ebsdimage.gui.exp.ops.identification.results;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.identification.results.PeaksXml;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>HoughPeaks</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PeaksXmlCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Save the identified peaks in a XML file.";
    }



    @Override
    public ExpOperation getOperation() {
        return new PeaksXml();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }



    @Override
    public String toString() {
        return "XML";
    }

}
