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
package org.ebsdimage.core.sim.ops.patternsim;

import org.ebsdimage.core.sim.Band;

import rmlimage.module.real.core.RealMap;
import crystallography.core.ScatteringFactorsEnum;

public class PatternSimOpMock extends PatternSimOp {

    public PatternSimOpMock() {
        super(1, 1, 1, ScatteringFactorsEnum.XRAY);
    }



    @Override
    protected RealMap createPatternMap() {
        return new RealMap(2, 2);
    }



    @Override
    protected void drawBand(RealMap canvas, Band band) {
    }
}
