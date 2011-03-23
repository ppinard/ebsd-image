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

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.abs;

public class ReflectorsTest {

    private Reflectors reflsFCC;

    private Reflectors reflsBCC;

    private Reflectors reflsHCP;



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
                new Crystal("FCC", unitCellFCC, atomsFCC, SpaceGroups2.SG216);
        Crystal crystalBCC =
                new Crystal("BCC", unitCellBCC, atomsBCC, SpaceGroups2.SG229);
        Crystal crystalHCP =
                new Crystal("HCP", unitCellHCP, atomsHCP, SpaceGroups2.SG168);

        // Reflectors
        reflsFCC =
                ReflectorsFactory.generate(crystalFCC,
                        ScatteringFactorsEnum.ELECTRON, 2);
        reflsBCC =
                ReflectorsFactory.generate(crystalBCC,
                        ScatteringFactorsEnum.ELECTRON, 2);
        reflsHCP =
                ReflectorsFactory.generate(crystalHCP,
                        ScatteringFactorsEnum.ELECTRON, 2);
    }



    @Test
    public void testGetPlane() {
        Reflector refl = reflsFCC.get(1, 1, 1);
        assertEquals(1, refl.h);
        assertEquals(1, refl.k);
        assertEquals(1, refl.l);
        assertEquals(3.1350119616996683, refl.planeSpacing, 1e-7);
        assertEquals(0.0859145669508, refl.intensity, 1e-7);
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetPlaneException() {
        reflsFCC.get(0, 1, 1);
    }



    @Test
    public void testIntensityBCC() {
        Reflector refl;

        refl = reflsBCC.get(1, 0, 1);
        assertEquals(0.0044562961611491949, refl.intensity, 1e-7);

        refl = reflsBCC.get(1, -1, 0);
        assertEquals(0.0044562961611491949, refl.intensity, 1e-7);

        refl = reflsBCC.get(2, 0, 0);
        assertEquals(0.0013809857316082104, refl.intensity, 1e-7);
    }



    @Test
    public void testIntensityFCC() {
        Reflector refl;

        refl = reflsFCC.get(1, 1, 1);
        assertEquals(0.0859145669508, refl.intensity, 1e-7);

        refl = reflsFCC.get(1, -1, 1);
        assertEquals(0.0859145669508, refl.intensity, 1e-7);

        refl = reflsFCC.get(2, -2, 0);
        assertEquals(0.0152361124413, refl.intensity, 1e-7);
    }



    @Test
    public void testIntensityHCP() {
        Reflector refl;

        refl = reflsHCP.get(0, 0, 2);
        assertEquals(0.0223094348195904, refl.intensity, 1e-7);

        refl = reflsHCP.get(1, 0, -1);
        assertEquals(0.00796522126479333, refl.intensity, 1e-7);

        refl = reflsHCP.get(2, -1, 0);
        assertEquals(0.004871818018137741, refl.intensity, 1e-7);
    }



    @Test
    public void testNormalizedIntensityBCC() {
        Reflector refl;

        refl = reflsBCC.get(1, 0, 1);
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsBCC.get(1, -1, 0);
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsBCC.get(2, 0, 0);
        assertEquals(0.30989541127178588, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testNormalizedIntensityFCC() {
        Reflector refl;

        refl = reflsFCC.get(1, 1, 1);
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsFCC.get(1, -1, 1);
        assertEquals(1.0, refl.normalizedIntensity, 1e-7);

        refl = reflsFCC.get(2, -2, 0);
        assertEquals(0.177340269316, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testNormalizedIntensityHCP() {
        Reflector refl;

        refl = reflsHCP.get(0, 0, 2);
        assertEquals(0.5870027442721996, refl.normalizedIntensity, 1e-7);

        refl = reflsHCP.get(1, 0, -1);
        assertEquals(0.20957979191091056, refl.normalizedIntensity, 1e-7);

        refl = reflsHCP.get(2, -1, 0);
        assertEquals(0.1281865967791338, refl.normalizedIntensity, 1e-7);
    }



    @Test
    public void testPlaneBCC() {
        ArrayList<Integer[]> planesBCC = new ArrayList<Integer[]>();
        planesBCC.add(new Integer[] { 0, 1, 1 });
        planesBCC.add(new Integer[] { 1, 1, 0 });
        planesBCC.add(new Integer[] { 1, 0, 1 });
        planesBCC.add(new Integer[] { 1, -1, 0 });
        planesBCC.add(new Integer[] { 1, 0, -1 });
        planesBCC.add(new Integer[] { 0, 1, -1 });
        planesBCC.add(new Integer[] { 0, 2, 0 });
        planesBCC.add(new Integer[] { 2, 0, 0 });
        planesBCC.add(new Integer[] { 0, 0, 2 });
        planesBCC.add(new Integer[] { 1, 1, 2 });
        planesBCC.add(new Integer[] { 1, 2, 1 });
        planesBCC.add(new Integer[] { 2, 1, 1 });
        planesBCC.add(new Integer[] { 1, -2, -1 });
        planesBCC.add(new Integer[] { 2, 1, -1 });
        planesBCC.add(new Integer[] { 2, -1, -1 });
        planesBCC.add(new Integer[] { 1, 1, -2 });
        planesBCC.add(new Integer[] { 1, -1, -2 });
        planesBCC.add(new Integer[] { 1, 2, -1 });
        planesBCC.add(new Integer[] { 2, -1, 1 });
        planesBCC.add(new Integer[] { 1, -2, 1 });
        planesBCC.add(new Integer[] { 1, -1, 2 });
        planesBCC.add(new Integer[] { 0, 2, 2 });
        planesBCC.add(new Integer[] { 2, 0, 2 });
        planesBCC.add(new Integer[] { 2, 2, 0 });
        planesBCC.add(new Integer[] { 2, -2, 0 });
        planesBCC.add(new Integer[] { 2, 0, -2 });
        planesBCC.add(new Integer[] { 0, 2, -2 });
        planesBCC.add(new Integer[] { 2, 2, 2 });
        planesBCC.add(new Integer[] { 2, -2, -2 });
        planesBCC.add(new Integer[] { 2, -2, 2 });
        planesBCC.add(new Integer[] { 2, 2, -2 });

        // Check count
        assertEquals(planesBCC.size(), reflsBCC.size());

        // Check indices
        for (Integer[] plane : planesBCC) {
            assertTrue(reflsBCC.contains(plane[0], plane[1], plane[2]));
        }

        // Check indices rule(s)
        for (Reflector refl : reflsBCC) {
            int modulo = ((refl.h + refl.k + refl.l) % 2);
            assertEquals(0, modulo);
        }
    }



    @Test
    public void testPlaneFCC() {
        ArrayList<Integer[]> planesFCC = new ArrayList<Integer[]>();
        planesFCC.add(new Integer[] { 1, 1, 1 });
        planesFCC.add(new Integer[] { 1, -1, -1 });
        planesFCC.add(new Integer[] { 1, 1, -1 });
        planesFCC.add(new Integer[] { 1, -1, 1 });
        planesFCC.add(new Integer[] { 0, 2, 0 });
        planesFCC.add(new Integer[] { 0, 0, 2 });
        planesFCC.add(new Integer[] { 2, 0, 0 });
        planesFCC.add(new Integer[] { 0, 2, 2 });
        planesFCC.add(new Integer[] { 2, 0, 2 });
        planesFCC.add(new Integer[] { 2, 2, 0 });
        planesFCC.add(new Integer[] { 0, 2, -2 });
        planesFCC.add(new Integer[] { 2, 0, -2 });
        planesFCC.add(new Integer[] { 2, -2, 0 });
        planesFCC.add(new Integer[] { 2, 2, 2 });
        planesFCC.add(new Integer[] { 2, -2, 2 });
        planesFCC.add(new Integer[] { 2, -2, -2 });
        planesFCC.add(new Integer[] { 2, 2, -2 });

        // Check count
        assertEquals(planesFCC.size(), reflsFCC.size());

        // Check indices
        for (Integer[] plane : planesFCC) {
            assertTrue(reflsFCC.contains(plane[0], plane[1], plane[2]));
        }

        // Check indices rule(s)
        for (Reflector refl : reflsFCC) {
            if (refl.h % 2 == 0) {
                assertEquals(refl.k % 2, 0, 1e-7);
                assertEquals(refl.l % 2, 0, 1e-7);
            } else {// plane[0] % 2 == 1
                assertEquals(abs(refl.k % 2), 1, 1e-7);
                assertEquals(abs(refl.l % 2), 1, 1e-7);
            }
        }
    }



    @Test
    public void testPlaneHCP() {
        for (Reflector refl : reflsHCP) {
            // From Rollett 2008
            boolean condition1 = abs((refl.h + 2 * refl.k) % 3) < 0;
            boolean condition2 = abs(refl.l % 2) == 1;
            assertFalse(condition1 && condition2);
        }
    }



    @Test
    public void testPlaneSpacingBCC() {
        // Compared with HKL Channel 5 Phases Database
        Reflector refl;

        refl = reflsBCC.get(1, 0, 1);
        assertEquals(2.0293964620053915, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(1, -1, 0);
        assertEquals(2.0293964620053915, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(2, 0, 0);
        assertEquals(1.4349999999999998, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(1, -1, 2);
        assertEquals(1.1716725936312868, refl.planeSpacing, 1e-7);

        refl = reflsBCC.get(2, 0, 2);
        assertEquals(1.0146982310026957, refl.planeSpacing, 1e-7);
    }



    @Test
    public void testPlaneSpacingFCC() {
        // Compared with HKL Channel 5 Phases Database
        Reflector refl;

        refl = reflsFCC.get(1, 1, 1);
        assertEquals(3.1350119616996683, refl.planeSpacing, 1e-7);

        refl = reflsFCC.get(1, -1, 1);
        assertEquals(3.1350119616996683, refl.planeSpacing, 1e-7);

        refl = reflsFCC.get(2, -2, 0);
        assertEquals(1.919794910921476, refl.planeSpacing, 1e-7);
    }



    @Test
    public void testPlaneSpacingHCP() {
        // Compared with HKL Channel 5 Phases Database
        Reflector refl;

        refl = reflsHCP.get(0, 0, 2);
        assertEquals(2.605, refl.planeSpacing, 1e-7);

        refl = reflsHCP.get(1, 0, -1);
        assertEquals(2.4526403546701228, refl.planeSpacing, 1e-7);

        refl = reflsHCP.get(2, -1, 0);
        assertEquals(1.605, refl.planeSpacing, 1e-7);
    }



    @Test
    public void testReflectors() {
        assertEquals(17, reflsFCC.size());
        assertEquals(31, reflsBCC.size());
        assertEquals(38, reflsHCP.size());
    }



    @Test
    public void testReflectorsCrystalScatteringFactorsInt() {
        Reflectors refls =
                ReflectorsFactory.generate(CrystalFactory.silicon(),
                        ScatteringFactorsEnum.ELECTRON, 2);
        assertEquals(17, refls.size());
    }



    @Test
    public void testSortByIntensity() {
        Reflector[] refls = reflsFCC.getReflectorsSortedByIntensity(true);
        assertEquals(1.0, refls[0].normalizedIntensity, 1e-7);
    }



    @Test
    public void testSortByIntensity2() {
        Reflector[] refls = reflsFCC.getReflectorsSortedByIntensity(false);
        assertEquals(1.0, refls[refls.length - 1].normalizedIntensity, 1e-7);
    }



    @Test
    public void testSortByPlaneSpacing() {
        Reflector[] refls = reflsFCC.getReflectorsSortedByPlaneSpacing(true);
        assertEquals(1.0, refls[0].normalizedIntensity, 1e-7);
        assertEquals(3.1350119617, refls[0].planeSpacing, 1e-7);
    }



    @Test
    public void testSortByPlaneSpacing2() {
        Reflector[] refls = reflsFCC.getReflectorsSortedByPlaneSpacing(false);
        assertEquals(1.0, refls[refls.length - 1].normalizedIntensity, 1e-7);
        assertEquals(3.1350119617, refls[refls.length - 1].planeSpacing, 1e-7);
    }

}
