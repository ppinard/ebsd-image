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

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.PhaseMap;

import rmlimage.io.BasicBmpSaver;
import static org.ebsdimage.core.HoughMap.FILE_HEADER;

/**
 * Saver for a <code>HoughMap</code> to a bmp file and prop file containing the
 * Hough properties.
 * 
 * @author Marin Lagac&eacute;
 */
public class HoughMapSaver extends BasicBmpSaver {

    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return fileFormat.equalsIgnoreCase("bmp") && (obj instanceof HoughMap);
    }



    /**
     * Saves a <code>HoughMAp</code> to a bmp file and prop file containing the
     * Hough properties. The location where to save the files is taken from
     * {@link PhaseMap#getFile()}.
     * 
     * @param map
     *            <code>HoughMap</code> to save
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(HoughMap map) throws IOException {
        save(map, map.getFile());
    }



    /**
     * Saves a <code>HoughMAp</code> to a bmp file and prop file containing the
     * Hough properties.
     * 
     * @param map
     *            <code>HoughMap</code> to save
     * @param file
     *            file for the bmp file
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(HoughMap map, File file) throws IOException {
        // Save ByteMap
        super.save(map, file);

        // Write the property file with the specified header
        File propFile = rmlshared.io.FileUtil.setExtension(file, "prop");
        saveProperties(map, propFile, FILE_HEADER);

        map.shouldSave(false); // No need to save anymore
    }



    /**
     * {@inheritDoc}
     * 
     * @see #save(HoughMap, File)
     */
    @Override
    public void save(Object map, File file) throws IOException {
        save((HoughMap) map, file);
    }

}
