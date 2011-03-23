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
package crystallography.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.AtomSite;
import crystallography.core.Crystal;
import crystallography.core.SpaceGroups1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CifLoaderTest extends TestCase {

    private File file;



    @Before
    public void setUp() throws Exception {
        file = getFile("crystallography/testdata/forsterite.cif");

    }



    @Test
    public void testLoad() throws IOException {
        Crystal crystal = new CifLoader().load(file);

        assertEquals("Forsterite", crystal.name);

        assertEquals(4.756, crystal.unitCell.a, 1e-3);
        assertEquals(10.207, crystal.unitCell.b, 1e-3);
        assertEquals(5.980, crystal.unitCell.c, 1e-3);
        assertEquals(90, Math.toDegrees(crystal.unitCell.alpha), 1e-3);
        assertEquals(91, Math.toDegrees(crystal.unitCell.beta), 1e-3);
        assertEquals(92, Math.toDegrees(crystal.unitCell.gamma), 1e-3);

        assertEquals(164, crystal.atoms.size());

        HashMap<Integer, Integer> atoms = new HashMap<Integer, Integer>();
        for (AtomSite atom : crystal.atoms)
            if (atoms.containsKey(atom.atomicNumber))
                atoms.put(atom.atomicNumber, atoms.get(atom.atomicNumber) + 1);
            else
                atoms.put(atom.atomicNumber, 1);

        assertTrue(atoms.get(12).equals(36)); // Mg
        assertTrue(atoms.get(14).equals(32)); // Si
        assertTrue(atoms.get(8).equals(96)); // O

        assertEquals(SpaceGroups1.SG62, crystal.spaceGroup);
    }

}
