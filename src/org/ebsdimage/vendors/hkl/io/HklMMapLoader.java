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
package org.ebsdimage.vendors.hkl.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.ebsdimage.io.EbsdMMapLoader;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.jdom.Document;
import org.jdom.Element;

import rmlimage.core.Map;

/**
 * Loader for an <code>HklMMap</code> from a zip file.
 * 
 * @author Philippe T. Pinard
 */
public class HklMMapLoader extends EbsdMMapLoader {

    /**
     * Checks if the file is a valid <code>HklMMap</code>.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     */
    public static boolean isHklMMap(File file) {
        return (new HklMMapLoader().getValidationMessage(file).length() == 0) ? true
                : false;
    }



    @Override
    protected HklMMap createMap(int version, int width, int height,
            HashMap<String, Map> mapList, Document metadata) {
    
        if (version != 1)
            throw new IllegalArgumentException("Invalid version: " + version);
    
        Element element = metadata.getRootElement();
        HklMetadata data = new HklMetadataXmlLoader().load(element);

        return new HklMMap(width, height, mapList, data);
    }



    @Override
    protected String getValidHeader() {
        return HklMMap.FILE_HEADER;
    }



    @Override
    public HklMMap load(File file) throws IOException {
        validate(file);

        return (HklMMap) super.load(file);
    }



    @Override
    public HklMMap load(File file, Object obj) throws IOException {
        return load(file);
    }

}
