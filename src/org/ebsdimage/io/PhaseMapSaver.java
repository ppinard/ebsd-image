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

import org.ebsdimage.core.PhaseMap;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Saver for a <code>PhaseMap</code> to a BMP file and XML file containing the
 * phases definition.
 * 
 * @author Philippe T. Pinard
 */
public class PhaseMapSaver extends IndexedByteMapSaver<Crystal> {

    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof PhaseMap) && fileFormat.equalsIgnoreCase("bmp");
    }



    @Override
    protected String getFileHeader() {
        return PhaseMap.FILE_HEADER;
    }



    @Override
    public void saveItems(java.util.Map<Integer, Crystal> items, File file)
            throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());

        saver.saveMap(items, xmlFile);
    }

}
