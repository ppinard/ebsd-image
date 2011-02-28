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

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import crystallography.io.simplexml.SpaceGroupMatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class CrystalTest extends TestCase {

    private String name;

    private Crystal crystal;

    private UnitCell unitCell;

    private AtomSites atoms;

    private SpaceGroup sg;



    @Before
    public void setUp() throws Exception {
        name = "silicon";
        unitCell = UnitCellFactory.cubic(2.0);
        atoms = AtomSitesFactory.atomSitesFCC(14);
        sg = SpaceGroups2.SG216;

        crystal = new Crystal(name, unitCell, atoms, sg);
    }



    @Test
    public void testCrystal() {
        assertEquals(2.0, crystal.unitCell.a, 1e-7);
        assertEquals(14, crystal.atoms.get(0).atomicNumber);
        assertEquals(name, crystal.name);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException1() {
        new Crystal(null, unitCell, atoms, sg);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCrystalException2() {
        new Crystal("", unitCell, atoms, sg);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException3() {
        new Crystal(name, null, atoms, sg);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException4() {
        new Crystal(name, unitCell, null, sg);
    }



    @Test(expected = NullPointerException.class)
    public void testCrystalException5() {
        new Crystal(name, unitCell, atoms, null);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(crystal.equals(crystal, 1e-3));

        assertFalse(crystal.equals(null, 1e-3));

        assertFalse(crystal.equals(new Crystal("other", unitCell, atoms, sg),
                1e-3));

        UnitCell otherUnitCell = UnitCellFactory.cubic(2.002);
        assertFalse(crystal.equals(new Crystal(name, otherUnitCell, atoms, sg),
                1e-3));

        AtomSites otherAtomSites = AtomSitesFactory.atomSitesFCC(15);
        assertFalse(crystal.equals(new Crystal(name, unitCell, otherAtomSites,
                sg), 1e-3));

        assertFalse(crystal.equals(new Crystal(name, unitCell, atoms,
                SpaceGroups2.SG215), 1e-3));

        Crystal other =
                new Crystal(name, UnitCellFactory.cubic(2.002), atoms, sg);
        assertTrue(crystal.equals(other, 1e-2));
    }



    @Test
    public void testToString() {
        assertEquals("silicon", crystal.toString());
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());
        saver.save(crystal, tmpFile);

        // Read
        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        loader.matchers.registerMatcher(new SpaceGroupMatcher());
        Crystal other = loader.load(Crystal.class, tmpFile);
        assertEquals(crystal, other, 1e-6);
    }
}
