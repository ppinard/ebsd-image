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
package org.ebsdimage.core.exp.ops.identification.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class FitTest {

    private Fit fit;

    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/fit.xml");

        if (filepath == null)
            throw new RuntimeException(
                    "File \"org/ebsdimage/testdata/fit.xml\" "
                            + "cannot be found.");

        fit = new Fit(filepath);
    }



    @Test
    public void testCompare() {
        // Peaks1
        HoughPeak[] detPeaks =
                new HoughPeak[] { new HoughPeak(1, 2), new HoughPeak(5, 1.5),
                        new HoughPeak(8, 1) };

        OpResult result = fit.calculate(null, detPeaks)[0];
        assertEquals(0.0, result.value.doubleValue(), 1e-6);
    }



    @Test
    public void testToString() {
        assertTrue(fit.toString().startsWith("Fit [filepath="));
    }

}
