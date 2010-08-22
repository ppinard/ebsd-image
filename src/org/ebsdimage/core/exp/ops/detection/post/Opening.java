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
package org.ebsdimage.core.exp.ops.detection.post;

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.MathMorph;

/**
 * Operation to remove small false peaks in the peaks map.
 * 
 * @author Philippe T. Pinard
 */
public class Opening extends DetectionPostOps {

    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        BinMap destMap = srcMap.duplicate();

        MathMorph.opening(destMap, 2, 8);
        destMap.setProperties(srcMap);

        return destMap;
    }



    @Override
    public String toString() {
        return "Opening";
    }

}
