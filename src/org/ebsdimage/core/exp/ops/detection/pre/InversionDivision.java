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
package org.ebsdimage.core.exp.ops.detection.pre;

import org.ebsdimage.core.Conversion;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.ByteMap;
import rmlimage.core.MapMath;

/**
 * Operation to perform an inversion followed by a division to increase the
 * contrast.
 * 
 * @author Philippe T. Pinard
 */
public class InversionDivision extends DetectionPreOps {

    @Override
    public String toString() {
        return "Inversion Division";
    }



    /**
     * Performs an inversion followed by a division. The output is a
     * <code>HoughMap</code>.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            input Hough map
     * @return output Hough map
     * 
     * @see MapMath#notOp(ByteMap)
     * @see MapMath#division(ByteMap, ByteMap, ByteMap)
     */
    @Override
    public HoughMap process(Exp exp, HoughMap srcMap) {
        // Inversion
        ByteMap srcMapInverted = srcMap.duplicate();
        MapMath.notOp(srcMapInverted);

        // Division
        ByteMap divisionMap = new ByteMap(srcMap.width, srcMap.height);
        MapMath.division(srcMap, srcMapInverted, 128.0, 0.0, divisionMap);

        // Put srcMap properties in dividedMap
        divisionMap.setProperties(srcMap);

        HoughMap destMap = Conversion.toHoughMap(divisionMap);

        return destMap;
    }

}
