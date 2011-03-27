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
package org.ebsdimage.core;

import java.util.Map;

/**
 * Map to track errors encountered during an experiment.
 * 
 * @author Philippe T. Pinard
 */
public class ErrorMap extends IndexedByteMap<ErrorCode> {

    /** Header key to identify a file as a ErrorMap. */
    public static final String FILE_HEADER = "ErrorMap2";

    /** Error code when there is no error. */
    public static final ErrorCode NO_ERROR = new ErrorCode("No error");



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     */
    public ErrorMap(int width, int height) {
        super(width, height, NO_ERROR);
    }



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param pixArray
     *            pixels in the map
     * @param items
     *            error codes
     */
    public ErrorMap(int width, int height, byte[] pixArray,
            Map<Integer, ErrorCode> items) {
        super(width, height, pixArray, NO_ERROR, items);
    }



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param items
     *            error codes
     */
    public ErrorMap(int width, int height, Map<Integer, ErrorCode> items) {
        super(width, height, NO_ERROR, items);
    }



    @Override
    public ErrorMap createMap(int width, int height) {
        return new ErrorMap(width, height);
    }



    @Override
    public ErrorMap duplicate() {
        return (ErrorMap) super.duplicate();
    }



    /**
     * Throws an error for the pixel at the specified index. The error code is
     * automatically registered if it is not already.
     * 
     * @param index
     *            index of the pixel where the error occurs
     * @param errorCode
     *            error
     */
    public void throwError(int index, ErrorCode errorCode) {
        setPixValue(index, errorCode);
    }

}
