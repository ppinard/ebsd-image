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

import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import crystallography.core.crystals.Silicon;

public class ReflectorsTest {

    private Reflectors reflsFCC;
    private Reflectors reflsBCC;
    private Reflectors reflsHCP;

    private ElectronScatteringFactors scatter;



    @Before
    public void setUp() throws Exception {
        // Unit cell
        UnitCell unitCellFCC = UnitCellFactory.cubic(5.43);
        UnitCell unitCellBCC = UnitCellFactory.cubic(2.87);
        UnitCell unitCellHCP = UnitCellFactory.hexagonal(3.21, 5.21);

        // Atom sites
        AtomSites atomsFCC = AtomSitesFactory.atomSitesFCC(14);
        AtomSites atomsBCC = AtomSitesFactory.atomSitesBCC(14);
        AtomSites atomsHCP = AtomSitesFactory.atomSitesHCP(14);

        // Crystal
        Crystal crystalFCC =
                new Crystal("FCC", unitCellFCC, atomsFCC, PointGroup.PG432);
        Crystal crystalBCC =
                new Crystal("BCC", unitCellBCC, atomsBCC, PointGroup.PG432);
        Crystal crystalHCP =
                new Crystal("HCP", unitCellHCP, atomsHCP, PointGroup.PG622);

        // Scattering factors
        scatter = new ElectronScatteringFactors();

        // Reflectors
        reflsFCC = new Reflectors(crystalFCC, scatter, 2);
        reflsBCC = new Reflectors(crystalBCC, scatter, 2);
        reflsHCP = new Reflectors(crystalHCP, scatter, 2);

    }



    @Test
    public void testIntensityBCC() {
        Reflector refl;

        refl = reflsBCC.get(new Plane(1, 0, 1));
        assertEquals(0.0044562961611491949, refl.intensity, 1e-7);

        refl = reflsBCC.get(new Plane(1, -1, 0));
        assertEquals(0.0044562961611491949, refl.intensity, 1e-7);

        refl = reflsBCC.get(new Plane(2, 0, 0));
        assertEquals(0.0013809857316082104, refl.intensity, 1e-7);
    }



    @Test
    public void testIntensityFCC() {
        Reflector refl;

        refl = reflsFCC.get(new Plane(1, 1, 1));
        assertEquals(0.0859145669508, refl.intensity, 1e-7);

        refl = reflsFCC.get(new Plane(1, -1, 1));
        assertEquals(0.0859145669508, refl.intensity, 1e-7);

        refl = reflsFCC.get(new Plane(2, -2, 0));
        assertEquals(0.0152361124413, refl.intensity, 1e-7);
    }



    @Test
    public void testIntensityHCP() {
        Reflector refl;

        refl = reflsHCP.get(new Plane(0, 0, 2));
        assertEquals(0.0099153044364262422, refl.intensity, 1e-7);

        refl = reflsHCP.get(new Plane(1, 0, -1));
        assertEquals(0.0059739159485949992, refl.intensity, 1e-7);

        refl = reflsHCP.get(new Plane(2, -1, 0));
        assertEquals(0.002165252452505663, refl.intensity, 1e-7);
    }



    @Test
    public void testNormalizedIntensityBCC() {
        Reflector refl;

        refl = reflsBCC.get(new Plane(1, 0, 1));
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsBCC.get(new Plane(1, -1, 0));
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsBCC.get(new Plane(2, 0, 0));
        assertEquals(0.30989541127178588, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testNormalizedIntensityFCC() {
        Reflector refl;

        refl = reflsFCC.get(new Plane(1, 1, 1));
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsFCC.get(new Plane(1, -1, 1));
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsFCC.get(new Plane(2, -2, 0));
        assertEquals(0.177340269316, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testNormalizedIntensityHCP() {
        Reflector refl;

        refl = reflsHCP.get(new Plane(0, 0, 2));
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsHCP.get(new Plane(1, 0, -1));
        assertEquals(0.60249445560626358, refl.normalizedIntensity, 1e-7);

        refl = reflsHCP.get(new Plane(2, -1, 0));
        assertEquals(0.21837478278035422, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testPlaneBCC() {
        ArrayList<Plane> planesBCC = new ArrayList<Plane>();
        planesBCC.add(new Plane(0, 1, 1));
        planesBCC.add(new Plane(1, 1, 0));
        planesBCC.add(new Plane(1, 0, 1));
        planesBCC.add(new Plane(1, -1, 0));
        planesBCC.add(new Plane(1, 0, -1));
        planesBCC.add(new Plane(0, 1, -1));
        planesBCC.add(new Plane(0, 2, 0));
        planesBCC.add(new Plane(2, 0, 0));
        planesBCC.add(new Plane(0, 0, 2));
        planesBCC.add(new Plane(1, 1, 2));
        planesBCC.add(new Plane(1, 2, 1));
        planesBCC.add(new Plane(2, 1, 1));
        planesBCC.add(new Plane(1, -2, -1));
        planesBCC.add(new Plane(2, 1, -1));
        planesBCC.add(new Plane(2, -1, -1));
        planesBCC.add(new Plane(1, 1, -2));
        planesBCC.add(new Plane(1, -1, -2));
        planesBCC.add(new Plane(1, 2, -1));
        planesBCC.add(new Plane(2, -1, 1));
        planesBCC.add(new Plane(1, -2, 1));
        planesBCC.add(new Plane(1, -1, 2));
        planesBCC.add(new Plane(0, 2, 2));
        planesBCC.add(new Plane(2, 0, 2));
        planesBCC.add(new Plane(2, 2, 0));
        planesBCC.add(new Plane(2, -2, 0));
        planesBCC.add(new Plane(2, 0, -2));
        planesBCC.add(new Plane(0, 2, -2));
        planesBCC.add(new Plane(2, 2, 2));
        planesBCC.add(new Plane(2, -2, -2));
        planesBCC.add(new Plane(2, -2, 2));
        planesBCC.add(new Plane(2, 2, -2));

        for (Reflector refl : reflsBCC) {
            Plane plane = refl.plane;
            assertTrue(planesBCC.contains(plane));

            int modulo = (plane.sum() % 2);
            assertEquals(0, modulo);
        }
    }



    @Test
    public void testPlaneFCC() {
        ArrayList<Plane> planesFCC = new ArrayList<Plane>();
        planesFCC.add(new Plane(1, 1, 1));
        planesFCC.add(new Plane(1, -1, -1));
        planesFCC.add(new Plane(1, 1, -1));
        planesFCC.add(new Plane(1, -1, 1));
        planesFCC.add(new Plane(0, 2, 0));
        planesFCC.add(new Plane(0, 0, 2));
        planesFCC.add(new Plane(2, 0, 0));
        planesFCC.add(new Plane(0, 2, 2));
        planesFCC.add(new Plane(2, 0, 2));
        planesFCC.add(new Plane(2, 2, 0));
        planesFCC.add(new Plane(0, 2, -2));
        planesFCC.add(new Plane(2, 0, -2));
        planesFCC.add(new Plane(2, -2, 0));
        planesFCC.add(new Plane(2, 2, 2));
        planesFCC.add(new Plane(2, -2, 2));
        planesFCC.add(new Plane(2, -2, -2));
        planesFCC.add(new Plane(2, 2, -2));

        for (Reflector refl : reflsFCC) {
            Plane plane = refl.plane;
            assertTrue(planesFCC.contains(plane));

            if (plane.get(0) % 2 == 0) {
                assertEquals(plane.get(1) % 2, 0, 1e-7);
                assertEquals(plane.get(2) % 2, 0, 1e-7);
            } else {// plane[0] % 2 == 1
                assertEquals(abs(plane.get(1) % 2), 1, 1e-7);
                assertEquals(abs(plane.get(2) % 2), 1, 1e-7);
            }
        }
    }



    @Test
    public void testPlaneHCP() {
        for (Reflector refl : reflsHCP) {
            Plane plane = refl.plane;

            // From Rollett 2008
            boolean condition1 = abs((plane.get(0) + 2 * plane.get(1)) % 3) < 0;
            boolean condition2 = abs(plane.get(2) % 2) == 1;
            assertFalse(condition1 && condition2);
        }
    }



    @Test
    public void testPlaneSpacingBCC() {
        // Compared with HKL Channel 5 Phases Database
        Reflector refl;

        refl = reflsBCC.get(new Plane(1, 0, 1));
        assertEquals(2.0293964620053915, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(new Plane(1, -1, 0));
        assertEquals(2.0293964620053915, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(new Plane(2, 0, 0));
        assertEquals(1.4349999999999998, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(new Plane(1, -1, 2));
        assertEquals(1.1716725936312868, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(new Plane(2, 0, 2));
        assertEquals(1.0146982310026957, refl.planeSpacing, 1e-7);
    }



    @Test
    public void testPlaneSpacingFCC() {
        // Compared with HKL Channel 5 Phases Database
        Reflector refl;

        refl = reflsFCC.get(new Plane(1, 1, 1));
        assertEquals(3.1350119616996683, refl.planeSpacing, 1e-7);

        refl = reflsFCC.get(new Plane(1, -1, 1));
        assertEquals(3.1350119616996683, refl.planeSpacing, 1e-7);

        refl = reflsFCC.get(new Plane(2, -2, 0));
        assertEquals(1.919794910921476, refl.planeSpacing, 1e-7);
    }



    @Test
    public void testPlaneSpacingHCP() {
        // Compared with HKL Channel 5 Phases Database
        Reflector refl;

        refl = reflsHCP.get(new Plane(0, 0, 2));
        assertEquals(2.605, refl.planeSpacing, 1e-7);

        refl = reflsHCP.get(new Plane(1, 0, -1));
        assertEquals(2.4526403546701228, refl.planeSpacing, 1e-7);

        refl = reflsHCP.get(new Plane(2, -1, 0));
        assertEquals(1.605, refl.planeSpacing, 1e-7);
    }



    @Test
    public void testReflectors() {
        assertEquals(17, reflsFCC.size());
        assertEquals(31, reflsBCC.size());
        assertEquals(53, reflsHCP.size());
    }



    @Test
    public void testReflectorsCrystalScatteringFactorsInt() {
        Reflectors refls = new Reflectors(new Silicon(), scatter, 2);
        assertEquals(17, refls.size());
    }



    @Test
    public void testSortByIntensity() {
        reflsFCC.sortByIntensity(true);
        assertEquals(1.0, reflsFCC.get(0).normalizedIntensity, 1e-7);
        assertEquals(new Plane(1, 1, 1), reflsFCC.get(0).plane);
    }



    @Test
    public void testSortByIntensity2() {
        reflsFCC.sortByIntensity(false);
        assertEquals(1.0,
                reflsFCC.get(reflsFCC.size() - 1).normalizedIntensity, 1e-7);
        assertEquals(new Plane(1, 1, 1),
                reflsFCC.get(reflsFCC.size() - 1).plane);
    }



    @Test
    public void testSortByPlaneSpacing() {
        reflsFCC.sortByPlaneSpacing(true);
        assertEquals(1.0, reflsFCC.get(0).normalizedIntensity, 1e-7);
        assertEquals(new Plane(1, 1, 1), reflsFCC.get(0).plane);
    }



    @Test
    public void testSortByPlaneSpacing2() {
        reflsFCC.sortByPlaneSpacing(false);
        assertEquals(1.0,
                reflsFCC.get(reflsFCC.size() - 1).normalizedIntensity, 1e-7);
        assertEquals(new Plane(1, 1, 1),
                reflsFCC.get(reflsFCC.size() - 1).plane);
    }



    @Test
    public void testGetPlane() {
        Plane plane = new Plane(1, 1, 1);
        Reflector refl = reflsFCC.get(plane);
        assertEquals(plane, refl.plane);
        assertEquals(3.1350119616996683, refl.planeSpacing, 1e-7);
        assertEquals(0.0859145669508, refl.intensity, 1e-7);
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetPlaneException() {
        reflsFCC.get(new Plane(0, 1, 1));
    }



    @Test
    public void testIterator() {
        int i = 0;

        for (Reflector refl : reflsFCC) {
            assertEquals(refl, reflsFCC.get(i));
            i++;
        }
    }
}
