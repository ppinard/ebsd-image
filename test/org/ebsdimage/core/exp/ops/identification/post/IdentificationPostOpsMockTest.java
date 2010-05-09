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
package org.ebsdimage.core.exp.ops.identification.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOps;
import org.junit.Before;
import org.junit.Test;


public class IdentificationPostOpsMockTest {

    private IdentificationPostOps op;
    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new IdentificationPostOpsMock();

        srcPeaks = new HoughPeak[] { new HoughPeak(7.0, 0.0),
                new HoughPeak(11.0, 1.0), new HoughPeak(15.0, 0.0) };
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = op.process(null, srcPeaks);

        assertEquals(3, destPeaks.length);
        assertTrue(destPeaks[0].equals(new HoughPeak(14.0, 0.0), 1e-6));
        assertTrue(destPeaks[1].equals(new HoughPeak(22.0, 1.0), 1e-6));
        assertTrue(destPeaks[2].equals(new HoughPeak(30.0, 0.0), 1e-6));
    }

}
