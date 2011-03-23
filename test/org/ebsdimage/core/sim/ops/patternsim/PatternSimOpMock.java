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
import org.ebsdimage.core.sim.BandsCalculator;
import org.ebsdimage.core.sim.LinearBandsCalculator;
import org.simpleframework.xml.Attribute;

import rmlimage.module.real.core.RealMap;

public class PatternSimOpMock extends PatternSimOp {

    public static final PatternSimOpMock DEFAULT = new PatternSimOpMock(2, 2);



    public PatternSimOpMock(@Attribute(name = "width") int width,
            @Attribute(name = "height") int height) {
        super(width, height);
    }



    @Override
    protected RealMap createPatternMap() {
        return new RealMap(width, height);
    }



    @Override
    protected void drawBand(RealMap canvas, Band band) {
    }



    @Override
    protected BandsCalculator getBandsCalculator() {
        return new LinearBandsCalculator();
    }
}
