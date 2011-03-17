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

import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.exp.ops.identification.results.ImageQuality;
import org.ebsdimage.gui.run.ops.OperationCreator;

/**
 * GUI creator for the <code>ImageQuality</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class ImageQualityCreator implements OperationCreator {

    @Override
    public ExpOperation getOperation() {
        return new ImageQuality();
    }



    @Override
    public int show() {
        return OperationCreator.OK;
    }



    @Override
    public String toString() {
        return "Image Quality";
    }



    @Override
    public String getDescription() {
        return "Quality index on the identified Hough peaks. "
                + "The image quality corresponds to the average intensity of the "
                + "identified Hough peaks.";
    }
}
