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

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class NoiseTest extends TestCase {

    @Test
    public void testRadialNoise() throws IOException {
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/core/noise_radialnoise.bmp");

        ByteMap map = new ByteMap(100, 100);
        map.clear(128);

        map = Noise.radialNoise(map, 0, -30, 50, 100, 1.0, 15.0, 1);

        map.assertEquals(expected);
    }

}
