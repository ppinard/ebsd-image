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

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ElectronScatteringFactorsTest {

    private ElectronScatteringFactors scatter;



    @Before
    public void setUp() throws Exception {
        scatter = new ElectronScatteringFactors();
    }



    @Test
    public void testElectronScatteringFactors() {
        assertEquals(scatter.coefficients02.size(), 98);
        assertEquals(scatter.coefficients26.size(), 98);

        // Try reading all atomic numbers
        for (int atomicNumber = 1; atomicNumber <= 98; atomicNumber++) {
            scatter.getFromS(atomicNumber, 1.0);
            scatter.getFromS(atomicNumber, 4.0);
        }
    }



    @Test
    public void testGetFromPlaneSpacing() {
        double factor;
        double expectedFactor;

        /* Aluminum */
        // s = 0.5
        factor = scatter.getFromPlaneSpacing(14, 4 * PI);
        expectedFactor = 0.742519788187;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromPlaneSpacing(14, 2.0 / 3.0 * PI);
        expectedFactor = 0.0349033998305;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromPlaneSpacing(14, 2.0 / 6.1 * PI);
        expectedFactor = 0.00650227565622;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Copper */
        // s = 0.5
        factor = scatter.getFromPlaneSpacing(29, 4 * PI);
        expectedFactor = 1.46390369208;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromPlaneSpacing(29, 2.0 / 3.0 * PI);
        expectedFactor = 0.0652299842623;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromPlaneSpacing(29, 2.0 / 6.1 * PI);
        expectedFactor = 0.000336590516491;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Gold */
        // s = 0.5
        factor = scatter.getFromPlaneSpacing(79, 4 * PI);
        expectedFactor = 3.07239344718;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromPlaneSpacing(79, 2.0 / 3.0 * PI);
        expectedFactor = 0.186031146844;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromPlaneSpacing(79, 2.0 / 6.1 * PI);
        expectedFactor = 0.0332559532;
        assertEquals(factor, expectedFactor, 1e-7);
    }



    @Test
    public void testGetFromS() {
        double factor;
        double expectedFactor;

        /* Aluminum */
        // s = 0.5
        factor = scatter.getFromS(14, 0.5);
        expectedFactor = 0.742519788187;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromS(14, 3.0);
        expectedFactor = 0.0349033998305;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromS(14, 6.1);
        expectedFactor = 0.00650227565622;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Copper */
        // s = 0.5
        factor = scatter.getFromS(29, 0.5);
        expectedFactor = 1.46390369208;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromS(29, 3.0);
        expectedFactor = 0.0652299842623;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromS(29, 6.1);
        expectedFactor = 0.000336590516491;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Gold */
        // s = 0.5
        factor = scatter.getFromS(79, 0.5);
        expectedFactor = 3.07239344718;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromS(79, 3.0);
        expectedFactor = 0.186031146844;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromS(79, 6.1);
        expectedFactor = 0.0332559532;
        assertEquals(factor, expectedFactor, 1e-7);
    }

}
