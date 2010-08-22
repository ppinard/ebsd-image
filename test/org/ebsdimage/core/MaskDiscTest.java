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

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.BinMap;

public class MaskDiscTest extends TestCase {

    @Test
    public void centered() {
        MaskDisc maskDisc = new MaskDisc(168, 128, 84, 64, 59);

        // Must copy the pixArray from the MaskDisc to a BinMap
        // because the assertEquals method will fail since the
        // expected map is loaded as a BinMap which is not a MaskDisc
        // thus not equal
        BinMap binMap = new BinMap(168, 128);
        System.arraycopy(maskDisc.pixArray, 0, binMap.pixArray, 0,
                maskDisc.size);

        BinMap expected = (BinMap) load("org/ebsdimage/testdata/maskDisc.bmp");
        binMap.assertEquals(expected);

        assertEquals(84, maskDisc.getProperty(MaskDisc.KEY_CENTROID_X, -1));
        assertEquals(64, maskDisc.getProperty(MaskDisc.KEY_CENTROID_Y, -1));
        assertEquals(59, maskDisc.getProperty(MaskDisc.KEY_RADIUS, -1));
    }



    @Test
    public void inTheCorner() {
        MaskDisc maskDisc = new MaskDisc(168, 128, 0, 0, 59);

        // Must copy the pixArray from the MaskDisc to a BinMap
        // because the assertEquals method will fail since the
        // expected map is loaded as a BinMap which is not a MaskDisc
        // thus not equal
        BinMap binMap = new BinMap(168, 128);
        System.arraycopy(maskDisc.pixArray, 0, binMap.pixArray, 0,
                maskDisc.size);

        BinMap expected = (BinMap) load("org/ebsdimage/testdata/maskDisc2.bmp");
        binMap.assertEquals(expected);

        assertEquals(0, maskDisc.getProperty(MaskDisc.KEY_CENTROID_X, -1));
        assertEquals(0, maskDisc.getProperty(MaskDisc.KEY_CENTROID_Y, -1));
        assertEquals(59, maskDisc.getProperty(MaskDisc.KEY_RADIUS, -1));
    }

}
