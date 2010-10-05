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
package org.ebsdimage.core.exp.ops.hough.results;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class EntropyTest {

    private Entropy entropy;



    @Before
    public void setUp() throws Exception {
        entropy = new Entropy();
    }



    @Test
    public void testCalculate() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap.bmp"));
        OpResult result = entropy.calculate(null, srcMap)[0];

        assertEquals(2.926016571945, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(entropy.toString(), "Entropy");
    }

}
