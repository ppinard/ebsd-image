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

import static org.ebsdimage.core.PhasesMap.FILE_HEADER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.ebsdimage.core.PhasesMap;

import ptpshared.util.xml.XmlLoader;
import rmlimage.core.Map;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.TextFileReader;
import crystallography.core.Crystal;

/**
 * Loader for <code>PhasesMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesMapLoader extends BasicBmpLoader {

    @Override
    public boolean canLoad(File file) {
        if (!super.canLoad(file))
            return false;

        if (getValidationMessage(file).length() != 0)
            return false;

        return true;
    }



    /**
     * Returns the validation message indication if the specified file is a
     * valid <code>ErrorMap</code>. An empty string if the file is valid. An
     * error message otherwise.
     * <p>
     * To be valid:
     * <ul>
     * <li>File must have a BMP extension</li>
     * <li>A XML file with the same filename as the file must exists</li>
     * <li>A PROP file with the same filename as the file must exists</li>
     * <li>The header of the PROP file must match the expected header</li>
     * </ul>
     * 
     * @param file
     *            a file of a map
     * @return error message or empty string
     */
    protected String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("bmp"))
            return "The extension of the file must be bmp, not " + ext + ".";

        // Check XML file exists
        File xmlFile = FileUtil.setExtension(file, "xml");
        if (!xmlFile.exists())
            return "Could not find required xml file: " + xmlFile.getPath()
                    + ".";

        // Check PROP file exists
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
                    + ") does not match expected header for PhasesMap ("
                    + FILE_HEADER + ").";

        // If we have reached this point, then the file is valid
        return "";

        // NOTE: To be rigorous, we should test if the needed properties
        // are present in the prop file.
        // We do not do that in order to avoid parsing the prop file.
        // We may need to re-evaluate this choice in the future.
    }



    @Override
    public PhasesMap load(File file) throws IOException {
        return load(file, null);
    }



    @Override
    public PhasesMap load(File file, Map map) throws IOException {
        if (!canLoad(file))
            throw new IOException(getValidationMessage(file));

        // Load XML file
        File xmlFile = FileUtil.setExtension(file, "xml");
        Crystal[] phases = new XmlLoader().loadArray(Crystal.class, xmlFile);

        // Create empty map
        PhasesMap phasesMap;
        if (map == null)
            phasesMap = new PhasesMap(1, 1, phases);
        else
            phasesMap = new PhasesMap(map.width, map.height, phases);

        map = super.load(file, phasesMap);

        return (PhasesMap) map;
    }



    @Override
    public PhasesMap load(File file, Object map) throws IOException {
        return load(file, (Map) map);
    }



    @Override
    public Map load(InputStream inStream) throws IOException {
        throw new IOException(
                "A PhasesMap cannot be loaded from an input stream.");
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

        if (!(map instanceof PhasesMap))
            throw new IllegalArgumentException(
                    "Map should be an PhasesMap, not a " + map.getType() + ".");

        if (map.width != biWidth || map.height != biHeight)
            map =
                    new PhasesMap(biWidth, biHeight,
                            ((PhasesMap) map).getPhases());

        return map;
    }

}
