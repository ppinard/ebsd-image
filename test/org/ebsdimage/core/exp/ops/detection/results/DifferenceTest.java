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
package org.ebsdimage.core.exp.ops.detection.results;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class DifferenceTest extends TestCase {

    private Difference difference;

    private BinMap peaksMap;

    private HoughMap houghMap;



    @Before
    public void setUp() throws Exception {
        difference = new Difference();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_top_hat.bmp"));
        houghMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Difference", difference.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = difference.calculate(peaksMap, houghMap);

        assertEquals(4, results.length);

        // Average
        assertEquals(26.666666, results[0].value.doubleValue(), 1e-6);

        // Std Dev
        assertEquals(21.242645, results[1].value.doubleValue(), 1e-6);

        // Min
        assertEquals(7.0, results[2].value.doubleValue(), 1e-6);

        // Max
        assertEquals(71.0, results[3].value.doubleValue(), 1e-6);
    }

}
