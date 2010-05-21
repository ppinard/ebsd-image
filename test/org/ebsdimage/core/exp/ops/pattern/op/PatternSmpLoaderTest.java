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
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

public class PatternSmpLoaderTest extends TestCase {

    private PatternSmpLoader loader;
    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/Project19.smp");
        if (filepath == null)
            throw new IOException(
                    "File \"org/ebsdimage/testdata/Project19.smp\" not found.");

        loader = new PatternSmpLoader(2, filepath);
    }



    @Test
    public void testEqualsObject() {
        PatternSmpLoader other = new PatternSmpLoader(2, filepath);
        assertFalse(loader == other);
        assertEquals(loader, other);
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap patternMap = loader.load(null);

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/Project19/Project193.jpg");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testPatternSmpLoader() {
        PatternSmpLoader loader = new PatternSmpLoader();
        assertEquals("", loader.filedir);
        assertEquals("", loader.filename);
        assertEquals(0, loader.index);
    }



    @Test
    public void testPatternSmpLoaderIntFile() {
        assertEquals(filepath.getParent(), loader.filedir);
        assertEquals(filepath.getName(), loader.filename);
        assertEquals(2, loader.index);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternSmpLoaderIntFileException() {
        new PatternSmpLoader(-1, filepath);
    }



    @Test
    public void testPatternSmpLoaderIntString() {
        PatternSmpLoader other =
                new PatternSmpLoader(2, "", filepath.getName());

        assertEquals("", other.filedir);
        assertEquals(filepath.getName(), other.filename);
        assertEquals(2, other.index);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternSmpLoaderIntStringException() {
        new PatternSmpLoader(-1, filepath);
    }



    @Test
    public void testToString() {
        String expected =
                "Pattern Smp Loader [index=2, filedir=" + filepath.getParent()
                        + ", filename=" + filepath.getName() + "]";
        assertEquals(expected, loader.toString());
    }

}
