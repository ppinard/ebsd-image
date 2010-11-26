package org.ebsdimage.core;

import java.util.HashMap;
import java.util.Random;

import rmlimage.core.ByteMap;
import rmlimage.core.LUT;

/**
 * Map to track errors encountered during an experiment.
 * 
 * @author ppinard
 */
public class ErrorMap extends ByteMap {

    /** Header key to identify a file as a ErrorMap. */
    public static final String FILE_HEADER = "ErrorMap1";



    /**
     * Returns a colour look-up table for the different error codes. Each error
     * code is assigned a specific colour. The colours are random so that two
     * consecutive error codes have different colours.
     * 
     * @param nbColors
     *            number of colours to generate
     * @return a look-up table
     */
    private static LUT randomColorLUT(int nbColors) {
        LUT lut = new LUT(nbColors);

        // Colour 0 = black = background
        lut.red[0] = (byte) 0;
        lut.green[0] = (byte) 0;
        lut.blue[0] = (byte) 0;

        // Set the colour components of the LUT
        Random rnd = new Random(1);
        int red;
        int green;
        int blue;
        for (int n = 1; n < nbColors; n++) {
            red = rnd.nextInt(256);
            green = rnd.nextInt(256);
            blue = rnd.nextInt(256);

            // If colour too dark, redo
            if (red + green + blue < 128) {
                n--;
                continue;
            }

            lut.red[n] = (byte) red;
            lut.green[n] = (byte) green;
            lut.blue[n] = (byte) blue;
        }

        return lut;
    }

    /** Registered error codes. */
    private HashMap<Integer, ErrorCode> errorCodes =
            new HashMap<Integer, ErrorCode>();



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     */
    public ErrorMap(int width, int height) {
        super(width, height, randomColorLUT(255));
        clear((byte) 0);
        register(new ErrorCode(0, "No error"));
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
     * @param errorCodes
     *            error codes for the pixels in the map
     * @throws NullPointerException
     *             if the error codes array is null
     * @throws IllegalArgumentException
     *             if a pixel refers to an undefined error code
     */
    public ErrorMap(int width, int height, byte[] pixArray,
            ErrorCode[] errorCodes) {
        // Update pixArray, size, etc.
        super(width, height, randomColorLUT(255), pixArray);

        if (errorCodes == null)
            throw new NullPointerException("Error codes cannot be null.");

        // Update error codes
        register(new ErrorCode(0, "No error"));
        for (ErrorCode errorCode : errorCodes)
            if (errorCode.id != 0)
                register(errorCode);

        // Validate
        validate();
    }



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param errorCodes
     *            error codes for the pixels in the map
     * @throws NullPointerException
     *             if the error codes array is null
     * @throws IllegalArgumentException
     *             if a pixel refers to an undefined error code
     */
    public ErrorMap(int width, int height, ErrorCode[] errorCodes) {
        // Update pixArray, size, etc.
        super(width, height, randomColorLUT(255));

        if (errorCodes == null)
            throw new NullPointerException("Error codes cannot be null.");

        // Update error codes
        register(new ErrorCode(0, "No error"));
        for (ErrorCode errorCode : errorCodes)
            if (errorCode.id != 0)
                register(errorCode);

        // Validate
        validate();
    }



    @Override
    public ErrorMap createMap(int width, int height) {
        return new ErrorMap(width, height);
    }



    @Override
    public ErrorMap duplicate() {
        ErrorMap dup =
                new ErrorMap(width, height, pixArray.clone(),
                        errorCodes.values().toArray(new ErrorCode[0]));

        // Copy metadata
        dup.cloneMetadataFrom(this);

        return dup;
    }



    /**
     * Returns the registered error code.
     * 
     * @return an array of <code>ErrorCode</code>
     */
    public ErrorCode[] getErrorCodes() {
        return errorCodes.values().toArray(new ErrorCode[0]);
    }



    @Override
    public String getPixelInfoLabel(int index) {
        // Error message
        ErrorCode errorCode = errorCodes.get(pixArray[index] & 0xff);

        // Build string
        StringBuilder str = new StringBuilder();
        str.append(super.getPixelInfoLabel(index));

        str.append(errorCode.id);
        str.append(" - ");
        str.append(errorCode.type);

        if (errorCode.description.length() != 0) {
            str.append(':');
            str.append(errorCode.description);
        }

        return str.toString();
    }



    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @return error message or empty string
     */
    private String getValidationMessage() {
        int value;

        for (int i = 0; i < size; i++) {
            value = pixArray[i] & 0xff;
            if (!errorCodes.containsKey(value))
                return "Error code (" + value + ") at pixel (index=" + i
                        + ") is undefined";
        }

        return "";
    }



    /**
     * Checks that all the values inside the <code>pixArray</code> are linked
     * with an error code.
     * 
     * @return <code>true</code> if all the values are linked with an error
     *         code, <code>false</code> otherwise
     */
    public boolean isCorrect() {
        return (getValidationMessage().length() == 0) ? true : false;
    }



    /**
     * Registers a new <code>ErrorCode</code>.
     * 
     * @param errorCode
     *            new error code
     * @throws IllegalArgumentException
     *             if the id of the new error code is already used by an error
     *             code in this <code>ErrorMap</code>
     */
    public void register(ErrorCode errorCode) {
        if (errorCodes.containsKey(errorCode.id))
            throw new IllegalArgumentException("An error code with id ("
                    + errorCode.id + ") is already registered.");
        errorCodes.put(errorCode.id, errorCode);
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
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Index must be between [0,"
                    + size + "[");

        if (!errorCodes.containsKey(errorCode.id))
            register(errorCode);

        pixArray[index] = (byte) errorCode.id;
    }



    /**
     * Unregisters an <code>ErrorCode</code> from this <code>ErrorMap</code>.
     * 
     * @param id
     *            id of the error code to unregister
     * @throws IllegalArgumentException
     *             if the specified id does not correspond to any registered
     *             error code
     */
    public void unregister(int id) {
        if (!errorCodes.containsKey(id))
            throw new IllegalArgumentException("No error code with id (" + id
                    + ") is registered.");
        if (id == 0)
            throw new IllegalArgumentException(
                    "The \"no error\" error code cannot be removed.");

        // Store old value in case of an error
        ErrorCode oldErrorcode = errorCodes.get(id);

        // Attempt to remove error code
        errorCodes.remove(id);

        try {
            validate();
        } catch (IllegalArgumentException e) {
            register(oldErrorcode); // Revert
            throw new IllegalArgumentException(e); // Throw error
        }
    }



    /**
     * Validates that all the values inside the <code>pixArray</code> are linked
     * with an error code.
     * 
     * @throws IllegalArgumentException
     *             if a pixel links to an undefined error code
     */
    public void validate() {
        String message = getValidationMessage();
        if (message.length() > 0)
            throw new IllegalArgumentException(message);
    }
}
