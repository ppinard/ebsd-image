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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.IndexedByteMap;
import org.ebsdimage.core.PhaseMap;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import rmlimage.core.Map;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Loader for <code>PhaseMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PhaseMapLoader extends IndexedByteMapLoader<Crystal> {

    @Override
    protected String getFileHeader() {
        return PhaseMap.FILE_HEADER;
    }



    @Override
    public PhaseMap load(File file) throws IOException {
        return (PhaseMap) super.load(file);
    }



    @Override
    protected Class<? extends Crystal> getItemClass() {
        return Crystal.class;
    }



    @Override
    public PhaseMap load(File file, Map map) throws IOException {
        return (PhaseMap) super.load(file, map);
    }



    @Override
    public PhaseMap load(File file, Object map) throws IOException {
        return (PhaseMap) super.load(file, map);
    }



    @Override
    protected IndexedByteMap<Crystal> createMap(int width, int height,
            java.util.Map<Integer, Crystal> items) {
        return new PhaseMap(width, height, items);
    }



    @Override
    protected java.util.Map<Integer, Crystal> loadItems(File file)
            throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        loader.matchers.registerMatcher(new SpaceGroupMatcher());

        return loader.loadMap(Integer.class, getItemClass(), xmlFile);
    }

}
