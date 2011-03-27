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
package org.ebsdimage.io;

import java.io.File;
import java.io.IOException;

import magnitude.core.Magnitude;

import org.ebsdimage.core.HoughMap;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.TextFileReader;
import static org.ebsdimage.core.HoughMap.FILE_HEADER;

/**
 * Loads a Hough map.
 * 
 * @author Marin Lagac&eacute;
 */
public class HoughMapLoader extends BasicBmpLoader {

    @Override
    public boolean canLoad(File file) {
        if (!super.canLoad(file))
            return false;

        if (getValidationMessage(file).length() != 0)
            return false;

        return true;
    }



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
    protected String getValidationMessage(File file) {
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



    @Override
    public HoughMap load(File file) throws IOException {
        return (HoughMap) super.load(file);
    }



    @Override
    public HoughMap load(File file, Map map) throws IOException {
        if (!canLoad(file))
            throw new IOException(getValidationMessage(file));

        // Create dummy map
        if (map == null)
            map = new ByteMap(1, 1);

        // Load properties to get the calibration
        File propFile = FileUtil.setExtension(file, "prop");
        loadProperties(propFile, map);

        // Calibration
        Magnitude deltaTheta = map.getCalibration().getDX();
        Magnitude deltaRho = map.getCalibration().getDY();

        // Test if the map is well calibrated
        if (!map.isCalibrated())
            throw new IllegalArgumentException(
                    "Properties must contain the map calibration.");
        if (!deltaTheta.areUnits("rad"))
            throw new IllegalArgumentException("Delta theta units ("
                    + deltaTheta.getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        // Create HoughMap
        HoughMap houghMap =
                new HoughMap(map.width, map.height, deltaTheta, deltaRho);
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

        if (!(map instanceof HoughMap))
            throw new IllegalArgumentException(
                    "Map should be an HoughMap, not a " + map.getType() + ".");

        if (map.width != biWidth || map.height != biHeight)
            map =
                    new HoughMap(biWidth, biHeight,
                            ((HoughMap) map).getDeltaTheta(),
                            ((HoughMap) map).getDeltaRho());

        return map;
    }
}
