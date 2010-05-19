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
package org.ebsdimage.core.exp.ops.pattern.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RadialNoiseTest {

    private RadialNoise noise;



    @Before
    public void setUp() throws Exception {
        noise = new RadialNoise();
    }



    @Test
    public void testEquals() {
        RadialNoise other = new RadialNoise();
        assertFalse(noise == other);
        assertEquals(noise, other);
    }



    @Test
    public void testProcess() {
        // Impossible to test because of the random seed for the gaussian noise
        assertTrue(true);
    }



    @Test
    public void testRadialNoise() {
        assertEquals(RadialNoise.DEFAULT_X, noise.x);
        assertEquals(RadialNoise.DEFAULT_Y, noise.y);
        assertEquals(RadialNoise.DEFAULT_STDDEV_X, noise.stdDevX, 1e-7);
        assertEquals(RadialNoise.DEFAULT_STDDEV_Y, noise.stdDevY, 1e-7);
        assertEquals(RadialNoise.DEFALT_INITIAL_NOISE_STDDEV,
                noise.initialNoiseStdDev, 1e-7);
        assertEquals(RadialNoise.DEFALT_FINAL_NOISE_STDDEV,
                noise.finalNoiseStdDev, 1e-7);
    }



    @Test
    public void testRadialNoiseIntIntDoubleDoubleDoubleDouble() {
        RadialNoise noise = new RadialNoise(2, 3, 4, 5, 6, 7);
        assertEquals(2, noise.x);
        assertEquals(3, noise.y);
        assertEquals(4, noise.stdDevX, 1e-7);
        assertEquals(5, noise.stdDevY, 1e-7);
        assertEquals(6, noise.initialNoiseStdDev, 1e-7);
        assertEquals(7, noise.finalNoiseStdDev, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRadialNoiseIntIntDoubleDoubleDoubleDoubleException1() {
        new RadialNoise(2, 3, 4, 5, 0, 7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRadialNoiseIntIntDoubleDoubleDoubleDoubleException2() {
        new RadialNoise(2, 3, 4, 5, 6, 0);
    }



    @Test
    public void testToString() {
        String expected =
                "RadialNoise [final noise std. dev.=15.0, initial noise std. dev.=1.0, std. dev. x=-1.0, std. dev. y=-1.0, x0=0 px, y0=0 px]";
        assertEquals(expected, noise.toString());
    }

}
