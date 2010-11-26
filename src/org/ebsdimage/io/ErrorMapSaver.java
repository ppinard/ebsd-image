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

import static org.ebsdimage.core.ErrorMap.FILE_HEADER;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.ErrorMap;

import ptpshared.util.xml.XmlSaver;
import rmlimage.io.BasicBmpSaver;
import rmlshared.io.FileUtil;

/**
 * Saver for a <code>ErrorMap</code> to a BMP file and XML file containing the
 * phases definition.
 * 
 * @author Philippe T. Pinard
 */
public class ErrorMapSaver extends BasicBmpSaver {

    @Override
    public boolean canSave(Object obj) {
        return obj instanceof ErrorMap;
    }



    /**
     * Saves a <code>ErrorMap</code> to a bmp file and XML file containing the
     * phases definition. The location where to save the files is taken from
     * {@link ErrorMap#getFile()}.
     * 
     * @param map
     *            <code>ErrorMap</code> to save
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(ErrorMap map) throws IOException {
        save(map, map.getFile());
    }



    /**
     * Saves a <code>ErrorMap</code> to a bmp file and XML file containing the
     * phases definition.
     * 
     * @param map
     *            <code>ErrorMap</code> to save
     * @param file
     *            file for the bmp file
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(ErrorMap map, File file) throws IOException {
        // Save pixArray and props as a normal ByteMap
        super.save(map, file);

        // Save phases
        File xmlFile = FileUtil.setExtension(file, "xml");
        new XmlSaver().saveArray(map.getErrorCodes(), xmlFile);

        // Write the property file with the specified header
        saveProperties(map, file, FILE_HEADER);

        map.shouldSave(false); // No need to save anymore
    }



    /**
     * {@inheritDoc}
     * 
     * @see #save(ErrorMap, File)
     */
    @Override
    public void save(Object obj, File file) throws IOException {
        save((ErrorMap) obj, file);
    }

}
