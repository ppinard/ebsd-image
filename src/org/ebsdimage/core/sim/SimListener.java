package org.ebsdimage.core.sim;

import org.ebsdimage.core.sim.ops.output.OutputOps;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;

/**
 * Listener on the simulation after each operation is performed.
 * 
 * @author Philippe T. Pinard
 */
public interface SimListener {

    /**
     * Action fired after an output operation is performed.
     * 
     * @param sim
     *            simulation calling this method
     * @param op
     *            operation performed
     */
    public void outputOpPerformed(Sim sim, OutputOps op);



    /**
     * Action fired after the pattern simulation operation is performed.
     * 
     * @param sim
     *            simulation calling this method
     * @param op
     *            operation performed
     */
    public void patternSimOpPerformed(Sim sim, PatternSimOp op);
}
