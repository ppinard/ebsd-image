package org.ebsdimage.gui.sim.ops.output;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.output.ExpMMapSmpFile;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * Creator for the operation <code>ExpMMapSmpFile</code>.
 * 
 * @author ppinard
 */
public class ExpMMapSmpFileCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Saves all simulated patterns in a SMP file and the "
                + "parameters in a multimap.";
    }



    @Override
    public String toString() {
        return "ExpMMap and SMP File";
    }



    @Override
    public Operation getOperation() {
        return new ExpMMapSmpFile();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
