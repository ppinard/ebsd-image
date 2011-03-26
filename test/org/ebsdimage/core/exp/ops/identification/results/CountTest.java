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

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class CountTest extends TestCase {

    private Count op;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        HoughPeak peak1 = new HoughPeak(0.5, 3.0, 1);
        HoughPeak peak2 = new HoughPeak(1.5, 5.0, 3);
        HoughPeak peak3 = new HoughPeak(1.0, 4.0, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };

        op = new Count();
    }



    @Test
    public void testCalculate() {
        double expected = peaks.length;
        OpResult result = op.calculate(null, peaks)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Count");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Count other = new XmlLoader().load(Count.class, file);
        assertEquals(op, other, 1e-6);
    }

}
