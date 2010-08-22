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
package org.ebsdimage.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

public class SmpOutputStreamTest extends TestCase {

    private File file;



    @Before
    public void setUp() throws Exception {
        file = new File(FileUtil.getTempDir(), "SmpOutputStreamTest.smp");
    }



    @After
    public void tearDown() throws Exception {
        if (!(file.delete()))
            throw new RuntimeException("File (" + file.getAbsolutePath()
                    + ") cannot be deleted.");
    }



    @Test
    public void writemap() throws IOException {
        // Create an Map Stream with four maps in it
        SmpOutputStream outStream = new SmpOutputStream(file);
        ByteMap lena = (ByteMap) load("org/ebsdimage/io/Lena.bmp");
        outStream.writeMap(lena);
        ByteMap lena90 =
                (ByteMap) load("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        outStream.writeMap(lena90);
        ByteMap lena180 =
                (ByteMap) load("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        outStream.writeMap(lena180);
        ByteMap lena270 =
                (ByteMap) load("org/ebsdimage/io/Lena_Rotate270deg.bmp");
        outStream.writeMap(lena270);
        outStream.close();

        // Read the maps back
        SmpInputStream inStream = new SmpInputStream(file);
        ByteMap map = (ByteMap) inStream.readMap(0);
        map.assertEquals(lena);
        map = (ByteMap) inStream.readMap(1);
        map.assertEquals(lena90);
        map = (ByteMap) inStream.readMap(2);
        map.assertEquals(lena180);
        map = (ByteMap) inStream.readMap(3);
        map.assertEquals(lena270);
        inStream.close();
    }



    // Write map bad size
    @Test(expected = IllegalArgumentException.class)
    public void write2() throws IOException {
        // Create an Map Stream with four maps in it
        SmpOutputStream outStream = new SmpOutputStream(file);
        ByteMap map = new ByteMap(320, 240);
        outStream.writeMap(map);
        ByteMap badSize = new ByteMap(321, 240);
        try {
            outStream.writeMap(badSize);
        } finally {
            outStream.close();
        }

    }



    // Write map bad type
    @Test(expected = IllegalArgumentException.class)
    public void write3() throws IOException {
        // Create an Map Stream with four maps in it
        SmpOutputStream outStream = new SmpOutputStream(file);
        ByteMap map = new ByteMap(320, 240);
        outStream.writeMap(map);
        BinMap badType = new BinMap(320, 240);
        try {
            outStream.writeMap(badType);
        } finally {
            outStream.close();
        }
    }

}
