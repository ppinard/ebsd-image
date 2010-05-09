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
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Pattern;
import org.ebsdimage.core.sim.ops.patternsim.op.PatternSimOp;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.crystals.Silicon;

public class PatternSimOpMockTest {

    private PatternSimOp op;
    private Camera camera;
    private Crystal crystal;
    private Energy energy;
    private Quaternion rotation;



    @Before
    public void setUp() throws Exception {
        op = new PatternSimOpMock();
        camera = new Camera(0, 0, 0.5);
        crystal = new Silicon();
        energy = new Energy(20e3);
        rotation = Quaternion.IDENTITY;
    }



    @Test
    public void testGetPattern() {
        op.simulate(null, camera, crystal, energy, rotation);

        Pattern pattern = op.getPattern();
        assertEquals(2, pattern.width);
        assertEquals(2, pattern.height);
        assertEquals(4, pattern.numberReflectors);
        assertEquals(20e3, pattern.energy.value, 1e-6);
        assertTrue(pattern.camera.equals(new Camera(0.0, 0.0, 0.5), 1e-6));
        assertTrue(pattern.rotation.equals(Quaternion.IDENTITY, 1e-6));
        assertEquals(4, pattern.getBands().length);
    }

}
