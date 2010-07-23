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

public class PatternFilesLoaderTest extends TestCase {

    private PatternFilesLoader loader;

    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/patternloader.bmp");

        if (filepath == null)
            throw new RuntimeException(
                    "File \"org/ebsdimage/testdata/patternloader.bmp\" "
                            + "cannot be found.");

        loader = new PatternFilesLoader(45, filepath);
    }



    @Test
    public void testEquals() {
        PatternFilesLoader other = new PatternFilesLoader(45, filepath);
        assertFalse(loader == other);
        assertEquals(loader, other);
    }



    @Test
    public void testLoad() throws IOException {
        ByteMap patternMap = loader.load(null, 45);

        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/patternloader.bmp");

        patternMap.assertEquals(expected);
    }



    @Test
    public void testPatternLoaderIntFile() {
        assertEquals(filepath.getAbsolutePath(),
                loader.getFiles()[0].getAbsolutePath());
        assertEquals(45, loader.startIndex);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternLoaderIntFileException() {
        new PatternFilesLoader(-1, filepath);
    }



    @Test
    public void testPatternLoaderIntString() {
        PatternFilesLoader other =
                new PatternFilesLoader(45, "", filepath.getName());

        assertEquals(filepath.getName(), other.getFiles()[0].getName());
        assertEquals(45, other.startIndex);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testPatternLoaderIntStringException() {
        new PatternFilesLoader(-1, filepath);
    }



    @Test
    public void testToString() {
        String expected = "Pattern Files Loader [startIndex=45, size=1]";
        assertEquals(expected, loader.toString());
    }

}
