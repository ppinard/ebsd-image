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
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class AreaTest extends TestCase {

    private Area area;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        area = new Area();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/peaksMap.bmp"));
    }



    @Test
    public void testToString() {
        assertEquals("Area", area.toString());
    }



    @Test
    public void testCalculate() {
        OpResult[] results = area.calculate(null, peaksMap);

        assertEquals(4, results.length);

        // Average
        assertEquals(67.25, results[0].value.doubleValue(), 1e-6);

        // Std Dev
        assertEquals(18.49131012, results[1].value.doubleValue(), 1e-6);

        // Min
        assertEquals(40.0, results[2].value.doubleValue(), 1e-6);

        // Max
        assertEquals(94.0, results[3].value.doubleValue(), 1e-6);
    }

}
