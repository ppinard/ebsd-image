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
package org.ebsdimage.core.exp.ops.indexing.op;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpError;
import org.ebsdimage.core.exp.ExpListener;
import org.ebsdimage.core.exp.ExpOperation;

/**
 * Superclass of operation to perform the indexing of the Hough peaks.
 * 
 * @author Philippe T. Pinard
 */
public abstract class IndexingOp extends ExpOperation {

    @Override
    public final Object execute(Exp exp, Object... args) throws ExpError {
        return index(exp, (HoughPeak[]) args);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object result) {
        listener.indexingOpPerformed(exp, this, (Solution[]) result);
    }



    /**
     * Find solution(s) for the input Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcPeaks
     *            Hough peaks
     * @return solutions
     * @throws ExpError
     *             if an error occurs during the execution
     */
    public abstract Solution[] index(Exp exp, HoughPeak[] srcPeaks)
            throws ExpError;

}
