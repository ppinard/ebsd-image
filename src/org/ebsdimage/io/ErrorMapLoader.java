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

import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.ErrorMap;
import org.ebsdimage.core.IndexedByteMap;

import rmlimage.core.Map;

/**
 * Loader for <code>ErrorMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ErrorMapLoader extends IndexedByteMapLoader<ErrorCode> {

    @Override
    protected IndexedByteMap<ErrorCode> createMap(int width, int height,
            java.util.Map<Integer, ErrorCode> items) {
        return new ErrorMap(width, height, items);
    }



    @Override
    protected String getFileHeader() {
        return ErrorMap.FILE_HEADER;
    }



    @Override
    protected Class<? extends ErrorCode> getItemClass() {
        return ErrorCode.class;
    }



    @Override
    public ErrorMap load(File file) throws IOException {
        return (ErrorMap) super.load(file);
    }



    @Override
    public ErrorMap load(File file, Map map) throws IOException {
        return (ErrorMap) super.load(file, map);
    }



    @Override
    public ErrorMap load(File file, Object map) throws IOException {
        return (ErrorMap) super.load(file, map);
    }

}
