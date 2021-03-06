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
package org.ebsdimage.core.exp.ops.positioning.post;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughPeak;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class PositioningPostOpsMockTest extends TestCase {

    private PositioningPostOpsMock op;

    private HoughPeak[] srcPeaks;



    @Before
    public void setUp() throws Exception {
        op = new PositioningPostOpsMock();

        HoughPeak peak1 = new HoughPeak(0.0, 7.0, 1.0);
        HoughPeak peak2 = new HoughPeak(1.0, 11.0, 2.0);
        HoughPeak peak3 = new HoughPeak(0.0, 15.0, 3.0);

        srcPeaks = new HoughPeak[] { peak1, peak2, peak3 };
    }



    @Test
    public void testProcess() {
        HoughPeak[] destPeaks = op.process(null, srcPeaks);

        assertEquals(3, destPeaks.length);

        HoughPeak other = new HoughPeak(0.0, 14.0, 1.0);
        assertEquals(other, destPeaks[0], 1e-6);

        other = new HoughPeak(1.0, 22.0, 2.0);
        assertEquals(other, destPeaks[1], 1e-6);

        other = new HoughPeak(0.0, 30.0, 3.0);
        assertEquals(other, destPeaks[2], 1e-6);
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PositioningPostOpsMock other =
                new XmlLoader().load(PositioningPostOpsMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
