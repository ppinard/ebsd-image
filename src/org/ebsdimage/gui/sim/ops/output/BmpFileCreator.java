package org.ebsdimage.gui.sim.ops.output;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.output.BmpFile;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * Creator for the operation <code>BmpFile</code>.
 * 
 * @author ppinard
 */
public class BmpFileCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Saves all simulated patterns as BMPs";
    }



    @Override
    public String toString() {
        return "Bmp File";
    }



    @Override
    public Operation getOperation() {
        return new BmpFile();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
