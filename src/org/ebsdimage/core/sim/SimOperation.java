package org.ebsdimage.core.sim;

import net.jcip.annotations.Immutable;

import org.ebsdimage.core.run.Operation;
import org.simpleframework.xml.Root;

/**
 * Superclass for all operations of a simulation.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
@Root
public class SimOperation implements Operation {

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        return true;
    }



    @Override
    public String getName() {
        return getClass().getSimpleName();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getName().hashCode();
        return result;
    }



    /**
     * Initializes the operation before running an simulation.
     * 
     * @param sim
     *            simulation executing this method
     */
    public void setUp(Sim sim) {
    }



    /**
     * Flushes the operation after running an simulation.
     * 
     * @param sim
     *            simulation executing this method
     */
    public void tearDown(Sim sim) {
    }



    /**
     * Returns a representation of this <code>Operation</code>, suitable for
     * debugging.
     * 
     * @return information about this <code>Operation</code>
     */
    @Override
    public String toString() {
        return getName() + " []";
    }

}
