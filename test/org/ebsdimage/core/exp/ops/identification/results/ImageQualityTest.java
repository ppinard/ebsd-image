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

public class ImageQualityTest extends TestCase {

    private ImageQuality op;

    private HoughPeak[] peaks;

    private HoughPeak peak1;

    private HoughPeak peak2;

    private HoughPeak peak3;



    @Before
    public void setUp() throws Exception {

        peak1 = new HoughPeak(0.5, 3.0, 1);
        peak2 = new HoughPeak(1.5, 5.0, 3);
        peak3 = new HoughPeak(1.0, 4.0, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };

        op = new ImageQuality();
    }



    @Test
    public void testCalculate() {
        double expected =
                (peak1.intensity + peak2.intensity + peak3.intensity) / 3.0;
        OpResult result = op.calculate(null, peaks)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Image Quality");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        ImageQuality other = new XmlLoader().load(ImageQuality.class, file);
        assertEquals(op, other, 1e-6);
    }

}
