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

import org.ebsdimage.core.IndexedByteMap;

import ptpshared.util.simplexml.XmlSaver;
import rmlimage.io.BasicBmpSaver;
import rmlshared.io.FileUtil;

/**
 * Saver for a <code>IndexedByteMap</code> to a BMP file and XML file containing
 * the items.
 * 
 * @param <Item>
 *            type of item
 * @author Philippe T. Pinard
 */
public abstract class IndexedByteMapSaver<Item> extends BasicBmpSaver {

    /**
     * Returns the file header.
     * 
     * @return file header
     */
    protected abstract String getFileHeader();



    /**
     * Saves an <code>IndexedByteMap</code> to a BMP file and XML file
     * containing the items. The location where to save the files is taken from
     * {@link IndexedByteMap#getFile()}.
     * 
     * @param map
     *            <code>IndexedByteMap</code> to save
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(IndexedByteMap<Item> map) throws IOException {
        save(map, map.getFile());
    }



    /**
     * Saves an <code>IndexedByteMap</code> to a BMP file and XML file
     * containing the items.
     * 
     * @param map
     *            <code>IndexedByteMap</code> to save
     * @param file
     *            file for the BMP file
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(IndexedByteMap<Item> map, File file) throws IOException {
        // Save pixArray and props as a normal ByteMap
        super.save(map, file);

        // Save phases
        saveItems(map.getItems(), file);

        // Write the property file with the specified header
        File propFile = rmlshared.io.FileUtil.setExtension(file, "prop");
        saveProperties(map, propFile, getFileHeader());

        map.shouldSave(false); // No need to save anymore
    }



    /**
     * {@inheritDoc}
     * 
     * @see #save(IndexedByteMap, File)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void save(Object obj, File file) throws IOException {
        save((IndexedByteMap<Item>) obj, file);
    }



    /**
     * Saves the items as a XML.
     * 
     * @param items
     *            items to save
     * @param file
     *            file of the map
     * @throws IOException
     *             if an error occurs while saving the items
     */
    protected void saveItems(java.util.Map<Integer, Item> items, File file)
            throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");
        new XmlSaver().saveMap(items, xmlFile);
    }

}
