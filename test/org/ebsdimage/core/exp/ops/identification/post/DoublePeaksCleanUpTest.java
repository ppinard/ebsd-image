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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

public class DoublePeaksCleanUpTest {

    private DoublePeaksCleanUp doublePeaksCleanUp;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        doublePeaksCleanUp = new DoublePeaksCleanUp(2, 3);

        srcPeaks =
                new HoughPeak[] { new HoughPeak(0.1, 0.2),
                        new HoughPeak(0.3, 0.6), new HoughPeak(0.4, 0.7),
                        new HoughPeak(0.0, 0.7) };
    }



    @Test
    public void testEqualsObject() {
        DoublePeaksCleanUp other = new DoublePeaksCleanUp(2, 3);

        assertTrue(other != doublePeaksCleanUp);
        assertTrue(other.equals(doublePeaksCleanUp));
        assertFalse(new DoublePeaksCleanUp(3, 4).equals(doublePeaksCleanUp));
    }



    @Test
    public void testToString() {
        String expected = "DoublePeaksCleanUp [spacingRho=2, spacingTheta=3]";
        assertEquals(expected, doublePeaksCleanUp.toString());
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = doublePeaksCleanUp.process(srcPeaks, 0.1, 0.1);

        assertEquals(3, destPeaks.length);
        assertEquals(new HoughPeak(0.0, 0.7), destPeaks[0]);
        assertEquals(new HoughPeak(0.3, 0.6), destPeaks[1]);
        assertEquals(new HoughPeak(0.1, 0.2), destPeaks[2]);
    }



    @Test
    public void testDoublePeaksCleanUp() {
        DoublePeaksCleanUp other = new DoublePeaksCleanUp();
        assertEquals(DoublePeaksCleanUp.DEFAULT_SPACING_RHO, other.spacingRho);
        assertEquals(DoublePeaksCleanUp.DEFAULT_SPACING_THETA,
                other.spacingTheta);
    }



    @Test
    public void testDoublePeaksCleanUpIntInt() {
        assertEquals(2, doublePeaksCleanUp.spacingRho);
        assertEquals(3, doublePeaksCleanUp.spacingTheta);
    }

}
