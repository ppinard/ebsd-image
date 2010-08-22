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

import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Energy;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import rmlimage.core.ByteMap;
import crystallography.core.Reflectors;
import crystallography.core.crystals.Silicon;

public class PatternSimOpMockTest {

    private PatternSimOp op;

    private Camera camera;

    private Reflectors reflectors;

    private Energy energy;

    private Quaternion rotation;



    @Before
    public void setUp() throws Exception {
        op = new PatternSimOpMock();
        camera = new Camera(0, 0, 0.5);
        reflectors = op.calculateReflectors(new Silicon());
        energy = new Energy(20e3);
        rotation = Quaternion.IDENTITY;
    }



    @Test
    public void testGetPattern() {
        op.simulate(null, camera, reflectors, energy, rotation);

        ByteMap pattern = op.getPatternMap();
        assertEquals(2, pattern.width);
        assertEquals(2, pattern.height);
        assertEquals(276, op.getBands().length);
    }

}
