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
package org.ebsdimage.core.exp.ops.detection.op;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.core.MathMorph;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class AutomaticTopHatTest extends TestCase {

    private AutomaticTopHat op;



    @Before
    public void setUp() throws Exception {
        op = new AutomaticTopHat();
    }



    @Test
    public void testDetect() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));
        BinMap peaksMap = op.detect(null, srcMap);

        // Remove small objects for comparison
        MathMorph.opening(peaksMap, 2, 8);

        IdentMap identMap = Identification.identify(peaksMap);

        assertEquals(9, identMap.getObjectCount());
        peaksMap.getCalibration().assertEquals(srcMap.getCalibration(), 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Automatic Top Hat");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        AutomaticTopHat other =
                new XmlLoader().load(AutomaticTopHat.class, file);
        assertEquals(op, other, 1e-6);
    }

}
