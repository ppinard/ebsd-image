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
package org.ebsdimage.gui.exp.ops.detection.results;

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.detection.results.Count;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>Count</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class CountCreator implements OperationCreator {

    @Override
    public String getDescription() {
        return "Calculates the number of detected peaks";
    }



    @Override
    public ExpOperation getOperation() {
        return new Count();
    }



    @Override
    public String toString() {
        return "Count";
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }

}
