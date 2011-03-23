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
package org.ebsdimage.gui.exp.ops.hough.results;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.hough.results.StandardDeviation;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>StandardDeviation</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class StandardDeviationCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Standard deviation of the pixels in the Hough map.";
    }



    @Override
    public ExpOperation getOperation() {
        return new StandardDeviation();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }



    @Override
    public String toString() {
        return "Standard Deviation";
    }

}
