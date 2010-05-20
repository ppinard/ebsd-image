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
package org.ebsdimage.core.exp.ops.identification.pre;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.Identification;

public class CleanEdgeTest extends TestCase {

    private CleanEdge cleanEdge;



    @Before
    public void setUp() throws Exception {
        cleanEdge = new CleanEdge();
    }



    @Test
    public void testToString() {
        String expected = "Clean Edge";
        assertEquals(expected, cleanEdge.toString());
    }



    @Test
    public void testProcess() {
        BinMap srcMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_stddev.bmp"));
        assertEquals(44, Identification.identify(srcMap).getObjectCount());

        BinMap destMap = cleanEdge.process(null, srcMap);

        assertEquals(43, Identification.identify(destMap).getObjectCount());
    }

}
