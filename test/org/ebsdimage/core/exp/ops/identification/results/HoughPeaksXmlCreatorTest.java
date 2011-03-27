/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class HoughPeaksXmlCreatorTest extends TestCase {

    private PeaksXml op;

    private HoughPeak peak1;

    private HoughPeak peak2;

    private HoughPeak peak3;

    private HoughPeak[] peaks;



    @Before
    public void setUp() throws Exception {
        op = new PeaksXml();

        peak1 = new HoughPeak(0.5, 3.0, 1);
        peak2 = new HoughPeak(1.5, 5.0, 3);
        peak3 = new HoughPeak(1.0, 4.0, 2);

        peaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testSave() throws IOException {
        File file = createTempFile();
        op.save(peaks, file);

        HoughPeak[] other = new XmlLoader().loadArray(HoughPeak.class, file);

        assertEquals(3, other.length);
        assertEquals(peak2, other[0], 1e-6);
        assertEquals(peak3, other[1], 1e-6);
        assertEquals(peak1, other[2], 1e-6);
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PeaksXml other = new XmlLoader().load(PeaksXml.class, file);
        assertEquals(op, other, 1e-6);
    }
}
