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

import static org.ebsdimage.core.HoughMap.*;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.HoughMap;

import rmlimage.core.EightBitMap;
import rmlimage.core.Map;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.TextFileReader;
import rmlshared.util.Properties;

/**
 * Loads a Hough map.
 * 
 * @author Marin Lagac&eacute;
 * 
 */
public class HoughMapLoader extends BasicBmpLoader {

    /**
     * Will return an error message if the file is not a valid HoughMap file or
     * an empty String if it is a valid file.
     * <p/>
     * To be valid a file:
     * <ul>
     * <li>The extension of the file must be bmp.</li>
     * <li>A prop file must exist.</li>
     * <li>The header of the prop file must match the HoughMap header.</li>
     * </ul>
     * 
     * @param file
     *            file to validate
     * @return an error message or an empty string if the file is valid
     */
    private static String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("bmp"))
            return "The extension of the file must be bmp, not " + ext + ".";

        // Check prop file exists
        File propFile = FileUtil.setExtension(file, "prop");
        if (!propFile.exists())
            return "Could not find required prop file: " + propFile.getPath()
                    + '.';

        // Check header line
        String header = null;

        try {
            TextFileReader reader = new TextFileReader(propFile);
            header = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            return ex.getMessage();
        }

        if (header == null || !header.startsWith("#"))
            return "Line 1 of the prop file should be a comment line.";

        header = header.substring(1); // Trim comment char

        if (!header.startsWith(FILE_HEADER))
            return "Header of prop file (" + header
                    + ") does not match expected header for HoughMap ("
                    + FILE_HEADER + ").";

        // If we have reached this point, then the file is valid
        return "";

        // NOTE: To be rigorous, we should test if the needed properties
        // are present in the prop file.
        // We do not do that in order to avoid parsing the prop file.
        // We may need to re-evaluate this choice in the future.
    }



    /**
     * Checks if the file is a valid <code>HoughMap</code>.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     */
    public static boolean isHoughMap(File file) {
        return (getValidationMessage(file).length() == 0) ? true : false;
    }



    /**
     * Validates the file to be a valid <code>HoughMap</code>.
     * 
     * @param file
     *            a file
     * @throws IOException
     *             if the file is not valid
     */
    private static void validate(File file) throws IOException {
        String message = getValidationMessage(file);
        if (message.length() > 0)
            throw new IOException(message);
    }



    @Override
    public HoughMap load(File file) throws IOException {
        return (HoughMap) super.load(file);
    }



    @Override
    public HoughMap load(File file, Map map) throws IOException {
        // Validate file
        validate(file);

        // Load properties
        File propFile = FileUtil.setExtension(file, "prop");
        Properties props = new Properties(propFile);

        // Read the needed properties
        int width = props.getProperty(WIDTH, 0);
        if (width <= 0)
            throw new IOException("Incorrect value for property " + WIDTH
                    + " (" + width + ").");

        int height = props.getProperty(HEIGHT, 0);
        if (height <= 0)
            throw new IOException("Incorrect value for property " + HEIGHT
                    + " (" + height + ").");

        double deltaR =
                Double.longBitsToDouble(props.getProperty(DELTA_R, -1l));
        if (deltaR < 0)
            throw new IOException("Incorrect value for property " + DELTA_R
                    + " (" + deltaR + ").");

        double deltaTheta =
                Double.longBitsToDouble(props.getProperty(DELTA_THETA, -1l));
        if (deltaTheta < 0)
            throw new IOException("Incorrect value for property " + DELTA_THETA
                    + " (" + deltaTheta + ").");

        HoughMap houghMap = new HoughMap(width, height, deltaR, deltaTheta);
        map = super.load(file, houghMap);

        return (HoughMap) map;
    }



    @Override
    public HoughMap load(File file, Object map) throws IOException {
        return (HoughMap) super.load(file, map);
    }



    /**
     * Validates that the width and height of the map are correct with the Hough
     * map parameters.
     * 
     * @param map
     *            map to validate
     * @return the valid map
     */
    @Override
    protected Map validateMap(Map map) {
        if (biBitCount != 8)
            throw new IllegalArgumentException(
                    "biBitCount should be equal to 8");

        if (map == null)
            throw new NullPointerException("Cannot validate a null map.");

        if (!(map instanceof EightBitMap))
            throw new IllegalArgumentException(
                    "Map should be a HoughMap, not a " + map.getType() + ".");

        if (map.width != biWidth)
            throw new IllegalArgumentException("Width of specified map ("
                    + map.width + ") does not match width of bmp file ("
                    + biWidth + ").");

        if (map.height != biHeight)
            throw new IllegalArgumentException("Height of specified map ("
                    + map.height + ") does not match height of bmp file ("
                    + biHeight + ").");

        return map;
    }
}
