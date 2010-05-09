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
package org.ebsdimage.core.sim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.sim.Energy;
import org.junit.Before;
import org.junit.Test;


public class EnergyTest {

    private Energy energy;



    @Before
    public void setUp() throws Exception {
        energy = new Energy(25e3);
    }



    @Test
    public void testEnergyDouble() {
        assertEquals(25e3, energy.value, 1e-6);
    }



    @Test
    public void testEquals() {
        Energy other = new Energy(25e3);

        assertFalse(energy == other);
        assertTrue(energy.equals(other, 1e-6));
    }



    @Test
    public void testGetValue() {
        assertEquals(25e3, energy.getValue(), 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals(energy.toString(), "Energy [25000.0 eV]");
    }

}
