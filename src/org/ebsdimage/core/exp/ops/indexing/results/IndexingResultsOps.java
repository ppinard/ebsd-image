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
package org.ebsdimage.core.exp.ops.indexing.results;

import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.core.run.Operation;

/**
 * Superclass of operation to calculate result(s) from the indexing solutions.
 * 
 * @author Philippe T. Pinard
 */
public abstract class IndexingResultsOps extends Operation {

    /**
     * Calculates the result(s) from the indexing solutions.
     * 
     * @param exp
     *            experiment executing this method
     * @param solutions
     *            indexing solutions
     * @return result(s)
     */
    public abstract OpResult[] calculate(Exp exp, Solution[] solutions);
}
