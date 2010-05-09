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

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;

public class BinningTest extends TestCase {

    private Binning binning;



    @Before
    public void setUp() throws Exception {
        binning = new Binning(2);
    }



    @Test
    public void testBinning() {
        Binning tmpBinning = new Binning();
        assertEquals(Binning.DEFAULT_BINNING_SIZE, tmpBinning.size);
    }



    @Test
    public void testBinningInt() {
        assertEquals(2, binning.size);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testBinningIntException() {
        new Binning(0);
    }



    @Test
    public void testEquals() {
        Binning other = new Binning(2);
        assertFalse(binning == other);
        assertEquals(binning, other);
    }



    @Test
    public void testProcess() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/binning.bmp");

        ByteMap destMap = binning.process(null, srcMap);

        destMap.assertEquals(expectedMap);
        assertEquals(destMap.width, srcMap.width / 2);
    }



    @Test
    public void testToString() {
        assertEquals(binning.toString(), "Binning [binning size=2]");
    }

}
