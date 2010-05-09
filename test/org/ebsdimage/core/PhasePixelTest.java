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

import org.ebsdimage.core.PhasePixel;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.crystals.Silicon;

public class PhasePixelTest {

    private PhasePixel pixel;



    @Before
    public void setUp() throws Exception {
        pixel = new PhasePixel(1, new Silicon());
    }



    @Test
    public void testGetValueLabel() {
        assertEquals("1: Silicon", pixel.getValueLabel());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPhasePixelIntCrystalException1() {
        new PhasePixel(0, new Silicon());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPhasePixelIntCrystalException2() {
        new PhasePixel(1, null);
    }



    @Test
    public void testPhasePixel() {
        PhasePixel other = new PhasePixel();
        assertEquals("0: Not indexed", other.getValueLabel());
    }

}
