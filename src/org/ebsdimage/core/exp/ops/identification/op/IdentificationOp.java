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
package org.ebsdimage.core.exp.ops.identification.op;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpListener;
import org.ebsdimage.core.exp.ExpOperation;

import rmlimage.core.BinMap;

/**
 * Superclass of operation to perform the peak identification from a peaks map.
 * 
 * @author Philippe T. Pinard
 */
public abstract class IdentificationOp extends ExpOperation {

    @Override
    public final Object execute(Exp exp, Object... args) {
        return identify(exp, (BinMap) args[0], (HoughMap) args[1]);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object result) {
        listener.identificationOpPerformed(exp, this, (HoughPeak[]) result);
    }



    /**
     * Identifies the Hough peaks from the peaks and Hough map.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaksMap
     *            peaks map
     * @param houghMap
     *            Hough map
     * @return Hough peaks
     */
    public abstract HoughPeak[] identify(Exp exp, BinMap peaksMap,
            HoughMap houghMap);
}
