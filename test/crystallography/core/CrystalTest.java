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
package crystallography.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CrystalTest {

    private String name;

    private Crystal crystal;

    private UnitCell unitCell;

    private AtomSites atomSites;

    private LaueGroup pointGroup;



    @Before
    public void setUp() throws Exception {
        name = "silicon";
        unitCell = UnitCellFactory.cubic(2.0);
        atomSites = AtomSitesFactory.atomSitesFCC(14);
        pointGroup = LaueGroup.LGm3m;

        crystal = new Crystal(name, unitCell, atomSites, pointGroup);
    }



    @Test
    public void testCrystal() {
        assertEquals(2.0, crystal.unitCell.a, 1e-7);
        assertEquals(14, crystal.atoms.get(0).atomicNumber);
        assertEquals(name, crystal.name);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException1() {
        new Crystal(null, unitCell, atomSites, pointGroup);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCrystalException2() {
        new Crystal("", unitCell, atomSites, pointGroup);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException3() {
        new Crystal(name, null, atomSites, pointGroup);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException4() {
        new Crystal(name, unitCell, null, pointGroup);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException5() {
        new Crystal(name, unitCell, atomSites, null);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(crystal.equals(crystal));

        assertFalse(crystal.equals(null));

        assertFalse(crystal.equals(new Object()));

        assertFalse(crystal.equals(new Crystal("other", unitCell, atomSites,
                pointGroup)));

        UnitCell otherUnitCell = UnitCellFactory.cubic(3.0);
        assertFalse(crystal.equals(new Crystal(name, otherUnitCell, atomSites,
                pointGroup)));

        AtomSites otherAtomSites = AtomSitesFactory.atomSitesFCC(15);
        assertFalse(crystal.equals(new Crystal(name, unitCell, otherAtomSites,
                pointGroup)));

        assertFalse(crystal.equals(new Crystal(name, unitCell, atomSites,
                LaueGroup.LGm3)));

        assertTrue(crystal.equals(new Crystal(name, unitCell, atomSites,
                pointGroup)));
    }



    @Test
    public void testEqualsCrystalDouble() {
        assertTrue(crystal.equals(crystal, 1e-3));

        assertFalse(crystal.equals(null, 1e-3));

        assertFalse(crystal.equals(new Crystal("other", unitCell, atomSites,
                pointGroup), 1e-3));

        UnitCell otherUnitCell = UnitCellFactory.cubic(2.002);
        assertFalse(crystal.equals(new Crystal(name, otherUnitCell, atomSites,
                pointGroup), 1e-3));

        AtomSites otherAtomSites = AtomSitesFactory.atomSitesFCC(15);
        assertFalse(crystal.equals(new Crystal(name, unitCell, otherAtomSites,
                pointGroup), 1e-3));

        assertFalse(crystal.equals(new Crystal(name, unitCell, atomSites,
                LaueGroup.LGm3), 1e-3));

        Crystal other =
                new Crystal(name, UnitCellFactory.cubic(2.002), atomSites,
                        pointGroup);
        assertTrue(crystal.equals(other, 1e-2));
    }



    @Test
    public void testToString() {
        assertEquals("silicon", crystal.toString());
    }



    @Test
    public void testHashCode() {
        Crystal other = new Crystal(name, unitCell, atomSites, pointGroup);
        assertEquals(crystal.hashCode(), other.hashCode());
    }
}
