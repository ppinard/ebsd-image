/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import static org.junit.Assert.assertEquals;

public class SmpInputStreamTest extends TestCase {

    private File file;

    private SmpInputStream inStream;



    @Before
    public void setUp() throws IOException {
        // Create an Map Stream with four maps in it
        file = new File(createTempDir(), "SmpInputStreamTest.smp");
        SmpOutputStream outStream = new SmpOutputStream(file, 1000);
        ByteMap lena = (ByteMap) load("org/ebsdimage/testdata/Lena.bmp");
        outStream.writeMap(lena);
        ByteMap lena90 =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate90deg.bmp");
        outStream.writeMap(lena90);
        ByteMap lena180 =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate180deg.bmp");
        outStream.writeMap(lena180);
        ByteMap lena270 =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate270deg.bmp");
        outStream.writeMap(lena270);
        outStream.close();

        // Open the stream
        inStream = new SmpInputStream(file);
    }



    @Override
    @After
    public void tearDown() throws Exception {
        inStream.close();
        inStream = null;

        super.tearDown();
    }



    @Test
    public void testGetEndIndex() {
        assertEquals(1003, inStream.getEndIndex());
    }



    @Test
    public void testGetMapCount() {
        assertEquals(4, inStream.getMapCount());
    }



    @Test
    public void testGetMapHeight() {
        assertEquals(256, inStream.getMapHeight());
    }



    @Test
    public void testGetMapType() {
        assertEquals(ByteMap.class, inStream.getMapType());
    }



    @Test
    public void testGetMapWidth() {
        assertEquals(256, inStream.getMapWidth());
    }



    @Test
    public void testGetStartIndex() {
        assertEquals(1000, inStream.getStartIndex());
    }



    @Test
    // Read maps unordered
    public void testReadMap() throws IOException {
        ByteMap map = (ByteMap) inStream.readMap(1002);
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate180deg.bmp");
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1000);
        expected = (ByteMap) load("org/ebsdimage/testdata/Lena.bmp");
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1003);
        expected =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate270deg.bmp");
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1001);
        expected =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate90deg.bmp");
        map.assertEquals(expected);
    }



    @Test
    // Read maps unordered in a preexisting map
    public void testReadMap2() throws IOException {
        ByteMap map = new ByteMap(256, 256);
        inStream.readMap(1002, map);
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate180deg.bmp");
        map.assertEquals(expected);

        inStream.readMap(1000, map);
        expected = (ByteMap) load("org/ebsdimage/testdata/Lena.bmp");
        map.assertEquals(expected);

        inStream.readMap(1003, map);
        expected =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate270deg.bmp");
        map.assertEquals(expected);

        inStream.readMap(1001, map);
        expected =
                (ByteMap) load("org/ebsdimage/testdata/Lena_Rotate90deg.bmp");
        map.assertEquals(expected);
    }



    // Read map in wrong size preexisting map
    @Test(expected = IllegalArgumentException.class)
    public void testReadMapException1() throws IOException {
        ByteMap map = new ByteMap(255, 256);
        inStream.readMap(1002, map);
    }



    // Read map in wrong type preexisting map
    @Test(expected = IllegalArgumentException.class)
    public void testReadMapException2() throws IOException {
        BinMap map = new BinMap(256, 256);
        inStream.readMap(1002, map);
    }

}
