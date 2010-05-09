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
package org.ebsdimage.core.exp.ops.identification.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.ops.identification.op.IdentificationOp;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;

public class IdentificationOpMockTest {

    private IdentificationOp op;
    private BinMap peaksMap;
    private HoughMap houghMap;



    @Before
    public void setUp() throws Exception {
        op = new IdentificationOpMock();

        peaksMap = new BinMap(4, 3);
        for (int i = 0; i < peaksMap.size; i++)
            peaksMap.pixArray[i] = (byte) (i % 2);

        houghMap = new HoughMap(4, 3, 1, 1);
        byte[] srcPixArray = new byte[] { 7, 11, 15, 19 };
        for (int i = 0; i < houghMap.size; i++)
            houghMap.pixArray[i] = srcPixArray[i % 4];
    }



    @Test
    public void testIdentify() {
        HoughPeak[] destPeaks = op.identify(null, peaksMap, houghMap);

        assertEquals(3, destPeaks.length);
        assertTrue(destPeaks[0].equals(new HoughPeak(7.0, 0.0), 1e-6));
        assertTrue(destPeaks[1].equals(new HoughPeak(11.0, 1.0), 1e-6));
        assertTrue(destPeaks[2].equals(new HoughPeak(15.0, 0.0), 1e-6));
    }

}
