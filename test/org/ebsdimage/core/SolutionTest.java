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
package org.ebsdimage.core;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class SolutionTest {

    private Solution solution;

    private Crystal crystal;

    private Rotation rotation;

    private double fit;



    @Before
    public void setUp() throws Exception {
        crystal = CrystalFactory.silicon();
        rotation = new Rotation(RotationOrder.ZXZ, 0.5, 0.6, 0.7);
        fit = 0.5;

        solution = new Solution(crystal, rotation, fit);
    }



    @Test
    public void testSolution() {
        assertEquals(crystal, solution.phase, 1e-6);
        assertEquals(rotation, solution.rotation, 1e-6);
        assertEquals(fit, solution.fit, 1e-6);
    }



    @Test
    public void testToString() {
        String expected =
                "Silicon\t{0.48; -0.05; 0.88}\t75.91387921717521\t0.5";
        assertEquals(expected, solution.toString());
    }

}
