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
package org.ebsdimage.core.exp.ops.detection.op;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpError;
import org.ebsdimage.core.exp.ExpListener;
import org.ebsdimage.core.exp.ExpOperation;

import rmlimage.core.BinMap;

/**
 * Superclass of operation to perform the peak detection.
 * 
 * @author Philippe T. Pinard
 */
public abstract class DetectionOp extends ExpOperation {

    /**
     * Detects peaks in the Hough map.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            Hough map
     * @return peaks map
     * @throws ExpError
     *             if an error occurs during the execution
     */
    public abstract BinMap detect(Exp exp, HoughMap srcMap) throws ExpError;



    @Override
    public final Object execute(Exp exp, Object... args) throws ExpError {
        return detect(exp, (HoughMap) args[0]);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object result) {
        listener.detectionOpPerformed(exp, this, (BinMap) result);
    }

}
