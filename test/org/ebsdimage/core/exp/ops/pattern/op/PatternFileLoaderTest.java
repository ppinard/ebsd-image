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

public class PatternFileLoaderTest extends TestCase {

    private PatternFileLoader loader;
    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/patternloader.bmp");
        loader = new PatternFileLoader(45, filepath);
    }



    @Test
    public void testEquals() {
        PatternFileLoader other = new PatternFileLoader(45, filepath);
        assertFalse(loader == other);
        assertEquals(loader, other);
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap patternMap = loader.load(null);

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/patternloader.bmp");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testPatternLoader() {
        PatternFileLoader loader = new PatternFileLoader();
        assertEquals("", loader.filedir);
        assertEquals("", loader.filename);
        assertEquals(0, loader.index);
    }



    @Test
    public void testPatternLoaderIntFile() {
        assertEquals(filepath.getName(), loader.filename);
        assertEquals(filepath.getParent(), loader.filedir);
        assertEquals(45, loader.index);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternLoaderIntFileException() {
        new PatternFileLoader(-1, filepath);
    }



    @Test
    public void testPatternLoaderIntString() {
        PatternFileLoader other =
                new PatternFileLoader(45, "", filepath.getName());

        assertEquals(filepath.getName(), other.filename);
        assertEquals("", other.filedir);
        assertEquals(45, other.index);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternLoaderIntStringException() {
        new PatternFileLoader(-1, filepath);
    }



    @Test
    public void testToString() {
        String expected =
                "Pattern File Loader [index=45, filedir="
                        + filepath.getParent() + ", filename="
                        + filepath.getName() + "]";
        assertEquals(expected, loader.toString());
    }

}
