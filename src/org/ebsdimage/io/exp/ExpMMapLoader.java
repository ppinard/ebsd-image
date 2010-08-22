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
package org.ebsdimage.io.exp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.exp.ExpMetadata;
import org.ebsdimage.io.EbsdMMapLoader;
import org.jdom.Document;
import org.jdom.Element;

import rmlimage.core.Map;

/**
 * Loader for <code>ExpMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMMapLoader extends EbsdMMapLoader {

    /**
     * Checks if the file is a valid <code>ExpMMap</code>.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     */
    public static boolean isExpMMap(File file) {
        return (new ExpMMapLoader().getValidationMessage(file).length() == 0) ? true
                : false;
    }



    @Override
    protected String getValidHeader() {
        return ExpMMap.FILE_HEADER;
    }



    @Override
    protected ExpMMap createMap(int version, int width, int height,
            HashMap<String, Map> mapList, Document metadata) {

        if (version != 1)
            throw new IllegalArgumentException("Invalid version: " + version);

        Element element = metadata.getRootElement();
        ExpMetadata data = new ExpMetadataXmlLoader().load(element);

        return new ExpMMap(width, height, mapList, data);
    }



    @Override
    public ExpMMap load(File file) throws IOException {
        validate(file);

        return (ExpMMap) super.load(file);
    }



    @Override
    public ExpMMap load(File file, Object obj) throws IOException {
        return load(file);
    }

}
