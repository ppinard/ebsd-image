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

import crystallography.core.Crystal;
import crystallography.core.Reflectors;
import crystallography.core.XrayScatteringFactors;
import crystallography.core.crystals.Silicon;

public class InterplanarAnglePairsTest {

    private InterplanarAnglePairs pairs;

    private Reflectors refls;

    private Crystal crystal;



    @Before
    public void setUp() throws Exception {
        crystal = new Silicon();
        XrayScatteringFactors scatter = new XrayScatteringFactors();
        refls = new Reflectors(crystal, scatter, 3);
        pairs = new InterplanarAnglePairs(refls);
    }



    @Test
    public void testInterplanarAnglePairs() {
        assertTrue(pairs.size() < refls.size() * (refls.size() - 1) / 2);
        // Less because of zeros

        assertEquals(0, pairs.get(0).directionCosine, 1e-6);
        assertEquals(0.9733285, pairs.get(pairs.size() - 1).directionCosine,
                1e-6);
    }



    @Test
    public void testSortByDirectionCosineBoolean1() {
        pairs.sortByDirectionCosine(false);

        assertEquals(0, pairs.get(0).directionCosine, 1e-6);
        assertEquals(0.9733285, pairs.get(pairs.size() - 1).directionCosine,
                1e-6);
    }



    @Test
    public void testSortByDirectionCosineBoolean2() {
        pairs.sortByDirectionCosine(true);

        assertEquals(0, pairs.get(pairs.size() - 10).directionCosine, 1e-6);
        assertEquals(0.9733285, pairs.get(0).directionCosine, 1e-6);
    }



    @Test
    public void testFindClosestMatches() {
        // Near 90 deg
        InterplanarAnglePair[] matches = pairs.findClosestMatches(0, 1e-3);

        assertEquals(72, matches.length);

        for (InterplanarAnglePair match : matches)
            assertEquals(0, match.directionCosine, 1e-3);
    }



    @Test
    public void testFindClosestMatches2() {
        // Near 0 deg
        InterplanarAnglePair[] matches = pairs.findClosestMatches(1, 1e-3);

        assertEquals(12, matches.length);

        for (InterplanarAnglePair match : matches)
            assertEquals(0.9733, match.directionCosine, 1e-3);

    }



    @Test
    public void testFindClosestMatches3() {
        // Near 60 deg
        InterplanarAnglePair[] matches = pairs.findClosestMatches(0.5, 1e-3);

        assertEquals(12, matches.length);

        for (InterplanarAnglePair match : matches)
            assertEquals(0.5, match.directionCosine, 1e-3);

    }
}
