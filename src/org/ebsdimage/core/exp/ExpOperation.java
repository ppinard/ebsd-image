/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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
package org.ebsdimage.core.exp;

import junittools.core.AlmostEquable;
import net.jcip.annotations.Immutable;

import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.run.Operation;
import org.simpleframework.xml.Root;

/**
 * Superclass for all operations.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
@Root
public abstract class ExpOperation implements AlmostEquable, Operation {

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
    public boolean equals(Object obj, Object precision) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        return true;
    }



    /**
     * Executes the operation with the specified arguments.
     * 
     * @param exp
     *            experiment executing this method
     * @param args
     *            arguments
     * @return return of the operation
     */
    public abstract Object execute(Exp exp, Object... args);



    /**
     * Fires the appropriate method from the listener.
     * 
     * @param listener
     *            listener
     * @param exp
     *            experiment executing this method
     * @param result
     *            result from this operation
     */
    public abstract void fireExecuted(ExpListener listener, Exp exp,
            Object result);



    /**
     * Returns the error codes associated with the operation. By default, no
     * error codes are defined.
     * 
     * @return error codes
     */
    public ErrorCode[] getErrorCodes() {
        return new ErrorCode[0];
    }



    /**
     * Returns the name of this operation which corresponds to the simple name
     * of the class.
     * 
     * @return name
     */
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
     * Initializes the operation before running an experiment.
     * 
     * @param exp
     *            experiment executing this method
     */
    public void setUp(Exp exp) {
    }



    /**
     * Flushes the operation after running an experiment.
     * 
     * @param exp
     *            experiment executing this method
     */
    public void tearDown(Exp exp) {

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
