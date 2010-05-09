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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;

/**
 * Saves a series of <code>Map</code>s to the "proprietary" <code>SMP</code>
 * format. All the <code>Map</code>s saved to the file must be of the same type
 * and dimensions.
 * <p/>
 * The file format is pretty straightforward. The first three bytes are 83, 77
 * and 80 which translate to SMP. The next byte is the number of characters of
 * the full class name of the <code>Map</code>'s in the file. The next bytes
 * represent the full class name of the <code>Map</code>s held in the file. The
 * rest of the file consists of a series of numbers saved in Java binary format.
 * The first two numbers are the width and the height of all the
 * <code>Map</code>s in integer format. The rest of the file is filled with the
 * values of the <code>pixArray</code>s of every <code>Map</code> sequentially.
 * It is straight dump of the <code>pixArray</code>s.
 * 
 * @author Marin Lagac&eacute;
 */
public class SmpOutputStream {
    /** Output stream to save the maps in a smp file. */
    private DataOutputStream outStream;

    /** Width of the maps to save. */
    private int width;

    /** Height of the maps to save. */
    private int height;

    /** Number of Maps written to the stream. */
    private int nbMaps = 0;



    /**
     * Creates a new <code>SmpOutputStream</code> to save maps in a smp file.
     * 
     * @param file
     *            smp file
     * @throws IOException
     *             if an error occurs while writing the header
     */
    public SmpOutputStream(File file) throws IOException {
        outStream = new DataOutputStream(new FileOutputStream(file));

        // Write the header
        outStream.write("SMP".getBytes());
    }



    /**
     * Closes the stream.
     * 
     * @throws IOException
     *             if an error occured while closing the stream
     */
    public void close() throws IOException {
        outStream.close();
    }



    /**
     * Write the specified <code>Map</code> to the file. All the
     * <code>Map</code>s saved to the file must have the same dimensions. The
     * first <code>Map</code> written to the file will set the dimensions that
     * must have all the other <code>Map</code>s.
     * 
     * @param map
     *            <code>Map</code> to write to the file
     * 
     * @throws IllegalArgumentException
     *             if the <code>Map</code>'s dimensions are not the same as the
     *             other <code>Map</code>s previously saved to the file.
     * @throws IllegalArgumentException
     *             if <code>map</code> is not of the proper & type.
     * @throws IOException
     *             if an error occurred while saving the <code>Map</code> to
     *             disk.
     */
    public void writeMap(Map map) throws IOException {
        if (!(map instanceof ByteMap))
            throw new IllegalArgumentException("map type (" + map.getName()
                    + ")(" + map.getClass().getName() + " must be a ByteMap");

        if (nbMaps == 0) // If no Map written yet
        {
            // Write the map type
            String mapTypeName = map.getClass().getName();
            outStream.writeByte(mapTypeName.length());
            outStream.write(mapTypeName.getBytes());

            // Write the map dimensions
            outStream.writeInt(map.width);
            outStream.writeInt(map.height);
            width = map.width;
            height = map.height;
        }

        if (map.width != width || map.height != height)
            throw new IllegalArgumentException("map dimension ("
                    + map.getName() + ")(" + map.getDimensionLabel()
                    + ") should be (" + width + 'x' + height + ')');

        ByteMap byteMap = (ByteMap) map;

        outStream.write(byteMap.pixArray);
        nbMaps++;
    }

}
