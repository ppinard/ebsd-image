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
package org.ebsdimage.io.sim;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.sim.SimMMap;
import org.ebsdimage.core.sim.SimMetadata;
import org.ebsdimage.io.EbsdMMapLoader;

import rmlimage.core.Map;
import rmlimage.module.multi.core.MultiMap;

/**
 * Loader for <code>SimMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SimMMapLoader extends EbsdMMapLoader {

    @Override
    protected MultiMap createMap(int version, int width, int height,
            HashMap<String, Map> mapList) {
        if (version != ExpMMap.VERSION)
            throw new IllegalArgumentException("Invalid version: " + version);

        return new SimMMap(width, height, mapList);
    }



    @Override
    protected Class<? extends EbsdMetadata> getMetadataClass() {
        return SimMetadata.class;
    }



    @Override
    protected String getValidHeader() {
        return SimMMap.FILE_HEADER;
    }



    @Override
    public SimMMap load(File file) throws IOException {
        return (SimMMap) super.load(file);
    }



    @Override
    public SimMMap load(File file, Object obj) throws IOException {
        return (SimMMap) super.load(file);
    }

}
