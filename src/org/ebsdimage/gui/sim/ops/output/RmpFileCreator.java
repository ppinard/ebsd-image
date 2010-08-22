package org.ebsdimage.gui.sim.ops.output;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.output.RmpFile;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * Creator for the operation <code>RmpFile</code>.
 * 
 * @author ppinard
 */
public class RmpFileCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Saves all simulated patterns as RMPs";
    }



    @Override
    public String toString() {
        return "RMP File";
    }



    @Override
    public Operation getOperation() {
        return new RmpFile();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
