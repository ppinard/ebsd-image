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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpTester;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

public class PatternSmpLoaderTest extends TestCase {

    private PatternSmpLoader loader;

    private File filepath;

    private Exp exp;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/Project19.smp");
        if (filepath == null)
            throw new IOException(
                    "File \"org/ebsdimage/testdata/Project19.smp\" not found.");

        exp = ExpTester.createExp();

        loader = new PatternSmpLoader(2, 4, filepath);
    }



    @Test
    public void testEqualsObject() {
        PatternSmpLoader other = new PatternSmpLoader(2, 4, filepath);
        assertFalse(loader == other);
        assertEquals(loader, other);
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap patternMap = loader.load(exp, 2);

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/Project19/Project193.jpg");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testPatternSmpLoaderIntFile() {
        assertEquals(filepath.getParent(), loader.filedir);
        assertEquals(filepath.getName(), loader.filename);
        assertEquals(2, loader.startIndex);
        assertEquals(4, loader.size);
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
        assertEquals(expected, loader.toString());
    }

}
