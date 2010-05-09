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

import rmlshared.util.Properties;

public class RplFile {

    private File file;

    private Properties props;



    public RplFile(String fileName) {
        this(new File(fileName));
    }



    public RplFile(File file) {
        if (file == null)
            throw new NullPointerException("file == null.");
        this.file = file;
    }



    // Get the number of bytes per pixel
    public int getDataLength() {
        int dataLength = props.getProperty("data-length", -1);
        if (dataLength < 0)
            throw new IllegalArgumentException("data-length not specified in "
                    + file);
        if (dataLength > 2)
            throw new IllegalArgumentException("data-length of " + dataLength
                    + " is not supported.");

        // Assertion that if data-length >= 2
        // there must be a specified byte ordering
        String byteOrder = props.getProperty("byte-order", "");
        if (dataLength >= 2 && byteOrder.equals("dont-care"))
            throw new IllegalArgumentException("There is an error in " + file
                    + '\n' + "data-length = " + dataLength
                    + " and byte-order = " + byteOrder);

        return dataLength;
    }



    public int getHeight() {
        if (props == null)
            throw new NullPointerException("Must use load() first");

        int height = props.getProperty("height", -1);
        if (height >= 0)
            return height;
        else
            throw new IllegalArgumentException("Height not specified in "
                    + file);
    }



    public int getWidth() {
        if (props == null)
            throw new NullPointerException("Must use load() first");

        int width = props.getProperty("width", -1);
        if (width >= 0)
            return width;
        else
            throw new IllegalArgumentException("Width not specified in " + file);
    }



    // Get whether the date is coded in big or little endian
    public boolean isBigEndian() {
        String byteOrder = props.getProperty("byte-order", "");
        if (byteOrder.equals("big-endian"))
            return true;
        else if (byteOrder.equals("little-endian"))
            return false;
        else if (byteOrder.equals("dont-care"))
            return true;
        else
            throw new IllegalArgumentException("Unknown byte-order ( "
                    + byteOrder + ") in " + file);
    }



    // Get whether the data is signed or unsigned
    public boolean isSigned() {
        String dataType = props.getProperty("data-type", "");
        if (dataType.equals("signed"))
            return true;
        else if (dataType.equals("unsigned"))
            return false;
        else
            throw new IllegalArgumentException("Unknown data-type (" + dataType
                    + ") in " + file);
    }



    public void load() throws IOException {
        props = (Properties) new RplLoader().load(file);
    }

}
