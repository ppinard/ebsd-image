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
import org.ebsdimage.io.RplFile;
import org.junit.Test;


public class RplFileTest extends TestCase {

    @Test(expected = IOException.class)
    public void load() throws IOException {
        RplFile file = new RplFile("gaga.txt");
        file.load();
    }



    @Test
    public void getDataLength() throws IOException {
        File file = getFile("org/ebsdimage/io/warp-x-map.rpl");
        RplFile rplFile = new RplFile(file);
        rplFile.load();

        assertEquals(1, rplFile.getDataLength());
    }



    @Test
    public void getHeight() throws IOException {
        File file = getFile("org/ebsdimage/io/warp-x-map.rpl");
        RplFile rplFile = new RplFile(file);
        rplFile.load();

        assertEquals(128, rplFile.getHeight());
    }



    @Test
    public void getWidth() throws IOException {
        File file = getFile("org/ebsdimage/io/warp-x-map.rpl");
        RplFile rplFile = new RplFile(file);
        rplFile.load();

        assertEquals(168, rplFile.getWidth());
    }



    @Test
    public void isBigEndian() throws IOException {
        File file = getFile("org/ebsdimage/io/warp-x-map.rpl");
        RplFile rplFile = new RplFile(file);
        rplFile.load();

        assertEquals(true, rplFile.isBigEndian());
    }



    @Test
    public void isSigned() throws IOException {
        File file = getFile("org/ebsdimage/io/warp-x-map.rpl");
        RplFile rplFile = new RplFile(file);
        rplFile.load();

        assertEquals(false, rplFile.isSigned());
    }

}
