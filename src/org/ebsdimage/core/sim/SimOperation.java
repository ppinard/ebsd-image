/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
