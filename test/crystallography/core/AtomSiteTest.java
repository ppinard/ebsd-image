/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class AtomSiteTest extends TestCase {

    private AtomSite atom1;

    private AtomSite atom2;

    private AtomSite atom3;

    private AtomSite atom4;



    @Before
    public void setUp() throws Exception {
        atom1 = new AtomSite(13, 0.0, 0.5, 1.5);
        atom2 = new AtomSite(14, new Vector3D(0.3, -0.8, 0.1));
        atom3 = new AtomSite(15, -0.1, 0.2, 0.3, 0.5);
        atom4 = new AtomSite(16, new Vector3D(0.1, 1.2, 2.3), 0.1);
    }



    @Test
    public void testAtomSiteIntDoubleDoubleDouble() {
        assertEquals(13, atom1.atomicNumber);
        assertEquals(new Vector3D(0.0, 0.5, 0.5), atom1.position, 1e-7);
        assertEquals(1.0, atom1.occupancy, 1e-7);
    }



    @Test
    public void testAtomSiteIntDoubleDoubleDoubleDouble() {
        assertEquals(15, atom3.atomicNumber);
        assertEquals(new Vector3D(0.9, 0.2, 0.3), atom3.position, 1e-7);
        assertEquals(0.5, atom3.occupancy, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntDoubleDoubleDoubleDoubleException1() {
        new AtomSite(15, -0.1, 0.2, 0.3, -0.1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntDoubleDoubleDoubleDoubleException2() {
        new AtomSite(15, -0.1, 0.2, 0.3, 1.1);
    }



    @Test
    public void testAtomSiteIntVector3D() {
        assertEquals(14, atom2.atomicNumber);
        assertEquals(new Vector3D(0.3, 0.2, 0.1), atom2.position, 1e-7);
        assertEquals(1.0, atom2.occupancy, 1e-7);
    }



    @Test
    public void testAtomSiteIntVector3DDouble() {
        assertEquals(16, atom4.atomicNumber);
        assertEquals(new Vector3D(0.1, 0.2, 0.3), atom4.position, 1e-7);
        assertEquals(0.1, atom4.occupancy, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntVector3DDoubleException1() {
        new AtomSite(-1, new Vector3D(0.1, 0.2, 0.3), 0.1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntVector3DDoubleException2() {
        new AtomSite(112, new Vector3D(0.1, 0.2, 0.3), 0.1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntVector3DDoubleException3() {
        new AtomSite(16, new Vector3D(0.1, 1.2, 2.3), -0.1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntVector3DDoubleException4() {
        new AtomSite(16, new Vector3D(0.1, 1.2, 2.3), 1.1);
    }



    @Test(expected = NullPointerException.class)
    public void testAtomSiteIntVector3DDoubleException5() {
        new AtomSite(16, null, 0.1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntVector3DException1() {
        new AtomSite(-1, new Vector3D(0.1, 0.2, 0.3));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAtomSiteIntVector3DException2() {
        new AtomSite(112, new Vector3D(0.1, 0.2, 0.3));
    }



    @Test(expected = NullPointerException.class)
    public void testAtomSiteIntVector3DException3() {
        new AtomSite(13, null);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(atom1.equals(atom1, 1e-4));

        assertFalse(atom1.equals(null, 1e-4));

        assertFalse(atom1.equals(new AtomSite(14, 0.0, 0.5, 1.5), 1e-3));

        AtomSite atom = new AtomSite(13, 0.0, 0.5002, 1.5);
        assertTrue(atom1.equals(atom, 0.001));
        assertFalse(atom1.equals(atom, 0.0001));

        atom = new AtomSite(13, 0.0, 0.5, 1.5, 0.999);
        assertTrue(atom1.equals(atom, 0.01));
        assertFalse(atom1.equals(atom, 0.001));
    }



    @Test
    public void testRefinePosition() {
        AtomSite atom = new AtomSite(13, 1.1, 1.2, 1.3);
        assertEquals(new AtomSite(13, 0.1, 0.2, 0.3), atom, 1e-3);

        atom = new AtomSite(13, -0.9, -0.8, -0.7);
        assertEquals(new AtomSite(13, 0.1, 0.2, 0.3), atom, 1e-3);
    }



    @Test
    public void testToString() {
        assertEquals(atom1.toString(), "Al->{0; 0.5; 0.5} [100%]");
        assertEquals(atom2.toString(), "Si->{0.3; 0.2; 0.1} [100%]");
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(atom1, tmpFile);

        // Read
        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        AtomSite other = loader.load(AtomSite.class, tmpFile);
        assertEquals(atom1, other, 1e-7);
    }

}
