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
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

public class SmpCreatorTest extends TestCase {

    @Test
    // Create from a list of files
    public void create() throws IOException {
        File[] files = new File[4];
        files[0] = getFile("org/ebsdimage/io/Lena.bmp");
        files[1] = getFile("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        files[2] = getFile("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        files[3] = getFile("org/ebsdimage/io/Lena_Rotate270deg.bmp");

        File smpFile = new File(FileUtil.getTempDir(), "SmpCreatorTest.smp");
        new SmpCreator().create(smpFile, files);

        // Read the maps back
        SmpInputStream inStream = new SmpInputStream(smpFile);

        ByteMap map = (ByteMap) inStream.readMap(0);
        ByteMap expected = (ByteMap) load(files[0]);
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1);
        expected = (ByteMap) load(files[1]);
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(2);
        expected = (ByteMap) load(files[2]);
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(3);
        expected = (ByteMap) load(files[3]);
        map.assertEquals(expected);

        inStream.close();

        if (!(smpFile.delete()))
            throw new RuntimeException("File (" + smpFile.getAbsolutePath()
                    + ") cannot be deleted.");
    }



    @Test
    // Create from a directory
    public void create2() throws IOException {
        // Copy the test maps to a new directory
        File tmpDir = FileUtil.createTempDir("SmpCreatorTest");
        File[] files = new File[5];
        files[0] = getFile("org/ebsdimage/io/Lena.bmp");
        files[1] = getFile("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        files[2] = getFile("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        files[3] = getFile("org/ebsdimage/io/Lena_Rotate270deg.bmp");
        files[4] = getFile("org/ebsdimage/io/warp-x-map.raw");
        FileUtil.copy(files, tmpDir);

        File smpFile = new File(FileUtil.getTempDir(), "SmpCreatorTest.smp");
        new SmpCreator().create(smpFile, tmpDir);

        // Read the maps back
        SmpInputStream inStream = new SmpInputStream(smpFile);

        assertEquals(4, inStream.getMapCount());

        ByteMap map = (ByteMap) inStream.readMap(0);
        ByteMap expected = (ByteMap) load(files[0]);
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(1);
        expected = (ByteMap) load(files[2]);
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(2);
        expected = (ByteMap) load(files[3]);
        map.assertEquals(expected);

        map = (ByteMap) inStream.readMap(3);
        expected = (ByteMap) load(files[1]);
        map.assertEquals(expected);

        inStream.close();

        // Clean up
        if (!(smpFile.delete()))
            throw new RuntimeException("File (" + smpFile.getAbsolutePath()
                    + ") cannot be deleted.");
        FileUtil.rmdir(tmpDir);
    }



    @Test
    public void extract() throws IOException {
        // Create a source smp file
        File[] files = new File[4];
        files[0] = getFile("org/ebsdimage/io/Lena.bmp");
        files[1] = getFile("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        files[2] = getFile("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        files[3] = getFile("org/ebsdimage/io/Lena_Rotate270deg.bmp");
        File srcFile = new File(FileUtil.getTempDir(), "SmpCreatorTest.smp");
        new SmpCreator().create(srcFile, files);

        // Extract the second and third map
        File destFile = new File(FileUtil.getTempDir(), "SmpExtractorTest.smp");
        new SmpCreator().extract(srcFile, 1, 2, destFile);

        // Test that the map were properly extracted
        SmpInputStream inStream = new SmpInputStream(destFile);
        ByteMap map = (ByteMap) inStream.readMap(1);
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/io/Lena_Rotate90deg.bmp");
        map.assertEquals(expected);
        map = (ByteMap) inStream.readMap(2);
        expected = (ByteMap) load("org/ebsdimage/io/Lena_Rotate180deg.bmp");
        map.assertEquals(expected);
        inStream.close();

        // Cleanup
        if (!(srcFile.delete()))
            throw new RuntimeException("File (" + srcFile.getAbsolutePath()
                    + ") cannot be deleted.");
        if (!(destFile.delete()))
            throw new RuntimeException("File (" + destFile.getAbsolutePath()
                    + ") cannot be deleted.");
    }

}
