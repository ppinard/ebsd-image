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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.BinMap;
import rmlimage.core.IdentMap;
import rmlimage.core.Identification;
import rmlimage.core.MathMorph;
import rmlshared.io.FileUtil;

public class AutomaticStdDevTest extends TestCase {

    private AutomaticStdDev op;



    @Before
    public void setUp() throws Exception {
        op = new AutomaticStdDev(2.0);
    }



    @Test
    public void testAutomaticStdDevDouble() {
        assertEquals(2.0, op.sigmaFactor, 1e-6);
    }



    @Test
    public void testIdentify() throws IOException {
        HoughMap srcMap =
                new HoughMapLoader().load(FileUtil.getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));
        BinMap peaksMap = op.detect(null, srcMap);

        // Remove small objects for comparison
        MathMorph.opening(peaksMap, 2, 8);

        IdentMap identMap = Identification.identify(peaksMap);

        assertEquals(9, identMap.getObjectCount());
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new AutomaticStdDev(1.5)));
        assertTrue(op.equals(new AutomaticStdDev(2.0)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new AutomaticStdDev(2.1), 1e-2));
        assertTrue(op.equals(new AutomaticStdDev(2.01), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-1416536444, op.hashCode());
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Automatic Std Dev [sigmaFactor=2.0]");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        AutomaticStdDev other =
                new XmlLoader().load(AutomaticStdDev.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
