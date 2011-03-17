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
package org.ebsdimage.core.exp.ops.identification.post;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpListener;
import org.ebsdimage.core.exp.ExpOperation;

/**
 * Superclass of operation to process the Hough peaks after the peak
 * identification.
 * 
 * @author Philippe T. Pinard
 */
public abstract class IdentificationPostOps extends ExpOperation {

    /**
     * Returns processed Hough peaks.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcPeaks
     *            input Hough peaks
     * @return output Hough peaks
     */
    public abstract HoughPeak[] process(Exp exp, HoughPeak[] srcPeaks);



    @Override
    public final Object execute(Exp exp, Object... args) {
        return process(exp, (HoughPeak[]) args);
    }



    @Override
    public final void fireExecuted(ExpListener listener, Exp exp, Object result) {
        listener.identificationPostPerformed(exp, this, (HoughPeak[]) result);
    }
}
