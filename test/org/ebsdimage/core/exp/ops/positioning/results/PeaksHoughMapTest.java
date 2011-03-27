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
package org.ebsdimage.core.exp.ops.positioning.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.ops.detection.op.AutomaticTopHat;
import org.ebsdimage.core.exp.ops.positioning.op.CenterOfMass;
import org.ebsdimage.core.exp.ops.positioning.results.PeaksHoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.*;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class PeaksHoughMapTest extends TestCase {

    private PeaksHoughMap op;



    @Before
    public void setUp() throws Exception {
        op = new PeaksHoughMap(5);
    }



    @Test
    public void testCreate() throws IOException {
        // Create
        HoughMap houghMap =
                new HoughMapLoader().load(getFile("org/ebsdimage/testdata/houghmap.bmp"));
        HoughPeak[] peaks =
                new CenterOfMass().identify(null,
                        new AutomaticTopHat().detect(null, houghMap), houghMap);
        File file = FileUtil.setExtension(createTempFile(), "bmp");

        houghMap.clear();
        op.create(houghMap, peaks, file);

        // Test
        BinMap peaksMap =
                Threshold.densitySlice((ByteMap) load(file), 255, 255);
        peaksMap.setCalibration(Calibration.NONE);
        Centroid centroid =
                Analysis.getCentroid(Identification.identify(peaksMap));

        assertEquals(5, centroid.x.length);

        assertEquals(19, centroid.x[0], 1e-6);
        assertEquals(70, centroid.y[0], 1e-6);

        assertEquals(153, centroid.x[1], 1e-6);
        assertEquals(86, centroid.y[1], 1e-6);

        assertEquals(102, centroid.x[2], 1e-6);
        assertEquals(104, centroid.y[2], 1e-6);

        assertEquals(43, centroid.x[3], 1e-6);
        assertEquals(132, centroid.y[3], 1e-6);

        assertEquals(114, centroid.x[4], 1e-6);
        assertEquals(148, centroid.y[4], 1e-6);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PeaksHoughMap(4)));
        assertTrue(op.equals(new PeaksHoughMap(5)));
    }



    @Test
    public void testHashCode() {
        assertEquals(849393477, op.hashCode());
    }



    @Test
    public void testPeaksHoughMap() {
        assertEquals(5, op.max);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "HoughMap [max=5]");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PeaksHoughMap other = new XmlLoader().load(PeaksHoughMap.class, file);
        assertEquals(op, other, 1e-6);
    }

}
