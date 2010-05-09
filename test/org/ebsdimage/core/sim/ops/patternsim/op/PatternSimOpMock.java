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
package org.ebsdimage.core.sim.ops.patternsim.op;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.PatternBandCenter;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.op.PatternSimOp;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.Reflectors;
import crystallography.core.XrayScatteringFactors;
import crystallography.core.crystals.Silicon;

public class PatternSimOpMock extends PatternSimOp {

    @Override
    public void simulate(Sim sim, Camera camera, Crystal crystal,
            Energy energy, Quaternion rotation) {
        Reflectors refls = new Reflectors(new Silicon(),
                new XrayScatteringFactors(), 1);
        pattern = new PatternBandCenter(2, 2, refls, 4, camera, energy,
                rotation);

    }
}
