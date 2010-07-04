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
import java.io.RandomAccessFile;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlshared.io.FileUtil;
import rmlshared.thread.Reflection;

/**
 * Reads a series of <code>Map</code>s saved in the "proprietary"
 * <code>SMP</code> format. All the <code>Map</code>s will have the same type
 * and the same dimensions.
 * <p/>
 * The file format is pretty straightforward. The first three bytes are 83, 77
 * and 80 which translate to SMP. The next byte is the version label. The next
 * byte is the number of characters of the full class name of the
 * <code>Map</code>'s in the file. The next bytes represent the full class name
 * of the <code>Map</code>s held in the file. The rest of the file consists of a
 * series of numbers saved in Java binary format. The first two numbers are the
 * width and the height of the <code>Map</code>s in integer format. Then comes
 * the index of the first <dfn>Map</dfn> (useful for split smp files) in integer
 * format. The rest of the file is filled with the values of the
 * <code>pixArray</code>s of every <code>Map</code> sequentially. It is straight
 * dump of the <code>pixArray</code>s.
 * 
 * @author Marin Lagac&eacute;
 */

public class SmpInputStream {

    /** Smp file. */
    private File file;

    /** Random access file to read the smp. */
    private RandomAccessFile raf;

    /** Type of maps in the smp. */
    private Class<? extends Map> mapType;

    /** Width of the maps in the smp. */
    private int width;

    /** Height of the maps in the smp. */
    private int height;

    /** Size of the maps in the smp. */
    private long size;

    /** Number of maps. */
    private int nbMaps;

    /** Index of the first Map in the file */
    private int startIndex;

    /** Length of the header (= header + width + height). */
    private final int headerLength;



    /**
     * Creates a new <code>SmpInputStream</code> to read the specified smp file.
     * Each map in smp can be access with {@link #readMap(int)} and
     * {@link #readMap(int, Map)}.
     * 
     * @param file
     *            smp file
     * @throws IOException
     *             if an error occurs while reading the header of the smp file
     */
    public SmpInputStream(File file) throws IOException {
        this.file = file;
        raf = new RandomAccessFile(file, "r");

        // Validate header
        byte[] header = new byte[3];
        raf.read(header);
        if (header[0] != (byte) 'S' || header[1] != (byte) 'M'
                || header[2] != (byte) 'P')
            throw new IOException(file + " is not a stream of Maps.");

        // Read the version
        byte versionByte = raf.readByte();

        // Read the map type
        int classNameLength = raf.readByte() & 0xff;
        byte[] classNameChars = new byte[classNameLength];
        raf.read(classNameChars);
        String className = new String(classNameChars);
        mapType = Reflection.forName(className);

        // Read the dimensions of the Maps
        width = raf.readInt();
        height = raf.readInt();
        if (width <= 0)
            throw new IOException("Invalid Map width (" + width + ") in "
                    + file);
        if (height <= 0)
            throw new IOException("Invalid Map height (" + height + ") in "
                    + file);
        size = width * height;

        // If it is an SMP2, read the index of the first Map in the file
        switch (versionByte) {
        case ((byte) '1'): // If smp1
            startIndex = 0;
            // = "SMP#" + classNameLength + className + width + height
            headerLength = 4 + 1 + classNameLength + 4 + 4;
            break;

        case ((byte) '2'): // If rmp2, read the start index
            startIndex = raf.readInt();
            // = "SMP#" + classNameLength + className + width + height
            // + startIndex
            headerLength = 4 + 1 + classNameLength + 4 + 4 + 4;

            break;

        default:
            throw new IllegalArgumentException("Invalid SMP version: "
                    + (char) (header[3] & 0xff));
        }

        nbMaps = (int) ((raf.length() - headerLength) / size);
        assert ((raf.length() - headerLength) % size == 0) : "Too many bytes ("
                + ((raf.length() - headerLength) % size) + ") in " + file;

    }



    /**
     * Closes the stream.
     * 
     * @throws IOException
     *             if an error occured while closing the stream
     */
    public void close() throws IOException {
        raf.close();
    }



    /**
     * Returns the index of the last <dfn>Map</dfn> in the file
     */
    public int getEndIndex() {
        return startIndex + nbMaps - 1;
    }



    /**
     * Returns the number of <code>Map</code>s in the file.
     * 
     * @return number of maps
     */
    public int getMapCount() {
        return nbMaps;
    }



    /**
     * Returns the height of all the <code>Map</code>s in the file.
     * 
     * @return maps' height
     */
    public int getMapHeight() {
        return height;
    }



    /**
     * Returns the type of the <code>Map</code>s in the file.
     * 
     * @return type of map
     */
    public Class<? extends Map> getMapType() {
        return mapType;
    }



    /**
     * Returns the width of all the <code>Map</code>s in the file.
     * 
     * @return maps' width
     */
    public int getMapWidth() {
        return width;
    }



    /**
     * Returns the index of the first <dfn>Map</dfn> in the file
     */
    public int getStartIndex() {
        return startIndex;
    }



    /**
     * Returns the <code>Map</code> at the specified index in the file. The
     * first <code>Map</code> in the file has the index specified by
     * <code>getStartIndex()</code>.
     * 
     * @param index
     *            index of the <code>Map</code> to return
     * @return the <code>Map</code> at the specified index in the file
     * @throws IOException
     *             if an error occurred while reading from the file
     * @see #getStartIndex()
     * @see #getEndIndex()
     */
    public Map readMap(int index) throws IOException {
        // Create a new empty ByteMap
        ByteMap byteMap = new ByteMap(width, height);

        readMap(index, byteMap);
        return byteMap;
    }



    /**
     * Returns the <code>Map</code> at the specified x and y position.
     * 
     * @param x
     *            x coordinate of the <code>Map</code> to return
     * @param y
     *            y coordinate of the <code>Map</code> to return
     * @return the <code>Map</code> at the specified index in the file
     * @throws IOException
     *             if an error occurred while reading from the file
     */
    public Map readMap(int x, int y) throws IOException {
        ByteMap byteMap = new ByteMap(width, height);

        int index = byteMap.getPixArrayIndex(x, y);

        readMap(index, byteMap);

        return byteMap;
    }



    /**
     * Reads the <code>Map</code> at the specified index in the file and places
     * it in the specified <code>Map</code>. The first <code>Map</code> in the
     * file has the index specified by <code>getStartIndex()</code>.
     * <p/>
     * To be sure to use a <code>Map</code> of the proper type and size as the
     * argument, first call {@link #readMap(int)} to get a new <code>Map</code>
     * and then use the same <code>Map</code> as the argument to the current
     * method.
     * 
     * @param index
     *            index of the <code>Map</code> to return
     * @param map
     *            <code>Map</code> to place it into
     * @throws ArrayIndexOutOfBoundsException
     *             if <code>index</code> is not between the bounds set by the
     *             <code>start index</code> and the number of <code>Map</code>s
     *             in the file
     * @throws IllegalArgumentException
     *             if <code>map</code> is not of the proper type or dimensions
     * @throws IOException
     *             if an error occurred while reading from the file
     * @see #getStartIndex()
     * @see #getEndIndex()
     */
    public void readMap(int index, Map map) throws IOException {
        if (index < getStartIndex() || index > getEndIndex())
            throw new ArrayIndexOutOfBoundsException("index (" + index
                    + ") must between " + getStartIndex() + " and "
                    + getEndIndex() + '.');

        if (!(map instanceof ByteMap))
            throw new IllegalArgumentException("map type (" + map.getName()
                    + ")(" + map.getClass().getName() + ") must be a ByteMap");

        if (map.width != width || map.height != height)
            throw new IllegalArgumentException("map dimension ("
                    + map.getName() + ")(" + map.getDimensionLabel()
                    + ") should be (" + width + 'x' + height + ')');

        ByteMap byteMap = (ByteMap) map;

        raf.seek((index - getStartIndex()) * size + headerLength);
        raf.read(byteMap.pixArray);

        File file = FileUtil.append(this.file, index);
        file = FileUtil.setExtension(file, "bmp");
        byteMap.setFile(file);
    }

}
