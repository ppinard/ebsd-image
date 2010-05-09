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

import org.ebsdimage.core.InterplanarAnglePair;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;
import crystallography.core.Plane;
import crystallography.core.Reflector;
import crystallography.core.XrayScatteringFactors;
import crystallography.core.crystals.Silicon;

public class InterplanarAnglePairTest {

    private Reflector refl0;
    private Reflector refl1;
    private Plane plane0;
    private Plane plane1;
    private InterplanarAnglePair pair;



    @Before
    public void setUp() throws Exception {
        Crystal crystal = new Silicon();
        XrayScatteringFactors scatter = new XrayScatteringFactors();

        plane0 = new Plane(1, 1, 1);
        refl0 = new Reflector(plane0, crystal, scatter);
        plane1 = new Plane(2, 0, 0);
        refl1 = new Reflector(plane1, crystal, scatter);

        pair = new InterplanarAnglePair(refl0, refl1, 0.577);
    }



    @Test
    public void testInterplanarAnglePair() {
        assertEquals(plane0.toVector3D().normalize(), pair.normal0);
        assertEquals(plane1.toVector3D().normalize(), pair.normal1);
        assertEquals(0.577, pair.directionCosine, 1e-3);
    }

}
