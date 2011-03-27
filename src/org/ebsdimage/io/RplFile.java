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

/**
 * Properties given by a RPL file.
 * 
 * @author Philippe T. Pinard
 */
public class RplFile {

    /** Number of bytes per pixel. */
    public final int dataLength;

    /** Height of the map. */
    public final int height;

    /** Width of the map. */
    public final int width;

    /** Specified if the data is little or big endian. */
    private final String byteOrder;

    /** Specified if the data is signed. */
    private final String dataType;



    /**
     * Creates a new <code>RplFile</code>.
     * 
     * @param dataLength
     *            number of bytes per pixel
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param byteOrder
     *            specified if the data is little or big endian
     * @param dataType
     *            specified if the data is signed
     */
    public RplFile(int dataLength, int width, int height, String byteOrder,
            String dataType) {
        // Data length
        if (dataLength > 2 || dataLength < 0)
            throw new IllegalArgumentException("data-length of " + dataLength
                    + " is not supported.");
        this.dataLength = dataLength;

        // Width
        if (width <= 0)
            throw new IllegalArgumentException("The width (" + width
                    + ") must be greater than zero.");
        this.width = width;

        // Height
        if (height <= 0)
            throw new IllegalArgumentException("The height (" + height
                    + ") must be greater than zero.");
        this.height = height;

        // Byte order
        if (!byteOrder.equals("big-endian")
                && !byteOrder.equals("little-endian")
                && !byteOrder.equals("dont-care"))
            throw new IllegalArgumentException("Unknown byte-order ( "
                    + byteOrder + ").");
        this.byteOrder = byteOrder;

        // Data type
        if (!dataType.equals("signed") && !dataType.equals("unsigned"))
            throw new IllegalArgumentException("Unknown data-type (" + dataType
                    + ").");
        this.dataType = dataType;

    }



    /**
     * Returns whether the data is coded in big or little endian.
     * 
     * @return <code>true</code> if the data is coded in big endian,
     *         <code>false</code> otherwise
     */
    public boolean isBigEndian() {
        if (byteOrder.equals("big-endian"))
            return true;
        else if (byteOrder.equals("little-endian"))
            return false;
        else if (byteOrder.equals("dont-care"))
            return true;
        else
            return false;
    }



    /**
     * Returns whether the data is signed or unsigned.
     * 
     * @return <code>true</code> if the data is signed, <code>false</code>
     *         otherwise
     */
    public boolean isSigned() {
        if (dataType.equals("signed"))
            return true;
        else
            return false;
    }

}
