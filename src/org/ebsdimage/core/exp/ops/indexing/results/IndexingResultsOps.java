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
package org.ebsdimage.core.exp.ops.indexing.results;

import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.*;

/**
 * Superclass of operation to calculate result(s) from the indexing solutions.
 * 
 * @author Philippe T. Pinard
 */
public abstract class IndexingResultsOps extends ExpOperation {

    /**
     * Calculates the result(s) from the indexing solutions.
     * 
     * @param exp
     *            experiment executing this method
     * @param solutions
     *            indexing solutions
     * @return result(s)
     * @throws ExpError
     *             if an error occurs during the execution
     */
    public abstract OpResult[] calculate(Exp exp, Solution[] solutions)
            throws ExpError;



    @Override
    public final Object execute(Exp exp, Object... args) throws ExpError {
        return calculate(exp, (Solution[]) args);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object results) {
        for (OpResult result : (OpResult[]) results)
            listener.indexingResultsPerformed(exp, this, result);
    }
}
