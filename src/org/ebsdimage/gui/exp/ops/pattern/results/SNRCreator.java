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
package org.ebsdimage.gui.exp.ops.pattern.results;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.pattern.results.SNR;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>SNR</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class SNRCreator implements OperationCreator {

    @Override
    public ExpOperation getOperation() {
        return new SNR();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }



    @Override
    public String toString() {
        return "SNR";
    }



    @Override
    public String getDescription() {
        return "Calculates the signal to noise ratio of the pattern.";
    }

}
