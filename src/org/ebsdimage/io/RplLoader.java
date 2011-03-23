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

import rmlshared.io.FileUtil;
import rmlshared.io.Loader;
import rmlshared.io.TsvReader;
import rmlshared.util.Properties;

/**
 * Loader of RPL file.
 * 
 * @author ppinard
 */
public class RplLoader implements Loader {

    @Override
    public boolean canLoad(File file) {
        return FileUtil.getExtension(file).equalsIgnoreCase("rpl");
    }



    @Override
    public double getTaskProgress() {
        return 0;
    }



    /**
     * Loads the RPL file and returns a <code>RplFile</code>.
     * 
     * @param file
     *            RPL file
     * @return a <code>RplFile</code>
     * @throws IOException
     *             if an error occurs while loading the RPL file
     */
    @Override
    public RplFile load(File file) throws IOException {
        if (!canLoad(file))
            throw new IOException("Invalid rpl file");

        TsvReader reader = new TsvReader(file);

        reader.skipLine(); // Skip the header

        Properties props = new Properties();
        String[] line;
        while (true) {
            line = reader.readLine();
            if (line == null)
                break;

            if (line.length != 2)
                throw new IOException("Incorrect number of values ("
                        + line.length + ")(" + line[0] + ") on line #"
                        + reader.getLineReadCount() + '\n' + "in file " + file
                        + ".\n" + "It should be 2.");

            props.setProperty(line[0], line[1]);
        }

        // Data length
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
            throw new IllegalArgumentException("There is an error in the file."
                    + '\n' + "data-length = " + dataLength
                    + " and byte-order = " + byteOrder);
        if (!byteOrder.equals("big-endian")
                && !byteOrder.equals("little-endian")
                && !byteOrder.equals("dont-care"))
            throw new IllegalArgumentException("Unknown byte-order ( "
                    + byteOrder + ").");

        // Width
        int width = props.getProperty("width", -1);
        if (width < 0)
            throw new IllegalArgumentException("Width not specified in " + file);

        // Height
        int height = props.getProperty("height", -1);
        if (height < 0)
            throw new IllegalArgumentException("Height not specified in "
                    + file);

        // Signed
        String dataType = props.getProperty("data-type", "");
        if (!dataType.equals("signed") && !dataType.equals("unsigned"))
            throw new IllegalArgumentException("Unknown data-type (" + dataType
                    + ").");

        return new RplFile(dataLength, width, height, byteOrder, dataType);
    }



    @Override
    public RplFile load(File file, Object arg) throws IOException {
        return load(file);
    }

}
