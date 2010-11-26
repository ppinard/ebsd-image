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
package org.ebsdimage.core.exp.ops.pattern.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.ByteMap;

public class PatternFilesLoaderTest extends TestCase {

    private PatternFilesLoader op;

    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = getFile("org/ebsdimage/testdata/patternloader.bmp");

        op = new PatternFilesLoader(45, filepath);
    }



    @Test
    public void testEquals() {
        PatternFilesLoader other = new PatternFilesLoader(45, filepath);
        assertFalse(op == other);
        assertEquals(op, other);
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap patternMap = op.load(null, 45);

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/patternloader.bmp");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testPatternLoaderIntFile() {
        assertEquals(filepath.getAbsolutePath(),
                op.getFiles()[0].getAbsolutePath());
        assertEquals(45, op.startIndex);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternLoaderIntFileException() {
        new PatternFilesLoader(-1, filepath);
    }



    @Test
    public void testToString() {
        String expected = "Pattern Files Loader [startIndex=45, size=1]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PatternFilesLoader(44, filepath)));
        assertTrue(op.equals(new PatternFilesLoader(45, filepath)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new PatternFilesLoader(43, filepath), 2));
        assertTrue(op.equals(new PatternFilesLoader(44, filepath), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1227302452, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PatternFilesLoader other =
                new XmlLoader().load(PatternFilesLoader.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
