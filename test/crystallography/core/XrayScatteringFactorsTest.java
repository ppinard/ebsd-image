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

import org.junit.Before;
import org.junit.Test;

public class XrayScatteringFactorsTest {

    private XrayScatteringFactors scatter;



    @Before
    public void setUp() throws Exception {
        scatter = new XrayScatteringFactors();
    }



    @Test
    public void testGetFromPlaneSpacing() {
        double factor;
        double expectedFactor;

        /* Aluminum */
        // s = 0.5
        factor = scatter.getFromPlaneSpacing(14, 1.0);
        expectedFactor = 6.2400885119901508;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromPlaneSpacing(14, 1.0 / 6.0);
        expectedFactor = 0.72266443008234738;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromPlaneSpacing(14, 1.0 / (2 * 6.1));
        expectedFactor = 0.10091802161492447;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Copper */
        // s = 0.5
        factor = scatter.getFromPlaneSpacing(29, 1.0);
        expectedFactor = 13.70070197002754;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromPlaneSpacing(29, 1.0 / 6.0);
        expectedFactor = 2.0104488387433492;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromPlaneSpacing(29, 1.0 / (2 * 6.1));
        expectedFactor = 0.9191770170219552;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Gold */
        // s = 0.5
        factor = scatter.getFromPlaneSpacing(79, 1.0);
        expectedFactor = 46.906655657698153;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromPlaneSpacing(79, 1.0 / 6.0);
        expectedFactor = 9.7301060945727169;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromPlaneSpacing(79, 1.0 / (2 * 6.1));
        expectedFactor = 4.2485964205405162;
        assertEquals(factor, expectedFactor, 1e-7);
    }



    @Test
    public void testGetFromS() {
        double factor;
        double expectedFactor;

        /* Aluminum */
        // s = 0.5
        factor = scatter.getFromS(14, 0.5);
        expectedFactor = 6.2400885119901508;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromS(14, 3.0);
        expectedFactor = 0.72266443008234738;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromS(14, 6.1);
        expectedFactor = 0.10091802161492447;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Copper */
        // s = 0.5
        factor = scatter.getFromS(29, 0.5);
        expectedFactor = 13.70070197002754;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromS(29, 3.0);
        expectedFactor = 2.0104488387433492;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromS(29, 6.1);
        expectedFactor = 0.9191770170219552;
        assertEquals(factor, expectedFactor, 1e-7);

        /* Gold */
        // s = 0.5
        factor = scatter.getFromS(79, 0.5);
        expectedFactor = 46.906655657698153;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 3.0
        factor = scatter.getFromS(79, 3.0);
        expectedFactor = 9.7301060945727169;
        assertEquals(factor, expectedFactor, 1e-7);

        // s = 6.1
        factor = scatter.getFromS(79, 6.1);
        expectedFactor = 4.2485964205405162;
        assertEquals(factor, expectedFactor, 1e-7);
    }



    @Test
    public void testXrayScatteringFactors() {
        assertEquals(scatter.coefficients02.size(), 98);
        assertEquals(scatter.coefficients26.size(), 97);

        // Try reading all atomic numbers
        for (int atomicNumber = 2; atomicNumber <= 98; atomicNumber++) {
            scatter.getFromS(atomicNumber, 1.0);
            scatter.getFromS(atomicNumber, 4.0);
        }
    }

}
