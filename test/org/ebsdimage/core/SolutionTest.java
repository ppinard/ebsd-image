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
package org.ebsdimage.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

public class SolutionTest {

    private Solution solution;

    private Crystal crystal;

    private Quaternion rotation;

    private double fit;



    @Before
    public void setUp() throws Exception {
        crystal = CrystalFactory.silicon();
        rotation = new Quaternion(new Eulers(0.5, 0.6, 0.7));
        fit = 0.5;

        solution = new Solution(crystal, rotation, fit);
    }



    @Test
    public void testSolution() {
        assertTrue(crystal.equals(solution.phase, 1e-6));
        assertTrue(rotation.equals(solution.rotation, 1e-6));
        assertEquals(fit, solution.fit, 1e-6);
    }



    @Test
    public void testToString() {
        String expected =
                "Silicon\tAxisAngle [angle=75.91387921717522, axis=(0.47806636403682407;-0.04796663187071836;0.8770129724260584)]\t0.5";
        assertEquals(expected, solution.toString());
    }

}
