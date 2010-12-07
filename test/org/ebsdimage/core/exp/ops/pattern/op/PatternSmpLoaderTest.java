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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpTester;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class PatternSmpLoaderTest extends TestCase {

    private PatternSmpLoader op;

    private File filepath;

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        filepath = getFile("org/ebsdimage/testdata/Project19.smp");

        exp = ExpTester.createExp();

        op = new PatternSmpLoader(2, 4, filepath);
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap patternMap = op.load(exp, 2);

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/Project19/Project193.jpg");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testPatternSmpLoaderIntFile() {
        assertEquals(filepath.getParent(), op.filedir);
        assertEquals(filepath.getName(), op.filename);
        assertEquals(2, op.startIndex);
        assertEquals(4, op.size);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternSmpLoaderIntFileException() {
        new PatternSmpLoader(-1, 4, filepath);
    }



    @Test
    public void testPatternSmpLoaderIntString() {
        PatternSmpLoader other =
                new PatternSmpLoader(2, 4, "", filepath.getName());

        assertEquals(filepath.getName(), other.filename);
        assertEquals(2, other.startIndex);
        assertEquals(4, other.size);
    }



    @Test
    public void testToString() {
        String expected =
                "Pattern Smp Loader [startIndex=2, size=4, filedir="
                        + filepath.getParent() + ", filename="
                        + filepath.getName() + "]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new PatternSmpLoader(1, 4, filepath)));
        assertFalse(op.equals(new PatternSmpLoader(2, 5, filepath)));
        assertFalse(op.equals(new PatternSmpLoader(2, 4, new File(""))));
        assertTrue(op.equals(new PatternSmpLoader(2, 4, filepath)));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 2));
        assertFalse(op.equals(null, 2));
        assertFalse(op.equals(new Object(), 2));

        assertFalse(op.equals(new PatternSmpLoader(4, 4, filepath), 2));
        assertFalse(op.equals(new PatternSmpLoader(2, 6, filepath), 2));
        assertFalse(op.equals(new PatternSmpLoader(2, 4, new File("")), 2));
        assertTrue(op.equals(new PatternSmpLoader(2, 4, filepath), 2));
    }



    @Test
    public void testHashCode() {
        assertEquals(1052419856, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        PatternSmpLoader other =
                new XmlLoader().load(PatternSmpLoader.class, file);
        assertEquals(op, other, 1e-6);
    }
}
