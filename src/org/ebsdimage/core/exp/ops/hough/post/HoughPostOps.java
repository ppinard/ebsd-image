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
package org.ebsdimage.core.exp.ops.hough.post;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpError;
import org.ebsdimage.core.exp.ExpListener;
import org.ebsdimage.core.exp.ExpOperation;

/**
 * Superclass of operation to process the Hough map after the Hough operation.
 * 
 * @author Philippe T. Pinard
 */
public abstract class HoughPostOps extends ExpOperation {

    @Override
    public final Object execute(Exp exp, Object... args) throws ExpError {
        return process(exp, (HoughMap) args[0]);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object result) {
        listener.houghPostPerformed(exp, this, (HoughMap) result);
    }



    /**
     * Returns a processed Hough map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input Hough map
     * @return output Hough map
     * @throws ExpError
     *             if an error occurs during the execution
     */
    public abstract HoughMap process(Exp exp, HoughMap srcMap) throws ExpError;
}
