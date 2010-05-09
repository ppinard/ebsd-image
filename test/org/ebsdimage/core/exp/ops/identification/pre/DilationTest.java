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

import rmlimage.core.*;

public class DilationTest extends TestCase {

    private Dilation dilation;



    @Before
    public void setUp() throws Exception {
        dilation = new Dilation();
    }



    @Test
    public void testToString() {
        String expected = "Dilation";
        assertEquals(expected, dilation.toString());
    }



    @Test
    public void testProcess() {
        BinMap srcMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/automatic_stddev.bmp"));

        IdentMap identMap = Identification.identify(srcMap);
        Area area = Analysis.getArea(identMap);
        assertEquals(21, area.val[0], 1e-6);

        BinMap destMap = dilation.process(null, srcMap);

        // Test
        identMap = Identification.identify(destMap);
        area = Analysis.getArea(identMap);

        assertEquals(31, area.val[0], 1e-6);
    }

}
