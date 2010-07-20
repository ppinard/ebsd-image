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
package org.ebsdimage.gui.exp.ops.identification.results;

import org.ebsdimage.core.exp.ops.identification.results.PatternQuality;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.exp.ops.OperationCreator;

/**
 * GUI creator for the <code>PatternQuality</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PatternQualityCreator implements OperationCreator {

    @Override
    public Operation getOperation() {
        return new PatternQuality();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }



    @Override
    public String toString() {
        return "Pattern Quality";
    }



    @Override
    public String getDescription() {
        return "Oxford's INCA pattern quality index. It consists of the sum "
                + "of the three most intense peaks divided by 3 times the "
                + "standard deviation of the Hough map.";
    }

}
