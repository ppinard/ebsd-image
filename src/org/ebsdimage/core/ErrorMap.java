package org.ebsdimage.core;

import java.util.Map;

/**
 * Map to track errors encountered during an experiment.
 * 
 * @author ppinard
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
