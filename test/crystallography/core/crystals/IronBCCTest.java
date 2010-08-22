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
package crystallography.core.crystals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import crystallography.core.Crystal;

public class IronBCCTest {

    @Test
    public void testIronBCC() {
        Crystal crystal = new IronBCC();

        assertEquals(2.87, crystal.unitCell.a, 1e-7);
        assertEquals(26, crystal.atoms.get(0).atomicNumber);
        assertEquals("Iron BCC", crystal.name);
    }

}
