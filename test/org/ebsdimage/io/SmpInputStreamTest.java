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

import static org.junit.Assert.assertEquals;


import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.SmpInputStream;
import org.ebsdimage.io.SmpOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;

public class SmpInputStreamTest extends TestCase {

    private File file;
    private SmpInputStream inStream;


    @Test
    public void getEndIndex() {
        assertEquals(1003, inStream.getEndIndex());
    }
    

    @Test
    public void getMapCount() {
        assertEquals(4, inStream.getMapCount());
    }



    @Test
    public void getMapHeight() {
        assertEquals(256, inStream.getMapHeight());
    }



    @Test
    public void getMapType() {
        assertEquals(ByteMap.class, inStream.getMapType());
    }



    @Test
    public void getMapWidth() {
        assertEquals(256, inStream.getMapWidth());
    }

    
    @Test
    public void getStartIndex() {
        assertEquals(1000, inStream.getStartIndex());
    }


    @Test
    // Read maps unordered
    public void readMap() throws IOException {
        ByteMap map = (ByteMap) inStream.readMap(1002);
        ByteMap expected = (ByteMap)load("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1000);
        expected = (ByteMap) load("org/ebsdimage/io/Lena.bmp");
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1003);
        expected = (ByteMap) load("org/ebsdimage/io/Lena_Rotate270deg.bmp");
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1001);
        expected = (ByteMap) load("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        map.assertEquals(expected);
    }



    @Test
    // Read maps unordered in a preexisting map
    public void readMap2() throws IOException {
        ByteMap map = new ByteMap(256, 256);
        inStream.readMap(1002, map);
        ByteMap expected = (ByteMap)load("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        map.assertEquals(expected);

        inStream.readMap(1000, map);
        expected = (ByteMap) load("org/ebsdimage/io/Lena.bmp");
        map.assertEquals(expected);

        inStream.readMap(1003, map);
        expected = (ByteMap) load("org/ebsdimage/io/Lena_Rotate270deg.bmp");
        map.assertEquals(expected);

        inStream.readMap(1001, map);
        expected = (ByteMap) load("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        map.assertEquals(expected);
    }



    // Read map in wrong size preexisting map
    @Test(expected = IllegalArgumentException.class)
    public void readMap3() throws IOException {
        ByteMap map = new ByteMap(255, 256);
        inStream.readMap(1002, map);
    }



    // Read map in wrong type preexisting map
    @Test(expected = IllegalArgumentException.class)
    public void readMap4() throws IOException {
        BinMap map = new BinMap(256, 256);
        inStream.readMap(1002, map);
    }



    @Before
    public void setup() throws IOException {
        // Create an Map Stream with four maps in it
        file = new File(FileUtil.getTempDir(), "SmpInputStreamTest.smp");
        SmpOutputStream outStream = new SmpOutputStream(file, 1000);
        ByteMap lena = (ByteMap) load("org/ebsdimage/io/Lena.bmp");
        outStream.writeMap(lena);
        ByteMap lena90 = (ByteMap) load("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        outStream.writeMap(lena90);
        ByteMap lena180 = (ByteMap)load("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        outStream.writeMap(lena180);
        ByteMap lena270 = (ByteMap)load("org/ebsdimage/io/Lena_Rotate270deg.bmp");
        outStream.writeMap(lena270);
        outStream.close();

        // Open the stream
        inStream = new SmpInputStream(file);
    }



    @After
    public void tearDown() throws IOException {
        inStream.close();
        inStream = null;

        if (!(file.delete()))
            throw new RuntimeException("File (" + file.getAbsolutePath()
                    + ") cannot be deleted.");
        file = null;
    }

}
