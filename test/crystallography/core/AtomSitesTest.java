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

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class AtomSitesTest {

    private AtomSites atoms;

    private AtomSite atom1;

    private AtomSite atom2;

    private AtomSite atom3;

    private AtomSite atom4;



    @Before
    public void setUp() throws Exception {
        atoms = new AtomSites();
        atom1 = new AtomSite(13, 0.0, 0.0, 0.0);
        atom2 = new AtomSite(13, 0.0, 0.5, 0.5);
        atom3 = new AtomSite(13, 0.5, 0.0, 0.5);
        atom4 = new AtomSite(13, 0.5, 0.5, 0.0);
    }



    @Test
    public void testAddAllCollection() {
        ArrayList<AtomSite> atomlist = new ArrayList<AtomSite>();
        atomlist.add(atom1);
        atomlist.add(atom2);
        atoms.addAll(atomlist);

        assertEquals(atoms.get(0), atom1);
        assertEquals(atoms.get(1), atom2);
        assertEquals(atoms.size(), 2);
    }



    @Test(expected = AtomSitePositionException.class)
    public void testAddAllCollectionException() {
        ArrayList<AtomSite> atomlist = new ArrayList<AtomSite>();
        atomlist.add(atom1);
        atomlist.add(atom2);
        atoms.addAll(atomlist);
        atoms.addAll(atomlist); // Throw exception
    }



    @Test
    public void testAddAllIntCollection() {
        atoms.add(atom1);
        atoms.add(atom2);

        ArrayList<AtomSite> atomlist = new ArrayList<AtomSite>();
        atomlist.add(atom3);
        atomlist.add(atom4);
        atoms.addAll(1, atomlist);

        assertEquals(atoms.get(0), atom1);
        assertEquals(atoms.get(1), atom3);
        assertEquals(atoms.get(2), atom4);
        assertEquals(atoms.get(3), atom2);
        assertEquals(atoms.size(), 4);
    }



    @Test(expected = AtomSitePositionException.class)
    public void testAddAllIntCollectionException() {
        atoms.add(atom1);
        atoms.add(atom2);

        ArrayList<AtomSite> atomlist = new ArrayList<AtomSite>();
        atomlist.add(atom3);
        atomlist.add(atom4);
        atoms.addAll(1, atomlist);
        atoms.addAll(1, atomlist); // Throw exception
    }



    @Test
    public void testAddAtomSite() {
        atoms.add(atom1);

        assertEquals(atoms.get(0), atom1);
        assertEquals(atoms.size(), 1);
    }



    @Test(expected = AtomSitePositionException.class)
    public void testAddAtomSiteException() {
        atoms.add(atom1);
        atoms.add(atom1); // Throw exception
    }



    @Test
    public void testAddIntAtomSite() {
        atoms.add(atom1);
        atoms.add(atom2);

        atoms.add(1, atom3);

        assertEquals(atoms.get(1), atom3);
        assertEquals(atoms.size(), 3);
    }



    @Test(expected = AtomSitePositionException.class)
    public void testAddIntAtomSiteException() {
        atoms.add(atom1);
        atoms.add(1, atom1); // Throw exception
    }



    @Test
    public void testAtomSites() {
        assertEquals(atoms.size(), 0);
    }



    @Test
    public void testClear() {
        atoms.add(atom1);
        atoms.clear();
        assertEquals(atoms.size(), 0);
    }



    @Test
    public void testEqualsObject() {
        atoms.add(atom1);
        atoms.add(atom2);

        AtomSites other = new AtomSites();
        other.add(atom1);
        other.add(atom2);

        assertTrue(atoms.equals(other));
    }



    @Test
    public void testEqualsAtomSitesDouble1() {
        assertFalse(atoms.equals(null, 1e-3));
    }



    @Test
    public void testEqualsAtomSitesDouble2() {
        atoms.add(atom1);
        atoms.add(atom2);

        AtomSites other = new AtomSites();
        other.add(atom1);
        other.add(new AtomSite(13, 0.0, 0.5002, 0.5));

        assertTrue(atoms.equals(other, 1e-3));
    }



    @Test
    public void testEqualsAtomSitesDouble3() {
        atoms.add(atom1);

        AtomSites other = new AtomSites();

        assertFalse(atoms.equals(other, 1e-3));
    }



    @Test
    public void testEqualsAtomSitesDouble4() {
        atoms.add(atom1);

        AtomSites other = new AtomSites();
        other.add(new AtomSite(13, 0.00001, 0.00001, 0.00001));

        assertFalse(atoms.equals(other, 1e-7));
        assertTrue(atoms.equals(other, 1e-4));
    }



    @Test
    public void testGet() {
        atoms.add(atom1);

        assertEquals(atoms.get(0), atom1);
    }



    @Test
    public void testIndexOf() {
        atoms.add(atom1);
        atoms.add(atom2);
        atoms.add(atom3);

        assertEquals(atoms.indexOf(atom1), 0);
        assertEquals(atoms.indexOf(atom2), 1);
        assertEquals(atoms.indexOf(atom3), 2);
    }



    @Test
    public void testIndexOfAtomSiteDouble() {
        atoms.add(atom1);
        atoms.add(atom2);
        atoms.add(atom3);

        assertEquals(1, atoms.indexOf(new AtomSite(13, 0.0, 0.5002, 0.5), 1e-3));
    }



    @Test
    public void testContainsAtomSiteDouble() {
        atoms.add(atom1);
        atoms.add(atom2);
        atoms.add(atom3);

        assertTrue(atoms.contains(new AtomSite(13, 0.0, 0.5002, 0.5), 1e-3));
        assertFalse(atoms.contains(new AtomSite(14, 0.0, 0.5, 0.5), 1e-3));
    }



    @Test
    public void testIsEmpty() {
        assertTrue(atoms.isEmpty());
    }



    @Test
    public void testLastIndexOf() {
        atoms.add(atom1);
        atoms.add(atom2);

        assertEquals(atoms.lastIndexOf(atom1), 0);
        assertEquals(atoms.lastIndexOf(atom2), 1);
    }



    @Test
    public void testRemoveInt() {
        atoms.add(atom1);
        atoms.add(atom2);
        atoms.add(atom3);

        atoms.remove(1);

        assertEquals(atoms.size(), 2);
        assertEquals(atoms.get(0), atom1);
        assertEquals(atoms.get(1), atom3);
    }



    @Test
    public void testRemoveObject() {
        atoms.add(atom1);
        atoms.add(atom2);
        atoms.add(atom3);

        atoms.remove(atom2);

        assertEquals(atoms.size(), 2);
        assertEquals(atoms.get(0), atom1);
        assertEquals(atoms.get(1), atom3);
    }



    @Test
    public void testSetIntAtomSite() {
        atoms.add(atom1);
        atoms.set(0, atom2);

        assertEquals(atoms.get(0), atom2);
        assertEquals(atoms.size(), 1);
    }



    @Test(expected = AtomSitePositionException.class)
    public void testSetIntAtomSiteException() {
        atoms.add(atom1);
        atoms.set(0, atom1); // Throw exception
    }



    @Test
    public void testSize() {
        assertEquals(atoms.size(), 0);

        atoms.add(atom1);
        assertEquals(atoms.size(), 1);
    }

}
