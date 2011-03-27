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
import java.io.InputStream;

import org.ebsdimage.core.IndexedByteMap;

import ptpshared.util.simplexml.XmlLoader;
import rmlimage.core.Map;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.TextFileReader;

/**
 * Loader for <code>IndexedByteMap</code>.
 * 
 * @param <Item>
 *            type of item
 * @author Philippe T. Pinard
 */
public abstract class IndexedByteMapLoader<Item> extends BasicBmpLoader {

    @Override
    public boolean canLoad(File file) {
        if (!super.canLoad(file))
            return false;

        if (getValidationMessage(file).length() != 0)
            return false;

        return true;
    }



    /**
     * Creates a new <code>IndexedByteMap</code> from the specified parameters.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param items
     *            items
     * @return new map
     */
    protected abstract IndexedByteMap<Item> createMap(int width, int height,
            java.util.Map<Integer, Item> items);



    /**
     * Returns the file header.
     * 
     * @return file header
     */
    protected abstract String getFileHeader();



    /**
     * Returns the class of the item type.
     * 
     * @return class of the item type.
     */
    protected abstract Class<? extends Item> getItemClass();



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

        String fileHeader = getFileHeader();
        if (!header.startsWith(fileHeader))
            return "Header of prop file (" + header
                    + ") does not match expected header (" + fileHeader + ").";

        // If we have reached this point, then the file is valid
        return "";

        // NOTE: To be rigorous, we should test if the needed properties
        // are present in the prop file.
        // We do not do that in order to avoid parsing the prop file.
        // We may need to re-evaluate this choice in the future.
    }



    @Override
    public IndexedByteMap<Item> load(File file) throws IOException {
        return load(file, null);
    }



    @SuppressWarnings("unchecked")
    @Override
    public IndexedByteMap<Item> load(File file, Map map) throws IOException {
        if (!canLoad(file))
            throw new IOException(getValidationMessage(file));

        // Load XML file
        java.util.Map<Integer, Item> items = loadItems(file);

        // Create empty map
        IndexedByteMap<Item> indexedByteMap;
        if (map == null)
            indexedByteMap = createMap(1, 1, items);
        else
            indexedByteMap = createMap(map.width, map.height, items);

        map = super.load(file, indexedByteMap);

        return (IndexedByteMap<Item>) map;
    }



    @Override
    public IndexedByteMap<Item> load(File file, Object map) throws IOException {
        return load(file, (Map) map);
    }



    @Override
    public Map load(InputStream inStream) throws IOException {
        throw new IOException(
                "An Indexed ByteMap cannot be loaded from an input stream.");
    }



    /**
     * Load items from an XML.
     * 
     * @param file
     *            file of the map
     * @return items
     * @throws IOException
     *             if an error occurs while loading
     */
    protected java.util.Map<Integer, Item> loadItems(File file)
            throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");
        return new XmlLoader().loadMap(Integer.class, getItemClass(), xmlFile);
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

        if (!(map instanceof IndexedByteMap))
            throw new IllegalArgumentException(
                    "Map should be an IndexedByteMap, not a " + map.getType()
                            + ".");

        if (map.width != biWidth || map.height != biHeight) {
            @SuppressWarnings("unchecked")
            java.util.Map<Integer, Item> items =
                    ((IndexedByteMap<Item>) map).getItems();

            map = createMap(biWidth, biHeight, items);
        }

        return map;
    }
}
